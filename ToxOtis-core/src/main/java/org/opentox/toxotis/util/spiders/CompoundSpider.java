/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IClient;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Conformer;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * Downloader and parser for a compound resource available in RDF.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class CompoundSpider extends Tarantula<Compound> {

    VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CompoundSpider.class);
    private boolean downloadDetails = true;
    private String keyword;
    private String lookupService;

    public void setDownloadDetails(boolean downloadDetails) {
        this.downloadDetails = downloadDetails;
    }

    public CompoundSpider(String keyword, String lookUpService) throws ServiceInvocationException, ToxOtisException {
        super();
        /*
         * identify whether the submitted keyword is a URI:
         */
        boolean keywordIsVri = false;
        VRI possibleVri = null;
        try {
            possibleVri = new VRI(keyword);
            Class<?> otClass = possibleVri.getOpenToxType();
            if (Compound.class.equals(otClass) || Conformer.class.equals(otClass)) {
                keywordIsVri = true;
            }
        } catch (URISyntaxException ex) {
            keywordIsVri = false;
        }
        this.keyword = keyword;
        if (lookUpService == null) {
            if (!keywordIsVri) {
                lookUpService = "http://apps.ideaconsult.net:8080/ambit2/query/compound/%s/all";
            } else {
                lookUpService = "http://apps.ideaconsult.net:8080/ambit2/query/compound/url/all";
            }
        }
        this.lookupService = lookUpService;

        if (!keywordIsVri) {
            try {
                keyword = URLEncoder.encode(keyword, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CompoundSpider.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        ////// !!!! Now 'keyword' is UTF-8-encoded !!!!
        
        String queryUri = String.format(lookUpService, keyword);
        VRI queryVri = null;
        try {            
            queryVri = !keywordIsVri?new VRI(queryUri):new VRI(lookUpService).addUrlParameter("search", keyword);
        } catch (URISyntaxException ex) {
            String msg = "Keyword '" + keyword + "' is not valid";
            logger.debug(msg, ex);
            throw new ToxOtisException(msg, ex);
        }
        /*
         * GET THE URI OF THE COMPOUND 
         */
        if (!keywordIsVri) {
            IClient client = ClientFactory.createGetClient(queryVri);
            client.setMediaType(Media.TEXT_URI_LIST);
            int responseCode = client.getResponseCode();
            if (responseCode != 200) {
                throw new ServiceInvocationException("Service responded with code " + responseCode);
            }
            Set<VRI> responseUri = client.getResponseUriList();
            if (responseUri == null || (responseUri != null && responseUri.isEmpty())) {
                throw new ServiceInvocationException("No response from service");
            }
            this.uri = responseUri.iterator().next();
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    logger.error("Client could not close", ex);
                    throw new RuntimeException(ex);
                }
            }
        } else {
            this.uri = possibleVri;
        }


        IGetClient compoundClient = ClientFactory.createGetClient(queryVri);
        try {
            compoundClient.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            int status = compoundClient.getResponseCode();
            if (status != 200) {
                OntModel om = compoundClient.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(om);
                ErrorReport er = ersp.parse();

                if (status == 403) {
                    ForbiddenRequest fr = new ForbiddenRequest("Access denied to : '" + uri + "'");
                    fr.setErrorReport(er);
                    throw fr;
                }
                if (status == 401) {
                    Unauthorized unauth = new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == 404) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    ConnectionException connectionException = new ConnectionException("Communication Error with : '" + uri + "'");
                    connectionException.setErrorReport(er);
                    throw connectionException;

                }
            }
            model = compoundClient.getResponseOntModel();
            resource = model.getResource(uri.toString());
        } finally { // Have to close the client (disconnect)
            if (compoundClient != null) {
                try {
                    compoundClient.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.toString() : null) + "'", ex);
                }
            }
        }
    }

    public CompoundSpider(VRI uri) throws ServiceInvocationException, ToxOtisException {
        super();
        this.uri = uri;
        IGetClient compoundClient = ClientFactory.createGetClient(uri);
        try {
            compoundClient.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            compoundClient.setUri(uri);
            int status = compoundClient.getResponseCode();
            if (status != 200) {
                OntModel om = compoundClient.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(om);
                ErrorReport er = ersp.parse();

                if (status == 403) {
                    ForbiddenRequest fr = new ForbiddenRequest("Access denied to : '" + uri + "'");
                    fr.setErrorReport(er);
                    throw fr;
                }
                if (status == 401) {
                    Unauthorized unauth = new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == 404) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    ConnectionException connectionException = new ConnectionException("Communication Error with : '" + uri + "'");
                    connectionException.setErrorReport(er);
                    throw connectionException;

                }
            }
            model = compoundClient.getResponseOntModel();
            resource = model.getResource(uri.toString());
        } finally { // Have to close the client (disconnect)
            if (compoundClient != null) {
                try {
                    compoundClient.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.toString() : null) + "'", ex);
                }
            }
        }
    }

    public CompoundSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
    }

    public CompoundSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
        this.resource = model.getResource(uri);
    }

    private String getValue(Resource valueNode) {
        return valueNode.getProperty(OTDatatypeProperties.value().asDatatypeProperty(model)).getLiteral().getString();
    }

    private String getFeatureUri(Resource valueNode) {
        StmtIterator featuresIt = model.listStatements(new SimpleSelector(valueNode,
                OTObjectProperties.feature().asObjectProperty(model), (RDFNode) null));
        if (featuresIt != null && featuresIt.hasNext()) {
            Resource featureRS = featuresIt.nextStatement().getObject().asResource();
            return featureRS.getProperty(OWL.sameAs).getObject().asResource().getURI();
        }
        return null;
    }

    @Override
    public Compound parse() throws ServiceInvocationException {
        Compound compound = null;
        try {
            compound = new Compound(uri);
        } catch (ToxOtisException ex) {
            throw new BadRequestException("Not a valid compound URI : '" + uri + "'. "
                    + "Parsing of remote resource won't continue!", ex);
        }
        if (resource != null) {
            compound.setMeta(new MetaInfoSpider(resource, model).parse());
        }
        StmtIterator datasetIt = null;
        StmtIterator dataEntryIt = null;
        StmtIterator valuesIt = null;

        Resource datasetRS = null;
        Resource dataEntryRS = null;
        Resource valueRS = null;

        if (downloadDetails && keyword != null && lookupService != null) {
            datasetIt = model.listStatements(new SimpleSelector(null, RDF.type, OTClasses.dataset().inModel(model)));
            if (datasetIt != null && datasetIt.hasNext()) { // dataset found
                datasetRS = datasetIt.nextStatement().getSubject();
                dataEntryIt = model.listStatements(
                        new SimpleSelector(
                        datasetRS, OTObjectProperties.dataEntry().asObjectProperty(model),
                        (RDFNode) null));
                if (dataEntryIt != null && dataEntryIt.hasNext()) {
                    dataEntryRS = dataEntryIt.nextStatement().getObject().asResource();
                    valuesIt = model.listStatements(new SimpleSelector(
                            dataEntryRS, OTObjectProperties.values().asObjectProperty(model), (RDFNode) null));
                    if (valuesIt != null) {
                        while (valuesIt.hasNext()) {
                            valueRS = valuesIt.nextStatement().getObject().asResource();
                            String featureUri = getFeatureUri(valueRS);
                            if (featureUri.equals("http://www.opentox.org/api/1.1#SMILES")) {
                                String smiles = getValue(valueRS);
                                if (smiles != null) {
                                    smiles = smiles.trim().split("\n")[0];
                                }
                                compound.setSmiles(smiles);
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#InChI_std")) {
                                compound.setInchi(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#InChIKey_std")) {
                                compound.setInchiKey(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#CASRN")) {
                                compound.setCasrn(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#REACHRegistrationDate")) {
                                compound.setRegistrationDate(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#IUPACName")) {
                                compound.setIupacName(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#EINECS")) {
                                compound.setEinecs(getValue(valueRS));
                            } else if (featureUri.equals("http://www.opentox.org/api/1.1#ChemicalName")) {
                                ArrayList<String> synonyms = new ArrayList<String>();
                                String names = getValue(valueRS);
                                String[] fragments = names.split("\n");
                                for (String line : fragments) {
                                    String[] tokens = line.split(";");
                                    for (String token : tokens) {
                                        synonyms.add(token.trim());
                                    }
                                }
                                compound.setSynonyms(synonyms);
                            }
                        }
                    }
                }
            }
        }
        return compound;
    }
}

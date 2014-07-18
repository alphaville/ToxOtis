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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.DescriptorCaclulation;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.factory.FeatureFactory;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 * Provides different representations for chemical compounds with a unique
 * and defined chemical structure up to its 2D characteristics. Also provides access
 * to different representations of the compound such as its SMILES or SDF formats.
 * Allows users to easily download these representations from remote locations and
 * store them locally in files. Also allows such representations to be POSTed to 
 * remote services and new compounds to be created (published) online.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Compound extends DescriptorCaclulation<Compound> {

    private transient final org.slf4j.Logger logger = 
            org.slf4j.LoggerFactory.getLogger(Compound.class);
    private List<String> synonyms;
    private javax.swing.ImageIcon depiction;
    private String iupacName;
    private String einecs;
    private String inchi;
    private String inchiKey;
    private String registrationDate;
    private String casrn;
    private String smiles;
    /**
     * Can be anything; varying from a SMILES String to a MOL file or an SD file
     * of anything.
     */
    private String molecularStructure;
    private Set<Conformer> conformers;

    /**
     * Construct a new compound identified by its URI. You should provide a
     * valid compound URI. Conformer URIs are not acceptable in this constructor.
     * The pattern matching your URI should be:
     * 
     * 
     * <code>.+[^query]+/(?i)compound(s||)/([^/]+/$|[^/]+)$</code>
     *
     * @param uri
     *      The URI of the compound which should be a valid compound URI
     * @throws ToxOtisException
     *      In case the provided URI is not a valid compound or conformer URI.
     * @see VRI#getOntologicalClass() Identify the type of a URI
     */
    public Compound(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null
                && !Compound.class.equals(uri.getOpenToxType()) && !Conformer.class.equals(uri.getOpenToxType())
                && !Substance.class.equals(uri.getOpenToxType())) {
            throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                    + "' is not a valid Compound uri according to the OpenTox specifications.");
        }
        setMeta(null);
    }

    /**
     * Create a new (empty) instance of Compound
     */
    public Compound() {
        super();
    }

    /**
     * Return the (local) identifier of the compound as it is extracted from its URI. For
     * example http://someserver.com/compound/phenol will return <code>phenol</code>.
     * When applied on a conformer, it will also return the (local) id of the
     * compound. For example, if applied on <code>http://someserver.com/compound/1/conformer/2</code>,
     * the method returns <code>1</code>.
     * @return
     *      Compound ID.
     */
    public String getCompoundId() {
        String[] uriFragments = getUri().getStringNoQuery().split("/compound/");
        if (uriFragments.length == 2) {
            if (!uriFragments[1].contains("/conformer/")) {
                return uriFragments[1];
            } else {
                String[] subFragments = uriFragments[1].split("/conformer/");
                if (subFragments != null && subFragments.length > 0) {
                    return subFragments[0];
                }
            }
        }
        return null;
    }

    /**
     * Performs a HTTP GET request on /compound/{id} to acquire a URI list of
     * all conformers providing also an authentication token (set it to
     * <code>null</code> if you think there's no need for Authentication.
     * @param token
     *      An authentication token.
     * @return
     *      List of conformers
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503.
     */
    public Set<Conformer> listConformers(AuthenticationToken token) 
            throws ServiceInvocationException {
        VRI newUri = null;


        if (getUri().getOpenToxType().equals(Conformer.class)) {
            String uriString = getUri().toString();
            String withoutConformer = uriString.split("/conformer/")[0];



            try {
                newUri = new VRI(withoutConformer).augment("conformer");
            } catch (URISyntaxException ex) {
                logger.error(null, ex);
                throw new IllegalArgumentException(ex);
            }
        } else {
            newUri = new VRI(getUri()).augment("conformer");
        }
        GetHttpClient client = new GetHttpClient(newUri);
        client.authorize(token);
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        Set<VRI> uriList = client.getResponseUriList();
        Set<Conformer> confmers = new HashSet<Conformer>();
        for (VRI confUri : uriList) {
            try {
                confmers.add(new Conformer(new VRI(confUri)));
            } catch (final ToxOtisException ex) {
                throw new RemoteServiceException("Remote service returned the URI : " + confUri + " which is "
                        + "not a valid conformer URI", ex);
            }
        }
        return confmers;
    }

    /**
     * Returns the sub-dataset that has properties of a given ontological
     * type.
     * @param featurePrototype
     *      A feature ontological class.
     * @param token
     *      An authentication token
     * @return
     * @throws ServiceInvocationException 
     */
    public Dataset getPropertiesByOnt(OntologicalClass featurePrototype, AuthenticationToken token)
            throws ServiceInvocationException {
        Set<VRI> features = FeatureFactory.lookupSameAs(featurePrototype, token);
        return getProperties(token, (VRI[]) features.toArray(new VRI[features.size()]));
    }

    public LiteralValue<?> getAssayProperty(OntologicalClass featurePrototype, AuthenticationToken token) throws ToxOtisException {
        return null;
    }

    /**
     * Retrieves a property as a typed value, for a given feature, from the remote
     * server that hosts the underlying compound.
     * @param feature
     *      Feature for which the value is retrieved
     * @param token
     *      Token used to authenticate the client and authorize it to perform the
     *      GET request to the remote servrer. If you think that no authentication
     *      is needed to access the resource, you may set it to <code>null</code>.
     * @return
     *      Feature value for this compound as a Typed Value
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public LiteralValue<?> getProperty(Feature feature, AuthenticationToken token) 
            throws ServiceInvocationException {
        /**
         *TODO: Should this request include the uri parameters for the feature?
         */
        VRI dsUri = new VRI(getUri()).addUrlParameter("feature_uris[]", feature.getUri().toString());
        GetHttpClient client = new GetHttpClient();
        client.authorize(token);
        client.setUri(dsUri);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        OntModel model = client.getResponseOntModel();
        RDF.type.inModel(model);
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.dataset().inModel(model));
        Resource baseResource = null;
        if (dsIt.hasNext()) {
            baseResource = dsIt.nextStatement().getSubject();
            DatasetSpider dsSpider = new DatasetSpider(baseResource, model);
            Dataset ds = dsSpider.parse();
            List<DataEntry> data = ds.getDataEntries();
            if (data != null && data.size() >= 1) {
                DataEntry firstEntry = ds.getDataEntries().get(0);
                if (firstEntry != null && firstEntry.getFeatureValues().size() >= 1) {
                    FeatureValue fVal = firstEntry.getFeatureValues().iterator().next();
                    if (fVal != null) {
                        return fVal.getValue();
                    }
                }
            }
        }
        return null;
    }

    public LiteralValue getProperty(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        Feature tempFeat = new Feature(uri);
        return getProperty(tempFeat, token);
    }

    /**
     * Creates a dataset with the current compound and the features 
     * prescribed in the list of input arguments. The dataset is fetched from 
     * a remote location.
     * 
     * @param token
     *      Authentication token that will allow us to access the resource.
     * @param featureUris
     *      List of Feature URIs to be included in the dataset.
     * @return
     *      The requested dataset.
     * @throws ServiceInvocationException 
     *      In case some of the involved services experiences some error or
     *      if the request is mal-formed.
     */
    public Dataset getProperties(AuthenticationToken token, VRI... featureUris) throws ServiceInvocationException {
        VRI dsUri = new VRI(getUri());
        for (VRI featureUri : featureUris) {
            dsUri.addUrlParameter("feature_uris[]", featureUri.toString());
        }

        GetHttpClient client = new GetHttpClient();
        client.setUri(dsUri);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        OntModel model = client.getResponseOntModel();
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.dataset().inModel(model));
        Resource baseResource = null;
        if (dsIt.hasNext()) {
            baseResource = dsIt.nextStatement().getSubject();
            DatasetSpider dsSpider = new DatasetSpider(baseResource, model);
            return dsSpider.parse();
        }
        return null;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String compoundUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(compoundUri, OTClasses.compound().inModel(model));
        //TODO: Featch MetaInfo using : getMeta().attachTo(individual, model).
        return indiv;
    }

    /**
     * This method is not implemented yet!
     * @param uri
     *      Identifier of the location from where the RDF document should be downloaded from
     *      and parsed into a Compound object.
     * @return
     *      Parsed instance of the component into an instance of Compound.
     * @throws ServiceInvocationException
     *      A ServiceInvocationException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data.
     */
    @Override
    protected Compound loadFromRemote(VRI uri, AuthenticationToken token) 
            throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        PostHttpClient client = new PostHttpClient(vri);
        client.authorize(token);
        client.setContentType(Media.APPLICATION_RDF_XML.getMime());
        client.setPostable(asOntModel());
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        client.post();
        int status;
        status = client.getResponseCode();
        if (status == HttpStatusCodes.Success.getStatus()) {
            Task readyTask = new Task();
            readyTask.setPercentageCompleted(100);
            readyTask.setStatus(Task.Status.COMPLETED);
            try {
                readyTask.setResultUri(new VRI(client.getResponseText()));
                return readyTask;
            } catch (final URISyntaxException ex) {
                throw new RemoteServiceException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                        + "'. Received status code 200 and messaage:" + client.getResponseText(), ex);
            }
        } else if (status == HttpStatusCodes.Accepted.getStatus()) {
            try {
                VRI taskUri = new VRI(client.getResponseText());
                TaskSpider tskSpider = new TaskSpider(taskUri);
                return tskSpider.parse();
            } catch (final URISyntaxException ex) {
                throw new RemoteServiceException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                        + "'. Received status code 202 and messaage:" + client.getResponseText(), ex);
            }
        }

        return null;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Performs a <code>GET</code> operation on <code>/compound/{id}/feature</code> and
     * obtains the set of available features for this compound. Afterwards, users can
     * invoke the method {@link Compound#getProperty(org.opentox.toxotis.client.VRI,
     * org.opentox.toxotis.util.aa.AuthenticationToken) getProperty} to get the value
     * of a feature on this compound.
     *
     * @return
     *      A set of the URIs of all available features.
     * 
     * @throws ServiceInvocationException
     *      In case the remote service responds with an error status code.
     */
    public Set<VRI> listAvailableFeatures() throws ServiceInvocationException {
        VRI featuresUri = new VRI(getUri()).augment("feature");
        GetHttpClient client = new GetHttpClient(featuresUri);
        Set<VRI> availableUris = new HashSet<VRI>();
        for (VRI fUri : client.getResponseUriList()) {
            availableUris.add(new VRI(fUri));
        }
        return availableUris;
    }

    /**
     * Get the <b>depiction</b> of the compound using some depiction service. The
     * service generates the depiction of the chemical compound based on its SMILES
     * string. By default this method performs a GET HTTP request on the remote
     * location identified by the URI of the compound and acquires with Header
     * <code>Accept: chemical/x-daylight-smiles</code> and acquires the SMILES
     * representation of the chemical structure. Afterwards, this is POSTed to the
     * default depiction service at <code>http://apps.ideaconsult.net:8080/ambit2/
     * depict/cdk?query={smiles}</code> and the result is cast as an instance of
     * <code>javax.swing.ImageIcon</code>.
     * @param token
     *      Authentication token used to be granted access to the compound and
     *      depiction services
     * @return
     *      Returns the depiction of the chemical compound as an ImageIcon.
     * @throws ServiceInvocationException
     *      In case the authentication fails or the user is not authorized to perform some
     *      request (e.g. access the compound or depiction service) or the coumpound or
     *      depiction services respond in an unexpected manner (e.g. return an error
     *      status code like 500 or 503).
     * @deprecated 
     *
     */
    @Deprecated
    public ImageIcon getDepictionFromRemote(AuthenticationToken token) 
            throws ServiceInvocationException {
        return depiction;
    }

    /**
     * Wraps the compound in a dataset object. The URI of the dataset can be specified
     * by the input argument of this method.
     *
     * @param datasetUri
     *      The identifier (URI) of the dataset which acts as a wrapper to the compound.
     *      If set to <code>null</code>, an anonymous node will be created for the dataset
     *      resource.
     * 
     * @return
     *      A dataset object that wraps the compound.
     *
     * @throws ToxOtisException
     *      In case the provided URI is not a valid dataset URI (does not comply
     *      with the OpenTox standards).
     * @throws ServiceInvocationException
     *      Service invocation exception.
     */
    public Dataset wrapInDataset(VRI datasetUri) 
            throws ToxOtisException, ServiceInvocationException {
        Dataset ds = new Dataset(datasetUri);
        ds.getDataEntries().add(new DataEntry(this, new ArrayList<FeatureValue>()));
        return ds;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method for performing similarity search for this compound given a similarity
     * measure which is usually a number between 0 and 1.
     * @param similarity
     *      Similarity Index. The smaller this index is, the larger the length of
     *      the resulting set will be. Setting it equal to 1 will return just the submitted
     *      compound.
     * @param service
     *      Remote similarity search service to be used for obtaining the list of
     *      similar compounds as a URI list. The remote invocation is a GET method
     *      applied on <code>service?search={smiles_string}&amp;threshold={threshold}</code>
     * @param token
     *      Authentication token used to obtain the SMILES string of the token and
     *      acquire access to the similarity service.
     * @return
     *      ParameterValue of URIs that are similar to the compound on which the method is
     *      applied up to a certain threshold.
     * @throws ServiceInvocationException
     *      In case the remote service responds with a non-success status code
     *      like 400 (bad request/bad smiles string) or 500 (internal error of
     *      the server) or if authentication or authorization fails.
     */
    public Set<VRI> getSimilar(double similarity, VRI service, AuthenticationToken token) 
            throws ServiceInvocationException {
        /** Download the string representation of a */
        StringWriter smilesWriter = new StringWriter();
        download(smilesWriter, Media.CHEMICAL_SMILES, token);
        String smilesDownloaded = smilesWriter.toString().trim();
        VRI similarityService = new VRI(service);
        similarityService.addUrlParameter("search", smilesDownloaded).addUrlParameter("threshold", similarity);
        GetHttpClient client = null;
        Set<VRI> resultSet = null;
        try {
            client = new GetHttpClient(similarityService);
            client.setMediaType(Media.TEXT_URI_LIST);
            client.authorize(token);

            int status = client.getResponseCode();
            if (status != HttpStatusCodes.Success.getStatus()) { //TODO: Tasks??? 201? 202?
                throw new RemoteServiceException("Received a status code '" + status + "' from the service at"
                        + similarityService);
            }
            resultSet = client.getResponseUriList();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Stream Could Not Close", ex);
                }
            }
        }
        return resultSet;
    }

    /**
     * Get a set of URIs for similar compounds using the default similarity search
     * service at <code>http://apps.ideaconsut.net:8080/ambit2/query/similarity</code>
     * which is based on the Tanimoto Distance between compounds.
     * @param similarity
     *      Similarity threshold; a number between 0 and 1.
     * @param token
     *      Authentication token used to obtain the SMILES string of the token and
     *      acquire access to the similarity service.
     * @return
     *      ParameterValue of URIs that are similar to the compound on which the method is
     *      applied up to a certain threshold.
     * @throws ServiceInvocationException
     *      In case the remote service responds with a non-success status code
     *      like 400 (bad request/bad smiles string) or 500 (internal error of
     *      the server) or if authentication or authorization fails.
     */
    public Set<VRI> getSimilar(double similarity, AuthenticationToken token) 
            throws ServiceInvocationException {
        return getSimilar(similarity, Services.ideaconsult().augment("query", "similarity"), token);
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public ImageIcon getDepiction(String depictionService) {
        if (depiction != null) {
            return depiction;
        } else {
            if (depictionService == null) {
                depictionService = "http://apps.ideaconsult.net:8080/ambit2/depict/cdk?search=%s";
            }
            if (getSmiles() == null) { // no smiles - no depiction
                depiction = null;
            } else {
                // Smiles - URL encoded:
                String smilesUrlEncoded = null;
                try {
                    smilesUrlEncoded = URLEncoder.encode(getSmiles(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    logger.error(null, ex);
                    throw new IllegalArgumentException(ex);
                }
                VRI requestDepictionVri = null;
                try {
                    requestDepictionVri = new VRI(String.format(depictionService, smilesUrlEncoded));
                } catch (URISyntaxException ex) {
                    logger.error(null, ex);
                    throw new IllegalArgumentException(ex);
                }
                IGetClient depictionClient = ClientFactory.createGetClient(requestDepictionVri);
                depictionClient.setMediaType(Media.IMAGE_PNG);
                try {
                    depiction = new ImageIcon(requestDepictionVri.toURI().toURL());
                } catch (MalformedURLException ex) {
                    logger.error(null, ex);
                    throw new IllegalArgumentException(ex);
                }
                try {
                    depictionClient.close();
                } catch (IOException ex) {
                    logger.error(null, ex);
                    throw new IllegalArgumentException(ex);
                }
            }
        }
        if (depiction != null) {
            if (!(depiction.getIconHeight() > 0 && depiction.getIconWidth() > 0)) {
                depiction = null;
            }
        }
        if (depiction == null) {
            try {
                try {
                    depiction = new ImageIcon(new VRI(getUri().toString()).
                            addUrlParameter("media", "image/png").toURI().toURL());
                } catch (URISyntaxException ex) {
                    logger.error(null, ex);
                    throw new IllegalArgumentException(ex);
                }
            } catch (MalformedURLException ex) {
                logger.error(null, ex);
                throw new IllegalArgumentException(ex);
            }
        }
        return depiction;
    }

    public void setDepiction(ImageIcon depiction) {
        this.depiction = depiction;
    }

    public String getCasrn() {
        return casrn;
    }

    public void setCasrn(String casrn) {
        this.casrn = casrn;
    }

    public String getEinecs() {
        return einecs;
    }

    public void setEinecs(String einecs) {
        this.einecs = einecs;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public Set<Conformer> getConformers() {
        if (conformers == null) {
            try {
                conformers = new HashSet<Conformer>(listConformers(null));


            } catch (ServiceInvocationException ex) {
                Logger.getLogger(Compound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conformers;
    }

    public void setConformers(Set<Conformer> conformers) {
        this.conformers = conformers;
    }

    /**
     * The molecular structure or a compound (MOL file, SDF etc).
     * @return The molecular structure as a string
     */
    public String getMolecularStructure() {
        return molecularStructure;
    }

    /**
     * Define the molecular structure or a compound (MOL file, SDF etc).
     * 
     * @param molecularStructure
     *      The molecualar structure as a String.
     */
    public void setMolecularStructure(String molecularStructure) {
        this.molecularStructure = molecularStructure;
    }
}

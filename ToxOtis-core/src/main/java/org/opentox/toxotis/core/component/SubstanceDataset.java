/*
 *
 * Jaqpot - version 3
 *
 * The JAQPOT-3 web services are OpenTox API-1.2 compliant web services. Jaqpot
 * is a web application that supports model training and data preprocessing algorithms
 * such as multiple linear regression, support vector machines, neural networks
 * (an in-house implementation based on an efficient algorithm), an implementation
 * of the leverage algorithm for domain of applicability estimation and various
 * data preprocessing algorithms like PLS and data cleanup.
 *
 * Copyright (C) 2009-2014 Pantelis Sopasakis & Charalampos Chomenides & Lampovas Nikolaos
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
import com.hp.hpl.jena.util.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @author Nikolaos Lampovas
 */
public class SubstanceDataset extends OTPublishable<SubstanceDataset>{
    
    private long timeInstancesConversion = -1;
    private long timeDownload = -1;
    private long timeParse = -1;
    private List<DataEntry> dataEntries = new ArrayList<DataEntry>();
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Dataset.class);
    private static final int HASH_OFFSET = 7, HASH_MOD = 29, PERCENTAGE_WHEN_COMPLETE = 100;
    private static final String INACTIVE_TOKEN_MSG = "The Provided token is inactive";
    private String csv;
/**
     * Constructor for a Dataset object providing its URI.
     * @param uri
     *      The URI of the created Dataset
     * @throws ToxOtisException
     *      In case the provided URI is not a valid dataset URI according to the
     *      OpenTox specifications.
     */
    public SubstanceDataset(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!SubstanceDataset.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Dataset uri according to the OpenTox specifications.");
            }
        }
        setMeta(null);
    }
    
    /**
     * Dummy constructor for a dataset. Creates an empty dataset.
     */
    public SubstanceDataset() {
    }


    /**
     * Create a new Dataset object providing a list of data entries.
     * @param dataEntries
     */
    public SubstanceDataset(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    @Override
    public Future<VRI> publish(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        return super.publish(vri, token); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Future<VRI> publish(VRI vri, AuthenticationToken token, ExecutorService executor) throws ServiceInvocationException {
        return super.publish(vri, token, executor); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
    
        int status;
        
        String remoteResult;

        InputStream is = (InputStream) new ByteArrayInputStream(csv.getBytes());
        IPostClient client = null;
        try {
            client = ClientFactory.createPostClient(vri);
        } catch (Exception ex) {
        }

        client.setContentType(Media.MEDIA_MULTIPART_FORM_DATA);
        client.setMediaType(Media.TEXT_URI_LIST);
        client.addPostParameter("da_uri", "new");
        client.setPostable(is);
        client.setPostableFilename("files[]", "testCSV.csv");
        client.post();

        status = client.getResponseCode();
        remoteResult = client.getResponseText();
        Task dsUpload = new Task();
        
        logger.debug("Publishing >> Response : " + remoteResult);
        logger.debug("Publishing >> STATUS   : " + status);
        if (status == HttpStatusCodes.Success.getStatus()) {
            dsUpload.setPercentageCompleted(PERCENTAGE_WHEN_COMPLETE);
            dsUpload.setStatus(Task.Status.COMPLETED);
            try {
                dsUpload.setUri(new VRI(remoteResult));
                dsUpload.setResultUri(vri);
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return dsUpload;
    
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest(INACTIVE_TOKEN_MSG);
        }
        return publishOnline(Services.ideaconsultenanomapper().augment("substance"), token);
    }

    @Override
    protected SubstanceDataset loadFromRemote(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }
}

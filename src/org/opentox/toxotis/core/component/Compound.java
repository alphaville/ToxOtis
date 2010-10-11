package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.factory.FeatureFactory;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.util.spiders.TypedValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.util.TaskRunner;
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
public class Compound extends OTPublishable<Compound> {

    /**
     * Construct a new compound identified by its URI. You should provide a
     * valid compound URI. Conformer URIs are not acceptable in this constructor.
     * The pattern matching your URI should be: <br/><br/>
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
        if (uri != null) {
            // Compound and Conformer URIs are allowed
            if (!Compound.class.equals(uri.getOpenToxType()) && !Conformer.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Compound uri according to the OpenTox specifications.");
            }
        }
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
     * @return
     *      List of conformers
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503.
     */
    public Set<Conformer> listConformers(AuthenticationToken token) throws ToxOtisException {
        VRI newUri = new VRI(uri).augment("conformer");
        if (token != null) {
            newUri.appendToken(token);
        }
        GetClient client = new GetClient(newUri);
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        Set<VRI> uriList = client.getResponseUriList();
        Set<Conformer> conformers = new HashSet<Conformer>();
        for (VRI confUri : uriList) {
            conformers.add(new Conformer(new VRI(confUri)));
        }
        return conformers;
    }

//TODO: It doesn't work oddly
    public Dataset getPropertiesByOnt(OntologicalClass featurePrototype, AuthenticationToken token) throws ToxOtisException {
        Set<VRI> features = FeatureFactory.lookupSameAs(featurePrototype, token);
        return getProperties(token, (VRI[]) features.toArray(new VRI[features.size()]));
    }

    public TypedValue<?> getAssayProperty(OntologicalClass featurePrototype, AuthenticationToken token) throws ToxOtisException {
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
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public TypedValue<?> getProperty(Feature feature, AuthenticationToken token) throws ToxOtisException {
        /**
         *TODO: Should this request include the uri parameters for the feature?
         */
        VRI dsUri = new VRI(getUri()).addUrlParameter("feature_uris[]", feature.getUri().toString());
        if (token != null) {
            dsUri.appendToken(token);
        }
        GetClient client = new GetClient();
        client.setUri(dsUri);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        OntModel model = client.getResponseOntModel();
        RDF.type.inModel(model);
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.Dataset().inModel(model));
        Resource baseResource = null;
        if (dsIt.hasNext()) {
            baseResource = dsIt.nextStatement().getSubject();
            DatasetSpider dsSpider = new DatasetSpider(baseResource, model);
            Dataset ds = dsSpider.parse();
            List<DataEntry> data = ds.getDataEntries();
            if (data != null && data.size() >= 1) {
                DataEntry firstEntry = ds.getDataEntries().get(0);
                if (firstEntry != null && firstEntry.getFeatureValues().size() >= 1) {
                    FeatureValue fVal = firstEntry.getFeatureValue(0);
                    if (fVal != null) {
                        return fVal.getValue();
                    }
                }
            }
        }
        return null;
    }

    public TypedValue getProperty(VRI uri, AuthenticationToken token) throws ToxOtisException {
        Feature tempFeat = new Feature(uri);
        return getProperty(tempFeat, token);
    }

    public Dataset getProperties(AuthenticationToken token, VRI... featureUris) throws ToxOtisException {
        VRI dsUri = new VRI(getUri());
        for (VRI featureUri : featureUris) {
            dsUri.addUrlParameter("feature_uris[]", featureUri.toString());
        }

        if (token != null) {
            dsUri.clearToken().appendToken(token);
        }
        GetClient client = new GetClient();
        client.setUri(dsUri);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        OntModel model = client.getResponseOntModel();
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.Dataset().inModel(model));
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
        Individual indiv = model.createIndividual(compoundUri, OTClasses.Compound().inModel(model));
        getMeta().attachTo(indiv, model);
        return indiv;
    }

    /**
     * This method is not implemented yet!
     * @param vri
     *      Identifier of the location from where the RDF document should be downloaded from
     *      and parsed into a Compound object.
     * @return
     *      Parsed instance of the component into an instance of Compound.
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data.
     */
    @Override
    protected Compound loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        /** Handle provided token */
        if (token != null) {
            // Replace existing token with the new one
            vri.clearToken().appendToken(token);
        }
        PostClient client = new PostClient(vri);
        client.setContentType(Media.APPLICATION_RDF_XML.getMime());
        client.setPostable(asOntModel());
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        client.post();
        int status;
        try {
            status = client.getResponseCode();
            if (status == 200) {
                Task readyTask = new Task();
                readyTask.setPercentageCompleted(100);
                readyTask.seStatus(Task.Status.COMPLETED);
                try {
                    readyTask.setResultUri(new VRI(client.getResponseText()));
                    return readyTask;
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                            + "'. Received status code 200 and messaage:" + client.getResponseText());
                }
            } else if (status == 202) {
                try {
                    VRI taskUri = new VRI(client.getResponseText());
                    TaskSpider tskSpider = new TaskSpider(taskUri);
                    return tskSpider.parse();
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                            + "'. Received status code 202 and messaage:" + client.getResponseText());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
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
     * @throws ToxOtisException
     *      In case the remote service responds with an error status code.
     */
    public Set<VRI> listAvailableFeatures() throws ToxOtisException {
        VRI featuresUri = new VRI(uri).augment("feature");
        GetClient client = new GetClient(featuresUri);
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
     * @throws ToxOtisException
     *      In case the authentication fails or the user is not authorized to perform some
     *      request (e.g. access the compound or depiction service) or the coumpound or
     *      depiction services respond in an unexpected manner (e.g. return an error
     *      status code like 500 or 503).
     *
     */
    public ImageIcon getDepictionFromRemote(AuthenticationToken token) throws ToxOtisException {
        ImageIcon depiction;
        try {
            StringWriter writer = new StringWriter();
            download(writer, Media.CHEMICAL_SMILES, token);
            String smiles = writer.toString();
            depiction = new ImageIcon(new URL(Services.ideaCdkImage().
                    addUrlParameter("query", smiles).appendToken(token).toString()));
        } catch (MalformedURLException ex) {
            throw new ToxOtisException(ex);
        }
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
     */
    public Dataset wrapInDataset(VRI datasetUri) throws ToxOtisException {
        Dataset ds = new Dataset(datasetUri);
        ds.getDataEntries().add(new DataEntry(this, new ArrayList<FeatureValue>()));
        return ds;
    }

    /**
     * Calculates
     * @param descriptorCalculationAlgorithm
     *      A descriptor calculation algorithm.
     * @param token
     *      Token used to authenticate the client and grant it access to the
     *      various OpenTox resources. 
     * @param serviceConfiguration
     *      A string array (<code>String[]</code>) used for fine tuning of the
     *      remote service. Successive pairs of values act as a parameter name -
     *      parameter value pair which is posted to the remote service.
     * @return
     * @throws ToxOtisException
     */
    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ToxOtisException {
        PostClient client = new PostClient(descriptorCalculationAlgorithm);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        descriptorCalculationAlgorithm.clearToken().appendToken(token);
        PostClient pc = new PostClient(descriptorCalculationAlgorithm);
        pc.addPostParameter("dataset_uri", getUri().toString()); // dataset_uri={compound_uri}
        if (serviceConfiguration != null) {
        }
        pc.setMediaType(Media.TEXT_URI_LIST);
        pc.post();
        String taskUri = pc.getResponseText();
        try {
            TaskSpider taskSpider = new TaskSpider(new VRI(taskUri));
            return taskSpider.parse();
        } catch (URISyntaxException ex) {
            throw new ToxOtisException("The remote service at " + descriptorCalculationAlgorithm
                    + " returned an invalid task URI : " + taskUri, ex);
        }
    }

    /**
     * Calculates all available descriptors using a remote descriptor calculation
     * service.
     * 
     * @param descriptorCalculationAlgorithm
     * @return
     * @throws ToxOtisException
     */
    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ToxOtisException {
        return calculateDescriptors(descriptorCalculationAlgorithm, token, "ALL", "true");
    }

    public Future<VRI> calculateDescriptorsDataset(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ToxOtisException {
        return calculateDescriptorsDataset(descriptorCalculationAlgorithm, token, Executors.newSingleThreadExecutor(), serviceConfiguration);
    }


    /**
     *
     * @param descriptorCalculationAlgorithm
     *      The URI of an OpenTox descriptor calculation algorithm. A compound or
     *      a dataset is posted to a descriptor calculation algorithm and the expected
     *      result is a dataset containing the submitted compound(s) and the calculated
     *      descriptor values for each compound.
     * @param token
     *      Authentication token used for accessing the descriptor calculation
     *      service.
     * @param executor
     *      An executor used to sumbit the thread in. Be aware that the executor
     *      is not shutdown in this method so it is up to the user whether it should
     *      be shutdown or not.
     * @param serviceConfiguration
     *      A string array (<code>String[]</code>) used for fine tuning of the
     *      remote service. Successive pairs of values act as a parameter name -
     *      parameter value pair which is posted to the remote service.
     * @return
     *      A <code>Future</code> waiting for the background job to complete and
     *      and return the URI of a Dataset with the calculated descriptors for
     *      this Compound.
     * @throws ToxOtisException
     *
     */
    public Future<VRI> calculateDescriptorsDataset(VRI descriptorCalculationAlgorithm,
            AuthenticationToken token, ExecutorService executor, String... serviceConfiguration) throws ToxOtisException {
        Task t = calculateDescriptors(descriptorCalculationAlgorithm, token, serviceConfiguration);
        final TaskRunner taskRunner = new TaskRunner(t);
        Future<VRI> future = executor.submit(new Callable<VRI>() {

            public VRI call() throws Exception {
                Task result = taskRunner.call();
                VRI resultUri = null;
                if (result != null) {
                    resultUri = result.getResultUri();
                }
                return resultUri;
            }
        });
        return future;
    }

    public Future<VRI> calculateDescriptorsDataset(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ToxOtisException {
        return calculateDescriptorsDataset(descriptorCalculationAlgorithm, token, "ALL", "true");
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
     *      similar compounds as a URI list. The remote invokation is a GET method
     *      applied on <code>service?search={smiles_string}&threshold={threshold}</code>
     * @param token
     *      Authentication token used to obtain the SMILES string of the token and
     *      acquire access to the similarity service.
     * @return
     *      Set of URIs that are similar to the compound on which the method is
     *      applied up to a certain threshold.
     * @throws ToxOtisException
     *      In case the remote service responds with a non-success status code
     *      like 400 (bad request/bad smiles string) or 500 (internal error of
     *      the server).
     */
    public Set<VRI> getSimilar(double similarity, VRI service, AuthenticationToken token) throws ToxOtisException {
        /** Download the string representation of a */
        StringWriter smilesWriter = new StringWriter();
        download(smilesWriter, Media.CHEMICAL_SMILES, token);
        String smiles = smilesWriter.toString().trim();
        VRI similarityService = new VRI(service);
        similarityService.clearToken().appendToken(token).addUrlParameter("search", smiles).addUrlParameter("threshol", similarity);
        GetClient client = null;
        Set<VRI> resultSet = null;
        try {
            client = new GetClient(similarityService);
            client.setMediaType(Media.TEXT_URI_LIST);
            try {
                int status = client.getResponseCode();
                if (status != 200) { // TODO: Tasks??? 201? 202?
                    throw new ToxOtisException("Received a status code '" + status + "' from the service at"
                            + similarityService.clearToken());
                }
            } catch (IOException ex) {
                throw new ToxOtisException(ErrorCause.ConnectionException, ex);
            }
            resultSet = client.getResponseUriList();
        } catch (ToxOtisException ex) {
            throw ex;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
        return resultSet;
    }

    public Set<VRI> getSimilar(double similarity, AuthenticationToken token) throws ToxOtisException {
        return getSimilar(similarity, Services.ideaconsult().augment("query", "similarity"), token);
    }
}

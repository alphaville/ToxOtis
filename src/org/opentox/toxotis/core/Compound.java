package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.factory.FeatureFactory;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.util.spiders.TypedValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
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
     *      In case the provided URI is not a valid compound URI
     * @see VRI#getOntologicalClass() Identify the type of a URI
     */
    public Compound(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            //TODO: What happens if one provides a Conformer URI?
            if (!Compound.class.equals(uri.getOpenToxType())) {
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
        List<String> uriList = client.getResponseUriList();
        Set<Conformer> conformers = new HashSet<Conformer>();
        for (String confUri : uriList) {
            try {
                conformers.add(new Conformer(new VRI(confUri)));
            } catch (URISyntaxException ex) {
                throw new ToxOtisException("Remote service at '" + uri.getStringNoQuery() + "' returned a uri list "
                        + "that contains the invalid URI '" + confUri + "'", ex);
            }
        }
        return conformers;
    }
    
//TODO: It doesn't work oddly
    public Dataset getProperties(OntologicalClass featurePrototype, AuthenticationToken token) throws ToxOtisException {
        Set<VRI> features = FeatureFactory.lookupSameAs(OTEchaEndpoints.Mutagenicity(), token);
        for(VRI vri : features){
            System.out.println(vri.toString());
        }
        return getProperties(token, (VRI[]) features.toArray());
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
        client.setMediaType("application/rdf+xml");
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
            dsUri.appendToken(token);
        }
        GetClient client = new GetClient();
        client.setUri(dsUri);
        client.setMediaType("application/rdf+xml");
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
                readyTask.setHasStatus(Task.Status.COMPLETED);
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

    public Set<VRI> listAvailableFeatures() throws ToxOtisException {
        VRI featuresUri = new VRI(uri).augment("feature");
        GetClient client = new GetClient();
        client.setUri(featuresUri);
        Set<VRI> availableUris = new HashSet<VRI>();
        for (String fUri : client.getResponseUriList()) {
            try {
                availableUris.add(new VRI(fUri));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
        return availableUris;
    }

    public ImageIcon getDepictionFromRemote() throws ToxOtisException {
        ImageIcon depiction;
        try {           
            StringWriter writer = new StringWriter();
            download(writer, Media.CHEMICAL_SMILES, null);
            String smiles = writer.toString();
            System.out.println(smiles);
            depiction = new ImageIcon(new URL(Services.IDEACONSULT_CDK_IMAGE.addUrlParameter("query", smiles).toString()));
        } catch (MalformedURLException ex) {
            throw new ToxOtisException(ex);
        }
        return depiction;
    }
}

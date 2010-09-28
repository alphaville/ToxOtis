package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.CompoundSpider;

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

    /*
     * SHOULD THERE BE A CONFORMER URI IN THE COMPOUND?
     */
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
     * Return the identifier of the compound as it is extracted from its URI. For
     * example http://someserver.com/compound/phenol will return <code>phenol</code>.
     * @return
     *      Compound ID.
     */
    public String getCompoundId() {
        String[] uriFragments = getUri().getStringNoQuery().split("/compound/");
        if (uriFragments.length == 2) {
            if (!uriFragments[1].contains("/conformer/")) {
                return uriFragments[1];
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
            newUri.addUrlParameter("tokenid", token.stringValue());
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

    /**
     * POSTs a file to the compound service using a specified Content-type header
     * in order to create a new Compound. The created compound is returned
     * to the user.
     * @param sourceFile
     *      File where information about the compound are stored. Can be a <code>mol</code>
     *      file, a <code>CML</code> one, an <code>SD</code> file or other file
     *      format that is accepted by the compound service.
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @param fileType
     *      The Content-type of the file to be posted.
     * @return
     *      The compound created by the Service.
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public static Compound publishFromFile(File sourceFile, String fileType, AuthenticationToken token) throws ToxOtisException {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            PostClient postClient = new PostClient(
                    new VRI(String.format(Services.IDEACONSULT.toString(), "compound")));
            fr = new FileReader(sourceFile);
            br = new BufferedReader(fr);
            String representation = "";
            String line = br.readLine();
            while (line != null) {
                representation += line;
                line = br.readLine();
            }
            postClient.addPostParameter("compound", representation);
            postClient.setContentType(fileType);
            postClient.post();
            VRI newVRI = new VRI(postClient.getResponseText());
            CompoundSpider compoundSpider = new CompoundSpider(newVRI);
            return compoundSpider.parse();
        } catch (FileNotFoundException ex) {
            throw new ToxOtisException(ex);
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        } catch (URISyntaxException ex) {
            throw new ToxOtisException(ex);
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Compound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      File where the data should be stored.
     * @param fileType
     *      Content type of the downloaded representation
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void downloadAsFile(File destination, String fileType, AuthenticationToken token) throws ToxOtisException {
        VRI newUri = new VRI(getUri());
        if (token != null) {
            newUri.addUrlParameter("tokenid", token.stringValue());
        }
        GetClient client = new GetClient(newUri);
        client.setMediaType(fileType);
        try {
            /* REMOTE STREAM */
            InputStream remote = client.getRemoteStream();
            InputStreamReader isr = new InputStreamReader(remote);
            BufferedReader remoteReader = new BufferedReader(isr);
            /* FILE STREAM */
            FileWriter fileWriter = new FileWriter(destination);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String line = null;
            while ((line = remoteReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            Throwable failure = null;

            if (remote != null) {
                try {
                    remote.close();
                } catch (Throwable th) {
                    failure = th;
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (Throwable th) {
                    failure = th;
                }
            }

            if (remoteReader != null) {
                try {
                    remoteReader.close();
                } catch (Throwable th) {
                    failure = th;
                }
            }


            if (bufferedWriter != null) {
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (Throwable th) {
                    failure = th;
                }
            }

            if (failure != null) {
                if (failure instanceof IOException) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, failure);
                } else {
                    throw new RuntimeException(failure);
                }
            }

        } catch (IOException ex) {
            throw new ToxOtisException("Remote stream from '" + newUri.getStringNoQuery() + "' is not readable!", ex);
        }
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Compound loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

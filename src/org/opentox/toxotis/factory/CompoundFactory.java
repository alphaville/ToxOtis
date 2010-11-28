package org.opentox.toxotis.factory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 * CompoundFactory provides methods for creating new compounds and publishing them
 * to remote compound services. You can create a new compound using an SDF, MOL or
 * SMILES representation of it provided as a file.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CompoundFactory {

    private static CompoundFactory factory = null;

    /**
     * Returns the CompoundFactory object associated with the current Java application.
     * All factories in ToxOtis are singletons and have a single access point.
     *
     * @return
     *      The CompoundFactory object associated with the current Java application.
     */
    public static CompoundFactory getInstance() {
        if (factory == null) {
            factory = new CompoundFactory();
        }
        return factory;
    }

    /** dummy constructor */
    private CompoundFactory() {
        super();
    }

    /**
     * POSTs a file to a default compound service using a specified Content-type header
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
    public Task publishFromFile(File sourceFile, String fileType, AuthenticationToken token)
            throws ToxOtisException {
        return publishFromFile(sourceFile, fileType, token, Services.ideaconsult().augment("compound").toString());
    }

    /**
     * POSTs a file to a default compound service using a specified Content-type header
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
    public Task publishFromFile(File sourceFile, Media fileType, AuthenticationToken token)
            throws ToxOtisException {
        return publishFromFile(sourceFile, fileType.getMime(), token, Services.ideaconsult().augment("compound").toString());
    }

    /**
     * POSTs a file to a specified compound service using a specified Content-type header
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
     * @param service
     *      The URI of the service on which the new Compound will be posted.
     * @return
     *      The compound created by the Service.
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(
            File sourceFile, String fileType, AuthenticationToken token, String service)
            throws ToxOtisException {
        try {
            PostHttpClient postClient = new PostHttpClient(
                    new VRI(service).appendToken(token));
            postClient.setPostable(sourceFile);
            postClient.setContentType(fileType);
            postClient.setMediaType(Media.TEXT_URI_LIST.getMime());
            postClient.post();
            VRI newVRI = new VRI(postClient.getResponseText());
            int responseStatus = -1;
            try {
                responseStatus = postClient.getResponseCode();
            } catch (IOException ex) {
                Logger.getLogger(CompoundFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (responseStatus == 202) {
                TaskSpider tskSp = new TaskSpider(newVRI);
                return tskSp.parse();
            } else if (responseStatus == 200) {
                Task t = new Task();
                t.setResultUri(newVRI);
                t.setStatus(Task.Status.COMPLETED);
                return t;
            } else {
                throw new ToxOtisException("HTTP Status : " + responseStatus);
            }
        } catch (URISyntaxException ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * POSTs a file to a specified compound service using a specified Content-type header
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
     *      The Content-type of the file to be posted as an element from {@link Media }.
     * @param service
     *      The URI of the service on which the new Compound will be posted.
     * @return
     *      The compound created by the Service.
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(
            File sourceFile, Media fileType, AuthenticationToken token, String service)
            throws ToxOtisException {
        return publishFromFile(sourceFile, fileType.getMime(), token, service);
    }


    public Set<VRI> lookUpComponent(VRI lookUpService, String keyword) {
        throw new UnsupportedOperationException();
    }
}

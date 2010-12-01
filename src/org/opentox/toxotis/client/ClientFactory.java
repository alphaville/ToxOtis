package org.opentox.toxotis.client;

import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.https.GetHttpsClient;
import org.opentox.toxotis.client.https.PostHttpsClient;

/**
 * Factory for creating clients. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ClientFactory {

    /**
     * Create a Get-client as an instance of {@link IGetClient } providing it's
     * URI. Either an HTTP or an HTTPS client is created according to the protocol
     * of the provided URI.
     * @param actionUri
     *      The URI on which the client addresses the request.
     * @return
     *      Instance of a GET-client.
     */
    public static IGetClient createGetClient(VRI actionUri) {
        String protocol = actionUri.getProtocol();
        if (protocol.equals("http")) {
            return new GetHttpClient(actionUri);
        } else if (protocol.equals("https")) {
            return new GetHttpsClient(actionUri);
        }
        return null;
    }

    /**
     * Create a POST-client as an instance of {@link IPostClient } providing it's
     * URI. Either an HTTP or an HTTPS client is created according to the protocol
     * of the provided URI.
     * @param actionUri
     *      The URI on which the client addresses the request.
     * @return
     *      Instance of a POST-client.
     */
    public static IPostClient createPostClient(VRI actionUri) {
        String protocol = actionUri.getProtocol();
        if (protocol.equals("http")) {
            return new PostHttpClient(actionUri);
        } else if (protocol.equals("https")) {
            return new PostHttpsClient(actionUri);
        }
        return null;
    }
}

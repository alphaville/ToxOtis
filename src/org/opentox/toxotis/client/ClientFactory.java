package org.opentox.toxotis.client;

import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.https.GetHttpsClient;
import org.opentox.toxotis.client.https.PostHttpsClient;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ClientFactory {

    public static IGetClient createGetClient(VRI actionUri) {
        String protocol = actionUri.getProtocol();
        if (protocol.equals("http")) {
            return new GetHttpClient(actionUri);
        } else if (protocol.equals("https")) {
            return new GetHttpsClient(actionUri);
        }
        return null;
    }

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

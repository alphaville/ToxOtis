package org.opentox.toxotis.ontology.collection;

import java.net.URISyntaxException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.ResourceValue;

/**
 * Collection of HTTP methods as ontological individuals, instances of {@link OTRestClasses#HTTPMethod() }
 * used to describe the available methods by a web service. These instances are related to the
 * REST interface self-definition of a web service which is available on GET from its
 * RDF representation.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpMethods {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpMethods.class);
    private static VRI POST_VRI, GET_VRI, PUT_VRI, DELETE_VRI, OPTIONS_VRI, HEAD_VRI;

    static {
        try {
            POST_VRI = new VRI(OTRestClasses.NS + "POST");
            GET_VRI = new VRI(OTRestClasses.NS + "GET");
            PUT_VRI = new VRI(OTRestClasses.NS + "PUT");
            DELETE_VRI = new VRI(OTRestClasses.NS + "DELETE");
            HEAD_VRI = new VRI(OTRestClasses.NS + "HEAD");
            OPTIONS_VRI = new VRI(OTRestClasses.NS + "OPTIONS");
        } catch (URISyntaxException ex) {
            logger.error(null, ex);
        }
    }
    public static ResourceValue GET = new ResourceValue(GET_VRI, OTRestClasses.HTTPMethod());
    public static ResourceValue POST = new ResourceValue(POST_VRI, OTRestClasses.HTTPMethod());
    public static ResourceValue PUT = new ResourceValue(PUT_VRI, OTRestClasses.HTTPMethod());
    public static ResourceValue DELETE = new ResourceValue(DELETE_VRI, OTRestClasses.HTTPMethod());
    public static ResourceValue HEAD = new ResourceValue(HEAD_VRI, OTRestClasses.HTTPMethod());
    public static ResourceValue OPTIONS = new ResourceValue(OPTIONS_VRI, OTRestClasses.HTTPMethod());

    public enum MethodsEnum {

        GET(HttpMethods.GET),
        POST(HttpMethods.POST),
        PUT(HttpMethods.PUT),
        DELETE(HttpMethods.DELETE),
        HEAD(HttpMethods.HEAD),
        OPTIONS(HttpMethods.OPTIONS);
        private ResourceValue rv;

        public ResourceValue getResourceValue() {
            return rv;
        }

        private MethodsEnum(ResourceValue rv) {
            this.rv = rv;
        }
    }
}

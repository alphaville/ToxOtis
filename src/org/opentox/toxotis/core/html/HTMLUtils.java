package org.opentox.toxotis.core.html;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;

/**
 * Utilities for automatically creating HTML code.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLUtils {

    /**
     * Returns a list of objects in an HTML container out of a given list of objects.
     * The method creates a list invoking the method toString() defined in Object for
     * each one of the objects in the list.
     *
     * @param objects
     *      List of objects for which a list in HTML is needed
     * @param listTag
     *      The tag that defines the list. It's default value is <code>ol</code>, so
     *      in case <code>null</code> is provided, a numbered list will be created.
     * @param divElement
     *      The <code>div</code> class to enclose the list. The value <code>_list</code>
     *      is the default in case <code>null</code> is provided.
     * @return
     *      A list of the submitted objects in HTML wrapped in an {@link HTMLContainer }.
     */
    public static HTMLContainer createObjectList(Collection objects, String listTag, String divElement) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : "_list");
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append("<li>");
            sb.append(o.toString());
            sb.append("</li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }

    public static HTMLContainer createLiteralList(Collection<LiteralValue> objects, String listTag, String divElement, boolean displayType) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : "_list");
        StringBuilder sb = new StringBuilder();
        for (LiteralValue o : objects) {
            sb.append("<li>");
            sb.append(o.getValueAsString());
            if (displayType) {
                sb.append(" <sup>^^" + o.getType() + "</sup>");
            }
            sb.append("</li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }

    public static HTMLContainer createResourceList(Collection<ResourceValue> objects, String listTag, String divElement, boolean doLink) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : "_list");
        StringBuilder sb = new StringBuilder();
        for (ResourceValue o : objects) {
            sb.append("<li>");
            if (doLink) {
                sb.append("<a href=\"" + o.getUri().toString() + "\">");
            }
            sb.append(o.getUri().toString());
            if (doLink) {
                sb.append("</a>");
            }
            sb.append("  <sup>(" + o.getOntologicalClass().getName() + ")</sup>");
            sb.append("</li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }

    public static HTMLContainer createOntClassList(Collection<OntologicalClass> objects, String listTag, String divElement, boolean doLink) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : "_list");
        StringBuilder sb = new StringBuilder();
        for (OntologicalClass o : objects) {
            sb.append("<li>");
            if (doLink) {
                sb.append("<a href=\"" + o.getUri().toString() + "\">");
            }
            sb.append(o.getUri().toString());
            if (doLink) {
                sb.append("</a>");
            }
            sb.append(" </li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }


    public static String linkUrlsInText(String plainText) {
        /*
         * Check out the snippet at http://blog.houen.net/java-get-url-from-string/
         * to potentially improve this code:
         */
        String[] fragments = plainText.split("\\s");// space
        StringBuffer sb = new StringBuffer();
        for (String word : fragments) {
            try {
                URL url = new URL(word);
                sb.append("<a href=\"" + url + "\">" + url + "</a> ");
            } catch (MalformedURLException e) {
                sb.append(word + " ");
            }
        }
        return sb.toString();
    }
}

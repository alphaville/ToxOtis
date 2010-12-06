package org.opentox.toxotis.core.html;

import java.util.Collection;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLUtils {

    /**
     * Returns a list of objects in an HTML container out of a given list of objects.
     * The method
     * @param objects
     * @param listTag
     * @param divElement
     * @return
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
            sb.append("</li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }
}

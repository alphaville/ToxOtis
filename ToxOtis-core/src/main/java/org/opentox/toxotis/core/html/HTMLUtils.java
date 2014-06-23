/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
package org.opentox.toxotis.core.html;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.opentox.toxotis.core.OTComponent;
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
    
    private HTMLUtils(){
        // Hidden Constructor - HTMLUtils is a utility class.
    }

    private static final String LIST_START = "<li>",
            LIST_END = "</li>\n", LIST_ELEMENT = "_list",
            OL_TAG = "ol",
            LINK_PATTERN = "(?:https?|ftps?)://?(?://((?:(([^:@]*):?([^:@]*))?@)?([^:/?#]*)(?::(\\d*))?))?((((?:[^?#/]*/)*)([^?#]*))(?:\\?([^#]*))?(?:#(.*))?)";

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
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : LIST_ELEMENT);
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(LIST_START);
            sb.append(o.toString());
            sb.append(LIST_END);
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : OL_TAG, sb.toString()));
        return builder.getDiv();
    }

    /**
     * Returns a list of objects in an HTML container out of a given list of ToxOtis Components.
     * The method creates a list invoking the method {@link OTComponent#getUri() } for
     * each one of the components in the list.
     *
     * @param objects
     *      List of components for which a uri list in HTML is needed
     * @param listTag
     *      The tag that defines the list. It's default value is <code>ol</code>, so
     *      in case <code>null</code> is provided, a numbered list will be created.
     * @param divElement
     *      The <code>div</code> class to enclose the list. The value <code>_list</code>
     *      is the default in case <code>null</code> is provided.
     * @return
     *      A list of the submitted components in HTML wrapped in an {@link HTMLContainer }.
     */
    public static HTMLContainer createComponentList(Collection<? extends OTComponent> objects, String listTag, String divElement) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : LIST_ELEMENT);
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(LIST_START);
            sb.append(HTMLUtils.linkUrlsInText(((OTComponent) o).getUri().toString()));
            sb.append(LIST_END);
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : OL_TAG, sb.toString()));
        return builder.getDiv();
    }

    /**
     * Returns a list of elements in an HTML container out of a given list of Literals.
     * Users can specify whether the type of each literal should appear in the list.
     *
     * @param objects
     *      List of literals for which a uri list in HTML is needed
     * @param listTag
     *      The tag that defines the list. It's default value is <code>ol</code>, so
     *      in case <code>null</code> is provided, a numbered list will be created.
     * @param divElement
     *      The <code>div</code> class to enclose the list. The value <code>_list</code>
     *      is the default in case <code>null</code> is provided.
     * @param displayType
     *      If <code>true</code> the datatype of each {@link LiteralValue literal} value
     *      will be displayed in the generated list using the notation <code>value^^type</code>.
     * @return
     *      A list of the submitted literals in HTML wrapped in an {@link HTMLContainer }.
     */
    public static HTMLContainer createLiteralList(Collection<LiteralValue> objects, String listTag, String divElement, boolean displayType) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : LIST_ELEMENT);
        StringBuilder sb = new StringBuilder();
        for (LiteralValue o : objects) {
            sb.append(LIST_START);
            sb.append(o.getValueAsString());
            if (displayType) {
                sb.append(" <sup>^^").append(o.getType()).append("</sup>");
            }
            sb.append(LIST_END);
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : OL_TAG, sb.toString()));
        return builder.getDiv();
    }

    /**
     * Returns a list of elements in an HTML container out of a given list of Resources.
     * Users can specify whether the HTML elements will appear as links or in plain text
     * format. The list is compiled as a list of URIs and is constructed my iterated
     * invocation of the method {@link ResourceValue#getUri() }.
     *
     * @param objects
     *      List of resources for which a URI list in HTML is needed
     * @param listTag
     *      The tag that defines the list. It's default value is <code>ol</code>, so
     *      in case <code>null</code> is provided, a numbered list will be created.
     * @param divElement
     *      The <code>div</code> class to enclose the list. The value <code>_list</code>
     *      is the default in case <code>null</code> is provided.
     * @param doLink
     *      If <code>true</code> the URI of each resource will appear as a link, wrapped
     *      in an <code>a</code> tag like &lt;a href="uri"&gt;uri&lt;/a&gt;
     * @return
     *      A list of the submitted resources in HTML wrapped in an {@link HTMLContainer }.
     */
    public static HTMLContainer createResourceList(Collection<ResourceValue> objects, String listTag, String divElement, boolean doLink) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : LIST_ELEMENT);
        StringBuilder sb = new StringBuilder();
        for (ResourceValue o : objects) {
            sb.append(LIST_START);
            if (doLink) {
                sb.append("<a href=\"").append(o.getUri().toString()).append("\">");
            }
            sb.append(o.getUri().toString());
            if (doLink) {
                sb.append("</a>");
            }
            sb.append("  <sup>(").append(o.getOntologicalClass().getName()).append(")</sup>");
            sb.append(LIST_END);
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : "ol", sb.toString()));
        return builder.getDiv();
    }

    /**
     * Returns a list of elements in an HTML container out of a given list of {@link OntologicalClass }
     * instances. Users can specify whether the HTML elements will appear as links or in plain text
     * format. The list is compiled as a list of URIs and is constructed my iterated
     * invocation of the method {@link OntologicalClass#getUri() }.
     *
     * @param objects
     *      List of {@link OntologicalClass ontological classes} for which a URI list in HTML is needed
     * @param listTag
     *      The tag that defines the list. It's default value is <code>ol</code>, so
     *      in case <code>null</code> is provided, a numbered list will be created.
     * @param divElement
     *      The <code>div</code> class to enclose the list. The value <code>_list</code>
     *      is the default in case <code>null</code> is provided.
     * @param doLink
     *      If <code>true</code> the URI of each resource will appear as a link, wrapped
     *      in an <code>a</code> tag like &lt;a href="uri"&gt;uri&lt;/a&gt;
     * @return
     *      A list of the submitted ontological classes in HTML wrapped in an {@link HTMLContainer }.
     */
    public static HTMLContainer createOntClassList(Collection<OntologicalClass> objects, String listTag, String divElement, boolean doLink) {
        HTMLDivBuilder builder = new HTMLDivBuilder(divElement != null ? divElement : LIST_ELEMENT);
        StringBuilder sb = new StringBuilder();
        for (OntologicalClass o : objects) {
            sb.append(LIST_START);
            if (doLink) {
                sb.append("<a href=\"").append(o.getUri()).append("\">");
            }
            sb.append(o.getUri());
            if (doLink) {
                sb.append("</a>");
            }
            sb.append(" </li>\n");
        }
        builder.addComponent(new HTMLTagImpl(listTag != null ? listTag : OL_TAG, sb.toString()));
        return builder.getDiv();
    }

    /**
     *
     * Detects all URLs in a given text and replaces them with HTML links using the
     * <code>a</code> tag. For example the text <code>'My new site is http://mysite.com'</code>
     * is converted into <code>'My new site is <a href="http://mysite.com">http://mysite.com</a>'</code>.
     * The method's implementation is based on a code snippet found at
     * <a href="http://blog.houen.net/java-get-url-from-string/">http://blog.houen.net/java-get-url-from-string/</a>
     *
     * @param plainText
     *      Given text
     * @return
     *      Text where all URLs are replaces by links.
     */
    public static String linkUrlsInText(String plainText) {
        /*
         * Check out the snippet at http://blog.houen.net/java-get-url-from-string/
         * to potentially improve this code:
         */
        Pattern pattern = Pattern.compile(LINK_PATTERN);
        String[] fragments = plainText.split("\\s");// space
        StringBuilder sb = new StringBuilder();
        for (String word : fragments) {
            Matcher matcher = pattern.matcher(word);
            if (matcher.find()) {
                sb.append("<a href=\"").append(word).append("\">").append(word).append("</a> ");
            } else {
                sb.append(word).append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Normalizes a text for use in HTML. 
     * 
     * Replaces all new lines (<code>/n</code>)
     * by the HTMLline-break element and all 
     * ampersands by <code>&amp;</code>.
     * 
     * @param plainText
     *      Text to be HTML-normalized
     * @return 
     *      HTML-normalized text.
     */
    public static String normalizeTextForHtml(String plainText) {
        return plainText.replaceAll("\n", "<br/>").replaceAll("&", "&amp;");
    }
}

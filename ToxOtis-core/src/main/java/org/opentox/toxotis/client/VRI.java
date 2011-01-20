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
package org.opentox.toxotis.client;

import java.io.Serializable;
import org.opentox.toxotis.core.component.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.opentox.toxotis.core.*;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * VRI is an alternative to URI. Being <code>final</code>, the class <code>java.net.URI</code>
 * cannot be subclassed. VRI offers greater flexibility in this context as it stores in a
 * highly structured way the parameters of the URL and applies URL encoding where necessary.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VRI implements Serializable { // Well tested!

    /** The URI as a string */
    private String uri;
    /** The mapping from parameter names to parameter values */
    private ArrayList<Pair<String, String>> urlParams = new ArrayList<Pair<String, String>>();
    /** The standard UTF-8 encoding */
    private static final String URL_ENCODING = "UTF-8";
    private static final String TOKENID = "tokenid";
    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VRI.class);

    /**
     * Keywords that appear in OpenTox URIs.
     */
    private enum UriKeywords {

        Dataset("dataset"),
        Compound("compound"),
        Feature("feature"),
        Algorithm("algorithm"),
        Task("task"),
        BibTex("bibtex"),
        Model("model"),
        Query("query");
        private final String keyword;

        private UriKeywords(final String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }
    }
    private static final String END_SLASH_orNothing = "([^/]+/$|[^/]+)$";

    /**
     * A collection of regular expressions for the identification of
     * OpenTox URI patterns.
     */
    private enum OpenToxRegEx {

        COMPOUND(OTClasses.Compound(), Compound.class, ".+[^query]+/(?i)compound(s||)/" + END_SLASH_orNothing),
        CONFORMER(OTClasses.Conformer(), Conformer.class, ".+[^query]+/(?i)compound(s||)/.+/(?i)conformer(s||)/" + END_SLASH_orNothing),
        FEATURE(OTClasses.Feature(), Feature.class, ".+/(?i)feature(s||)/" + END_SLASH_orNothing),
        DATASET(OTClasses.Dataset(), Dataset.class, ".+/(?i)dataset(s||)/" + END_SLASH_orNothing,
        ".+/(?i)query/(?i)compound/.+/" + END_SLASH_orNothing),
        ALGORITHM(OTClasses.Algorithm(), Algorithm.class, ".+/(?i)algorithm(s||)/" + END_SLASH_orNothing),
        BIBTEX(KnoufBibTex.Entry(), BibTeX.class, ".+/(?i)bibtex(s||)/" + END_SLASH_orNothing),
        MODEL(OTClasses.Model(), Model.class, ".+/(?i)model(s||)/" + END_SLASH_orNothing),
        TASK(OTClasses.Task(), Task.class, ".+/(?i)task(s||)/" + END_SLASH_orNothing);
        /**
         * ParameterValue of regular expressions that identify a
         * certain resource.
         */
        private final Set<String> regexp = new HashSet<String>();
        /**
         * The Java class that corresponds to the OpenTox resource
         */
        private final Class<?> clazz;
        private OntologicalClass ontologicalClass;

        private OpenToxRegEx(final OntologicalClass ont, final Class<?> claz, final String... regexp) {
            Collections.addAll(this.regexp, regexp);
            this.clazz = claz;
            this.ontologicalClass = ont;
        }

        /**
         * Return the set of regular expression that identify a
         * certain OpenTox resource (e.g. ot:Dataset, ot:Compound etc).
         * @return
         */
        public Set<String> getRegexp() {
            return regexp;
        }

        /**
         * Return the Java class that is related to the OpenTox
         * resource. For example {@link OpenToxRegEx#ALGORITHM } returns
         * {@link Algorithm }.
         * @return
         */
        public Class<?> getClazz() {
            return clazz;
        }

        public OntologicalClass getOntologicalClass() {
            return ontologicalClass;
        }
    }

    /**
     * Checks whether a given {@link VRI uri} matches a given pattern.
     *
     * @param vri
     *      A URI (instance of {@link VRI }.
     * @param otreg
     *      A regular expression provided as an instance of {@link OpenToxRegEx }.
     * @return
     *      <code>true</code>If and only if the provided uri matches the
     *      regular expression.
     */
    private static boolean match(VRI vri, OpenToxRegEx otreg) {
        String noQuery = vri.getStringNoQuery();
        for (String re : otreg.getRegexp()) {
            if (noQuery.matches(re)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dummy contructor
     */
    public VRI() {
    }

    /**
     * Construct a new VRI providing its String representation. Parameters are parsed
     * and stored separately in a map.
     * @param uri
     *      The URI as a string.
     * @throws URISyntaxException
     *      In case the provided URI is syntactically incorrect.
     */
    public VRI(String uri) throws URISyntaxException {
        this();
        doProcessUri(uri);
    }

    private void doProcessUri(String uri) throws URISyntaxException {
        new URI(uri);
        if (!uri.contains("://")) {
            uri = "http://" + uri;
        }
        this.uri = uri;
        if (uri.contains("?")) {
            String[] splitted = uri.split(Pattern.quote("?"));
            this.uri = splitted[0];
            if (splitted.length >= 2) {// Could be http://something.abc/service? where there is no query...
                String query = splitted[1];
                String[] queryParts = query.split(Pattern.quote("&"));
                String paramName, paramValue;
                try {
                    for (int i = 0; i < queryParts.length; i++) {
                        paramName = null;
                        paramValue = null;
                        String queryFragment = queryParts[i];
                        String[] queryFragmentComponents = queryFragment.split(Pattern.quote("="));
                        if (queryFragmentComponents.length == 1) {
                            paramName = queryFragmentComponents[0];
                        } else if (queryFragmentComponents.length > 1) {
                            paramName = queryFragmentComponents[0];
                            for (int k = 1; k < queryFragmentComponents.length; k++) {
                                paramValue = paramValue == null ? queryFragmentComponents[k] : paramValue + queryFragmentComponents[k];
                                if (k != queryFragmentComponents.length - 1) {
                                    paramValue += "=";
                                }
                            }
                        }
                        urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING), // paramname cannot be null
                                paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : ""));
                    }
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }

        }
    }

    /**
     * Creates a VRI as a clone of a given VRI.
     * @param other
     *      VRI to be cloned.
     */
    public VRI(VRI other) {
        this.uri = other.uri;
        this.urlParams = other.urlParams;
    }

    /**
     * Create a new VRI object providing its URI as String (can possibly include
     * a query part which will be parsed) and a set of parameters (name-value pairs).
     * @param uri
     *      The base URI
     * @param params
     *      A sequence of parameter names followed by their values. For example
     *      <code>VRI v = new VRI("http://myserver.com","a","2","option","true")</code>.
     *      This will generate the URI: http://myserver.com?a=2&option=true. Note that
     *      the parameter names and values are URL encoded (using the UTF-8 encoding)
     *      as soon as they are provided to the constructor.
     * @throws URISyntaxException
     */
    public VRI(String uri, String... params) throws URISyntaxException {
        this(uri);

        if (params.length > 0) {
            String paramName = null;
            String paramValue = null;
            for (int i = 0; i < params.length; i++) {
                paramName = null;
                paramValue = null;
                paramName = params[i];
                if (i != params.length - 1) {
                    paramValue = params[++i];
                }
                try {
                    urlParams.add(new Pair(URLEncoder.encode(paramName, URL_ENCODING),
                            paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : null));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Returns a set of name-value pairs that constitute the set of
     * URL parameters of the VRI. Every key in the map is a parameter name
     * while the corresponding value is the value for this parameter.
     *
     * @return
     *      The ArrayList of URL parameters as a list of name-value pairs.
     */
    public ArrayList<Pair<String, String>> getUrlParams() {
        return urlParams;
    }

    /**
     * Removes all parameters that match the given parameter name. If no parameter
     * is found to match the search criterion, no action is taken and the VRI is
     * returned without any modifications.
     * 
     * @param paramName
     *      Name of parameter to be removed from the set of name-value pairs
     * @return
     *      The updated VRI without the parameter.
     */
    public VRI removeUrlParameter(String paramName) {
        String encodedParamName = null;
        try {
            encodedParamName = URLEncoder.encode(paramName, URL_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            logger.debug("Unsupported encoding exception", ex);
        }
        ArrayList<Pair<String, String>> urlParamsClone = new ArrayList<Pair<String, String>>(urlParams);
        for (Pair<String, String> pair : urlParamsClone) {
            if (encodedParamName.equals(pair.getKey())) {
                urlParams.remove(pair);
            }
        }
        return this;
    }

    /**
     * Clears any tokens that might be contained in the URI. Though tokens are not
     * provided any more within the set of URL parameters, this method is useful, and
     * should be used, to avoid persistense of token-related parameter in URLs in
     * case a client provides such information within the URL parameters.
     *
     * @return
     *      Updated URI without tokens.    
     */
    public VRI clearToken() {
        return removeUrlParameter(TOKENID);
    }

    /**
     * Add a URL parameter. As soon as you provide the URL parameter and its value,
     * these are encoded using the UTF-8 encoding so you do not need to encode them
     * before submitting them. 
     *
     * @param paramName
     *      The name of the parameter
     * @param paramValue
     *      The value for the parameter
     * @return
     *      The updated VRI on which the method is applied.
     */
    public VRI addUrlParameter(String paramName, String paramValue) {
        try {
            urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING),
                    URLEncoder.encode(paramValue, URL_ENCODING)));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    /**
     * Add a double valued URL parameter. As soon as you provide the URL parameter and its value,
     * these are encoded using the UTF-8 encoding so you do not need to encode them
     * before submitting them.
     *
     * @param paramName
     *      The name of the parameter
     * @param paramValue
     *      The value for the parameter (double)
     * @return
     *      The updated VRI on which the method is applied.
     */
    public VRI addUrlParameter(String paramName, double paramValue) {
        try {
            urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING),
                    URLEncoder.encode(new Double(paramValue).toString(), URL_ENCODING)));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    /**
     * Add an integer valued URL parameter. As soon as you provide the URL parameter and its value,
     * these are encoded using the UTF-8 encoding so you do not need to (and must not) encode them
     * before submitting them.
     *
     * @param paramName
     *      The name of the parameter
     * @param paramValue
     *      The value for the parameter (integer value)
     * @return
     *      The updated VRI on which the method is applied.
     */
    public VRI addUrlParameter(String paramName, int paramValue) {
        try {
            urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING), URLEncoder.encode(new Integer(paramValue).toString(), URL_ENCODING)));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    /**
     * Adds a URL parameter that corresponds to an authentication token using the
     * URL parameter <code>tokenid</code>.
     * @param token
     *      Authentication Token
     * @return
     *      The updated VRI with the token.
     * @deprecated
     *      This method is deprectated due to migration from OpenTox API 1.1.
     *      to version 1.2. Authentication is materialized using the HTTP Header
     *      <code>Authorization</code> instead of the URL parameter
     *      <code>toked_id</code>.
     */
    @Deprecated
    public VRI appendToken(AuthenticationToken token) {
        if (token != null) {
            return addUrlParameter(TOKENID, token.stringValue());
        }
        return this;
    }

    /**
     * Converts the VRI object into the corresponding <code>java.net.URI</code>
     * @return
     *      Corresponding URI
     */
    public java.net.URI toURI() {
        try {
            return new URI(toString());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(uri);
        String query = getQueryAsString();
        if (query != null && !query.isEmpty()) {
            string.append("?");
            string.append(getQueryAsString());
        }
        return new String(string);
    }

    public VRI augment(String... fragments) {
        String noQueryUri = getStringNoQuery();
        StringBuilder builder = new StringBuilder(getStringNoQuery());
        if (!noQueryUri.matches(".+/$")) {
            builder.append("/"); // Append a slash at the end (if not any)
        }

        for (int i = 0; i < fragments.length; i++) {
            String frg = fragments[i];
            if (frg != null) {
                try {
                    builder.append(URLEncoder.encode(frg, URL_ENCODING));
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
                if (i < fragments.length - 1) {
                    builder.append("/");
                }
            }
        }
        this.uri = new String(builder);
        try {
            new URI(uri);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public VRI augment(Object... params) {
        if (params == null || (params != null && params.length == 0)) {
            return this;
        }
        String[] parametersStringArray = new String[params.length];
        int i = 0;
        for (Object o : params) {
            parametersStringArray[i] = params[i].toString();
            i++;
        }
        augment(parametersStringArray);
        return this;
    }

    /**
     * Returns the protocol of the URI (HTTP, HTTPS, etc)
     * @return
     *      Protocol as a string
     */
    public String getProtocol() {
        if (uri.contains("://")) {
            return uri.split(Pattern.quote("://"))[0];
        } else {
            return "http";
        }
    }

    /** Delegates the method java.net.URI#getPort()::int */
    public int getPort() {
        int port = toURI().getPort();
        if (port == -1) {
            if (getProtocol().equals("http")) {
                port = 80;// Default
            } else if (getProtocol().equals("https")) {
                port = 443;// Default
            }
        }
        return port;
    }

    /**
     * Returns the query part of the URI as a String without the question mark.
     * @return
     *      Query as String.
     */
    public String getQueryAsString() {
        StringBuilder string = new StringBuilder();
        final int nParams = urlParams.size();
        if (nParams > 0) {
            int counter = 0;
            for (Pair<String, String> e : urlParams) {
                string.append(e.getKey());
                string.append("=");
                if (e.getValue() != null) {
                    string.append(e.getValue());
                }
                if (counter != nParams - 1) {
                    string.append("&");
                }
                counter++;
            }
        }
        return new String(string);
    }

    /**
     * Returns the regular expression pattern that matches this URI.
     * @return
     *      Instance of {@link OpenToxRegEx } or <code>null</code> if it doess not
     *      match any reg. exp.
     */
    private OpenToxRegEx getMatchingRegEx() {
        for (OpenToxRegEx x : OpenToxRegEx.values()) {
            if (match(this, x)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Returns a Java class that best fits the pattern matched by this URI. For
     * example the URI <code>http://someserver.com:9876/OpenTox/query/compound/phenol/smiles</code>
     * will return the class <code>org.opentox.toxotis.core.Dataset</code>. Note that this
     * method will return <code>null</code> in case the URI does not comply with the pattern
     * for the URI of OpenTox resource and it will also return <code>null</code> for URIs
     * that identify sets/collections of resource such as /compound (list all
     * compounds). So the following code will print <code>null</code>:
     * <pre> VRI uri = new VRI("http://someserver.com/opentox/dataset");
     * Class&lt;?&gt; cl = uri.getOpenToxType();
     * System.out.println(cl);
     * </pre>
     * @return
     *      Corresponding OpenTox class (usually a subclass of {@link OTComponent }).
     * @see OTComponent
     * @see Dataset
     */
    public Class<?> getOpenToxType() {
        OpenToxRegEx rex = getMatchingRegEx();
        if (rex == null) {
            return null;
        }
        if (rex == OpenToxRegEx.COMPOUND || rex == OpenToxRegEx.CONFORMER) {
            String queryString = getQueryAsString();
            if (queryString == null || (queryString != null && queryString.isEmpty())) {
                return rex.getClazz();
            }
        } else {
            return rex.getClazz();
        }
        return null;
    }

    public OntologicalClass getOntologicalClass() {
        OpenToxRegEx rex = getMatchingRegEx();
        if (rex == null) {
            return null;
        }
        if (rex == OpenToxRegEx.COMPOUND || rex == OpenToxRegEx.CONFORMER) {
            String queryString = getQueryAsString();
            if (queryString == null || (queryString != null && queryString.isEmpty())) {
                return rex.getOntologicalClass();
            }
        } else {
            return rex.getOntologicalClass();
        }
        return null;
    }

    public String getHost() {
        URI asUri = this.toURI();
        return asUri != null ? asUri.getHost() : null;
    }

    /**
     * Returns the base URI of the service.
     * @return
     *      The base URI of the service.
     */
    public VRI getServiceBaseUri() {
        String noQuery = getStringNoQuery();
        String fragment;
        for (UriKeywords key : UriKeywords.values()) {
            fragment = key.getKeyword();
            if (noQuery.contains(fragment)) {
                try {
                    if (fragment.equals(UriKeywords.Compound.getKeyword())) {
                        if (noQuery.contains("/query/compound")) {
                            return new VRI(noQuery.split("/query/compound")[0]);
                        }
                    }
                    return new VRI(noQuery.split("/" + fragment)[0]);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return this;
    }

    /**
     * Returns the URI without the query part.
     * @return
     *      URI without the query part
     */
    public String getStringNoQuery() {
        return uri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VRI other = (VRI) obj;
        if ((this.uri == null) || (other.uri == null)) {
            return false;
        }
        try {
            return new URI(getStringNoQuery()).equals(new URI(other.getStringNoQuery()));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }

    public String getUri() {
        return toString();
    }

    public void setUri(String uri) {
        try {
            doProcessUri(uri);
        } catch (URISyntaxException ex) {
            logger.debug("URI Syntax Exception", ex);
        }
    }

    public String getId() {
        String residual = getStringNoQuery().replaceAll(getServiceBaseUri().toString(), "").trim();
        String[] parts = residual.split("/");
        if (parts.length == 3) {
            String id = parts[2];
            if (id.endsWith("/")) {
                id = id.substring(0, id.length() - 2);
            }
            return id;
        }
        return null;
    }
    
}

package org.opentox.toxotis.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.opentox.toxotis.core.*;

/**
 * VRI is an alternative to URI. Being <code>final</code>, the class <code>java.net.URI</code>
 * cannot be subclassed. VRI offers greater flexibility in this context as it stores in a
 * highly structured way the parameters of the URL and applies URL encoding where necessary.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VRI { // Well tested!

    /** The URI as a string */
    private String uri;
    /** The mapping from parameter names to parameter values */
    private Map<String, String> urlParams = new LinkedHashMap<String, String>();
    /** The standard UTF-8 encoding */
    private static final String URL_ENCODING = "UTF-8";

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

        COMPOUND(Compound.class, ".+[^query]+/(?i)compound(s||)/" + END_SLASH_orNothing),
        CONFORMER(Conformer.class, ".+[^query]+/(?i)compound(s||)/.+/(?i)conformer(s||)/" + END_SLASH_orNothing),
        FEATURE(Feature.class, ".+/(?i)feature(s||)/" + END_SLASH_orNothing),
        DATASET(Dataset.class, ".+/(?i)dataset(s||)/" + END_SLASH_orNothing,
        ".+/(?i)query/(?i)compound/.+/" + END_SLASH_orNothing),
        ALGORITHM(Algorithm.class, ".+/(?i)algorithm(s||)/" + END_SLASH_orNothing),
        BIBTEX(BibTeX.class, ".+/(?i)bibtex(s||)/" + END_SLASH_orNothing),
        MODEL(Model.class, ".+/(?i)model(s||)/" + END_SLASH_orNothing),
        TASK(Task.class, ".+/(?i)task(s||)/" + END_SLASH_orNothing);
        /**
         * Set of regular expressions that identify a
         * certain resource.
         */
        private final Set<String> regexp = new HashSet<String>();
        /**
         * The Java class that corresponds to the OpenTox resource
         */
        private final Class<?> clazz;

        private OpenToxRegEx(final Class<?> claz, final String... regexp) {
            Collections.addAll(this.regexp, regexp);
            this.clazz = claz;
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
     * Construct a new VRI providing its String representation. Parameters are parsed
     * and stored separately in a map.
     * @param uri
     *      The URI as a string.
     * @throws URISyntaxException
     *      In case the provided URI is syntactically incorrect.
     */
    public VRI(String uri) throws URISyntaxException {
        new URI(uri);
        this.uri = uri;
        if (uri.contains("?")) {
            String[] splitted = uri.split(Pattern.quote("?"));
            this.uri = splitted[0];
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
                        paramName = queryFragment;
                    } else if (queryFragmentComponents.length > 1) {
                        paramName = queryFragmentComponents[0];
                        paramValue = queryFragmentComponents[1];
                    }
                    urlParams.put(URLEncoder.encode(paramName, URL_ENCODING), URLEncoder.encode(paramValue, URL_ENCODING));
                }
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

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
                    urlParams.put(URLEncoder.encode(paramName, URL_ENCODING),
                            paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : null);
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
     *      The Map of URL parameters
     */
    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    /**
     * Add a URL parameter. As soon as you provide the URL parameter and its value,
     * these are encoded using the UTF-8 encoding so you do not need to encode them
     * before submitting them.
     * @param paramName
     *      The name of the parameter
     * @param paramValue
     *      The value for the parameter
     * @return
     *      The updated VRI on which the method is applied.
     */
    public VRI addUrlParameter(String paramName, String paramValue) {
        try {
            urlParams.put(URLEncoder.encode(paramName, URL_ENCODING), URLEncoder.encode(paramValue, URL_ENCODING));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
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
            for (Map.Entry<String, String> e : urlParams.entrySet()) {
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
     * will return the class <code>org.opentox.toxotis.core.Dataset</code>
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
            if (getQueryAsString() == null || (getQueryAsString() != null && getQueryAsString().isEmpty())) {
                return rex.getClazz();
            }
        } else {
            return rex.getClazz();
        }
        return null;
    }

    /**
     * Returns the base URI of the service.
     * @return
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
        return null;
    }

    /**
     * Returns the URI without the query part.
     * @return
     *      URI without the query part
     */
    public String getStringNoQuery() {
        return uri;
    }
}

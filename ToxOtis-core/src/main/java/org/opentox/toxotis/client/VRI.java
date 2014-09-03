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
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * VRI is an alternative to URI. Being <code>final</code>, the class <code>java.net.URI</code>
 * cannot be sub-classed. VRI offers greater flexibility in this context as it stores in a
 * highly structured way the parameters of the URL and applies URL encoding where necessary.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VRI implements Serializable { // Well tested!

    /** The URI as a string */
    private String uri;
    /** The mapping from parameter names to parameter values */
    private List<Pair<String, String>> urlParams = new ArrayList<Pair<String, String>>();
    /** The standard UTF-8 encoding */
    private static final String URL_ENCODING = "UTF-8";
    private static final long serialVersionUID = 184328712643L;
    private transient final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VRI.class);
    private static final int HASH_OFFSET = 3, HASH_MOD = 37;
    private static final int PORT_HTTP_DEFAULT = 80, PORT_HTTPS_DEFAULT = 443;
    private static final String END_SLASH_OR_NOTHING = "([^/]+/$|[^/]+)$", TRAILING_SLASH = ".+/$",
            COL_SLSL = "://";
    private static final String SLASH = "/", QUESTION_MARK = "?", AMPBESAND = "&", EQUALS = "=";
    private static final String HTTP_PROTOCOL = "http", HTTPS_PROTOCOL = "https",
            UNSUPPORTED_ENC_MSG = "Unsupported encoding!";

    /**
     * Keywords that appear in OpenTox URIs.
     */
    private enum UriKeywords {

        Dataset("dataset"),
        Compound("compound"),
        Substance("substance"),
        Feature("feature"),
        Algorithm("algorithm"),
        Task("task"),
        BibTex("bibtex"),
        Model("model"),
        Query("query"),
        Error("error"),
        Parameter("parameter"),
        Report("reach_report"),
        Qprf("qprf");
        private final String keyword;

        private UriKeywords(final String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    /**
     * A collection of regular expressions for the identification of
     * OpenTox URI patterns.
     */
    private enum OpenToxRegEx {

        COMPOUND(OTClasses.compound(), Compound.class, ".+[^query]+/(?i)compound(s||)/" + END_SLASH_OR_NOTHING),
        SUBSTANCE(OTClasses.substance(), Substance.class, ".+[^query]+/(?i)substance(s||)/" + END_SLASH_OR_NOTHING),
        CONFORMER(OTClasses.conformer(), Conformer.class, ".+[^query]+/(?i)compound(s||)/.+/(?i)conformer(s||)/" + END_SLASH_OR_NOTHING),
        FEATURE(OTClasses.feature(), Feature.class, ".+/(?i)feature(s||)/" + END_SLASH_OR_NOTHING),
        DATASET(OTClasses.dataset(), Dataset.class, ".+/(?i)dataset(s||)/" + END_SLASH_OR_NOTHING,
        ".+/(?i)query/(?i)compound/.+/" + END_SLASH_OR_NOTHING),
        SUBSTANCE_DATASET(OTClasses.substanceDataset(), SubstanceDataset.class, ".+/(?i)substanceowner/[a-zA-Z0-9-]*/dataset"),
        ALGORITHM(OTClasses.algorithm(), Algorithm.class, ".+/(?i)algorithm(s||)/" + END_SLASH_OR_NOTHING),
        BIBTEX(KnoufBibTex.entry(), BibTeX.class, ".+/(?i)bibtex(s||)/" + END_SLASH_OR_NOTHING),
        MODEL(OTClasses.model(), Model.class, ".+/(?i)model(s||)/" + END_SLASH_OR_NOTHING),
        TASK(OTClasses.task(), Task.class, ".+/(?i)task(s||)/" + END_SLASH_OR_NOTHING),
        ERROR(OTClasses.errorReport(), ErrorReport.class, ".+/(?i)error(s||)/" + END_SLASH_OR_NOTHING),
        PARAMETER(OTClasses.parameter(), Parameter.class, ".+/(?i)parameter(s||)/" + END_SLASH_OR_NOTHING),
        QPRFREPORT(OTClasses.qprfReport(), QprfReport.class, ".+/(?i)reach_report(s||)/(?i)qprf/" + END_SLASH_OR_NOTHING);
        /**
         * ParameterValue of regular expressions that identify a
         * certain resource.
         */
        private final Set<String> regexp = new HashSet<String>();
        /**
         * The Java class that corresponds to the OpenTox resource
         */
        private final Class<?> clazz;
        private final OntologicalClass ontologicalClass;

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
     * Dummy constructor
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
        URI uRItest = new URI(uri);
        String modifiedUri = uri;
        StringBuilder sb = new StringBuilder();
        if (!modifiedUri.contains(COL_SLSL)) {
            sb.append(HTTP_PROTOCOL).append(COL_SLSL).append(modifiedUri);
            modifiedUri = sb.toString();
        }
        this.uri = modifiedUri;
        if (modifiedUri.contains(QUESTION_MARK)) {
            String[] splitted = modifiedUri.split(Pattern.quote(QUESTION_MARK));
            this.uri = splitted[0];
            if (splitted.length >= 2) {// Could be http://something.abc/service? where there is no query...
                String query = splitted[1];
                String[] queryParts = query.split(Pattern.quote(AMPBESAND));
                String paramName, paramValue;
                try {
                    for (String queryPart : queryParts) {
                        paramName = null;
                        paramValue = null;
                        String queryFragment = queryPart;
                        String[] queryFragmentComponents = queryFragment.split(Pattern.quote(EQUALS));
                        if (queryFragmentComponents.length == 1) {
                            paramName = queryFragmentComponents[0];
                        } else if (queryFragmentComponents.length > 1) {
                            paramName = queryFragmentComponents[0];
                            for (int k = 1; k < queryFragmentComponents.length; k++) {
                                paramValue = paramValue == null ? queryFragmentComponents[k] : paramValue + queryFragmentComponents[k];
                                if (k != queryFragmentComponents.length - 1) {
                                    paramValue += EQUALS;
                                }
                            }
                        }
                        urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING), // paramname cannot be null
                                paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : ""));
                    }
                } catch (final UnsupportedEncodingException ex) {
                    logger.error(UNSUPPORTED_ENC_MSG, ex);
                    throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
        this.uri = String.valueOf(other.uri);//@chung: new String(#) replaced by String.valueOf(#)
        this.urlParams = new ArrayList<Pair<String, String>>(other.urlParams);
    }

    /**
     * Create a new VRI object providing its URI as String (can possibly include
     * a query part which will be parsed) and a set of parameters (name-value pairs).
     * @param uri
     *      The base URI
     * @param params
     *      A sequence of parameter names followed by their values. For example
     *      <code>VRI v = new VRI("http://myserver.com","a","2","option","true")</code>.
     *      This will generate the URI: <code>http://myserver.com?a=2&amp;option=true</code>. Note that
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
                } catch (final UnsupportedEncodingException ex) {
                    logger.error(UNSUPPORTED_ENC_MSG, ex);
                    throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
    public List<Pair<String, String>> getUrlParams() {
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
        } catch (final UnsupportedEncodingException ex) {
            logger.error(UNSUPPORTED_ENC_MSG, ex);
            throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
        } catch (final UnsupportedEncodingException ex) {
            logger.error(UNSUPPORTED_ENC_MSG, ex);
            throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
                    URLEncoder.encode(Double.toString(paramValue), URL_ENCODING)));
        } catch (final UnsupportedEncodingException ex) {
            logger.error(UNSUPPORTED_ENC_MSG, ex);
            throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
            urlParams.add(new Pair<String, String>(URLEncoder.encode(paramName, URL_ENCODING),
                    URLEncoder.encode(Integer.toString(paramValue), URL_ENCODING)));
        } catch (final UnsupportedEncodingException ex) {
            logger.error(UNSUPPORTED_ENC_MSG, ex);
            throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
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
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public String toString() {
        if (uri == null) {
            return null;
        }
        if (uri.isEmpty()) {
            return "";
        }
        StringBuilder string = new StringBuilder(uri);
        String query = getQueryAsString();
        if (query != null && !query.isEmpty()) {
            string.append(QUESTION_MARK);
            string.append(getQueryAsString());
        }
        return new String(string);
    }

    /**
     * Augments the URI with additional string blocks. Each string provided
     * in the list of arguments of this method is URL-encoded and appended
     * at the end of the current URI. The current URI is also modified and this is
     * an important detail to take into account. So, for example consider the
     * following piece of code:
     * <pre>
     * VRI myUri = new VRI("http://server.com/");
     * VRI datasetUri = myUri.augment("dataset");
     * VRI modelUri = myUri.augment("model");</pre>
     * Not surprisingly, <code>modelUri</code> will be <code>http://server.com/dataset/model</code>
     * instead of <code>http://server.com/model</code>. This is because the invocation
     * <code>myUri.augment("dataset")</code> already modified the URI. In order to achieve the
     * desired result, one should use:
     * <pre>
     * VRI myUri = new VRI("http://server.com/");
     * VRI datasetUri = new VRI(myUri).augment("dataset");
     * VRI modelUri = new VRI(myUri).augment("model");</pre>
     * This way, a copy of the initial URI is created and modified on-the-fly.
     * 
     * @param fragments
     *      List of fragments to be appended at the end of the current URI.
     * @return 
     *      The current modifiable URI updated with the new additional
     *      fragments.
     */
    public VRI augment(String... fragments) {
        String noQueryUri = getStringNoQuery();
        StringBuilder builder = new StringBuilder(getStringNoQuery());
        if (!noQueryUri.matches(TRAILING_SLASH)) {
            builder.append(SLASH); // Append a slash at the end (if not any)
        }

        for (int i = 0; i < fragments.length; i++) {
            String frg = fragments[i];
            if (frg != null) {
                try {
                    builder.append(URLEncoder.encode(frg, URL_ENCODING));
                } catch (final UnsupportedEncodingException ex) {
                    logger.error(UNSUPPORTED_ENC_MSG, ex);
                    throw new IllegalArgumentException(UNSUPPORTED_ENC_MSG, ex);
                }
                if (i < fragments.length - 1) {
                    builder.append(SLASH);
                }
            }
        }
        this.uri = new String(builder);
        try {
            new URI(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        return this;
    }

    /**
     * An alternative to {@link #augment(java.lang.String[]) 
     * #augment(String[])}
     * 
     * @param params
     *      List of fragments
     * @return 
     *      The instance of the current modifiable URI updated with the new additional
     *      fragments.
     */
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
        if (uri.contains(COL_SLSL)) {
            return uri.split(Pattern.quote(COL_SLSL))[0];
        } else {
            return HTTP_PROTOCOL;
        }
    }

    /** 
     * Delegates the method java.net.URI#getPort()::int
     * 
     * @return  
     * The HTTP port of the URI.
     *
     */
    public int getPort() {
        int port = toURI().getPort();
        if (port == -1) {
            if (HTTP_PROTOCOL.equals(getProtocol())) {
                port = PORT_HTTP_DEFAULT;// Default
            } else if (HTTPS_PROTOCOL.equals(getProtocol())) {
                port = PORT_HTTPS_DEFAULT;// Default
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
                string.append(EQUALS);
                if (e.getValue() != null) {
                    string.append(e.getValue());
                }
                if (counter != nParams - 1) {
                    string.append(AMPBESAND);
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
     *      Corresponding OpenTox class (usually a subclass of {@link IOTComponent }).
     * @see IOTComponent
     * @see Dataset
     */
    public Class<?> getOpenToxType() {
        OpenToxRegEx rex = getMatchingRegEx();
        if (rex == null) {
            return null;
        }
        if (rex == OpenToxRegEx.COMPOUND || rex == OpenToxRegEx.CONFORMER || rex == OpenToxRegEx.SUBSTANCE) {
            String queryString = getQueryAsString();
            if (queryString == null || (queryString != null && queryString.isEmpty())) {
                return rex.getClazz();
            }
        } else {
            return rex.getClazz();
        }
        return null;
    }

    /**
     * The Ontological Class of the URI. Every OpenTox resource follows a pattern
     * according to the resource it points to. For example, all dataset URIs follow 
     * the pattern <code>/dataset/{id}</code> or formally <code>.+/(?i)dataset(s||)/([^/]+/$|[^/]+)$</code>
     * or as well <code>.+/(?i)query/(?i)compound/.+/([^/]+/$|[^/]+)$</code>.
     * @return 
     *  The ontological class of the object represented by this URI.
     */
    public OntologicalClass getOntologicalClass() {
        OpenToxRegEx rex = getMatchingRegEx();
        if (rex == null) {
            return null;
        }
        if (rex == OpenToxRegEx.COMPOUND || rex == OpenToxRegEx.CONFORMER) {
            String queryString = getQueryAsString();
            if (queryString == null || (queryString != null && queryString.isEmpty())) {
                return rex.getOntologicalClass();
            } else if (queryString.contains("feature_uri")) {
                // If the compound or conformer VRI contains a uri parameter
                // like feature_uri=... or feature_uris[]=..., then the result
                // is a dataset and not a conformer.
                return OTClasses.dataset();
            }
        }
        return rex.getOntologicalClass();
    }

    /**
     * Returns the host of the URI. This is a proxy method to URI#getHost().
     * @return 
     *      The host of the URI as a String.
     */
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
                    } else if (noQuery.contains("/substanceowner")) {
                        String res = noQuery.split("/substanceowner")[0];
                        return new VRI(res);
                        //TODO: API EXT custom enanomapper
                    }
                    return new VRI(noQuery.split(SLASH + fragment)[0]);
                } catch (URISyntaxException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }
        return this;
    }

    /**
     * Returns the URI of the service.
     * @return
     *      The base URI of the service.
     */
    public VRI getServiceUri() {
        String noQuery = getStringNoQuery();
        String fragment;
        for (UriKeywords key : UriKeywords.values()) {
            fragment = key.getKeyword();
            if (noQuery.contains(fragment)) {
                try {
                    if (fragment.equals(UriKeywords.Compound.getKeyword())) {
                        return getServiceBaseUri().augment("dataset");
                    } else if (noQuery.contains("/substanceowner")) {
                        return getServiceBaseUri().augment("substance");
                        //TODO: API EXT custom enanomapper
                    } else if (fragment.equals(UriKeywords.Dataset.getKeyword())) {
                        return getServiceBaseUri().augment("dataset");
                    }
                    return new VRI(noQuery.split(SLASH + fragment)[0]);
                } catch (URISyntaxException ex) {
                    throw new IllegalArgumentException(ex);
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
        String stringNoQuery = uri;
        if (stringNoQuery.endsWith(SLASH)) {
            stringNoQuery = stringNoQuery.substring(0, stringNoQuery.length() - 1);
        }
        return stringNoQuery;
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
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = HASH_OFFSET;
        hash = HASH_MOD * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }

    /**
     * Equivalent method to {@link #toString() }.
     * @return 
     *      The current object as a String (representing the URI of the resource).
     */
    public String getUri() {
        return toString();
    }

    /**
     * Simple setter method of the URI as String.
     * @param uri 
     *      The URI as String.
     */
    public void setUri(String uri) {
        try {
            doProcessUri(uri);
        } catch (URISyntaxException ex) {
            logger.debug("URI Syntax Exception", ex);
        }
    }

    /**
     * Returns the ID of the current VRI object. 
     * 
     * <p>For example, for the VRI
     * <code>http://server.com:8080/algorithm/mlr</code> will return <code>mlr</code>.
     * This method will first remove the query part of the VRI and then will remove
     * also the base part of the URI. the rest of the string will be split at the
     * slash symbol from where the Id is properly retrieved.</p>
     * 
     * <p>In case the VRI has more than one IDs, such as a conformer, then the most
     * specific one is returned. For instance, in the VRI 
     * <code>http://server.com:8080/compound/1/conformer/25</code>, the ID of the 
     * compound is <code>1</code>, but the ID of the conformer is <code>25</code>; and
     * this particular VRI is identified as a conformer, therefore its ID is 
     * <code>25</code> and not <code>1</code>.</p>
     * 
     * <p>One little detail to be noted is that in case the VRI is of the form
     * <code>http://anonymous.org:9090/compound/1/conformer/2?feature_uri[]=...</code>,
     * i.e. it is a {@link OTClasses#dataset() dataset}, the returned ID will
     * be the ID of the conformer. In such cases you should combine this method
     * with {@link #getOntologicalClass() #getOntologicalClass()} to have full
     * control of what is returned.</p>
     * 
     * @return 
     *      The ID of the current URI as a String or <code>null</code> if no
     *      ID is found.
     */
    public String getId() {
        String residual = getStringNoQuery().replaceAll(getServiceBaseUri().toString(), "").trim();
        String[] parts = residual.split(SLASH);
        if (parts.length >= 3) {
            String id = parts[parts.length - 1];
            if (id.endsWith(SLASH)) {
                id = id.substring(0, id.length() - 2);
            }
            return id;
        }
        return null;
    }
}

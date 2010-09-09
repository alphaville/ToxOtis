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
    private static final String END_SLASH_orNothing = "([^/]+/$|[^/]+)$";

    private enum OpenToxRegEx {

        COMPOUND(Compound.class, ".+/(?i)compound(s||)/" + END_SLASH_orNothing),
        CONFORMER(Conformer.class, ".+/(?i)compound(s||)/.+/(?i)conformer(s||)/" + END_SLASH_orNothing),
        FEATURE(Feature.class, ".+/(?i)feature(s||)/" + END_SLASH_orNothing),
        DATASET(Dataset.class, ".+/(?i)dataset(s||)/" + END_SLASH_orNothing,
        ".+/(?i)query/(?i)compound/.+/" + END_SLASH_orNothing),
        ALGORITHM(Algorithm.class, ".+/(?i)algorithm(s||)/" + END_SLASH_orNothing),
        BIBTEX(BibTeX.class, ".+/(?i)bibtex(s||)/" + END_SLASH_orNothing),
        MODEL(Model.class, ".+/(?i)model(s||)/" + END_SLASH_orNothing),
        TASK(Task.class, ".+/(?i)task(s||)/" + END_SLASH_orNothing);
        
        private Set<String> regexp = new HashSet<String>();
        private Class<?> clazz;

        private OpenToxRegEx(Class<?> claz, String... regexp) {
            Collections.addAll(this.regexp, regexp);
            this.clazz = claz;
        }

        public Set<String> getRegexp() {
            return regexp;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }

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

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    public VRI addUrlParameter(String paramName, String paramValue) {
        urlParams.put(paramName, paramValue);
        return this;
    }

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

    public Class<?> getOpenToxType() {
        OpenToxRegEx[] reg = OpenToxRegEx.values();
        for (OpenToxRegEx r : reg) {
            if (match(this, r)) {
                if (r.equals(OpenToxRegEx.COMPOUND)) {
                    if (getUrlParams().isEmpty()) {
                        return r.getClazz();
                    }
                } else {
                    return r.getClazz();
                }
            }
        }
        return null;
    }

    public VRI getServiceBaseUri() {
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

    public static void main(String... art) throws URISyntaxException {
        String uri = "http://opentox.ntua.gr:3000/query/compound/Phenol/all";
        VRI vri = new VRI(uri);
        System.out.println(match(vri, OpenToxRegEx.CONFORMER));
        System.out.println(vri.getOpenToxType());


    }
}

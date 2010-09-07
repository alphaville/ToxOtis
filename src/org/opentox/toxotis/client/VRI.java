package org.opentox.toxotis.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
            return new URI(uri);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(uri);
        string.append(getQueryAsString());
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
}

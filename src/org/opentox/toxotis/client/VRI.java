package org.opentox.toxotis.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VRI {

    private String uri;
    private Map<String, String> urlParams = new LinkedHashMap<String, String>();

    public VRI(String uri) throws URISyntaxException {
        new URI(uri);
        this.uri = uri;
        if (uri.contains("?")) {
            String[] splitted = uri.split(Pattern.quote("?"));
            this.uri = splitted[0];
            String query = splitted[1];
            String[] queryParts = query.split(Pattern.quote("&"));
            String paramName, paramValue;
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
                urlParams.put(paramName, paramValue);
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
                urlParams.put(paramName, paramValue);
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
        final int nParams = urlParams.size();
        if (nParams > 0) {
            string.append("?");
            int counter = 0;
            for (Map.Entry<String, String> e : urlParams.entrySet()) {
                string.append(e.getKey());
                string.append("=");
                string.append(e.getValue());
                if (counter != nParams - 1) {
                    string.append("&");
                }
                counter++;
            }
        }
        return new String(string);
    }
}

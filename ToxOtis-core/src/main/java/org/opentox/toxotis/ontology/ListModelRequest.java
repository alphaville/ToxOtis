package org.opentox.toxotis.ontology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;

/**
 *
 * @author chung
 */
public class ListModelRequest {

    private static final String DEFAULT_PREFIXES =
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
            + "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
            + "PREFIX dcterms:<http://purl.org/dc/terms/> "
            + "PREFIX bo:<http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#> "
            + "PREFIX ot:<http://www.opentox.org/api/1.1#> "
            + "PREFIX ota:<http://www.opentox.org/algorithmTypes.owl#> "
            + "PREFIX otee:<http://www.opentox.org/echaEndpoints.owl#> "
            + "PREFIX toxcast:<http://www.opentox.org/toxcast.owl#> ";
    private static final String DEFAULT_SPARQL =
            "select distinct ?model ?title ?creator ?trainingDataset ?algorithm"
            + "		where {"
            + "			?model rdf:type ot:Model;"
            + "		    OPTIONAL {?model dc:title ?title}."
            + "			OPTIONAL {?model dc:creator ?creator}."
            + "			OPTIONAL {?model ot:trainingDataset ?trainingDataset}."
            + "			OPTIONAL {?model ot:algorithm ?algorithm }."
            + "		}";
    private String prefixes = DEFAULT_PREFIXES;
    private String sparql = DEFAULT_SPARQL;
    private String ontologyServer = "http://apps.ideaconsult.net:8080/ontology";

    public List<String[]> getModelInfo() throws ServiceInvocationException, IOException, ToxOtisException {
        VRI ontServer = null;
        try {
            ontServer = new VRI(ontologyServer);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        IPostClient client = new PostHttpClient(ontServer);
        client.addPostParameter("query", prefixes + sparql);
        client.setMediaType(Media.TEXT_CSV);
        client.setContentType(Media.APPLICATION_FORM_URL_ENCODED);
        client.post();
        int responseCode = client.getResponseCode();
        if (responseCode != 200) {
            throw new ServiceInvocationException("Remote service at " + ontologyServer
                    + " responded with code " + responseCode);
        }
        InputStream input = client.getRemoteStream();
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(isr);
        String line = null;
        int i = 0;
        ArrayList<String[]> result = new ArrayList<String[]>();
        while ((line = reader.readLine()) != null) {
            if (i > 0) {
                result.add(line.split(","));
            }
            i++;
        }
        client.close();
        return result;
    }
}

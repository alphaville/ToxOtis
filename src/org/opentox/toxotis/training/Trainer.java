package org.opentox.toxotis.training;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.ModelSpider;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class Trainer {

    private Algorithm algorithm;
    private Dataset dataset;
    private Feature predictionFeature;

    public Trainer(Algorithm algorithm, Dataset dataset, Feature predictionFeature) {
        setAlgorithm(algorithm);
        this.dataset = dataset;
        this.predictionFeature = predictionFeature;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        if(algorithm == null){
            throw new NullPointerException("Cannot set a null Algorithm to a Trainer.");
        }
        this.algorithm = algorithm;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Feature getPredictionFeature() {
        return predictionFeature;
    }

    public void setPredictionFeature(Feature predictionFeature) {
        this.predictionFeature = predictionFeature;
    }

    public Task train(AuthenticationToken token) throws ToxOtisException {
        /** Handle provided token */
        VRI vri = algorithm.getUri();
        if (token != null) {
            // Replace existing token with the new one
            vri.removeUrlParameter("tokenid").addUrlParameter("tokenid", token.stringValue());
        }
        PostClient client = new PostClient(vri);
        client.setMediaType(Media.TEXT_URI_LIST);
        if (dataset != null) {
            client.addPostParameter("dataset_uri", dataset.getUri().toString());
        }
        if (predictionFeature != null) {
            client.addPostParameter("prediction_feature", predictionFeature.getUri().toString());
        }
        for (Parameter parameter : algorithm.getParameters()) {
            client.addPostParameter(parameter.getName(), parameter.getTypedValue().getValue().toString());
        }
        client.post();

        try {
            int status = client.getResponseCode();

            if(status == 202){
               return new TaskSpider(new VRI(client.getResponseText())).parse();
            }else {
                Task task = new Task();
                task.setHttpStatus(status);
                task.setPercentageCompleted(100);
                task.setResultUri(new VRI(client.getResponseText()));
                return task;
            }            
        } catch (IOException ex){
            throw new ToxOtisException(ex);
        }catch (URISyntaxException ex) {
            throw new ToxOtisException(ErrorCause.TaskNotFoundError, ex);
        }
    }
}

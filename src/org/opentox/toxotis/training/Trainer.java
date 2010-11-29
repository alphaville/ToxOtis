package org.opentox.toxotis.training;

import java.io.IOException;
import java.net.URISyntaxException;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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
        if (algorithm == null) {
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
        IPostClient client = ClientFactory.createPostClient(vri);
        client.authorize(token);
        client.setMediaType(Media.TEXT_URI_LIST);
        if (dataset != null) {
            client.addPostParameter("dataset_uri", dataset.getUri().toString());
        }
        if (predictionFeature != null) {
            client.addPostParameter("prediction_feature", predictionFeature.getUri().toString());
        }

        if (algorithm.getParameters() != null && !algorithm.getParameters().isEmpty()) {
            for (Parameter parameter : algorithm.getParameters()) {
                client.addPostParameter(parameter.getName().getValueAsString(),
                        parameter.getTypedValue().getValue().toString());
            }
        }
        client.post();
        String response = client.getResponseText();
        try {
            int status = client.getResponseCode();
            if (status == 202) {
                return new TaskSpider(new VRI(response)).parse();
            } else {
                Task task = new Task();
                task.setHttpStatus(status);
                task.setPercentageCompleted(100);
                task.setResultUri(new VRI(response));
                return task;
            }
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        } catch (URISyntaxException ex) {
            throw new ToxOtisException(ErrorCause.TrainingError, response);
        }
    }
}

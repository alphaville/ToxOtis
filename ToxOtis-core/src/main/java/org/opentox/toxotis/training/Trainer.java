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
package org.opentox.toxotis.training;

import java.io.IOException;
import java.net.URISyntaxException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
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

    public Task train(AuthenticationToken token) throws ServiceInvocationException {
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
        } catch (URISyntaxException ex) {
            throw new ServiceInvocationException("TrainingError: Bad URI returned", ex);
        }
    }
}

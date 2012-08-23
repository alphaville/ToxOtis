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
package org.opentox.toxotis.util;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 */
public class SuperPredictor {

    private VRI compoundUri;
    private VRI modelUri;
    private VRI superServiceUri;
    private Compound compound;
    private Model model;
    private AuthenticationToken token;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SuperPredictor.class);

    private SuperPredictor(AuthenticationToken token) {
        this.token = token;
        superServiceUri = Services.ideaconsult().augment("algorithm", "superservice");
    }

    public SuperPredictor(Compound compound, Model model, AuthenticationToken token) {
        this(token);
        this.compoundUri = compound.getUri();
        this.modelUri = model.getUri();
        this.compound = compound;
        this.model = model;
        if (model.getPredictedFeatures() == null
                || (model.getPredictedFeatures() != null && model.getPredictedFeatures().isEmpty())) {
            try {
                model.loadFromRemote(token);
            } catch (ServiceInvocationException ex) {
                Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public SuperPredictor(VRI compoundUri, VRI modelUri, AuthenticationToken token) {
        this(token);
        this.compoundUri = compoundUri;
        this.modelUri = modelUri;
        model = new Model(modelUri);
        try {
            compound = new Compound(compoundUri);
        } catch (ToxOtisException ex) {
            Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            model.loadFromRemote(token);
        } catch (ServiceInvocationException ex) {
            Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public VRI getSuperServiceUri() {
        return superServiceUri;
    }

    public void setSuperServiceUri(VRI superServiceUri) {
        this.superServiceUri = superServiceUri;
    }

    public LiteralValue prediction() {
        try {
            LiteralValue lv = compound.getProperty(model.getPredictedFeatures().get(0), token);
            if (lv == null) {
                predict(token);
                lv = compound.getProperty(model.getPredictedFeatures().get(0), token);
                logger.debug("Value predicted : " + lv);
            } else {
                System.out.println("Value found in DB : " + lv);
            }
            return lv;
        } catch (ServiceInvocationException ex) {
            Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    VRI predict(AuthenticationToken token) {
        IPostClient poster = ClientFactory.createPostClient(superServiceUri);
        poster.authorize(token);
        poster.addPostParameter("dataset_uri", compoundUri.toString());
        poster.addPostParameter("model_uri", modelUri.toString());
        poster.addPostParameter("algorithm_uri", "");
        poster.addPostParameter("dataset_service", "");
        poster.addPostParameter("launch", "Run");
        poster.setMediaType(Media.TEXT_URI_LIST);
        try {
            poster.post();
            int reponseCode = poster.getResponseCode();
            String response = poster.getResponseText();
            Task task = null;
            try {
                task = new Task(new VRI(response));
            } catch (URISyntaxException ex) {
                Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
            }
            TaskRunner runner = new TaskRunner(task);
            Task completedTask = runner.call();
            return completedTask.getResultUri();
        } catch (ServiceInvocationException ex) {
            Logger.getLogger(SuperPredictor.class.getName()).log(Level.SEVERE, null, ex);
        }


        return null;
    }
}

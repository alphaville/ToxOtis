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
package org.opentox.toxotis.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 * Abstact class that implements the interface {@link IDescriptorCalculation } offering
 * some facilities for easier calculation of descriptors using a remote DC algorithm.
 * 
 * @param <T> 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DescriptorCaclulation<T extends OTPublishable> extends OTPublishable<T> implements IDescriptorCalculation {

    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DescriptorCaclulation.class);

    public DescriptorCaclulation() {
        super();
    }

    public DescriptorCaclulation(VRI uri) {
        super(uri);
    }

    @Override
    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ServiceInvocationException {

        /** REQUEST */
        IPostClient pc = ClientFactory.createPostClient(descriptorCalculationAlgorithm);
        pc.authorize(token);
        pc.addPostParameter("dataset_uri", getUri().toString()); // and also we have dataset_uri={compound_uri} in certain cases
        if (serviceConfiguration != null) {
            for (int i = 0; i < serviceConfiguration.length; i++) {
                pc.addPostParameter(serviceConfiguration[i], serviceConfiguration[++i]);
            }
        }
        pc.setMediaType(Media.TEXT_URI_LIST);
        pc.post(); //Request is performed...

        /** RESPONSE */
        String taskUri = pc.getResponseText().trim();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            logger.error("Interruption exception while sleeping", ex);
            throw new ServiceInvocationException(ex);
        }

        try {
            TaskSpider taskSpider = new TaskSpider(new VRI(taskUri));
            return taskSpider.parse();
        } catch (URISyntaxException ex) {
            throw new ServiceInvocationException("The remote service at " + descriptorCalculationAlgorithm
                    + " returned an invalid task URI : " + taskUri, ex);
        } finally {
            if (pc != null) {
                try {
                    pc.close();
                } catch (IOException ex) {
                    String message = "Client used to perform the POST operation "
                            + "at '" + descriptorCalculationAlgorithm.toString() + "' could not close!";
                    logger.error(message, ex);
                    throw new ServiceInvocationException(message, ex);
                }
            }
        }
    }

    @Override
    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ServiceInvocationException {
        return calculateDescriptors(descriptorCalculationAlgorithm, token, "ALL", "true");
    }

    @Override
    public Future<VRI> futureDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ServiceInvocationException {
        return futureDescriptors(descriptorCalculationAlgorithm, token, Executors.newSingleThreadExecutor(), serviceConfiguration);
    }

    @Override
    public Future<VRI> futureDescriptors(final VRI descriptorCalculationAlgorithm,
            final AuthenticationToken token, ExecutorService executor, final String... serviceConfiguration) {
        Future<VRI> future = executor.submit(new Callable<VRI>() {

            @Override
            public VRI call() throws Exception {
                Task t = calculateDescriptors(descriptorCalculationAlgorithm, token, serviceConfiguration);
                final TaskRunner taskRunner = new TaskRunner(t);
                Task result = taskRunner.call();
                VRI resultUri = null;
                if (result != null) {
                    resultUri = result.getResultUri();
                }
                return resultUri;
            }
        });
        return future;
    }

    @Override
    public Future<VRI> futureCDKPhysChemDescriptors(AuthenticationToken token, VRI datasetService) throws ServiceInvocationException {
        return futureDescriptors(Services.DescriptorCalculation.cdkPhysChem(), token, datasetService);
    }

    @Override
    public Future<VRI> futureJoeLibDescriptors(AuthenticationToken token, VRI datasetService) throws ServiceInvocationException {
        return futureDescriptors(Services.DescriptorCalculation.joelib(), token, datasetService);
    }

    @Override
    public Future<VRI> futureDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, VRI datasetService) throws ServiceInvocationException {
        ArrayList<String> options = new ArrayList<String>();
        options.add("ALL");
        options.add("true");
        if (datasetService != null) {
            options.add("dataset_service");
            options.add(datasetService.toString());
        }
        String[] ops = new String[]{};
        ops = options.toArray(ops);
        return futureDescriptors(descriptorCalculationAlgorithm, token, ops);
    }
}

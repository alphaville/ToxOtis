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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * An interface implemented by OpenTox components on which descriptor calculation
 * can be applied such as Datasets and Compounds.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IDescriptorCalculation {

    /**
     * Calculates descriptors for this compound empoying a descriptor calculation
     * web service at the specified location. The method accepts a set or parameters
     * as a String array (<code>String[]</code>) for fine tuning of the remote
     * service.
     *
     * @param descriptorCalculationAlgorithm
     *      A descriptor calculation algorithm. Some descriptor calculation algorithm
     *      URIs are found in the collection <code>org.opentox.toxotis.client.collection.
     *      Services.DescriptorCalculation</code> such as TUM's implementation using
     *      CDK and JOELib descriptors.
     * @param token
     *      Token used to authenticate the client and grant it access to the
     *      various OpenTox resources.
     * @param serviceConfiguration
     *      A string array (<code>String[]</code>) used for fine tuning of the
     *      remote service. Successive pairs of values act as a parameter name -
     *      parameter value pair which is posted to the remote service.
     * @return
     *      The task returned by the remote service that will be used to monitor
     *      the progress of the descriptor calculation process.
     * @throws ToxOtisException
     *      In case some communication or connection error occurs between the
     *      client and the descriptor calculation service or the stream cannot
     *      open or close.
     */
    Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ServiceInvocationException;

    /**
     * Calculates all available descriptors using a remote descriptor calculation
     * service.
     *
     * @param descriptorCalculationAlgorithm
     *      A descriptor calculation algorithm. Some descriptor calculation algorithm
     *      URIs are found in the collection <code>org.opentox.toxotis.client.collection.
     *      Services.DescriptorCalculation</code> such as TUM's implementation using
     *      CDK and JOELib descriptors.
     * @param token
     *      Token used to authenticate the client and grant it access to the
     *      various OpenTox resources.
     * @return
     *      The task returned by the remote service that will be used to monitor
     *      the progress of the descriptor calculation process.
     * @throws ToxOtisException
     *      In case some communication or connection error occurs between the
     *      client and the descriptor calculation service or the stream cannot
     *      open or close.
     */
    Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ServiceInvocationException;

    /**
     * Use a remote Service to calculate (some or all available) descriptors for a
     * chemical compound. Such processes might take long to complete so are usually
     * executed asynchronously or put in an execution pool.
     *
     * @param descriptorCalculationAlgorithm
     *      The URI of an OpenTox descriptor calculation algorithm. A compound or
     *      a dataset is posted to a descriptor calculation algorithm and the expected
     *      result is a dataset containing the submitted compound(s) and the calculated
     *      descriptor values for each compound.
     * @param token
     *      Authentication token used for accessing the descriptor calculation
     *      service.
     * @param executor
     *      An executor used to sumbit the thread in. Be aware that the executor
     *      is not shutdown in this method so it is up to the user whether it should
     *      be shutdown or not.
     * @param serviceConfiguration
     *      A string array (<code>String[]</code>) used for fine tuning of the
     *      remote service. Successive pairs of values act as a parameter name -
     *      parameter value pair which is posted to the remote service. You can
     *      choose the features to be calculated using this argument. For instance
     *      if you want only the descriptor <code>Zagreb Index</code> calculated,
     *      you should set the options to <code>{"ALL","false","ZagrebIndexDescriptor",
     *      "true"}</code>.
     *
     * @return
     *      A <code>Future</code> waiting for the background job to complete and
     *      and return the URI of a Dataset with the calculated descriptors for
     *      this Compound.
     *
     */
    Future<VRI> futureDescriptors(final VRI descriptorCalculationAlgorithm, final AuthenticationToken token, ExecutorService executor, final String... serviceConfiguration);

    /**
     * Calculate descriptors for a chemical compound or a dataset providing the URI
     * of the descriptor calculation algorithm, the URI of a dataset service where the
     * result should be stored and an authentication token for accessing the various
     * services.
     *
     * @param descriptorCalculationAlgorithm
     *      URI of an OpenTox descriptor calculation algorithm
     * @param token
     *      Authentication token used for accessing the descriptor calculation service.
     * @param datasetService
     *      The URI of a dataset service, i.e. a URI of the form <code>http://domain_name.ext:1234/
     *      blah/blah/dataset</code>.
     * @return
     *      A <code>Future</code> result of an asynchronous job initiated
     * @throws ToxOtisException
     */
    Future<VRI> futureDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, VRI datasetService) throws ServiceInvocationException;

    Future<VRI> futureJoeLibDescriptors(AuthenticationToken token, VRI datasetService) throws ServiceInvocationException;

    Future<VRI> futureCDKPhysChemDescriptors(AuthenticationToken token, VRI datasetService) throws ServiceInvocationException;

    Future<VRI> futureDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token, String... serviceConfiguration) throws ServiceInvocationException;
}

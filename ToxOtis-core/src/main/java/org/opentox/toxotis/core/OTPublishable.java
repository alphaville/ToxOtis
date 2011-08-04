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

import org.opentox.toxotis.core.component.Task;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * <p align=justify>
 * An OpenTox component is <code>OTPublishable</code> if it is a class of online resources
 * that can be created by clients,  i.e. a client can perform a POST request on a service
 * and obtain a URI for its resource. Then it will be available online on the corresponding server
 * to which it was posted.
 * </p>
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTPublishable<T extends OTPublishable> extends OTOnlineResource<T> {

    /**
     * Create a new empty publishable object
     */
    public OTPublishable() {
    }

    /**
     * Create a new publishable object providing its URI
     * @param uri
     *      URI of the publishable object
     */
    public OTPublishable(VRI uri) {
        super(uri);
    }

    /**
     * Publish the component to a proper server identified by the uri of the
     * publishing service provided in this method. The resource will be posted to the
     * server in RDF format (application/rdf+xml).
     * @param vri
     *      URI of the service that is responsible for the publication of
     *      this kind of resources.
     * @param token 
     *      Provide an authentication token. If you think that the service does not
     *      require auhtentication/authorization, you can leave this field <code>null</code> or
     *      you can provide an empty authentication token. If the provided URI
     *      already contains an authentication token (as the URL parameter <code>
     *      tokenid</code>) it will be replaced by the new token provided to
     *      this method.
     * @return
     *      A Task for monitoring the progress of your request. If the service
     *      returns the URI of the resource right away and does not return a task,
     *      then the object you will receive from this method will now have an identifier,
     *      its status will be set to {@link org.opentox.toxotis.core.component.Task.Status#COMPLETED Completed}, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a 
     *      {@link org.opentox.toxotis.core.component.Task Task}
     *      object.
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     * @throws InactiveTokenException
     *      In case the provided token is invalidated (user has logged out), or
     *      has expired.
     */
    public abstract Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException;

    public Future<VRI> publish(final VRI vri, final AuthenticationToken token, ExecutorService executor) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest("The Provided token is inactive");
        }

        Callable<VRI> backgroundJob = new Callable<VRI>() {

            @Override
            public VRI call() throws Exception {
                Task t = publishOnline(vri, token);

                while (t.getStatus().equals(Task.Status.RUNNING)||t.getStatus().equals(Task.Status.QUEUED)) {
                    t.loadFromRemote();
                    Thread.sleep(100);
                }
                if (!Task.Status.COMPLETED.equals(t.getStatus())) {
                    throw new ServiceInvocationException("Task failed! This entity was not published online "
                            + "due to some unexpected error. Error Report : " + t.getErrorReport());
                }
                return t.getResultUri();
            }
        };
        Future<VRI> future = executor.submit(backgroundJob);
        executor.shutdown();
        return future;
    }

    public Future<VRI> publish(final VRI vri, final AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest("The Provided token is inactive");
        }
        return publish(vri, token, Executors.newSingleThreadExecutor());
    }

    /**
     * Publish the component to a standard (default) server. The resource will be posted to the
     * server in RDF format (application/rdf+xml). If you want to specify at which
     * server the resource should be posted, use the {@link
     * OTPublishable#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken) other method}.
     * @param token
     *      Provide an authentication token. If you think that the service does not
     *      require auhtentication/authorization, you can leave this field <code>null</code> or
     *      you can provide an empty authentication token.If the provided URI
     *      already contains an authentication token (as the URL parameter <code>
     *      tokenid</code>) it will be replaced by the new token provided to
     *      this method.
     * @return
     *      A Task for monitoring the progress of your request. If the service
     *      returns the URI of the resource right away and does not return a task,
     *      then the object you will receive from this method will now have an identifier,
     *      its status will be set to 
     *      {@link org.opentox.toxotis.core.component.Task.Status#COMPLETED Completed}, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     * @throws ServiceInvocationException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     * @see OTPublishable#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken) alternative method
     */
    public abstract Task publishOnline(AuthenticationToken token) throws ServiceInvocationException;
}

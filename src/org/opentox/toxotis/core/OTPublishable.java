package org.opentox.toxotis.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
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
     *      its status will be set to {@link Task.Status#COMPLETED }, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     */
    public abstract Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException;

    public Future<VRI> publish(final VRI vri, final AuthenticationToken token, ExecutorService executor) throws ToxOtisException {
        Callable<VRI> backgroundJob = new Callable<VRI>() {

            public VRI call() throws Exception {
                Task t = publishOnline(vri, token);
                while (t.getStatus().equals(Task.Status.RUNNING)) {
                    t.loadFromRemote();
                    Thread.sleep(100);
                }
                if (!Task.Status.COMPLETED.equals(t.getStatus())) {
                    throw new ToxOtisException("Task failed! This entity was not published online "
                            + "due to some unexpected error. Error Report : " + t.getErrorReport());
                }
                return t.getResultUri();
            }
        };
        return executor.submit(backgroundJob);
    }

    public Future<VRI> publish(final VRI vri, final AuthenticationToken token) throws ToxOtisException {
        return publish(vri, token, Executors.newSingleThreadExecutor());
    }


    /**
     * Publish the component to a standard server. The resource will be posted to the
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
     *      its status will be set to {@link Task.Status#COMPLETED }, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     * @see OTPublishable#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken) alternative method
     */
    public abstract Task publishOnline(AuthenticationToken token) throws ToxOtisException;
}

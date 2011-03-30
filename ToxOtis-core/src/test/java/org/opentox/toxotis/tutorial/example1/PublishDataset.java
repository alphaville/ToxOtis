package org.opentox.toxotis.tutorial.example1;

import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 */
public class PublishDataset {

    public PublishDataset() {
    }

    @Test
    public void test1() throws ServiceInvocationException, ToxOtisException {
        Dataset ds = new Dataset(Services.ideaconsult().augment("dataset", 9).addUrlParameter("max", 10));

        System.out.println("Downloading " + ds.getUri() + "...");
        ds.loadFromRemote();
        System.out.println("Download complete");

        AuthenticationToken at = new AuthenticationToken("guest", "guest");

        System.out.println("Publishing...");
        Task t = ds.publishOnline(Services.ambitUniPlovdiv().augment("dataset"), at);
        TaskRunner runner = new TaskRunner(t);
        Task ran = runner.call();
        System.out.println(ran.getResultUri());

        Dataset newDs = new Dataset(ran.getResultUri());
        newDs.loadFromRemote(at);
        System.out.println(newDs.getInstances());
    }
}

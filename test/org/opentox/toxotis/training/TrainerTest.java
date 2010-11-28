package org.opentox.toxotis.training;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author hampos
 */
public class TrainerTest {

    public TrainerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTrain() throws URISyntaxException, ToxOtisException, InterruptedException, ExecutionException, IOException {
        Algorithm alg = new Algorithm(Services.NtuaAlgorithms.mlr());

//        Parameter p1 = new Parameter("gamma", new LiteralValue(0.746));
//        Parameter p2 = new Parameter("cost", new LiteralValue(243.00));
//
//        alg.getParameters().add(p1);
//        alg.getParameters().add(p2);

        Dataset ds = new Dataset(new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/6"));
        Feature pf = new Feature(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/20180"));

        Trainer trainer = new Trainer(alg, ds, pf);

        AuthenticationToken at =
                new AuthenticationToken(new java.io.File("/home/chung/toxotisKeys/my.key")); // << Provide your private key here

        if (at != null) {
            System.out.println(at.stringValue());
            System.out.println(at.getTokenUrlEncoded());
            System.out.println(at.getTokenCreationTimestamp());
            System.out.println(at.getTokenCreationDate());
            System.out.println(at.getUser());
        }

        Task task = trainer.train(at);// << Use your credentials here!
        TaskRunner tr = new TaskRunner(task);
        Future<Task> future = Executors.newSingleThreadExecutor().submit(tr);
        System.out.println("Processing...");
        Task completedTask = future.get();
        System.out.println(completedTask.getStatus());
        if (completedTask.getStatus() == Task.Status.COMPLETED) {
            System.out.println(completedTask.getResultUri());
            Model model = new Model(completedTask.getResultUri()).loadFromRemote();
            System.out.println("Predicted Feature : " + model.getPredictedFeature().getUri());
            return;
        }
        System.out.println(completedTask.getErrorReport());
    }
}

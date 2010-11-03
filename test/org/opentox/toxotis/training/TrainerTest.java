/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.training;

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
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TypedValue;
import static org.junit.Assert.*;

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
    public void testTrain() throws URISyntaxException, ToxOtisException, InterruptedException, ExecutionException {
        Algorithm alg = new Algorithm(new VRI("http://opentox.ntua.gr:3000/algorithm/mlr"));
        Parameter p1 = new Parameter();
        p1.setName("gamma");
        p1.setTypedValue(new TypedValue("1"));
        alg.getParameters().add(p1);
       // alg.loadFromRemote();

        Dataset ds = new Dataset(new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/6"));

        Feature pf = new Feature(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/20180"));

        Trainer trainer = new Trainer(alg, ds, pf);

        Task task = trainer.train(null);

        TaskRunner tr = new TaskRunner(task);

        Future<Task> future = Executors.newSingleThreadExecutor().submit(tr);

        Task completedTask = future.get();

        System.out.println(completedTask.getResultUri());

    }

}
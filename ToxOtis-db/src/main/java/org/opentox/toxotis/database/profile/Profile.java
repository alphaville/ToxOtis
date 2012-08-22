package org.opentox.toxotis.database.profile;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.engine.task.AddTask;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Profile {

    public Profile() {
    }

    public void testWriteTaskMultithreadedly() throws InterruptedException {
        int poolSize = 200;
        int folds = 50 * poolSize + 20;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new Profile().testWriteTask();
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

    }

    public void testWriteTask() throws Exception {
        ErrorReport er_trace_2 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er_trace_1 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er = new ErrorReport(502, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        er_trace_1.setErrorCause(er_trace_2);
        er.setErrorCause(er_trace_1);

        Task t = new Task(Services.ntua().augment("task", UUID.randomUUID()));
        t.setMeta(new MetaInfoImpl().addTitle("ZZZZZZZZZz").
                addContributor("ME").
                addHasSource(new ResourceValue(new VRI("http://something.org/resource/model.234"), OTClasses.model())));
        t.setErrorReport(er);
        t.setPercentageCompleted(0);
        t.setHttpStatus(407);
        t.setStatus(Task.Status.ERROR);

        User u = User.GUEST; // this user is already in the database - no need to re-add it.
        t.setCreatedBy(u);
        DbWriter writer = new AddTask(t);
        writer.write();
        writer.close(); // CLOSE the writer!!! (SHORTLY after you don't need it!!!)
       
    }

    public static void main(String... art) throws Exception{        
        new Profile().testWriteTaskMultithreadedly();
    }
}

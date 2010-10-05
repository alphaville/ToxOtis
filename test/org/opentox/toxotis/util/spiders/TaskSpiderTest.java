package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Task;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TaskSpiderTest {

    public TaskSpiderTest() {
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
    public void testSomeMethod() throws URISyntaxException , ToxOtisException{
        VRI vri = new VRI("http://opentox.informatik.tu-muenchen.de:8080/OpenTox-dev/task/20f3a684-80f0-4305-afe5-122b06d9ee50");
        TaskSpider ts = new TaskSpider(vri);
        Task t = ts.parse();
        System.out.println(t.getResultUri());
        System.out.println(t.getStatus());
        System.out.println(t.getMeta());
    }

}
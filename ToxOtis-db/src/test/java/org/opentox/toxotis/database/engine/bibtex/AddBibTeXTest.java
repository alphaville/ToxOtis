/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.database.engine.bibtex;

import java.net.URISyntaxException;
import junit.framework.TestCase;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;

/**
 *
 * @author chung
 */
public class AddBibTeXTest extends TestCase {
    
    public AddBibTeXTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomeMethod() throws Exception {
        User u = User.GUEST;
        BibTeX bt = new BibTeX(new VRI("http://someservice.com/bibtex/123"));
        bt.setAuthor("asfd");
        bt.setCreatedBy(u);
        bt.setVolume(1);
        
        System.out.println(new AddBibTeX(bt).write());
    }

}

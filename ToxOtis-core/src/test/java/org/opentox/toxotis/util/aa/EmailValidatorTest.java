package org.opentox.toxotis.util.aa;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class EmailValidatorTest {
    
    public EmailValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testValidateEmail() {
        assertTrue(EmailValidator.validate("mymail@mail.ntua.gr"));
        assertTrue(EmailValidator.validate("pantelis@yahoo.com"));
        assertTrue(EmailValidator.validate("pantelis@yahoo.co.uk"));        
    }
    
    @Test
    public void testValidateBadEmail(){
        assertFalse(EmailValidator.validate("pantelis@someserver.xyz"));
        assertFalse(EmailValidator.validate("pantelis_att_is_missing"));
        assertFalse(EmailValidator.validate("illegalCharact*r@hotmail.com"));
    }
}

package org.opentox.toxotis.tutorial.example2;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.TokenPool;

/**
 *
 * @author Pantelis Sopasakis
 */
public class Token {

    public Token() {
    }

    @Test
    public void test1() throws ServiceInvocationException, ToxOtisException {
        System.out.println("Test 1 - Get token, retrieve user, validate, invalidate");
        AuthenticationToken token = new AuthenticationToken("guest", "guest");
        User u = token.getUser();
        System.out.println(u);

        boolean isValid = token.validate();
        System.out.println("The token is " + (isValid ? "" : "**NOT** ") + "valid!");


        String uri = "http://google.com";
        String questionTemplate = "- Is %s on %s allowed?\n- %s";
        boolean isAllowed = token.authorize("GET", "http://google.com");
        System.out.println(String.format(questionTemplate, "GET", uri, isAllowed));

        uri = "https://ambit.uni-plovdiv.bg:8443/ambit2/dataset/6";
        isAllowed = token.authorize("GET", uri);
        System.out.println(String.format(questionTemplate, "GET", uri, isAllowed));

        uri = "https://ambit.uni-plovdiv.bg:8443/ambit2/dataset/6";
        isAllowed = token.authorize("DELETE", uri);
        System.out.println(String.format(questionTemplate, "DELETE", uri, isAllowed));

        token.invalidate();
        isValid = token.validate();
        System.out.println("The token is " + (isValid ? "" : "**NOT** ") + "valid!\n");
    }

    @Test
    public void test2() throws ServiceInvocationException, ToxOtisException, IOException {
        System.out.println("Test 2 - AA using files");
        File secretFile = new File("/home/chung/toxotisKeys/Sopasakis.key");
        AuthenticationToken token = new AuthenticationToken(secretFile);
        System.out.println(token);
        token.invalidate();
        System.out.println("");

    }

    @Test
    public void test3() throws ServiceInvocationException, ToxOtisException, IOException {
        System.out.println("Test 2 - Using a token pool");
        TokenPool pool = TokenPool.getInstance();
        File secretFileMe = new File("/home/chung/toxotisKeys/Sopasakis.key");
        File secretFileHampos = new File("/home/chung/toxotisKeys/hampos.key");
        System.out.println("Login twice a single user...");
        pool.login(secretFileMe);
        pool.login(secretFileMe);
        System.out.println("Pool size is " + pool.size());
        System.out.println("Logging out a user");
        pool.logout("Sopasakis");
        System.out.println("Pool size is " + pool.size());
        System.out.println("Login two users...");
        pool.login(secretFileMe);
        pool.login(secretFileHampos);
        System.out.println("Pool size is " + pool.size());
        System.out.println("Logging out everyone");
        pool.logoutAll();
        System.out.println("The pool is " + (pool.size() == 0 ? "" : "**NOT** ") + "empty");


    }
}

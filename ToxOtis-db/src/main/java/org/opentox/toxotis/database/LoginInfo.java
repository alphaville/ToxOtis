package org.opentox.toxotis.database;

import java.net.URI;
import java.sql.Connection;

public class LoginInfo {

    protected String scheme;
    protected String host;
    protected String port;
    protected String database;
    protected String user;
    protected String password;
    private static final long serialVersionUID = 7128437130482954122L;
    
    public static LoginInfo LOGIN_INFO = new LoginInfo().setPassword("opensess@me");

    public LoginInfo(String scheme, String host, String port, String database, String user, String password) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public LoginInfo() {
        setScheme("jdbc:mysql");
        setHostname("localhost");
        setDatabase("toxotisdb");
        setPort("3306");
        setUser("root");

    }

    public void setURI(URI uri) throws Exception {
        String driver = uri.getScheme();

        URI u1 = new URI(uri.getSchemeSpecificPart());
        setScheme(driver + ":" + u1.getScheme());

        setHostname(u1.getHost());
        setPort(Integer.toString(u1.getPort()));
        setDatabase(u1.getPath().substring(1));
    }

    public void setURI(Connection connection) throws Exception {
        setURI(new URI(connection.getMetaData().getURL()));
        String username = connection.getMetaData().getUserName();
        setUser(username.substring(0, username.indexOf("@")));
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHostname() {
        return host;
    }

    public void setHostname(String hostname) {
        this.host = hostname;
    }

    public String getPassword() {
        return password;
    }

    public LoginInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return user;
    }
}



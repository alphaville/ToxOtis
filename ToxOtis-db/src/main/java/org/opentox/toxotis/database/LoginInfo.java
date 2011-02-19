/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */

package org.opentox.toxotis.database;

import java.net.URI;
import java.sql.Connection;

/**
 * 
 * @deprecated
 */
@Deprecated
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



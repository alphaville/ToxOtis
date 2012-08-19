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
package org.opentox.toxotis.util.aa.policy;

/**
 * Interface for Policy Rules.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPolicyRule {

    /**
     * Name of the rule.
     * @return 
     *      Name of the policy rule as String.
     */
    String getName();

    /**
     * Name of the service.
     * @return 
     *      Service name as String.
     */
    String getServiceName();

    /**
     * URI of the resource for which the rule is written.
     * @return 
     *      The URI of the target resource as a String.
     */
    String getTargetUri();

    /**
     * Whether the DELETE method is allowed
     * @return 
     *      <code>true</code> if a DELETE is allowed and <code>false</code> otherwise.
     */
    boolean isAllowDelete();

    /**
     * Whether the GET method is allowed
     * @return 
     *      <code>true</code> if a GET is allowed and <code>false</code> otherwise.
     */
    boolean isAllowGet();

    /**
     * Whether the POST method is allowed
     * @return 
     *      <code>true</code> if a POST is allowed and <code>false</code> otherwise.
     */
    boolean isAllowPost();

    /**
     * Whether the PUT method is allowed
     * @return 
     *      <code>true</code> if a PUT is allowed and <code>false</code> otherwise.
     */
    boolean isAllowPut();

    /**
     * Set the access regime for the DELETE method.
     * 
     * @param allowDelete
     *      Whether the DELETE method is allowed
     * @return 
     *      The current modifiable policy rule object with updated rule for the
     *      DELETE method.
     */
    IPolicyRule setAllowDelete(boolean allowDelete);

    /**
     * Set the access regime for the GET method.
     * 
     * @param allowDelete
     *      Whether the GET method is allowed
     * @return 
     *      The current modifiable policy rule object with updated rule for the
     *      GET method.
     */
    IPolicyRule setAllowGet(boolean allowGet);

    /**
     * Set the access regime for the POST method.
     * 
     * @param allowDelete
     *      Whether the POST method is allowed
     * @return 
     *      The current modifiable policy rule object with updated rule for the
     *      POST method.
     */
    IPolicyRule setAllowPost(boolean allowPost);

    /**
     * Set the access regime for the PUT method.
     * 
     * @param allowDelete
     *      Whether the PUT method is allowed
     * @return 
     *      The current modifiable policy rule object with updated rule for the
     *      PUT method.
     */
    IPolicyRule setAllowPut(boolean allowPut);

    /**
     * A method to easily set which HTTP methods are allowed and which not.
     * @param get
     *      Whether GET is allowed.
     * @param post
     *      Whether POST is allowed.
     * @param put
     *      Whether PUT is allowed.
     * @param delete
     *      Whether DELETE is allowed.
     * @return 
     *      The current modifiable policy rule object with updated
     *      rules per method.
     */
    IPolicyRule setAllowances(boolean get, boolean post, boolean put, boolean delete);

    /**
     * Specify the name of the rule.
     * 
     * @param name
     *      Name of the rule as a String.
     * @return 
     *      The current modifiable policy rule object with updated name.
     */
    IPolicyRule setName(String name);

    /**
     * Setter method for the service name.
     * 
     * @param serviceName
     *      The service name as a String.
     * @return 
     *      The current modifiable policy rule object with updated service name.
     */
    IPolicyRule setServiceName(String serviceName);

    /**
     * Setter method for the URI of the target resource.
     * 
     * @param targetUri
     *      The URI of the resource for which the rule is written.
     * @return 
     *      The current modifiable policy rule object with updated target URI.
     */
    IPolicyRule setTargetUri(String targetUri);
}

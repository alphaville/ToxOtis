package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLForm extends HTMLExpandableComponent {

    HTMLForm setActionUrl(String actionUrl);
    HTMLForm addInput(String description, String parameterName, String value, int size);

}
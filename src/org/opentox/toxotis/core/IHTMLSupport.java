package org.opentox.toxotis.core;

import org.opentox.toxotis.core.html.HTMLContainer;

/**
 * HTML support for online resources. This interface is implemented by online
 * resources that need to provide HTML representations appart from the obligatory
 * RDF one.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IHTMLSupport extends IOnlineResource {

    /**
     * Create an HTML container for the online resource.
     * @return
     */
    HTMLContainer inHtml();
}

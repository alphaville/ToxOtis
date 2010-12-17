package org.opentox.toxotis.core;

import org.opentox.toxotis.core.html.HTMLContainer;

/**
 * HTML support for online resources. This interface is implemented by online
 * resources that need to provide HTML representations appart from the obligatory
 * RDF one and by any other class that needs to provide an HTML representation of
 * itself or needs to provide fragments of HTML to other resources. 
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IHTMLSupport {

    /**
     * Create an HTML container for the online resource.
     * @return
     */
    HTMLContainer inHtml();
}

package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.OntModel;
import java.net.URI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTComponent {

    protected URI uri;

    public OTComponent() {
    }

    public OTComponent(URI uri) {
        this.uri = uri;
    }

//    public abstract OntModel toOntModel();
//    public abstract void createFrom(OntModel model);

    
}

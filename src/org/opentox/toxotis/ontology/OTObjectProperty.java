package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;


public interface OTObjectProperty extends OTProperty {

    Collection<OntologicalClass> getRange();

    void setRange(Collection<OntologicalClass> range);

    ObjectProperty asObjectProperty(OntModel model);

}
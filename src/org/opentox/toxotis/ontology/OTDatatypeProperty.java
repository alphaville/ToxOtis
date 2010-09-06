package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;

public interface OTDatatypeProperty extends OTProperty{

    Collection<XSDDatatype> getRange();

    void setRange(Collection<XSDDatatype> range);

    DatatypeProperty asDatatypeProperty(OntModel model);
}
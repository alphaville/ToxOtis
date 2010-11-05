package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.spiders.AnyValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VariableValue<T> extends OTComponent<VariableValue<T>> {

    private AnyValue<T> value;
    private VariableInfo variableInfo;

    public AnyValue<T> getValue() {
        return value;
    }

    public void setValue(AnyValue<T> value) {
        this.value = value;
    }

    public VariableInfo getVariableInfo() {
        return variableInfo;
    }

    public void setVariableInfo(VariableInfo variableInfo) {
        this.variableInfo = variableInfo;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.VariableValue().inModel(model));
        if (getMeta()!=null){
            getMeta().attachTo(indiv, model);
        }
        if (value != null) {
            T v = value.getValue();
            if (v != null) {
                indiv.addLiteral(OTDatatypeProperties.value().asDatatypeProperty(model),
                        model.createTypedLiteral(v, value.getType()));
            }
        }
        if (variableInfo != null) {
            indiv.addProperty(OTObjectProperties.variable().asObjectProperty(model),
                    variableInfo.asIndividual(model));
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

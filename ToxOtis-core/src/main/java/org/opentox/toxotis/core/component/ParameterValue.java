package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ParameterValue extends OTComponent<ParameterValue> {  // ot:ParameterValue

    public ParameterValue() {
    }

    public ParameterValue(VariableValue... values) {
        this();
        if (values != null) {
            for (VariableValue vv : values) {
                getValues().add(vv);
            }
        }
    }

    public ParameterValue(int index, VariableValue... values) {
        this(values);
        setIndex(index);
    }
    private int index = -1;// optional index for the ot:ParameterValue node.
    private ArrayList<VariableValue> values = new ArrayList<VariableValue>();

    public ArrayList<VariableValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<VariableValue> values) {
        this.values = values;
    }

    public int getIndex() {
        return index;
    }

    public ParameterValue setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.ParameterValue().inModel(model));
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        ObjectProperty multiParam = OTObjectProperties.variableValues().asObjectProperty(model);
        if (values != null) {
            for (VariableValue vv : values) {
                indiv.addProperty(multiParam, vv.asIndividual(model));
            }
        }
        if (index != -1) {
            indiv.addLiteral(OTDatatypeProperties.index().asDatatypeProperty(model), model.createTypedLiteral(index, XSDDatatype.XSDinteger));
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

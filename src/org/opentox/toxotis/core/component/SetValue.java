package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SetValue extends OTComponent<SetValue>{  // ot:Set

    private ArrayList<VariableValue> values = new ArrayList<VariableValue>();

    public ArrayList<VariableValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<VariableValue> values) {
        this.values = values;
    }    

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.Set().inModel(model));
        if (getMeta()!=null){
            getMeta().attachTo(indiv, model);
        }
        ObjectProperty multiParam = OTObjectProperties.multiParameter().asObjectProperty(model);
        if (values!=null){
            for (VariableValue vv : values){
                indiv.addProperty(multiParam, vv.asIndividual(model));
            }
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

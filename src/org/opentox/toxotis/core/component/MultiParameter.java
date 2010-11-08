package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MultiParameter extends OTComponent<MultiParameter> {

    private ArrayList<SetValue> setValues = new ArrayList<SetValue>();

    public ArrayList<SetValue> getSetValues() {
        return setValues;
    }

    public void setSetValues(ArrayList<SetValue> setValues) {
        this.setValues = setValues;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.SetValuedParameter().inModel(model));
        if (setValues.size() > 1) {
            indiv.addRDFType(OTClasses.VectorParameter().inModel(model));
            for (int i = 0; i < setValues.size(); i++) {
                SetValue setValue = setValues.get(i);
                Individual valueIndiv = setValue.asIndividual(model);
                valueIndiv.addLiteral(OTDatatypeProperties.index().asDatatypeProperty(model), i);
                indiv.addProperty(OTObjectProperties.setValues().asObjectProperty(model), valueIndiv);
            }
        } else {
            SetValue setValue = setValues.get(1);
            Individual valueIndiv = setValue.asIndividual(model);
            indiv.addProperty(OTObjectProperties.setValues().asObjectProperty(model), valueIndiv);
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }


        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

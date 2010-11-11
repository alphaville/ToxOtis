package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureValue extends OTComponent<FeatureValue> {

    private Feature feature;
    private LiteralValue value;

    public FeatureValue() {
    }

    public FeatureValue(Feature feature, LiteralValue value) {
        this.feature = feature;
        this.value = value;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public LiteralValue getValue() {
        return value;
    }

    public void setValue(LiteralValue value) {
        this.value = value;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String featureValueUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(featureValueUri, OTClasses.FeatureValue().inModel(model));
        indiv.addProperty(OTObjectProperties.feature().asObjectProperty(model), feature.asIndividual(model));
        if (value != null) {
            indiv.addLiteral(OTDatatypeProperties.value().asDatatypeProperty(model), model.createTypedLiteral(value.getValue(), value.getType()));
        }
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

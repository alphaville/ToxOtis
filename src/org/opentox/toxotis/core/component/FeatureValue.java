package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A pair of a feature and a Literal value. Feature Values appear in Datasets wrapping
 * all values in it.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureValue extends OTComponent<FeatureValue> {

    private Feature feature;
    private LiteralValue value;
    private static final String DISCRIMINATOR = "featureValue";

    @Override
    public VRI getUri() {
        if (uri == null) {
            uri = Services.anonymous().augment(DISCRIMINATOR, hashCode());
        }
        return uri;
    }

    public FeatureValue() {
        super();
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeatureValue other = (FeatureValue) obj;
        if (this.feature != other.feature && (this.feature == null || !this.feature.equals(other.feature))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.feature != null ? this.feature.hashCode() : 0);
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}

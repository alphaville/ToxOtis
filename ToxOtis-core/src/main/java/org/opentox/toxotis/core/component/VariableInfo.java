package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.Parameter.ParameterScope;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VariableInfo extends OTComponent<VariableInfo> {

    private Parameter.ParameterScope scope;

    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        if (getUri() == null) {
            setUri(Services.opentox().augment(getMeta().getTitles().iterator().next().getValue().toString()));
        }
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.VariableInfo().inModel(model));
        MetaInfo metaInfo = getMeta();
        if (metaInfo != null) {
            metaInfo.attachTo(indiv, model);
        }
        // scope
        if (getScope() != null) {
            indiv.addLiteral(OTDatatypeProperties.paramScope().asDatatypeProperty(model),
                    model.createTypedLiteral(getScope().toString(), XSDDatatype.XSDstring));
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

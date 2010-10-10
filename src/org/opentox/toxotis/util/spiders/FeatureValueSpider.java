package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.core.component.FeatureValue;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class FeatureValueSpider extends Tarantula<FeatureValue> {

    public FeatureValueSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public FeatureValue parse() throws ToxOtisException {
        Statement featureResourceStatement = resource.getProperty(OTObjectProperties.feature().asObjectProperty(model));
        if (featureResourceStatement == null) {
            throw new ToxOtisException("Error while parsing a feature value node: No features where assigned to the feature value node!");
        }
        Resource featureResource = featureResourceStatement.getResource();
        FeatureSpider fSpider = new FeatureSpider(featureResource, model);
        Feature feature = fSpider.parse();
        Statement valueStatement = resource.getProperty(OTDatatypeProperties.value().asDatatypeProperty(model));
        if (valueStatement == null) {
            throw new ToxOtisException("Error while parsing a feature value node: No value is assigned to the feature value node!");
        }
        Literal value = valueStatement.getObject().as(Literal.class);
        FeatureValue fValue;
        /*
         * TODO: Also handle dates!
         */
        if (value.getDatatype().equals(XSDDatatype.XSDdouble)) {
            fValue = new FeatureValue(feature, new TypedValue<Double>(value.getDouble(), XSDDatatype.XSDdouble));
        } else if (value.getDatatype().equals(XSDDatatype.XSDinteger)) {
            fValue = new FeatureValue(feature, new TypedValue<Integer>(value.getInt(), XSDDatatype.XSDinteger));
        } else if (value.getDatatype().equals(XSDDatatype.XSDfloat)) {
            fValue = new FeatureValue(feature, new TypedValue<Float>(value.getFloat(), XSDDatatype.XSDfloat));
        } else {
            fValue = new FeatureValue(feature, new TypedValue<String>(value.getString(), XSDDatatype.XSDstring));
        }

        return fValue;
    }
}

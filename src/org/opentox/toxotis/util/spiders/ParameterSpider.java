package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.toxotis.core.Parameter;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ParameterSpider extends Tarantula<Parameter> {

    public ParameterSpider(OntModel model, Resource resource) {
        super(resource, model);
    }

    @Override
    public Parameter parse() {
        Parameter parameter = new Parameter();
        TypedValue<String> typedScope = retrieveProp(OTDatatypeProperties.paramScope().
                asDatatypeProperty(model));
        String scope = typedScope != null ? typedScope.getValue().toUpperCase() : null;
        parameter.setScope(
                Parameter.ParameterScope.valueOf(scope));
        TypedValue paramTypedValue = retrieveProp(OTDatatypeProperties.paramValue().asDatatypeProperty(model));
        parameter.setTypedValue(paramTypedValue);

        MetaInfoSpider metaSpider = new MetaInfoSpider(resource, model);
        MetaInfo mi = metaSpider.parse();
        parameter.setMeta(mi);
        if (mi.getIdentifier() != null) {
            parameter.setName(mi.getIdentifier().getValue());
        } else if (mi.getComment() != null) {
            parameter.setName(mi.getComment().getValue());
        }

        return parameter;
    }
}

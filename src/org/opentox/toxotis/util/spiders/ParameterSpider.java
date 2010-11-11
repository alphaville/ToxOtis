package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Set;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.ontology.LiteralValue;
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


        /*
         * Parse parameter scope...
         */
        Set<LiteralValue> scopes = retrievePropertyLiterals(OTDatatypeProperties.paramScope().
                asDatatypeProperty(model));
        if (scopes != null && !scopes.isEmpty()) {
            if (scopes.size() > 1) {
                System.err.println("[WARN ] Multiple scopes declared for the parsed parameter! " +
                        "Only one will be taken into account.");
            }
            LiteralValue scope = scopes.iterator().next();
            parameter.setScope(Parameter.ParameterScope.valueOf(scope.getValue().toString().toUpperCase()));
        }

        Set<LiteralValue> paramTypedValues = retrievePropertyLiterals(OTDatatypeProperties.paramValue().asDatatypeProperty(model));
        if (paramTypedValues != null && !paramTypedValues.isEmpty()) {
            if (paramTypedValues.size()>1){
                System.err.println("[WARN ] Multiple parameter values are declared for a single parameter. " +
                        "Only one of them will be taken into account!");
            }
            LiteralValue paramTypedValue = paramTypedValues.iterator().next();
            parameter.setTypedValue(paramTypedValue);
        }
        

        MetaInfoSpider metaSpider = new MetaInfoSpider(resource, model);
        MetaInfo mi = metaSpider.parse();
        parameter.setMeta(mi);
        // The name of the parameter is defined using the dc:title property
        if (mi.getTitles() != null && !mi.getTitles().isEmpty()) {
            parameter.setName(mi.getTitles().iterator().next().getValue().toString());
        }

        return parameter;
    }
}


package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.toxotis.core.Parameter;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ParameterSpider extends Tarantula<Parameter>{

    public ParameterSpider(OntModel model, Resource resource) {
        super(resource, model);
    }

    @Override
    public Parameter parse(){
        Parameter parameter = new Parameter();
        String scope = retrieveProp(OTDatatypeProperties.paramScope().
                asDatatypeProperty(model)).toUpperCase();
        parameter.setScope(
                Parameter.ParameterScope.valueOf(scope));
        parameter.setValue(retrieveProp(OTDatatypeProperties.paramValue().asDatatypeProperty(model)));
        MetaInfoSpider metaSpider = new MetaInfoSpider(resource, model);
        parameter.setMeta(metaSpider.parse());
        return parameter;
    }

}

package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.core.Model;
import org.opentox.toxotis.core.Parameter;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ModelSpider extends Tarantula<Model>{

    VRI uri;

    public ModelSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
    }

    public ModelSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ModelSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.resource = model.getResource(uri);
    }

    @Override
    public Model parse() throws ToxOtisException {
        Model m = new Model();

        m.setUri(uri);
        m.setMeta(new MetaInfoSpider(resource, model).parse());

        StmtIterator itDataset = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.trainingDataset().asObjectProperty(model),
                (RDFNode) null));
        if (itDataset.hasNext()) {
            try {
                m.setDataset(new VRI(itDataset.nextStatement().getObject().as(Resource.class).getURI()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(ModelSpider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        StmtIterator itFeature = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.predictedVariables().asObjectProperty(model),
                (RDFNode) null));

        if (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(model,
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            m.setPredictedFeature(fspider.parse());
        }

        itFeature = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.dependentVariables().asObjectProperty(model),
                (RDFNode) null));
        if (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(model,
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            m.setDependentFeature(fspider.parse());
        }

        itFeature = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.independentVariables().asObjectProperty(model),
                (RDFNode) null));
        ArrayList<Feature> indepFeatures = new ArrayList<Feature>();
        while (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(model,
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            indepFeatures.add(fspider.parse());
        }
        m.setIndependentFeatures(indepFeatures);

        StmtIterator itAlgorithm = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.algorithm().asObjectProperty(model),
                (RDFNode) null));
        if (itAlgorithm.hasNext()) {
            AlgorithmSpider aspider;
//            try {
                aspider = new AlgorithmSpider(itAlgorithm.nextStatement().getObject().as(Resource.class),model);
                m.setAlgorithm(aspider.parse());
//            } catch (URISyntaxException ex) {
//                Logger.getLogger(ModelSpider.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }

        StmtIterator itParam = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.parameters().asObjectProperty(model),
                (RDFNode) null));

        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        while (itParam.hasNext()) {
            ParameterSpider paramSpider = new ParameterSpider(model, itParam.nextStatement().getObject().as(Resource.class));
            parameters.add(paramSpider.parse());
        }
        m.setParameters(parameters);
        

        return m;
    }

}

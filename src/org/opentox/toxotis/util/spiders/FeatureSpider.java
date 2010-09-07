package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class FeatureSpider extends Tarantula<Feature> {

    VRI uri;

    public FeatureSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
        model.write(System.out);
    }

    public FeatureSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FeatureSpider(OntModel model, String uri) {
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
    public Feature parse() {
        Feature feature = new Feature();
        feature.setMeta(new MetaInfoSpider(resource, model).parse());
        feature.setUri(uri);

        //feature.setOntologies(getFeatureTypes(resource));

        Statement unitsStatement = resource.getProperty(
                OTDatatypeProperties.units().asDatatypeProperty(model));
        if (unitsStatement != null) {
            //feature.setUnits(unitsStatement.getString());
            System.out.println(unitsStatement.getString());
        }

        return feature;
    }

    public static void main(String... args) throws Exception {

        FeatureSpider fSpider = new FeatureSpider(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/22202"));
        Feature f = fSpider.parse();
        fSpider.model.write(System.out);
        System.out.println(f.getMeta().getTitle());

    }
}

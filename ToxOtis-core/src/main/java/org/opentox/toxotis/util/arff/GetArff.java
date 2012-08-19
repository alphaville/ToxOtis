package org.opentox.toxotis.util.arff;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Downloads ARFF from a remote location. If text/x-arff is available, 
 * then it's directly downloaded from the remote location without the need for
 * conversion.
 * @author Pantelis Sopasakis
 */
public class GetArff {

    private final VRI datasetUri;
    private boolean parseString = false;
    private boolean parseNominal = true;
    private static final double MISSING_VALUE = Instance.missingValue();
    private static final String COMPOUND_URI = "compound_uri";

    public GetArff(VRI datasetUri) {
        if (datasetUri == null) {
            throw new NullPointerException();
        }
        this.datasetUri = datasetUri;
    }

    public Instances getInstances() {
        ArffDownloader downloader = new ArffDownloader(datasetUri);
        Instances instances = null;
        instances = downloader.getInstances();
        if (instances != null) {
            return instances;
        }
        OntModel model = ModelFactory.createOntologyModel();
        model.read(datasetUri.toString());


        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(COMPOUND_URI, (FastVector) null));

        /**
         * Add all numeric features
         */
        Resource numericFeatureResource = model.getResource(OTClasses.numericFeature().getUri());
        if (numericFeatureResource != null) {
            StmtIterator numericFeaturesIterator = model.listStatements(null, RDF.type, (RDFNode) numericFeatureResource);
            String featureUri = null;
            Statement numFeatureStmt = null;
            while (numericFeaturesIterator.hasNext()) {
                numFeatureStmt = numericFeaturesIterator.next();
                try {
                    featureUri = new VRI(numFeatureStmt.getSubject().getURI()).getStringNoQuery();
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                attributes.addElement(new Attribute(featureUri));
            }
            numericFeaturesIterator.close();
        }

        //TODO: Parse String variables
//        if (parseString) {
//        }
        
        //TODO: Parse Nominal variables
//        if (parseNominal) {
//        }

        Instances data = new Instances(datasetUri.getStringNoQuery(), attributes, 0);



        StmtIterator dataEntryIterator = model.listStatements(new SimpleSelector(model.getResource(datasetUri.getStringNoQuery()),
                OTObjectProperties.dataEntry().asObjectProperty(model), (RDFNode) null));
        Statement dataEntryStmt = null;
        Resource dataEntryNode = null;
        Resource valueNode = null;
        String feature = null;
        String compoundUri = null;
        double value = 0;


        while (dataEntryIterator.hasNext()) {
            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < data.numAttributes(); i++) {
                vals[i] = MISSING_VALUE;
            }
            dataEntryStmt = dataEntryIterator.next();
            dataEntryNode = (Resource) dataEntryStmt.getObject();
            compoundUri = dataEntryNode.getPropertyResourceValue(OTObjectProperties.compound().asObjectProperty(model)).getURI();
            vals[0] = data.attribute(COMPOUND_URI).addStringValue(compoundUri);
            StmtIterator valuesIterator = model.listStatements(new SimpleSelector(
                    dataEntryNode, OTObjectProperties.values().asObjectProperty(model), (RDFNode) null));
            while (valuesIterator.hasNext()) {
                valueNode = (Resource) valuesIterator.next().getObject();
                feature = valueNode.getPropertyResourceValue(OTObjectProperties.feature().asObjectProperty(model)).getURI();
                value = valueNode.getProperty(OTDatatypeProperties.value().asDatatypeProperty(model)).getObject().asLiteral().getDouble();
                vals[data.attribute(feature).index()] = value;
            }
            data.add(new Instance(1.0, vals));
            valuesIterator.close();
        }
        dataEntryIterator.close();
        model.close();
        System.out.println(data);

        return data;
    }
}

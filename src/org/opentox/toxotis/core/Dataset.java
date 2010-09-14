package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.util.spiders.TypedValue;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * A dataset provides access to chemical compounds and their features
 * (e.g. structural, physical-chemical, biological, toxicological properties)
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Dataset extends OTOnlineResource<Dataset> {

    private static final String compound_uri = "compound_uri";

    private enum WekaDataTypes {

        string,
        nominal,
        numeric,
        general;

        static WekaDataTypes getFromFeature(Feature feature) {
            if (feature.getOntologies().contains(OTClasses.NominalFeature())) {
                return nominal;
            } else if (feature.getOntologies().contains(OTClasses.StringFeature())) {
                return string;
            } else if (feature.getOntologies().contains(OTClasses.NumericFeature())) {
                return numeric;
            } else {
                return string;
            }
        }
    }
    private List<DataEntry> dataEntries;

    public Dataset(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!Dataset.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Dataset uri according to the OpenTox specifications.");
            }
        }
    }

    public Dataset() {
    }

    public Dataset(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    public List<DataEntry> getDataEntries() {
        return dataEntries;
    }

    public void setDataEntries(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String datasetUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(datasetUri, OTClasses.Dataset().inModel(model));
        /* Attach the metadata to the dataset node... */
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        /** */
        if (dataEntries != null && !dataEntries.isEmpty()) {
            ObjectProperty otdataentry = OTObjectProperties.dataEntry().asObjectProperty(model);
            for (DataEntry dataEntry : dataEntries) {
                indiv.addProperty(otdataentry, dataEntry.asIndividual(model));
            }
        }
        return indiv;
    }

    protected Dataset loadFromRemote(VRI uri) throws ToxOtisException {
        DatasetSpider spider = new DatasetSpider(uri);
        Dataset ds = spider.parse();
        setDataEntries(ds.getDataEntries());
        setUri(ds.getUri());
        setMeta(ds.getMeta());
        return this;
    }

    public Set<Feature> getContainedFeatures() {
        Set<Feature> features = new HashSet<Feature>();
        for (DataEntry dataEntry : getDataEntries()) {
            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                System.out.println("FEATURE IS: " + featureValue.getFeature().getUri().getStringNoQuery());
                features.add(featureValue.getFeature());
            }
        }
        return features;
    }

    public Instances getInstances() {
        // GET THE ATTRIBUTES FOR THE DATASET:
        FastVector attributes = new FastVector();
        Set<Feature> features = getContainedFeatures();
        // THE EXISTENCE OF THE (STRING) ATTRIBUTE 'COMPOUND_URI' IS MANDATORY FOR ALL
        // DATASETS. THIS IS ALWAYS THE FIRST ATTRIBUTE IN THE LIST.
        attributes.addElement(new Attribute(compound_uri, (FastVector) null));
        // ADD NUMERIC AND STRING ATTRIBUTES INTO THE FASTVECTOR:
        for (Feature feature : features) {

            WekaDataTypes dataType = WekaDataTypes.getFromFeature(feature);
            if (dataType.equals(WekaDataTypes.numeric)) {
                attributes.addElement(new Attribute(feature.getUri().getStringNoQuery()));
            } else if (dataType.equals(WekaDataTypes.string) || dataType.equals(WekaDataTypes.general)) {
                attributes.addElement(new Attribute(feature.getUri().getStringNoQuery(), (FastVector) null));
            } else if (dataType.equals(WekaDataTypes.nominal)) {
                // COPE WITH NOMINAL VALUES:
                FastVector nominalFVec = new FastVector(feature.getAdmissibleValue().size());
                for (TypedValue value : feature.getAdmissibleValue()) {
                    nominalFVec.addElement(value.getValue());
                }
                attributes.addElement(new Attribute(feature.getUri().getStringNoQuery(), nominalFVec));
            }
        }

        Instances data = new Instances(this.getUri().getStringNoQuery(), attributes, 0);

        //POPULATE WITH VALUES:
        for (DataEntry dataEntry : this.getDataEntries()) {
            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < data.numAttributes(); i++) {
                vals[i] = Instance.missingValue();
            }

            Conformer conformer = dataEntry.getConformer();

            vals[data.attribute(compound_uri).index()] =
                    data.attribute(compound_uri).addStringValue(conformer.getUri().getStringNoQuery());

            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                Feature feature = featureValue.getFeature();
                String featureName = feature.getUri().getStringNoQuery();
                TypedValue value = featureValue.getValue();

                if (WekaDataTypes.getFromFeature(feature).equals(WekaDataTypes.numeric)) {
                    try {
                        vals[data.attribute(featureName).index()] =
                                Double.parseDouble(value.getValue().toString());
                    } catch (NumberFormatException ex) {
                        // Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE,"Type is: "+ value.getType().getURI(), ex);
                    }
                } else if (WekaDataTypes.getFromFeature(feature).equals(WekaDataTypes.string)) {
                    vals[data.attribute(featureName).index()] =
                            data.attribute(featureName).addStringValue((String) value.getValue().toString());
                } else if (XSDDatatype.XSDdate.getURI().equals(featureName)) {
                    try {
                        vals[data.attribute(featureName).index()] = data.attribute(featureName).parseDate((String) value.getValue());
                    } catch (ParseException ex) {
                        Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (WekaDataTypes.getFromFeature(feature).equals(WekaDataTypes.nominal)) {
                    //TODO: Nominals may not work, testing is needed.
                    vals[data.attribute(featureName).index()] =
                            data.attribute(featureName).indexOfValue((String) value.getValue());
                }
            }
            Instance valuesInstance = new Instance(1.0, vals);
            // Add the Instance only if its compatible with the dataset!
            if (data.checkInstance(valuesInstance)) {
                data.add(valuesInstance);
            } else {
                Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, "Warning! The instance " + valuesInstance + " is not compatible with the dataset!");
            }
        }
        return data;
    }

    public static void main(String... args) throws URISyntaxException, ToxOtisException {

        Dataset ds = new DatasetSpider(new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/55")).parse();

        System.out.println(ds.getInstances().toString());

    }
}

package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.InactiveTokenException;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.util.spiders.TaskSpider;
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
public class Dataset extends OTPublishable<Dataset> {

    public static final String compound_uri = "compound_uri";
    private long timeInstancesConversion = -1;
    private long timeDownload = -1;
    private long timeParse = -1;
    private List<DataEntry> dataEntries = new ArrayList<DataEntry>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Dataset.class);

    /**
     * Constructor for a Dataset object providing its URI.
     * @param uri
     *      The URI of the created Dataset
     * @throws ToxOtisException
     *      In case the provided URI is not a valid dataset URI according to the
     *      OpenTox specifications.
     */
    public Dataset(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!Dataset.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Dataset uri according to the OpenTox specifications.");
            }
        }
    }

    /**
     * Dummy constructor for a dataset. Creates an empty dataset.
     */
    public Dataset() {
    }

    /**
     * Create a new Dataset object providing a list of data entries.
     * @param dataEntries
     */
    public Dataset(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    public Set<FeatureValue> getFeatureValues() {
        Set<FeatureValue> featureValues = new HashSet<FeatureValue>();
        for (DataEntry de : getDataEntries()) {
            featureValues.addAll(de.getFeatureValues());
        }
        return featureValues;
    }

    /**
     * Due to the large size of some dataset objects, it is advisable to use this method
     * for serializing dataset objects to RDF/XML rather than the method <code>OntModel#write(OutputStream)</code>.
     * It has been shown that this method performs much faster (about 7,5 times faster on a
     * dataset of 21 features and 1000 compounds).
     * @param writer
     *      XML Stream Writer used for the serialization of the dataset object.
     * @throws XMLStreamException
     *      In case the serialization is not possible due to syntax errors.
     */
    public void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
        initRdfWriter(writer);

        writeClass(writer, OTClasses.Dataset());
        writeClass(writer, OTClasses.DataEntry());
        writeClass(writer, OTClasses.Compound());
        writeClass(writer, OTClasses.Feature());
        writeClass(writer, OTClasses.FeatureValue());
        writeClass(writer, OTClasses.NominalFeature());
        writeClass(writer, OTClasses.NumericFeature());
        writeClass(writer, OTClasses.StringFeature());

        /*
         * Append Object Properties
         */
        writeObjectProperty(writer, OTObjectProperties.compound());
        writeObjectProperty(writer, OTObjectProperties.dataEntry());
        writeObjectProperty(writer, OTObjectProperties.values());
        writeObjectProperty(writer, OTObjectProperties.feature());

        writeMetaDataProperties(writer);

        /**
         * Append Datatype Properties
         */
        writeDatatypeProperty(writer, OTDatatypeProperties.acceptValue());
        writeDatatypeProperty(writer, OTDatatypeProperties.value());
        writeDatatypeProperty(writer, OTDatatypeProperties.units());

        writer.writeStartElement("ot:Dataset");// #NODE_BASE: Start Base Dataset Node
        if (getUri() != null) {
            writer.writeAttribute("rdf:about", getUri().clearToken().toString()); // REFERS TO #NODE_BASE
        }
        /* Meta-information about the dataset */
        if (getMeta() != null) {
            getMeta().writeToStAX(writer);
        }

        for (DataEntry dataEntry : getDataEntries()) {
            writer.writeStartElement("ot:dataEntry");// #NODE_HAS_DATAENTRY_PROP
            writer.writeStartElement("ot:DataEntry");// #NODE_DE
                /* Feature values in Data Entry*/
            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                writer.writeStartElement("ot:values");// #NODE_VALUES_PROP
                writer.writeStartElement("ot:FeatureValue");// #NODE_VALUES_HAS_FV_PROP
                writer.writeEmptyElement("ot:feature");// #ENODE_VALUES_HAS_FEAT_PROP
                writer.writeAttribute("rdf:resource", featureValue.getFeature().getUri().clearToken().toString()); // REFERS TO #ENODE_VALUES_HAS_FEAT_PROP
                writer.writeStartElement("ot:value");// #NODE_VALUE_PROP
                writer.writeAttribute("rdf:datatype", featureValue.getValue().getType().getURI()); // REFERS TO #NODE_VALUE_PROP
                writer.writeCharacters(featureValue.getValue().getValue().toString());// REFERS TO #NODE_VALUE_PROP
                writer.writeEndElement();// #__NODE_VALUE_PROP
                writer.writeEndElement();// #__NODE_VALUES_HAS_FV_PROP
                writer.writeEndElement();// #__NODE_VALUES_PROP
            }
            writer.writeStartElement("ot:compound");// #NODE_HAS_COMPOUND_PROP
                        /* Compound in Data Entry... */
            writer.writeStartElement("ot:Compound");// #NODE_COMPOUND
            writer.writeAttribute("rdf:about", dataEntry.getConformer().getUri().clearToken().toString()); // REFERS TO #NODE_COMPOUND
            writer.writeEndElement();// #__NODE_COMPOUND
            writer.writeEndElement();// #__NODE_HAS_COMPOUND_PROP
            writer.writeEndElement();// // #__NODE_DE
            writer.writeEndElement();// // #__NODE_HAS_DATAENTRY_PROP
        }
        writer.writeEndElement();// #NODE_BASE: End Base Dataset Node

        if (getMeta() != null) {
            if (getMeta().getSeeAlso() != null) {
                for (ResourceValue rv : getMeta().getSeeAlso()) {
                    if (rv.getOntologicalClass() != null) {
                        writer.writeEmptyElement(rv.getOntologicalClass().getNameSpace(), rv.getOntologicalClass().getName());
                        writer.writeAttribute("rdf:about", rv.getUri().toString());
                    }
                }
            }
            if (getMeta().getHasSources() != null) {
                for (ResourceValue rv : getMeta().getHasSources()) {
                    if (rv.getOntologicalClass() != null) {
                        writer.writeEmptyElement(rv.getOntologicalClass().getNameSpace(), rv.getOntologicalClass().getName());
                        writer.writeAttribute("rdf:about", rv.getUri().toString());
                    }
                }
            }
            if (getMeta().getSameAs() != null) {
                for (ResourceValue rv : getMeta().getSameAs()) {
                    if (rv.getOntologicalClass() != null) {
                        writer.writeEmptyElement(rv.getOntologicalClass().getNameSpace(), rv.getOntologicalClass().getName());
                        writer.writeAttribute("rdf:about", rv.getUri().toString());
                    }
                }
            }
        }

        Set<Feature> containedFeatures = getContainedFeatures();
        Set<OntologicalClass> featureOntologies = null;
        Set<String> sameAsFeatures = new HashSet<String>();
        for (Feature f : containedFeatures) {
            writer.writeStartElement("ot:Feature"); // #NODE_FEATURE_DECLARATION
            writer.writeAttribute("rdf:about", f.getUri().clearToken().toString()); // REFERS TO #NODE_FEATURE_DECLARATION: Feature URI
            featureOntologies = f.getOntologies();
            boolean explicitTypeDeclaration = false;
            if (featureOntologies != null && !featureOntologies.isEmpty()) {
                if (featureOntologies.contains(OTClasses.NominalFeature()) || featureOntologies.contains(OTClasses.Nominal())) {
                    writer.writeEmptyElement("rdf:type"); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute("rdf:resource", OTClasses.NominalFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                    for (LiteralValue admissibleVal : f.getAdmissibleValues()) {
                        writer.writeStartElement("ot:acceptValue"); // #NODE_ACCEPT_VALUE
                        // TODO: Include also the XSD datatype of the value...
                        writer.writeCharacters(admissibleVal.getValue().toString());// REFERS TO #NODE_ACCEPT_VALUE
                        writer.writeEndElement();// #__NODE_ACCEPT_VALUE
                    }
                }
                if (featureOntologies.contains(OTClasses.NumericFeature()) || featureOntologies.contains(OTClasses.Numeric())) {
                    writer.writeEmptyElement("rdf:type"); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute("rdf:resource", OTClasses.NumericFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                }
                if (featureOntologies.contains(OTClasses.StringFeature()) || featureOntologies.contains(OTClasses.String())) {
                    writer.writeEmptyElement("rdf:type"); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute("rdf:resource", OTClasses.StringFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                }
            }
            if (!explicitTypeDeclaration) { // Declare as Feature
                writer.writeEmptyElement("rdf:type"); // #NODE_FEATURE_TYPE_DECL
                writer.writeAttribute("rdf:resource", OTClasses.Feature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
            }
            /* Units of the feature*/
            if (f.getUnits() != null) {
                if (f.getUnits().isEmpty()) {
                    writer.writeEmptyElement("ot:units");
                } else {
                    writer.writeStartElement("ot:units");// #NODE_UNITS_VALUE
                    writer.writeCharacters(f.getUnits());// REFERS TO #NODE_UNITS_VALUE
                    writer.writeEndElement();// #__NODE_UNITS_VALUE
                }
            }
            /* Feature Meta Data*/
            if (f.getMeta() != null) {
                f.getMeta().writeToStAX(writer);
//                if (f.getMeta().getSameAs() != null && f.getMeta().getSameAs().getValue() != null) {
//                    String featureUri = f.getMeta().getSameAs().getValue().toString();
//                    if (!featureUri.contains("http")) {
//                        featureUri = OTClasses.NS + featureUri;
//                    }
//                    sameAsFeatures.add(featureUri);
//                }
            }
            writer.writeEndElement();// #__NODE_FEATURE_DECLARATION
        }

        for (String sameAsFeatureUri : sameAsFeatures) {
            writer.writeStartElement("ot:Feature"); // #NODE_ADDITIONAL_FEATURE
            writer.writeAttribute("rdf:about", sameAsFeatureUri); // REFERS TO #NODE_ADDITIONAL_FEATURE
            writer.writeEndElement();// #__NODE_ADDITIONAL_FEATURE
        }

        writer.writeEndElement();// #__NODE_rdf:RDF_CORE_ELEMENT
        writer.flush();
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        PostHttpClient client = new PostHttpClient(vri);
        client.setContentType(Media.APPLICATION_RDF_XML);
        client.setMediaType(Media.TEXT_URI_LIST);
        client.setPostable(asOntModel());
        client.post();
        int status;
        try {
            status = client.getResponseCode();
        } catch (final IOException ex) {
            throw new ToxOtisException(ErrorCause.CommunicationError,
                    "Could not read the stream from '" + vri.getStringNoQuery() + "'", ex);
        }
        Task dsUpload = new Task();
        String remoteResult = client.getResponseText();
        if (status == 202) {
            try {
                dsUpload.setUri(new VRI(remoteResult));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            dsUpload.loadFromRemote();
        } else if (status == 200) {
            dsUpload.setPercentageCompleted(100);
            dsUpload.setStatus(Task.Status.COMPLETED);
            try {
                dsUpload.setUri(new VRI(remoteResult));
                dsUpload.setResultUri(new VRI(remoteResult));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
        return dsUpload;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        return publishOnline(Services.ideaconsult(), token);
    }

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
        if (dataEntries != null && !dataEntries.isEmpty()) {
            ObjectProperty otdataentry = OTObjectProperties.dataEntry().asObjectProperty(model);
            for (DataEntry dataEntry : dataEntries) {
                indiv.addProperty(otdataentry, dataEntry.asIndividual(model));
            }
        }
        return indiv;
    }

    @Override
    protected Dataset loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        DatasetSpider spider = new DatasetSpider(uri, token);
        Dataset ds = spider.parse();
        setDataEntries(ds.getDataEntries());
        setUri(ds.getUri());
        setMeta(ds.getMeta());
        spider.close();
        timeParse = spider.getParseTime();
        timeDownload = spider.getReadRemoteTime();
        return this;
    }

    public Set<Feature> getContainedFeatures() {
        Set<Feature> features = new LinkedHashSet<Feature>();
        for (DataEntry dataEntry : getDataEntries()) {
            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                features.add(featureValue.getFeature());
            }
        }
        return features;
    }

    /**
     * <p align="justify">Creates and returns a <code>weka.core.Instances</code>
     * object from the data contained in this Dataset. The Instances object created has the following specific structure:
     * The first element in each Instance is always the Compound's URI. It is
     * identified by the keyword <code>compound_uri</code>. Following that comes a sequence
     * of all Features contained the Dataset's DataEntries, described as
     * either <code>String</code>,<code>Numeric</code> or <code> Nominal</code>.
     * If a compound doesn't possess a value for a specific Feature, or the value is
     * unreadable or unacceptable (e.g. a String value is present when a Numeric is
     * expected), a missing value is placed instead. If a Feature is tagged as both
     * Numeric|String and Nominal, the Nominal property wins. If it is tagged as
     * both Numeric and String, the String property wins.
     * </p>
     *
     * @return
     *      Weka Instances from the data contained in this Dataset.
     */
    public Instances getInstances() {
        long timeFlag = System.currentTimeMillis();
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
                FastVector nominalFVec = new FastVector(feature.getAdmissibleValues().size());
                for (LiteralValue value : feature.getAdmissibleValues()) {
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

            Compound conformer = dataEntry.getConformer();

            vals[data.attribute(compound_uri).index()] =
                    data.attribute(compound_uri).addStringValue(conformer.getUri().getStringNoQuery());

            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                Feature feature = featureValue.getFeature();
                String featureName = feature.getUri().getStringNoQuery();
                LiteralValue value = featureValue.getValue();

                if (value != null) {
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
                            logger.error("Parsing Exception for Date in Dataset", ex);
                        }
                    } else if (WekaDataTypes.getFromFeature(feature).equals(WekaDataTypes.nominal)) {
                        //TODO: Nominals may not work, testing is needed.
                        vals[data.attribute(featureName).index()] =
                                data.attribute(featureName).indexOfValue(value.getValue().toString());
                    }
                }
            }

            Instance valuesInstance = new Instance(1.0, vals);
            // Add the Instance only if its compatible with the dataset!
            if (data.checkInstance(valuesInstance)) {
                data.add(valuesInstance);
            } else {
                logger.warn("Warning! The instance "
                        + valuesInstance + " is not compatible with the dataset!");
            }
        }
        timeInstancesConversion = System.currentTimeMillis() - timeFlag;
        return data;
    }

    public int countCompounds() {
        throw new UnsupportedOperationException();
    }

    public int countFeatures() {
        throw new UnsupportedOperationException();
    }

    public long getTimeDownload() {
        return timeDownload;
    }

    public long getTimeInstancesConversion() {
        return timeInstancesConversion;
    }

    public long getTimeParse() {
        return timeParse;
    }

    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        PostHttpClient client = new PostHttpClient(descriptorCalculationAlgorithm);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        descriptorCalculationAlgorithm.clearToken();
        IPostClient pc = new PostHttpClient(descriptorCalculationAlgorithm);
        pc.authorize(token);
        pc.addPostParameter("dataset_uri", getUri().toString()); // dataset_uri={dataset_uri}
        pc.addPostParameter("ALL", "true");
        pc.setMediaType(Media.TEXT_URI_LIST);
        pc.post();
        String taskUri = pc.getResponseText();
        try {
            TaskSpider taskSpider = new TaskSpider(new VRI(taskUri));
            return taskSpider.parse();
        } catch (URISyntaxException ex) {
            throw new ToxOtisException("The remote service at " + descriptorCalculationAlgorithm
                    + " returned an invalid task URI : " + taskUri, ex);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dataset other = (Dataset) obj;
        if (this.dataEntries != other.dataEntries && (this.dataEntries == null || !this.dataEntries.equals(other.dataEntries))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.dataEntries != null ? this.dataEntries.hashCode() : 0);
        return hash;
    }
}

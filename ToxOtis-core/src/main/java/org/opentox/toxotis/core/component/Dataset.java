/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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

    public static final String COMPOUND_URI = "compound_uri";
    private long timeInstancesConversion = -1;
    private long timeDownload = -1;
    private long timeParse = -1;
    private List<DataEntry> dataEntries = new ArrayList<DataEntry>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Dataset.class);
    private static final int HASH_OFFSET = 7, HASH_MOD = 29, PERCENTAGE_WHEN_COMPLETE = 100;
    private static final String INACTIVE_TOKEN_MSG = "The Provided token is inactive";

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
        setMeta(null);
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
    @Override
    public void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
        initRdfWriter(writer);

        writeClass(writer, OTClasses.openToxResource());
        writeClass(writer, OTClasses.dataset());
        writeClass(writer, OTClasses.dataEntry());
        writeClass(writer, OTClasses.compound());
        writeClass(writer, OTClasses.feature());
        writeClass(writer, OTClasses.featureValue());
        writeClass(writer, OTClasses.nominalFeature());
        writeClass(writer, OTClasses.numericFeature());
        writeClass(writer, OTClasses.stringFeature());


        writeSuperClassRelationships(writer);

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
            writer.writeAttribute(RDF_ABOUT, getUri().toString()); // REFERS TO #NODE_BASE
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
                writer.writeAttribute(RDF_RESOURCE, featureValue.getFeature().getUri().toString()); // REFERS TO #ENODE_VALUES_HAS_FEAT_PROP
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
            writer.writeAttribute(RDF_ABOUT, dataEntry.getConformer().getUri().toString()); // REFERS TO #NODE_COMPOUND
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
                        writer.writeAttribute(RDF_ABOUT, rv.getUri().toString());
                    }
                }
            }
            if (getMeta().getHasSources() != null) {
                for (ResourceValue rv : getMeta().getHasSources()) {
                    if (rv.getOntologicalClass() != null) {
                        writer.writeEmptyElement(rv.getOntologicalClass().getNameSpace(), rv.getOntologicalClass().getName());
                        writer.writeAttribute(RDF_ABOUT, rv.getUri().toString());
                    }
                }
            }
            if (getMeta().getSameAs() != null) {
                for (ResourceValue rv : getMeta().getSameAs()) {
                    if (rv.getOntologicalClass() != null) {
                        writer.writeEmptyElement(rv.getOntologicalClass().getNameSpace(), rv.getOntologicalClass().getName());
                        writer.writeAttribute(RDF_ABOUT, rv.getUri().toString());
                    }
                }
            }
        }

        Set<Feature> containedFeatures = getContainedFeatures();
        Set<OntologicalClass> featureOntologies = null;
        Set<String> sameAsFeatures = new HashSet<String>();
        for (Feature f : containedFeatures) {
            writer.writeStartElement("ot:Feature"); // #NODE_FEATURE_DECLARATION
            writer.writeAttribute(RDF_ABOUT, f.getUri().toString()); // REFERS TO #NODE_FEATURE_DECLARATION: Feature URI
            featureOntologies = f.getOntologicalClasses();
            boolean explicitTypeDeclaration = false;
            if (featureOntologies != null && !featureOntologies.isEmpty()) {
                if (featureOntologies.contains(OTClasses.nominalFeature()) || featureOntologies.contains(OTClasses.nominal())) {
                    writer.writeEmptyElement(RDF_TYPE); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute(RDF_RESOURCE, OTClasses.nominalFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                    for (LiteralValue admissibleVal : f.getAdmissibleValues()) {
                        writer.writeStartElement("ot:acceptValue"); // #NODE_ACCEPT_VALUE
                        // TODO: Include also the XSD datatype of the value...
                        writer.writeCharacters(admissibleVal.getValue().toString());// REFERS TO #NODE_ACCEPT_VALUE
                        writer.writeEndElement();// #__NODE_ACCEPT_VALUE
                    }
                }
                if (featureOntologies.contains(OTClasses.numericFeature())
                        || featureOntologies.contains(OTClasses.numeric())) {
                    writer.writeEmptyElement(RDF_TYPE); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute(RDF_RESOURCE, OTClasses.numericFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                }
                if (featureOntologies.contains(OTClasses.stringFeature())
                        || featureOntologies.contains(OTClasses.string())) {
                    writer.writeEmptyElement(RDF_TYPE); // #NODE_FEATURE_TYPE_DECL
                    explicitTypeDeclaration = true;
                    writer.writeAttribute(RDF_RESOURCE, OTClasses.stringFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                }
            }
            if (!explicitTypeDeclaration) { // Declare as Feature
                writer.writeEmptyElement(RDF_TYPE); // #NODE_FEATURE_TYPE_DECL
                writer.writeAttribute(RDF_RESOURCE, OTClasses.feature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
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
            }
            writer.writeEndElement();// #__NODE_FEATURE_DECLARATION
        }

        for (String sameAsFeatureUri : sameAsFeatures) {
            writer.writeStartElement("ot:Feature"); // #NODE_ADDITIONAL_FEATURE
            writer.writeAttribute(RDF_ABOUT, sameAsFeatureUri); // REFERS TO #NODE_ADDITIONAL_FEATURE
            writer.writeEndElement();// #__NODE_ADDITIONAL_FEATURE
        }

        writer.writeEndElement();// #__NODE_rdf:RDF_CORE_ELEMENT
        writer.flush();
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest(INACTIVE_TOKEN_MSG);
        }
        PostHttpClient client = new PostHttpClient(vri);
        client.setContentType(Media.APPLICATION_RDF_XML);
        client.setMediaType(Media.TEXT_URI_LIST);
        client.setPostable(asOntModel());
        client.post();
        int status;
        status = client.getResponseCode();

        Task dsUpload = new Task();
        String remoteResult = client.getResponseText();
        logger.debug("Publishing >> Response : " + remoteResult);
        logger.debug("Publishing >> STATUS   : " + status);
        if (status == HttpStatusCodes.Accepted.getStatus()) {
            try {
                dsUpload.setUri(new VRI(remoteResult));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
            dsUpload.loadFromRemote();
        } else if (status == HttpStatusCodes.Success.getStatus()) {
            dsUpload.setPercentageCompleted(PERCENTAGE_WHEN_COMPLETE);
            dsUpload.setStatus(Task.Status.COMPLETED);
            try {
                dsUpload.setUri(new VRI(remoteResult));
                dsUpload.setResultUri(new VRI(remoteResult));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return dsUpload;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest(INACTIVE_TOKEN_MSG);
        }
        return publishOnline(Services.ideaconsult().augment("dataset"), token);
    }

    private enum WekaDataTypes {

        string,
        nominal,
        numeric,
        general;

        static WekaDataTypes getFromFeature(Feature feature) {
            if (feature.getOntologicalClasses().contains(OTClasses.nominalFeature())) {
                return nominal;
            } else if (feature.getOntologicalClasses().contains(OTClasses.stringFeature())) {
                return string;
            } else if (feature.getOntologicalClasses().contains(OTClasses.numericFeature())) {
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
        Individual indiv = model.createIndividual(datasetUri, OTClasses.dataset().inModel(model));
        /* Attach the metadata to the dataset node... */
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
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
    protected Dataset loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest(INACTIVE_TOKEN_MSG);
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

    /**
     * All features in the dataset.
     * @return 
     *      A set of {@link Feature}
     */
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
     * The set of all feature URIs in the dataset.
     * @return 
     *      A set of {@link VRI VRIs}
     */
    public Set<VRI> getContainedFeatureUris() {
        Set<VRI> features = new LinkedHashSet<VRI>();
        for (DataEntry dataEntry : getDataEntries()) {
            for (FeatureValue featureValue : dataEntry.getFeatureValues()) {
                features.add(featureValue.getFeature().getUri());
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
        attributes.addElement(new Attribute(COMPOUND_URI, (FastVector) null));
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

            vals[data.attribute(COMPOUND_URI).index()] =
                    data.attribute(COMPOUND_URI).addStringValue(conformer.getUri().getStringNoQuery());

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
                            logger.warn("NFE while trying to convert to double the value " + value.getValue(), ex);
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

    public Task calculateDescriptors(VRI descriptorCalculationAlgorithm, AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest(INACTIVE_TOKEN_MSG);
        }
        PostHttpClient client = new PostHttpClient(descriptorCalculationAlgorithm);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        IPostClient pc = new PostHttpClient(descriptorCalculationAlgorithm);
        pc.authorize(token);
        pc.addPostParameter("dataset_uri", getUri().toString()); // notice that dataset_uri={dataset_uri}
        pc.addPostParameter("ALL", "true");
        pc.setMediaType(Media.TEXT_URI_LIST);
        pc.post();
        String taskUri = pc.getResponseText();
        try {
            TaskSpider taskSpider = new TaskSpider(new VRI(taskUri));
            return taskSpider.parse();
        } catch (URISyntaxException ex) {
            throw new RemoteServiceException("The remote service at " + descriptorCalculationAlgorithm
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
        int hash = HASH_OFFSET;
        hash = HASH_MOD * hash + (this.dataEntries != null ? this.dataEntries.hashCode() : 0);
        return hash;
    }
}

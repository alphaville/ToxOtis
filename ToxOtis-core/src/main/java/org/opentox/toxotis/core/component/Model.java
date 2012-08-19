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
import com.hp.hpl.jena.rdf.model.Property;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IBibTexReferencable;
import org.opentox.toxotis.core.IHTMLSupport;
import org.opentox.toxotis.core.OTOnlineResource;
import org.opentox.toxotis.core.IOntologyServiceSupport;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLForm;
import org.opentox.toxotis.core.html.HTMLInput;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
import org.opentox.toxotis.core.html.impl.HTMLAppendableTableImpl;
import org.opentox.toxotis.core.html.impl.HTMLInputImpl;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.core.html.impl.HTMLTextImpl;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.ModelSpider;

/**
 * QSAR, DoA or other OpenTox models. Models in OpenTox are services on which a
 * dataset can be POSTed for processing. An OpenTox model may correspond to a predictive
 * QSAR model, a domain of applicability estimator,  a filtering model that outputs
 * a (filtered) dataset or any other process on datasets and is not identical to the
 * notion of a predictive model as it appears in QSAR.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Model extends OTOnlineResource<Model> implements IOntologyServiceSupport<Model>, IHTMLSupport, IBibTexReferencable {

    private VRI dataset;
    private Set<VRI> references = new HashSet<VRI>();
    private Algorithm algorithm;
    private List<Feature> predictedFeatures;
    private List<Feature> dependentFeatures;
    /*
     * Note: Converted from Set<Feature> to List<Feature> to make sure that the order
     * is preserved given that the use of LinkedHashSet<?> may guarrantee that fact in
     * Java but when it comes to SQL (and hibernate) there's no way to certify that a
     * Set, in general, will respect the initial order in which features are arranged.
     */
    private List<Feature> independentFeatures;
    private Set<Parameter> parameters;
    private List<MultiParameter> multiParameters;
    private String localCode;
    private Serializable actualModel;
    private byte[] modelBytes;
    private User createdBy;
    private static final long serialVersionUID = 184328712643L;
    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Model.class);
    private static final int baosSize = 4000;
    private static final int hashOffset = 3, hashMod = 71;
    private static final int htmlTextBoxSize = 60,
            interfaceCellPadding = 5,
            interfaceCellSpacing = 2,
            interfaceTableBorder = 0,
            validationCellPadding = 5,
            validationCellSpacing = 2,
            validationTableBorder = 0, 
            featuresCellPadding = 5, 
            featuresCellSpacing = 2, 
            featuresTableBorder = 1;
    private static final int[] interfaceTableDimensions = new int[]{250, 550},
            validationTableDimensions = new int[]{250, 550},
            featuresTableDimensions = new int[]{150, 650},
            parametersTableDimensions = new int[]{400, 150, 240},
            metaTableDimensions = new int[]{150, 650};

    /**
     * Create a new empty Model with a given URI.
     * @param uri 
     *      URI of the model.
     */
    public Model(VRI uri) {
        super(uri);
    }

    /**
     * Creates an empty model.
     */
    public Model() {
        super();
    }

    /**
     * The actual model, serializable object, that encapsulates all necessary
     * information for the calculated model that was trained by some algorithm.
     * Can be any implementation of {@link Serializable }, meaning practically
     * almost anything. Users can implement their own models that can encapsulate
     * potentially a Weka model file and a PMML representation of the model.
     * @return
     *      Model object with which predictions can be done.
     */
    public Serializable getActualModel() {
        return actualModel;
    }

    /**
     * Set the actual model. This object is converted into an array of bytes using a
     * <code>ByteArrayOutputStream</code> and an <code>ObjectOutputStream</code>.
     * 
     * @param actualModel
     *      The object which can be used to perform predictions.
     * @return
     *      The current updated instance of the model containing the actual model
     * @throws NotSerializableException
     *      In case the provided actual model cannot be serialized. This is the case
     *      when the provided model is an instance of a class which implements
     *      <code>java.io.Serializable</code> but has some non-serializable fields.
     *      In that case it is good practise to either exclude these fields from the
     *      serialization tagging them as <code>transient</code> or replace them with
     *      other serializable fields if possible
     *
     */
    public Model setActualModel(Serializable actualModel) throws NotSerializableException {
        try {
            this.actualModel = actualModel;
            this.modelBytes = getBytes(actualModel);
            return this;
        } catch (IOException ex) {
            logger.error("Could not serialize actual model", ex);
            throw new NotSerializableException(actualModel.getClass().getName());
        }

    }

    private byte[] getBytes(Object obj) throws java.io.IOException {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        byte[] data = bos.toByteArray();
        return data;
    }

    /**
     * If the actual model is stored as a file on the file system, this method returns
     * the pathname for it.
     * @return
     *
     */
    public String getLocalCode() {
        return localCode;
    }

    /**
     * Sets the local code of the model. With this code the model is locally 
     * identified (e.g. on the file system).
     * 
     * @param localCode 
     *      A custom (unique) identifier.
     */
    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    /**
     * The algorithm with which the model was built/trained.
     * @return 
     *      An instance of {@link Algorithm}
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Specify the algorithm that was used to train the model.
     * @param algorithm 
     *      An instance of {@link Algorithm}.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * The user that created the model.
     * @return 
     *      An instance of {@link User}
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Specify who created this model.
     * @param createdBy 
     *      An instance of {@link User}
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the URI of the dataset that was used to train the model.
     * The actual data should be fetched from the remote location if
     * necessary.
     * 
     * @return 
     *      URI of training dataset.
     */
    public VRI getDataset() {
        return dataset;
    }

    /**
     * Specify the URI of the training dataset.
     * @param dataset
     *      URI of the training set.
     * @return 
     *      The current modifiable instance of {@link Model} 
     *      with updated training dataset.
     */
    public Model setDataset(VRI dataset) {
        this.dataset = dataset;
        return this;
    }

    /**
     * A list of the dependent features of the model, i.e. the
     * target property (the response, e.g. experimentally measured property).
     * @return 
     *      List of {@link Feature features}
     */
    public List<Feature> getDependentFeatures() {
        return dependentFeatures;
    }

    /**
     * Set the list of dependent features of the current model
     * @param dependentFeature
     *      List of dependent features.
     * @return 
     *      The current Model instance with an updated list of features.
     */
    public Model setDependentFeatures(List<Feature> dependentFeature) {
        this.dependentFeatures = dependentFeature;
        return this;
    }

    /**
     * Adds dependent features to the current list of features.
     * @param features
     *      Array of features.
     * @return 
     *      The current model with updated list of dependent features.
     */
    public Model addDependentFeatures(Feature... features) {
        if (getDependentFeatures() == null) {
            setDependentFeatures(new ArrayList<Feature>(features.length));
        }
        getDependentFeatures().addAll(Arrays.asList(features));
        return this;
    }

    /**
     * A list of the independent features of the model, i.e. the
     * descriptors.
     * 
     * @return 
     *      List of {@link Feature features}
     */
    public List<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    /**
     * Set the list of independent features of the current model.
     * @param independentFeatures 
     *      List of independent features.
     * @return 
     *      The current Model instance with an updated list of independent features.
     */
    public Model setIndependentFeatures(List<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
        return this;
    }

    /**
     * Adds independent features to the current list of features.
     * @param features
     *      Array of features.
     * @return 
     *      The current model with updated list of independent features.
     */
    public Model addIndependentFeatures(Feature... features) {
        if (getIndependentFeatures() == null) {
            setIndependentFeatures(new ArrayList<Feature>(features.length));
        }
        getIndependentFeatures().addAll(Arrays.asList(features));
        return this;
    }

    /**
     * The parameters of the model.
     * @return 
     *      A set of parameters for the current model.
     * @see Parameter
     */
    public Set<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Specify the set of parameters for the current model.
     * @param parameters 
     */
    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<MultiParameter> getMultiParameters() {
        return multiParameters;
    }

    public Model setMultiParameters(List<MultiParameter> multiParameters) {
        this.multiParameters = multiParameters;
        return this;
    }

    /**
     * A list of the predicted features of the model, i.e. the properties
     * that the model predicts.
     * 
     * @return 
     *      List of predicted {@link Feature features}.
     */
    public List<Feature> getPredictedFeatures() {
        return predictedFeatures;
    }

    /**
     * Set the list of predicted features of the current model.
     * @param predictedFeature  
     *      List of predicted features.
     * @return 
     *      The current Model instance with an updated list of predicted features.
     */
    public Model setPredictedFeatures(List<Feature> predictedFeature) {
        this.predictedFeatures = predictedFeature;
        return this;
    }

    /**
     * Adds predicted features to the current list of features.
     * @param features
     *      Array of predicted features.
     * @return 
     *      The current model with updated list of predicted features.
     */
    public Model addPredictedFeatures(Feature... features) {
        if (getPredictedFeatures() == null) {
            setPredictedFeatures(new ArrayList<Feature>(features.length));
        }
        getPredictedFeatures().addAll(Arrays.asList(features));
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String modelUri = getUri() != null ? getUri().getStringNoQuery() : null;

        Individual indiv = model.createIndividual(modelUri, OTClasses.Model().inModel(model));

        indiv.addComment(model.createTypedLiteral("Representation automatically generated by ToxOtis (http://github.com/alphaville/ToxOtis).",
                XSDDatatype.XSDstring));

        final MetaInfo metaInfo = getMeta();
        if (metaInfo != null) {
            metaInfo.attachTo(indiv, model);
        }
        if (parameters != null) {
            ObjectProperty parameterProperty = OTObjectProperties.parameters().asObjectProperty(model);
            for (Parameter param : parameters) {
                indiv.addProperty(parameterProperty, param.asIndividual(model));
            }
        }
        if (multiParameters != null) {
            ObjectProperty multiParameterProperty = OTObjectProperties.multiParameter().asObjectProperty(model);
            for (MultiParameter mp : multiParameters) {
                indiv.addProperty(multiParameterProperty, mp.asIndividual(model));
            }
        }

        if (dependentFeatures != null) {
            Property dependentVariablsProperty = OTObjectProperties.dependentVariables().asObjectProperty(model);
            for (Feature f : dependentFeatures) {
                indiv.addProperty(dependentVariablsProperty, f.asIndividual(model));
            }
        }

        if (predictedFeatures != null) {
            Property predictedVariablesProperty = OTObjectProperties.predictedVariables().asObjectProperty(model);
            for (Feature f : predictedFeatures) {
                indiv.addProperty(predictedVariablesProperty, new Feature(f.getUri()).asIndividual(model));
            }
        }

        if (independentFeatures != null) {
            Property indepVarProp = OTObjectProperties.independentVariables().asObjectProperty(model);
            for (Feature indFeat : independentFeatures) {
                indiv.addProperty(indepVarProp, indFeat.asIndividual(model));
            }
        }

        if (dataset != null) {
            indiv.addProperty(OTObjectProperties.trainingDataset().asObjectProperty(model),
                    model.createResource(getDataset().getStringNoQuery(), OTClasses.Dataset().inModel(model)));

        }
        if (algorithm != null) {
            indiv.addProperty(OTObjectProperties.algorithm().asObjectProperty(model), algorithm.asIndividual(model));
        }

        return indiv;
    }

    @Override
    protected Model loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        ModelSpider spider = new ModelSpider(uri, token);
        Model m = spider.parse();
        setAlgorithm(m.getAlgorithm());
        setDataset(m.getDataset());
        setDependentFeatures(m.getDependentFeatures());
        setIndependentFeatures(m.getIndependentFeatures());
        setMeta(m.getMeta());
        setParameters(m.getParameters());
        setPredictedFeatures(m.getPredictedFeatures());
        setCreatedBy(m.getCreatedBy());
        return this;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Model publishToOntService(VRI ontologyService, AuthenticationToken token)
            throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Model other = (Model) obj;
        if (getUri() != other.getUri() && (getUri() == null || !getUri().equals(other.getUri()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = hashOffset;
        hash = hashMod * hash + (getUri() != null ? getUri().hashCode() : 0);
        return hash;
    }

    public void setBlob(Blob modelBlob) {
        this.modelBytes = toByteArray(modelBlob);
        this.actualModel = (Serializable) toObject(modelBytes);
    }

    public Object toObject(byte[] bytes) {
        Object object = null;
        try {
            object = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        } catch (java.io.IOException ioe) {
        } catch (java.lang.ClassNotFoundException cnfe) {
        }
        return object;
    }

    public Blob getBlob() throws SerialException, SQLException {
        if (this.modelBytes == null) {
            return null;
        }
        try {
            return new SerialBlob(modelBytes);
        } catch (SerialException ex) {
            logger.warn("", ex);
            throw ex;
        } catch (SQLException ex) {
            logger.warn("", ex);
            throw ex;
        }

    }

    private byte[] toByteArray(Blob fromModelBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromModelBlob, baos);
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] toByteArrayImpl(Blob fromModelBlob,
            ByteArrayOutputStream baos) throws SQLException, IOException {
        byte buf[] = new byte[baosSize];
        int dataSize;
        InputStream is = fromModelBlob.getBinaryStream();
        try {
            while ((dataSize = is.read(buf)) != -1) {
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos.toByteArray();
    }

    @Override
    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("jaqpot_model");
        builder.addComment("Model Representation automatically generated by JAQPOT");
        builder.addSubHeading("Model Report");
        builder.addSubSubHeading(uri.toString());
        builder.getDiv().setAlignment(Alignment.justify).breakLine().horizontalSeparator();


        /**
         * Interface for making predictions
         */
        builder.addSubSubHeading("Use the model");
        builder.addParagraph("Specify the dataset you want to submit for prediction", Alignment.justify);
        HTMLForm form = builder.addForm("", "POST");
        form.addComponent(new HTMLInputImpl().setType(HTMLInput.HTMLInputType.HIDDEN).setName("comingFrom").setValue("webInterface"));
        HTMLTable interfacetable = new HTMLAppendableTableImpl(2);
        interfacetable.setAtCursor(new HTMLTextImpl("Dataset URI").formatBold(true)).
                setAtCursor(new HTMLInputImpl().setName("dataset_uri").setType(HTMLInput.HTMLInputType.TEXT).
                setValue(getDataset() != null ? getDataset().toString() : "").setSize(htmlTextBoxSize)).
                setAtCursor(new HTMLTextImpl("Dataset Service").formatBold(true)).
                setAtCursor(new HTMLInputImpl().setName("dataset_service").
                setType(HTMLInput.HTMLInputType.TEXT).
                setValue(dataset != null ? dataset.getServiceBaseUri().augment("dataset").toString() : "").
                setSize(htmlTextBoxSize)).
                setAtCursor(new HTMLInputImpl().setType(HTMLInput.HTMLInputType.SUBMIT).
                setValue("Predict")).
                setTextAtCursor("");

        interfacetable.setCellPadding(interfaceCellPadding).
                setCellSpacing(interfaceCellSpacing).
                setTableBorder(interfaceTableBorder).
                setColWidth(1, interfaceTableDimensions[0]).
                setColWidth(2, interfaceTableDimensions[1]);
        form.addComponent(interfacetable);

        builder.getDiv().breakLine().breakLine();



        /**
         * Interface for model validation
         */
        builder.addSubSubHeading("Validate the model (using a test set)");
        builder.addParagraph("Test-set validation of this model", Alignment.justify);
        HTMLForm form2 = builder.addForm("/iface/validation", "POST");
        form2.addComponent(new HTMLInputImpl().setType(HTMLInput.HTMLInputType.HIDDEN).setName("comingFrom").setValue("webInterface"));
        form2.addComponent(new HTMLInputImpl().setType(HTMLInput.HTMLInputType.HIDDEN).setName("model_uri").setValue(uri.toString()));
        int textBoxSize2 = 60;
        HTMLTable validationtable = new HTMLAppendableTableImpl(2);
        validationtable.setAtCursor(new HTMLTextImpl("Test Dataset URI").formatBold(true)).
                setAtCursor(new HTMLInputImpl().setName("test_dataset_uri").setType(HTMLInput.HTMLInputType.TEXT).
                setValue(getDataset() != null ? getDataset().toString() : "").setSize(textBoxSize2)).
                setAtCursor(new HTMLTextImpl("Target Dataset URI").formatBold(true)).
                setAtCursor(new HTMLInputImpl().setName("test_target_dataset_uri").
                setType(HTMLInput.HTMLInputType.TEXT).setValue(dataset != null ? dataset.toString() : "").setSize(textBoxSize2)).
                setAtCursor(new HTMLTextImpl("Validation Service").formatBold(true)).
                setAtCursor(new HTMLInputImpl().setName("validation_service").
                setType(HTMLInput.HTMLInputType.TEXT).setValue("http://toxcreate2.in-silico.ch/validation/test_set_validation").setSize(textBoxSize2)).
                setAtCursor(new HTMLInputImpl().setType(HTMLInput.HTMLInputType.SUBMIT).setValue("Validate")).setTextAtCursor("");
        validationtable.setCellPadding(validationCellPadding).
                setCellSpacing(validationCellSpacing).
                setTableBorder(validationTableBorder).
                setColWidth(1, validationTableDimensions[0]).
                setColWidth(2, validationTableDimensions[1]);
        form2.addComponent(validationtable);

        builder.getDiv().breakLine().breakLine();

        builder.addSubSubHeading("Information about the Model");
        builder.getDiv().breakLine();

        HTMLTable featuresTable = builder.addTable(2);

        featuresTable.setAtCursor(new HTMLTextImpl("Dependent Feature(s)").formatBold(true)).
                setAtCursor(getDependentFeatures() != null ? HTMLUtils.createComponentList(getDependentFeatures(), null, null) : new HTMLTextImpl("-")).
                setAtCursor(new HTMLTextImpl("Predicted Feature(s)").formatBold(true)).
                setAtCursor(getPredictedFeatures() != null ? HTMLUtils.createComponentList(getPredictedFeatures(), null, null) : new HTMLTextImpl("-")).
                setAtCursor(getIndependentFeatures() != null ? new HTMLTextImpl("Independent Features").formatBold(true) : new HTMLTextImpl("-")).
                setAtCursor(HTMLUtils.createComponentList(getIndependentFeatures(), null, null)).
                setAtCursor(new HTMLTextImpl("Training Algorithm").formatBold(true)).
                setTextAtCursor(HTMLUtils.linkUrlsInText(getAlgorithm() != null ? getAlgorithm().getUri().toString() : "-")).
                setAtCursor(new HTMLTextImpl("Training Dataset").formatBold(true)).
                setTextAtCursor(HTMLUtils.linkUrlsInText(getDataset() != null ? getDataset().toString() : "-"));

        if (getBibTeXReferences() != null && !getBibTeXReferences().isEmpty()) {
            featuresTable.setAtCursor(new HTMLTextImpl("References").formatBold(true));
            StringBuilder refs = new StringBuilder();
            if (getBibTeXReferences().size() == 1) {
                VRI nextVri = getBibTeXReferences().iterator().next();
                refs.append("<a href=\"").append(nextVri).append("\">").append(nextVri).append("</a>\n");
            } else {
                refs.append("<ol>\n");
                for (VRI vriRef : getBibTeXReferences()) {
                    refs.append("<li><a href=\"").append(vriRef).append("\">").append(vriRef).append("</a></li>\n");
                }
                refs.append("</ol>\n");
            }
            featuresTable.setTextAtCursor(refs.toString());
        }


        featuresTable.setCellPadding(featuresCellPadding).
                setCellSpacing(featuresCellSpacing).
                setTableBorder(featuresTableBorder).
                setColWidth(1, featuresTableDimensions[0]).
                setColWidth(2, featuresTableDimensions[1]);

        if (getParameters() != null && !getParameters().isEmpty()) {
            builder.addSubSubSubHeading("Model Parameters");
            HTMLTable parametersTable = builder.addTable(3);
            parametersTable.setAtCursor(new HTMLTextImpl("Parameter URI").formatBold(true)).
                    setAtCursor(new HTMLTextImpl("Parameter Name").formatBold(true)).setAtCursor(new HTMLTextImpl("Value").formatBold(true));
            for (Parameter prm : getParameters()) {
                parametersTable.setAtCursor(new HTMLTagImpl("a", "/parameter/" + prm.getUri().getId()).addTagAttribute("href", prm.getUri().toString()));
                parametersTable.setAtCursor(new HTMLTagImpl("a", prm.getName().getValueAsString()).addTagAttribute("href", getAlgorithm().getUri().toString()));
                parametersTable.setAtCursor(prm.getTypedValue() != null
                        ? new HTMLTagImpl("a", prm.getTypedValue().getValueAsString()).addTagAttribute(
                        "href", XSD_DATATYPE_LINKS.get(prm.getType().getURI())) : new HTMLTextImpl("-"));
            }
            parametersTable.setCellPadding(5).
                    setCellSpacing(2).
                    setTableBorder(1).
                    setColWidth(1, parametersTableDimensions[0]).
                    setColWidth(2, parametersTableDimensions[1]).
                    setColWidth(3, parametersTableDimensions[2]);

        }

        if (getMeta() != null && !getMeta().isEmpty()) {
            builder.getDiv().breakLine().breakLine();
            builder.addSubSubSubHeading("Meta Information");
            HTMLContainer metaContainer = getMeta().inHtml();
            HTMLTable metaTable = (HTMLTable) metaContainer.getComponents().get(0);
            metaTable.setColWidth(1, metaTableDimensions[0]).
                    setColWidth(2, metaTableDimensions[1]);
            builder.addComponent(metaContainer);
        }

        StringBuilder paragraphBuilder = new StringBuilder();
        paragraphBuilder.append("<small>Other Formats <a href=\"").
                append(getUri()).
                append("?accept=application/rdf%2Bxml\">RDF/XML</a>,<a href=\"").
                append(getUri()).
                append("?accept=application/x-turtle\">Turtle</a>,<a href=\"").
                append(getUri()).
                append("?accept=text/n-triples\">N-Triple</a><a href=\"").
                append(getUri()).
                append("?accept=text/uri-list\">Uri-list</a>.</small>");
        builder.addParagraph(paragraphBuilder.toString(), Alignment.left);
        return builder.getDiv();
    }

    @Override
    public Set<VRI> getBibTeXReferences() {
        return references;
    }

    @Override
    public IBibTexReferencable addBibTeXReferences(VRI... references) {
        this.references.addAll(Arrays.asList(references));
        return this;
    }

    @Override
    public IBibTexReferencable setBibTeXReferences(VRI... references) {
        this.references = new HashSet<VRI>();
        return addBibTeXReferences(references);
    }

    @Override
    public IBibTexReferencable setBibTeXReferences(Set<VRI> references) {
        this.references = references;
        return this;
    }
}

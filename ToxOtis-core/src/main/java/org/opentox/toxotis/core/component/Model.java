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
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
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
    private ArrayList<MultiParameter> multiParameters;
    private String localCode;
    private Serializable actualModel;
    private byte[] modelBytes;
    private User createdBy;
    private static final long serialVersionUID = 184328712643L;
    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Model.class);

    public Model(VRI uri) {
        super(uri);
    }

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

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public VRI getDataset() {
        return dataset;
    }

    public Model setDataset(VRI dataset) {
        this.dataset = dataset;
        return this;
    }

    public List<Feature> getDependentFeatures() {
        return dependentFeatures;
    }

    public Model setDependentFeatures(List<Feature> dependentFeature) {
        this.dependentFeatures = dependentFeature;
        return this;
    }

    public Model addDependentFeatures(Feature... features) {
        if (getDependentFeatures() == null) {
            setDependentFeatures(new ArrayList<Feature>(features.length));
        }
        for (Feature f : features) {
            getDependentFeatures().add(f);
        }
        return this;
    }

    public List<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    public Model setIndependentFeatures(List<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
        return this;
    }

    public Model addIndependentFeatures(Feature... features) {
        if (getIndependentFeatures() == null) {
            setIndependentFeatures(new ArrayList<Feature>(features.length));
        }
        for (Feature f : features) {
            getIndependentFeatures().add(f);
        }
        return this;
    }

    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<MultiParameter> getMultiParameters() {
        return multiParameters;
    }

    public Model setMultiParameters(ArrayList<MultiParameter> multiParameters) {
        this.multiParameters = multiParameters;
        return this;
    }

    public List<Feature> getPredictedFeatures() {
        return predictedFeatures;
    }

    public Model setPredictedFeatures(List<Feature> predictedFeature) {
        this.predictedFeatures = predictedFeature;
        return this;
    }

    public Model addPredictedFeatures(Feature... features) {
        if (getPredictedFeatures() == null) {
            setPredictedFeatures(new ArrayList<Feature>(features.length));
        }
        for (Feature f : features) {
            getPredictedFeatures().add(f);
        }
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
    public Model publishToOntService(VRI ontologyService, AuthenticationToken token) throws ServiceInvocationException {
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
        int hash = 3;
        hash = 71 * hash + (getUri() != null ? getUri().hashCode() : 0);
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
        byte buf[] = new byte[4000];
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
        builder.addSubSubHeading("Information about the Model");
        builder.getDiv().breakLine().breakLine();

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
                refs.append("<a href=\"" + nextVri + "\">" + nextVri + "</a>\n");
            } else {
                int i = 1;
                refs.append("<ol>\n");
                for (VRI vriRef : getBibTeXReferences()) {
                    refs.append("<li><a href=\"" + vriRef + "\">" + vriRef + "</a></li>\n");
                }
                refs.append("</ol>\n");
            }
            featuresTable.setTextAtCursor(refs.toString());
        }

        featuresTable.setCellPadding(5).
                setCellSpacing(2).
                setTableBorder(1).
                setColWidth(1, 150).
                setColWidth(2, 650);

        if (getParameters() != null && !getParameters().isEmpty()) {
            builder.addSubSubSubHeading("Model Parameters");
            HTMLTable parametersTable = builder.addTable(3);
            parametersTable.setAtCursor(new HTMLTextImpl("Parameter URI").formatBold(true)).
                    setAtCursor(new HTMLTextImpl("Parameter Name").formatBold(true)).setAtCursor(new HTMLTextImpl("Value").formatBold(true));
            for (Parameter prm : getParameters()) {
                parametersTable.setAtCursor(new HTMLTagImpl("a", prm.getUri().toString()).addTagAttribute("href", prm.getUri().toString()));
                parametersTable.setAtCursor(new HTMLTagImpl("a", prm.getName().getValueAsString()).addTagAttribute("href", getAlgorithm().getUri().toString()));
                parametersTable.setAtCursor(prm.getTypedValue() != null
                        ? new HTMLTagImpl("a", prm.getTypedValue().getValueAsString()).addTagAttribute(
                        "href", XSD_DATATYPE_LINKS.get(prm.getType().getURI())) : new HTMLTextImpl("-"));
            }
            parametersTable.setCellPadding(5).
                    setCellSpacing(2).
                    setTableBorder(1).
                    setColWidth(1, 500).
                    setColWidth(2, 150).
                    setColWidth(3, 400);

        }

        if (getMeta() != null && !getMeta().isEmpty()) {
            builder.addSubSubSubHeading("Meta Information");
            builder.addComponent(getMeta().inHtml());
        }

        builder.addParagraph("<small>Other Formats: "
                + "<a href=\"" + getUri() + "?accept=application/rdf%2Bxml" + "\">RDF/XML</a>,"
                + "<a href=\"" + getUri() + "?accept=application/x-turtle" + "\">Turtle</a>,"
                + "<a href=\"" + getUri() + "?accept=text/n-triples" + "\">N-Triple</a>,"
                + "<a href=\"" + getUri() + "?accept=text/uri-list" + "\">Uri-list</a>,"
                + "</small>", Alignment.left);

        return builder.getDiv();
    }

    @Override
    public Set<VRI> getBibTeXReferences() {
        return references;
    }

    @Override
    public IBibTexReferencable addBibTeXReferences(VRI... references) {
        for (VRI x : references) {
            this.references.add(x);
        }
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

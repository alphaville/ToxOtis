package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IHTMLSupport;
import org.opentox.toxotis.core.OTOnlineResource;
import org.opentox.toxotis.core.IOntologyServiceSupport;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
import org.opentox.toxotis.core.html.impl.HTMLTextImpl;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.User;
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
public class Model extends OTOnlineResource<Model> implements IOntologyServiceSupport<Model>, IHTMLSupport {

    private VRI dataset;
    private Algorithm algorithm;
    private Feature predictedFeature;
    private Feature dependentFeature;
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
    private byte[] model;
    private User createdBy;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Model.class);

    public Model(VRI uri) {
        super(uri);
    }

    public Model() {
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

    public void setActualModel(Serializable actualModel) {
        try {
            this.actualModel = actualModel;
            this.model = getBytes(actualModel);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static byte[] getBytes(Object obj) throws java.io.IOException {
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

    public void setDataset(VRI dataset) {
        this.dataset = dataset;
    }

    public Feature getDependentFeature() {
        return dependentFeature;
    }

    public void setDependentFeature(Feature dependentFeature) {
        this.dependentFeature = dependentFeature;
    }

    public List<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    public void setIndependentFeatures(List<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
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

    public void setMultiParameters(ArrayList<MultiParameter> multiParameters) {
        this.multiParameters = multiParameters;
    }

    public Feature getPredictedFeature() {
        return predictedFeature;
    }

    public void setPredictedFeature(Feature predictedFeature) {
        this.predictedFeature = predictedFeature;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().getStringNoQuery() : null, OTClasses.Model().inModel(model));
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
        if (dependentFeature != null) {
            indiv.addProperty(OTObjectProperties.dependentVariables().asObjectProperty(model), dependentFeature.asIndividual(model));
        }

        if (predictedFeature != null) {
            indiv.addProperty(OTObjectProperties.predictedVariables().asObjectProperty(model), predictedFeature.asIndividual(model));
        }

        if (independentFeatures != null) {
            for (Feature indFeat : independentFeatures) {
                indiv.addProperty(OTObjectProperties.independentVariables().asObjectProperty(model), indFeat.asIndividual(model));
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

    protected Model loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        ModelSpider spider = new ModelSpider(uri, token);
        Model m = spider.parse();
        setAlgorithm(m.getAlgorithm());
        setDataset(m.getDataset());
        setDependentFeature(m.getDependentFeature());
        setIndependentFeatures(m.getIndependentFeatures());
        setMeta(m.getMeta());
        setParameters(m.getParameters());
        setPredictedFeature(m.getPredictedFeature());
        setCreatedBy(m.getCreatedBy());
        return this;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Model publishToOntService(VRI ontologyService, AuthenticationToken token) throws ToxOtisException {
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
        this.model = toByteArray(modelBlob);
        this.actualModel = (Serializable) toObject(model);
    }

    public static Object toObject(byte[] bytes) {
        Object object = null;
        try {
            object = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        } catch (java.io.IOException ioe) {
        } catch (java.lang.ClassNotFoundException cnfe) {
        }
        return object;
    }

    public Blob getBlob() throws SerialException, SQLException {
        if (this.model == null) {
            return null;
        }
        try {
            return new SerialBlob(model);
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

    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("jaqpot_model");
        builder.addComment("Model Representation automatically generated by YAQP");
        builder.addSubHeading("Model Report");
        builder.addSubSubHeading(uri.toString());
        builder.getDiv().setAlignment(Alignment.justify).breakLine().horizontalSeparator();
        builder.addSubSubHeading("Information about the Model");
        builder.getDiv().breakLine().breakLine();

        HTMLTable featuresTable = builder.addTable(2);

        featuresTable.setAtCursor(new HTMLTextImpl("Dependent Feature").formatBold(true)).
                setTextAtCursor(HTMLUtils.linkUrlsInText(getDependentFeature().getUri().toString())).
                setAtCursor(new HTMLTextImpl("Predicted Feature").formatBold(true)).
                setTextAtCursor(HTMLUtils.linkUrlsInText(getPredictedFeature() != null ? getPredictedFeature().getUri().toString() : "-")).
                setAtCursor(new HTMLTextImpl("Independent Features").formatBold(true)).
                setAtCursor(HTMLUtils.createComponentList(getIndependentFeatures(), null, null)).
                setAtCursor(new HTMLTextImpl("Training Algorithm").formatBold(true)).
                setTextAtCursor(HTMLUtils.linkUrlsInText(getAlgorithm() != null ? getAlgorithm().getUri().toString() : "-"));

        featuresTable.setCellPadding(5).
                setCellSpacing(2).
                setTableBorder(1).
                setColWidth(1, 150).
                setColWidth(2, 600);

        if (getMeta() != null) {
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
}

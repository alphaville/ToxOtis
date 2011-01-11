package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
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
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.InactiveTokenException;
import org.opentox.toxotis.util.spiders.AlgorithmSpider;

/**
 * Provides access to different types of algorithms. An algorithm object contains
 * very general metadata about an algorithm and in fact describes its input and
 * output requirements and gives information about its parametrization. The different
 * types of algorithms used in <a href="http://opentox.org">OpenTox</a> and the
 * connection to each other is formally established throught the 
 * <a href="http://opentox.org/dev/apis/api-1.1/Algorithms">OpenTox Algorithm Ontology</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm extends OTOnlineResource<Algorithm>
        implements IOntologyServiceSupport<Algorithm>, IHTMLSupport {

    /** ParameterValue of parameters of the algorithm. Specify the way the algorithm is parametrized */
    private Set<Parameter> parameters = new HashSet<Parameter>();
    /** ParameterValue of ontological classes that characterize the algorithm*/
    private Set<OntologicalClass> ontologies;
    /** List of multi-parameters */
    private Set<MultiParameter> multiParameters = new LinkedHashSet<MultiParameter>();

    /**
     * Dummy constructor for an Algorithm object. Creates an algorithm with <code>null</code>
     * URI and no other characteristics.
     */
    public Algorithm() {
        super();
        addOntologicalClasses(OTClasses.Algorithm());
        setEnabled(true);
    }

    /**
     * Create a new instance of Algorithm providing its identifier as a {@link VRI }.
     * @param uri
     *      The URI of the algorithm as a {@link VRI }.
     */
    public Algorithm(VRI uri) throws ToxOtisException {
        super(uri);
        addOntologicalClasses(OTClasses.Algorithm());
        if (uri != null) {
            if (!Algorithm.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Algorithm uri according to the OpenTox specifications.");
            }
        }
    }

    /**
     * Constructs a new instance of Algorithm with given URI.
     * @param uri
     *      URI of the algorithm
     * @throws URISyntaxException
     *      In case the provided string cannot be cast as a {@link VRI }.
     */
    public Algorithm(String uri) throws URISyntaxException {
        super(new VRI(uri));
    }

    /**
     * Get the ontological classes of the algorithm
     * @return
     *      The collection of ontological classes for this algorithm.
     */
    public Set<OntologicalClass> getOntologies() {
        return ontologies;
    }

    /**
     * Specify the ontologies for this algorithm.
     * @param ontologies
     *      A collection of ontological classes that characterize this algorithm.
     */
    public void setOntologies(Set<OntologicalClass> ontologies) {
        this.ontologies = ontologies;
    }

    /**
     * Retrieve the set of parameters for this algorithm.
     * @return
     *      ParameterValue of parameters.
     */
    public Set<Parameter> getParameters() {
        return parameters;
    }

    /**
     * ParameterValue the parameters of the algorithm.
     * @param parameters
     *      ParameterValue of parameters.
     */
    public Algorithm setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Set<MultiParameter> getMultiParameters() {
        return multiParameters;
    }

    public void setMultiParameters(Set<MultiParameter> multiParameters) {
        this.multiParameters = multiParameters;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri().getStringNoQuery(), OTClasses.Algorithm().inModel(model));
        indiv.addComment(model.createTypedLiteral("Representation automatically generated by ToxOtis.",
                XSDDatatype.XSDstring));

        final MetaInfo metaInfo = getMeta();
        if (metaInfo != null) {
            metaInfo.attachTo(indiv, model);
        }

        if (ontologies != null && !ontologies.isEmpty()) {
            final Iterator<OntologicalClass> ontClassIter = ontologies.iterator();
            while (ontClassIter.hasNext()) {
                OntologicalClass oc = ontClassIter.next();
                if (oc.getName() != null) {
                    indiv.addRDFType(oc.inModel(model));
                }
            }
        }
        if (parameters != null) {
            for (Parameter param : parameters) {
                indiv.addProperty(OTObjectProperties.parameters().asObjectProperty(model), param.asIndividual(model));
            }
        }
        if (multiParameters != null) {
            for (MultiParameter mp : multiParameters) {
                indiv.addProperty(OTObjectProperties.multiParameter().asObjectProperty(model), mp.asIndividual(model));
            }
        }
        return indiv;
    }

    /**
     * Allows algorithms to be created from arbitrary input sources
     * (in general files, URLs or other).Note that this is still an experimental
     * method.
     *
     * @param stream
     *      Input stream used to create the algorithm
     * @param uri
     *      The URI of the algorithm resource. To obfuscate any misunderstanding
     *      we underline that this URI needs not be a URL, i.e. it will not be used
     *      to retrieve any information from the corresponding (remote) location
     *      but serves exclusively as a reference. It indicates which individual
     *      or the data model should be parsed. If set to <code>null</code>, then
     *      an arbitrary individual is chosen. This is not a good practise in cases
     *      where more than one instances of <code>ot:Algorithm</code> might be
     *      present in the same data model.
     *
     * @return
     *      Updates this algorithm object and returns the updated instance.
     *
     * @throws ToxOtisException
     *      In case the input stream does not provide a valid data model for
     *      an algorithm
     */
    public Algorithm loadFromRemote(InputStream stream, VRI uri) throws ToxOtisException {
        com.hp.hpl.jena.ontology.OntModel om = new SimpleOntModelImpl();
        om.read(stream, null);
        AlgorithmSpider spider = new AlgorithmSpider(null, om);
        Algorithm alg = spider.parse();
        return alg;
    }

    @Override
    protected Algorithm loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        AlgorithmSpider spider = new AlgorithmSpider(uri, token);
        Algorithm algorithm = spider.parse();
        setMeta(algorithm.getMeta());
        setOntologies(algorithm.getOntologies());
        setParameters(algorithm.getParameters());
        return this;
    }

    public Algorithm publishToOntService(VRI ontologyService, AuthenticationToken token) throws ToxOtisException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new InactiveTokenException("The Provided token is inactive");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("jaqpot_algorithm");
        builder.addComment("Algorithm Representation automatically generated by YAQP");
        builder.addSubHeading("Algorithm Report");
        builder.addSubSubHeading(uri.toString());
        builder.getDiv().setAlignment(Alignment.justify).breakLine().horizontalSeparator();

        builder.addSubSubHeading("Information about the Algorithm");
        builder.getDiv().breakLine().breakLine();

        builder.addSubSubSubHeading("Algorithm Types");
        builder.addComponent(HTMLUtils.createOntClassList(ontologies, "ul", "_list", true));
        builder.getDiv().breakLine();

        if (getParameters() != null && !getParameters().isEmpty()) {
            builder.addSubSubSubHeading("Algorithm Parameters");
            HTMLTable parametersTable = builder.addTable(4);
            parametersTable.setAtCursor(new HTMLTextImpl("Parameter Name").formatBold(true)).
                    setAtCursor(new HTMLTextImpl("Scope").formatBold(true)).
                    setAtCursor(new HTMLTextImpl("Default Value").formatBold(true)).
                    setAtCursor(new HTMLTextImpl("Meta information").formatBold(true));
            for (Parameter p : getParameters()) {
                parametersTable.setTextAtCursor(p.getName().getValueAsString()).
                        setTextAtCursor(p.getScope().name()).
                        setTextAtCursor(p.getTypedValue() != null ? p.getTypedValue().getValueAsString() : "-").
                        setAtCursor(((HTMLTable) p.getMeta().inHtml().getComponents().get(0)).setTableBorder(0));
            }
            parametersTable.setCellPadding(5).
                    setCellSpacing(2).
                    setTableBorder(1).
                    setColWidth(1, 150).
                    setColWidth(2, 150).
                    setColWidth(3, 150).
                    setColWidth(4, 500);
        }
        builder.getDiv().breakLine();


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

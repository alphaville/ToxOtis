package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.Closeable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public abstract class Tarantula<Result> implements Closeable {

    protected Resource resource;
    protected OntModel model;

    public Tarantula(Resource resource, OntModel model) {
        this.resource = resource;
        this.model = model;
    }

    public Tarantula() {
    }

    public abstract Result parse() throws ToxOtisException;

    protected TypedValue retrieveProp(Property prop) {
        StmtIterator it = model.listStatements(new SimpleSelector(resource, prop, (RDFNode) null));
        if (it.hasNext()) {
            try {
                RDFNode node = it.nextStatement().getObject();
                if (node.isLiteral()) {
                    XSDDatatype datatype = (XSDDatatype) node.as(Literal.class).getDatatype();
                    String stringVal = node.as(Literal.class).getString();
                    if (datatype.equals(XSDDatatype.XSDdateTime)) {
                        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        try {
                            Date date = (Date) formatter.parse(stringVal);
                            return (new TypedValue<Date>(date, datatype));
                        } catch (ParseException ex) {
                            try {
                                long longDate = Long.parseLong(stringVal);
                                return new TypedValue<Date>(new Date(longDate), datatype);
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                            }
                        }
                    } else {
                        return (new TypedValue(stringVal, datatype));
                    }
                } else if (node.isResource()) {
                    return (new TypedValue(node.as(Resource.class).getURI()));
                }
            } finally {
                it.close();
            }
        }
        return null;
    }

    protected ArrayList<String> retrieveProps(Property prop) {
        ArrayList<String> props = new ArrayList<String>();

        StmtIterator it = model.listStatements(
                new SimpleSelector(resource, prop, (Literal) null));
        while (it.hasNext()) {
            try {
                props.add(it.nextStatement().getObject().as(Literal.class).getString());
            } finally {
                it.close();
            }
        }
        return props;
    }

    protected Set<OntologicalClass> getOTATypes(Resource currentResource) {
        Set<OntologicalClass> ontClasses = new HashSet<OntologicalClass>();

        StmtIterator classIt = model.listStatements(
                new SimpleSelector(currentResource, RDF.type,
                (RDFNode) null));
        Set<OntClass> ontClassSet = new HashSet<OntClass>();
        while (classIt.hasNext()) {
            OntClass tempClass = classIt.nextStatement().getObject().as(OntClass.class);
            if (tempClass.getNameSpace().equals(OTAlgorithmTypes.NS)) {
                ontClassSet.add(tempClass);
                ontClassSet = getSuperTypes(ontClassSet);
            }
        }
        for (OntClass oc : ontClassSet) {
            ontClasses.add(OTAlgorithmTypes.forName(oc.getLocalName()));
        }
        return ontClasses;
    }

    protected Set<OntologicalClass> getFeatureTypes(Resource currentResource) {
        Set<OntologicalClass> ontClasses = new HashSet<OntologicalClass>();

        StmtIterator classIt = model.listStatements(
                new SimpleSelector(currentResource, RDF.type,
                (RDFNode) null));
        Set<OntClass> ontClassSet = new HashSet<OntClass>();
        while (classIt.hasNext()) {
            OntClass tempClass = classIt.nextStatement().getObject().as(OntClass.class);
            if (tempClass.getNameSpace().equals(OTClasses.NS)) {
                ontClassSet.add(tempClass);
                ontClassSet = getSuperTypes(ontClassSet);
            }
        }
        for (OntClass oc : ontClassSet) {
            ontClasses.add(OTClasses.forName(oc.getLocalName()));
        }
        return ontClasses;
    }

    private Set<OntClass> getSuperTypes(Set<OntClass> ontClasses) {
        Set<OntClass> newSet = new HashSet<OntClass>();
        newSet.addAll(ontClasses);
        for (OntClass oc : ontClasses) {
            newSet.addAll(oc.listSuperClasses().toSet());
        }
        if (!ontClasses.equals(newSet)) {
            newSet.addAll(getSuperTypes(newSet));
        }
        return newSet;
    }

    /**
     * Closes the ontological model used by the Spider (if any) and releases all
     * resources it holds. Closing the Spider is considered good practise and prefered
     * comparing to leaving it to the finalizer.
     */
    @Override
    public void close() {
        if (model != null) {
            model.close();
        }
    }

    public OntModel getOntModel() {
        return model;
    }
}

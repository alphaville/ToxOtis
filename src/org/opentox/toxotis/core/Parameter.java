package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Parameter<T> extends OTComponent<Parameter<T>>{

    public enum ParameterScope {

        OPTIONAL,
        MANDATORY;
    };

    /** The name of the parameter*/
    private String name;
    /** The type of the parameter according to the XSD specification */
    private XSDDatatype type;
    /** The parameter's value cast as the generic datatype <code>T</code>.*/
    private T value;
    /** The scope of the parameter (mandatory/optional)*/
    private ParameterScope scope;


    public Parameter() {
        super();
    }    

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    public XSDDatatype getType() {
        return type;
    }

    public void setType(XSDDatatype type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }// </editor-fold>


    @Override
    public Parameter<T> createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri().toString(), OTClasses.Parameter().inModel(model));
        MetaInfo metaInfo = getMeta();
        if (metaInfo != null) {
            // rdfs:comment
            String comment = metaInfo.getComment();
            if (comment != null) {
                indiv.addComment(model.createTypedLiteral(comment, XSDDatatype.XSDstring));
            }

            // dc:description
            String description = metaInfo.getDescription();
            if (description != null) {
                indiv.addProperty(model.createAnnotationProperty(DC.description.getURI()),
                        model.createTypedLiteral(description, XSDDatatype.XSDstring));
            }

            // dc:title
            String title = metaInfo.getDescription();
            if (title != null) {
                indiv.addProperty(model.createAnnotationProperty(DC.title.getURI()),
                        model.createTypedLiteral(title, XSDDatatype.XSDstring));
            }

        }

        // scope
        if (getScope() != null) {
            indiv.addLiteral(OTDatatypeProperties.paramScope().asDatatypeProperty(model),
                    model.createTypedLiteral(getScope().toString(), XSDDatatype.XSDstring));
        }

        // value
        if (getValue() != null) {
            XSDDatatype xsdType = getType();
            if (xsdType == null) {
                xsdType = XSDDatatype.XSDstring;
            }
            indiv.addLiteral(OTDatatypeProperties.paramValue().asDatatypeProperty(model),
                    model.createTypedLiteral(getValue(), xsdType));
        }

        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name  : ");
        builder.append(name);
        builder.append("\n");
        builder.append("Value : ");
        builder.append(value);
        builder.append("\n");
        builder.append("Scope : ");
        builder.append(scope);
        builder.append("\n");
        builder.append("Type  : ");
        builder.append(type);
        return new String(builder);
    }

    
}

package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

public class OntologicalClassImpl implements OntologicalClass {

    private String ns = OTClasses.NS;
    private String name;
    private MetaInfo metaInfo = new MetaInfoImpl();
    private Collection<OntologicalClass> superClasses = new ArrayList<OntologicalClass>();
    private Collection<OntologicalClass> disjointWith = new ArrayList<OntologicalClass>();

    public OntologicalClassImpl() {
    }

    public OntologicalClassImpl(String name) {
        this.name = name;
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public Collection<OntologicalClass> getSuperClasses() {
        return superClasses;
    }

    @Override
    public void setSuperClasses(Collection<OntologicalClass> superClasses) {
        this.superClasses = superClasses;
    }

    @Override
    public Collection<OntologicalClass> getDisjointWith() {
        OntModel om = null;
        return disjointWith;
    }

    @Override
    public void setDisjointWith(Collection<OntologicalClass> disjointWith) {
        this.disjointWith = disjointWith;
    }

    @Override
    public String getNameSpace() {
        return ns;
    }

    @Override
    public void setNameSpace(String ns) {
        this.ns = ns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUri() {
        return ns + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OntologicalClass) {
            OntologicalClass other = (OntologicalClass) obj;
            return other.getUri().equals(getUri());
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.ns != null ? this.ns.hashCode() : 0);
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public OntClass inModel(OntModel model) {
        OntClass clazz = model.getOntClass(getUri());
        if (clazz == null) {
            clazz = model.createClass(getUri());
            MetaInfo meta = getMetaInfo();
            if (meta != null) {
                String comment = meta.getComment() != null ? meta.getComment().getValue() : null;
                String desc = meta.getDescription() != null ? meta.getDescription().getValue() : null;
                String identifier = meta.getIdentifier() != null ? meta.getIdentifier().getValue() : null;
                String title = meta.getTitle() != null ? meta.getTitle().getValue() : null;
                String version = meta.getVersionInfo() != null ? meta.getVersionInfo().getValue() : null;
                if (comment != null) {
                    clazz.addComment(model.createTypedLiteral(comment, XSDDatatype.XSDstring));
                }
                if (desc != null) {
                    clazz.addProperty(model.createAnnotationProperty(DC.description.getURI()),
                            model.createTypedLiteral(desc, XSDDatatype.XSDstring));
                }
                if (identifier != null) {
                    clazz.addProperty(model.createAnnotationProperty(DC.identifier.getURI()),
                            model.createTypedLiteral(identifier, XSDDatatype.XSDstring));
                }
                if (title != null) {
                    clazz.addProperty(model.createAnnotationProperty(DC.title.getURI()),
                            model.createTypedLiteral(title, XSDDatatype.XSDstring));
                }
                if (version != null) {
                    clazz.setVersionInfo(version);
                }
            }
            for (OntologicalClass superClazz : getSuperClasses()) {
                clazz.addSuperClass(superClazz.inModel(model));
            }

            for (OntologicalClass disjointClazz : getDisjointWith()) {
                clazz.addDisjointWith(disjointClazz.inModel(model));
            }
        }
        return clazz;

    }
}

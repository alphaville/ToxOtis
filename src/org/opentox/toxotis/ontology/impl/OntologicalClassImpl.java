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

    /**
     * Contruct an empty instance of OntologicalClass. The namespace is by default
     * set to {@link OTClasses#NS ot} and its name is <code>null</code>.
     */
    public OntologicalClassImpl() {
    }

    /**
     * Create a new Ontological Class with gicen name. The namespace is by default
     * set to {@link OTClasses#NS ot}. Thus the URI of this class will be
     * <code>ot:{name}</code>.
     * @param name
     *      Name of the ontological class.
     */
    public OntologicalClassImpl(String name) {
        this();
        this.name = name;
    }

    /**
     * Constructs a new Ontological class with given name and namespace. In case
     * the provided namespace is <code>null</code> then the default value for it
     * is set, that is {@link OTClasses#NS ot}.
     * @param name
     *      Local name of the ontological class
     * @param namespace
     *      The namespace in which the ontological class belongs.
     */
    public OntologicalClassImpl(String name, String namespace) {
        this(name);
        if (namespace != null) {
            setNameSpace(namespace);
        }
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
                meta.attachTo(clazz, model);
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

package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

public class OntologicalClassImpl implements OntologicalClass {

    private String ns = OTClasses.NS;
    private String name;
    private MetaInfo metaInfo = new MetaInfoImpl();
    private Set<OntologicalClass> superClasses = new HashSet<OntologicalClass>();
    private Set<OntologicalClass> disjointWith = new HashSet<OntologicalClass>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OntologicalClassImpl.class);

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
        if (name == null) {
            logger.warn("Setting null as the name of an ontological class");
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            for (StackTraceElement s : ste) {
                System.out.println(s);
            }
        }
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
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (StackTraceElement s : ste) {
            System.out.println(s);
        }
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public OntologicalClass setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
        return this;
    }

    @Override
    public Set<OntologicalClass> getSuperClasses() {
        return superClasses;
    }

    @Override
    public OntologicalClass setSuperClasses(Set<OntologicalClass> superClasses) {
        this.superClasses = superClasses;
        return this;
    }

    @Override
    public Set<OntologicalClass> getDisjointWith() {
        OntModel om = null;
        return disjointWith;
    }

    @Override
    public OntologicalClass setDisjointWith(Set<OntologicalClass> disjointWith) {
        this.disjointWith = disjointWith;
        return this;
    }

    @Override
    public String getNameSpace() {
        return ns;
    }

    @Override
    public OntologicalClass setNameSpace(String ns) {
        if (ns == null) {
            logger.warn("Setting null as the namespace of an ontological class");
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            for (StackTraceElement s : ste) {
                System.out.println(s);
            }
        }
        this.ns = ns;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OntologicalClass setName(String name) {
        if (name == null) {
            logger.warn("Setting null as the namespace of an ontological class");
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            for (StackTraceElement s : ste) {
                System.out.println(s);
            }
        }
        this.name = name;
        return this;
    }

    @Override
    public String getUri() {
        return ns + name;
    }

    public OntologicalClass setUri(String uri) {
        return this;
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
            if (getSuperClasses() != null) {
                for (OntologicalClass superClazz : getSuperClasses()) {
                    clazz.addSuperClass(superClazz.inModel(model));
                }
            }
            if (disjointWith != null) {
                for (OntologicalClass disjointClazz : getDisjointWith()) {
                    clazz.addDisjointWith(disjointClazz.inModel(model));
                }
            }
        }
        return clazz;
    }

    @Override
    public String toString() {
        return getUri();
    }
}

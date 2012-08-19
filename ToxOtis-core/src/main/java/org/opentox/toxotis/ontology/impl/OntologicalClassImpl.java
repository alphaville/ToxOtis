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
    private static final long serialVersionUID = 2954714099334L;
    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OntologicalClassImpl.class);
    
    private static final int HASH_OFFSET = 7, HASH_MOD = 83;

    /**
     * Construct an empty instance of OntologicalClass. The name-space is by default
     * set to {@link OTClasses#NS ot} and its name is <code>null</code>.
     */
    public OntologicalClassImpl() {
    }

    /**
     * Create a new Ontological Class with given name. The name-space is by default
     * set to {@link OTClasses#NS ot}. Thus the URI of this class will be
     * <code>ot:{name}</code>.
     * @param name
     *      Name of the ontological class.
     */
    public OntologicalClassImpl(String name) {
        this();
        if (name == null) {
            logger.trace("Setting null as the name of an ontological class");
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
            this.ns = namespace;
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
            logger.trace("Setting null as the namespace of an ontological class");
        }
        this.name = name;
        return this;
    }

    @Override
    public String getUri() {
        return ns + name;
    }

    @Override
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
        int hash = HASH_OFFSET;
        hash = HASH_MOD * hash + (this.ns != null ? this.ns.hashCode() : 0);
        hash = HASH_MOD * hash + (this.name != null ? this.name.hashCode() : 0);
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

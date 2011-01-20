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
package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.vocabulary.OWL;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * A collection of all ontological classes defined by the Knouf BibTeX ontology.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class KnoufBibTex {

    private KnoufBibTex() {
    }
    public static final String NS = "http://purl.oclc.org/NET/nknouf/ns/bibtex#";
    private static OntologicalClass ms_Thing;
    private static OntologicalClass ms_Entry;
    private static OntologicalClass ms_Article;
    private static OntologicalClass ms_Book;
    private static OntologicalClass ms_Conference;
    private static OntologicalClass ms_Phdthesis;
    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : KnoufBibTex.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OntologicalClass forName(String name) throws ToxOtisException {
        initMethodCache();
        try {
            Method method = ms_methodCache.get(name);
            if (method == null) {
                throw new ToxOtisException(ErrorCause.KnoufBibTexClassNotFound, "BibTeX class : '" + name
                        + "' not found in the cache");
            }
            OntologicalClass oc = (OntologicalClass) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static OntologicalClass Thing() {
        if (ms_Thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            clazz.getMetaInfo().addComment("All classes subclass of owl:Thing");
            ms_Thing = clazz;
        }
        return ms_Thing;
    }

    public static OntologicalClass Entry() {
        if (ms_Entry == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Entry");
            clazz.getSuperClasses().add(Thing());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Generic bibtex entry");
            clazz.getMetaInfo().addComment("Take a look at http://zeitkunst.org/bibtex/0.1/");
            ms_Entry = clazz;
        }
        return ms_Entry;
    }

    public static OntologicalClass Article() {
        if (ms_Article == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Article");
            clazz.getSuperClasses().add(Entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("An article from a journal or magazine.");
            ms_Article = clazz;
        }
        return ms_Article;
    }

    public static OntologicalClass Book() {
        if (ms_Book == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Book");
            clazz.getSuperClasses().add(Entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A book with an explicit publisher.");
            ms_Book = clazz;
        }
        return ms_Book;
    }

    public static OntologicalClass Conference() {
        if (ms_Conference == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conference");
            clazz.getSuperClasses().add(Entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("The same as INPROCEEDINGS, included for Scribe compatibility.");
            ms_Conference = clazz;
        }
        return ms_Conference;
    }

    public static OntologicalClass Phdthesis() {
        if (ms_Phdthesis == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Phdthesis");
            clazz.getSuperClasses().add(Entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A PhD Thesis.");
            ms_Phdthesis = clazz;
        }
        return ms_Phdthesis;
    }
}

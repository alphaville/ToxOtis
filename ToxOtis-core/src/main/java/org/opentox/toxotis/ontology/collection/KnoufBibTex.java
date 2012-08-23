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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * A collection of all ontological classes defined by the Knouf BibTeX ontology.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class KnoufBibTex {

    private KnoufBibTex() {
    }
    public static final String NS = "http://purl.oclc.org/NET/nknouf/ns/bibtex#";
    private static OntologicalClass msEntry;
    private static OntologicalClass msArticle;
    private static OntologicalClass msBook;
    private static OntologicalClass msConference;
    private static OntologicalClass msPhdthesis;
    private static OntologicalClass msBooklet;
    private static OntologicalClass msInbook;
    private static OntologicalClass msIncollection;
    private static OntologicalClass msInproceedings;
    private static OntologicalClass msManual;
    private static OntologicalClass msMastersthesis;
    private static OntologicalClass msMisc;
    private static OntologicalClass msProceedings;
    private static OntologicalClass msTechReport;
    private static OntologicalClass msUnpublished;
    private static Map<String, Method> msmethodCache;

    private synchronized static void initMethodCache() {
        if (msmethodCache == null) {
            msmethodCache = new HashMap<String, Method>();
            for (Method method : KnoufBibTex.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    try {
                        Object o = method.invoke(null);
                        OntologicalClass oc = (OntologicalClass) o;
                        msmethodCache.put(oc.getName().toLowerCase(), method);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(KnoufBibTex.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(KnoufBibTex.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * Case-insensitive search for a Knouf ontological class.
     * @param name
     *      Name of the ontological class.
     * @return
     *      Ontological class with the specified name.
     * @throws ToxOtisException 
     *      In case such an ontological class does not exist.
     */
    public static OntologicalClass forName(String name) throws ToxOtisException {
        initMethodCache();
        try {
            Method method = msmethodCache.get(name.toLowerCase());
            if (method == null) {
                throw new ToxOtisException("KnoufBibTexClassNotFound: BibTeX class : '" + name
                        + "' not found in the cache");
            }
            OntologicalClass oc = (OntologicalClass) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    

    /**
     * Any BibTeX entry.
     */
    public static OntologicalClass entry() {
        if (msEntry == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Entry");
            clazz.getSuperClasses().add(OTClasses.thing());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Generic bibtex entry");
            clazz.getMetaInfo().addComment("Take a look at http://zeitkunst.org/bibtex/0.1/");
            msEntry = clazz;
        }
        return msEntry;
    }

    /**
     * An article in a journal.
     */
    public static OntologicalClass article() {
        if (msArticle == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Article");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("An article from a journal or magazine.");
            msArticle = clazz;
        }
        return msArticle;
    }

    /**
     * A booklet.
     */
    public static OntologicalClass booklet() {
        if (msBooklet == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Booklet");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A work that is printed and bound, but without a named publisher or sponsoring institution.");
            msBooklet = clazz;
        }
        return msBooklet;
    }

    /**
     * A chapter or section in a book.
     */
    public static OntologicalClass inbook() {
        if (msInbook == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Inbook");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A part of a book, which may be a chapter (or section or whatever) and/or a range of pages.");
            msInbook = clazz;
        }
        return msInbook;
    }

    /**
     * Part of a collection.
     */
    public static OntologicalClass incollection() {
        if (msIncollection == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Incollection");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A part of a book having its own title.");
            msIncollection = clazz;
        }
        return msIncollection;
    }

    /**
     * An article published in conference proceedings.
     */
    public static OntologicalClass inproceedings() {
        if (msInproceedings == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Inproceedings");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("An article in a conference proceedings.");
            msInproceedings = clazz;
        }
        return msInproceedings;
    }

    /**
     * A user's manual
     */
    public static OntologicalClass manual() {
        if (msManual == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Manual");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A Technical Manual");
            msManual = clazz;
        }
        return msManual;
    }

    /**
     * A master's thesis.
     */
    public static OntologicalClass mastersThesis() {
        if (msMastersthesis == null) {
            OntologicalClass clazz = new OntologicalClassImpl("A Master's thesis.");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A Technical Manual");
            msMastersthesis = clazz;
        }
        return msMastersthesis;
    }

    /**
     * Miscellaneous.
     */
    public static OntologicalClass miscellaneous() {
        if (msMisc == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Misc");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Misc., Use this type when nothing else fits.");
            msMisc = clazz;
        }
        return msMisc;
    }

    /**
     * A technical report.
     */
    public static OntologicalClass techReport() {
        if (msTechReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("TechReport");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("TechReport, A report published by a school or other institution, usually numbered within a series.");
            msTechReport = clazz;
        }
        return msTechReport;
    }

    /**
     * Unpublished material.
     */
    public static OntologicalClass unpublished() {
        if (msUnpublished == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Unpublished");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Unpublished, A document having an author and title, but not formally published.");
            msUnpublished = clazz;
        }
        return msUnpublished;
    }

    /*
     * A book.
     */
    public static OntologicalClass book() {
        if (msBook == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Book");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A book with an explicit publisher.");
            msBook = clazz;
        }
        return msBook;
    }

    /**
     * A conference.
     */
    public static OntologicalClass conference() {
        if (msConference == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conference");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("The same as INPROCEEDINGS, included for Scribe compatibility.");
            msConference = clazz;
        }
        return msConference;
    }

    /**
     * A PhD thesis.
     */
    public static OntologicalClass phdThesis() {
        if (msPhdthesis == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Phdthesis");
            clazz.getSuperClasses().add(entry());
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("A PhD Thesis.");
            msPhdthesis = clazz;
        }
        return msPhdthesis;
    }
}

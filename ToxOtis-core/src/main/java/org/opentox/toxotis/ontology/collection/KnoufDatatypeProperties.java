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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.impl.OTDatatypePropertyImpl;

/**
 * A collection of all ontological datatype properties specified by the Knouf
 * BibTeX ontology.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class KnoufDatatypeProperties {

    private KnoufDatatypeProperties() {
    }
    private static OTDatatypeProperty ms_hasAbstract;
    private static OTDatatypeProperty ms_hasAddress;
    private static OTDatatypeProperty ms_hasAffiliation;
    private static OTDatatypeProperty ms_hasAnnotation;
    private static OTDatatypeProperty ms_hasKey;
    private static OTDatatypeProperty ms_hasKeywords;
    private static OTDatatypeProperty ms_hasMonth;
    private static OTDatatypeProperty ms_hasPages;
    private static OTDatatypeProperty ms_hasNumber;
    private static OTDatatypeProperty ms_hasSchool;
    private static OTDatatypeProperty ms_hasSeries;
    private static OTDatatypeProperty ms_hasTitle;
    private static OTDatatypeProperty ms_hasURL;
    private static OTDatatypeProperty ms_hasVolume;
    private static OTDatatypeProperty ms_hasYear;
    private static OTDatatypeProperty ms_hasAuthor;
    private static OTDatatypeProperty ms_hasBookTitle;
    private static OTDatatypeProperty ms_hasChapter;
    private static OTDatatypeProperty ms_hasCopyright;
    private static OTDatatypeProperty ms_hasEdition;
    private static OTDatatypeProperty ms_hasCrossref;
    private static OTDatatypeProperty ms_hasEditor;
    private static OTDatatypeProperty ms_hasOrganization;
    private static OTDatatypeProperty ms_hasISBN;
    private static OTDatatypeProperty ms_hasISSN;
    private static OTDatatypeProperty ms_hasJournal;
    /**
     * Cache of methods to avoid runtime reflective lookups
     */
    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : KnoufDatatypeProperties.class.getDeclaredMethods()) {
                if (OTDatatypeProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OTDatatypeProperty forName(String name) throws ToxOtisException {
        initMethodCache();
        try {
            Method method = ms_methodCache.get(name);
            if (method == null) {
                throw new ToxOtisException(ErrorCause.KnoufDatatypePropertyNotFound, "The property '"
                        + name + "' was not found in the Knouf ontology");
            }
            OTDatatypeProperty oc = (OTDatatypeProperty) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static OTDatatypeProperty hasAbstract() {
        if (ms_hasAbstract == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAbstract");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Abstract");
            property.getMetaInfo().addComment("An abstract of the work.");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasAbstract = property;
        }
        return ms_hasAbstract;
    }

    public static OTDatatypeProperty hasTitle() {
        if (ms_hasTitle == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasTitle");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has title");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasTitle = property;
        }
        return ms_hasTitle;
    }

    public static OTDatatypeProperty hasURL() {
        if (ms_hasURL == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasURL");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has url");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasURL = property;
        }
        return ms_hasURL;
    }

    public static OTDatatypeProperty hasAuthor() {
        if (ms_hasAuthor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAuthor");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Author");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasAuthor = property;
        }
        return ms_hasAuthor;
    }

    public static OTDatatypeProperty hasBookTitle() {
        if (ms_hasBookTitle == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasBookTitle");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has BookTitle");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasBookTitle = property;
        }
        return ms_hasBookTitle;
    }

    public static OTDatatypeProperty hasChapter() {
        if (ms_hasChapter == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasChapter");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Chapter");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasChapter = property;
        }
        return ms_hasChapter;
    }

    public static OTDatatypeProperty hasCopyright() {
        if (ms_hasCopyright == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasCopyright");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Copyright");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasCopyright = property;
        }
        return ms_hasCopyright;
    }

    public static OTDatatypeProperty hasCrossRef() {
        if (ms_hasCrossref == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasCrossref");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDanyURI);
            property.getMetaInfo().addTitle("has Cross-reference");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasCrossref = property;
        }
        return ms_hasCrossref;
    }

    public static OTDatatypeProperty hasEdition() {
        if (ms_hasEdition == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasEdition");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Edition");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasEdition = property;
        }
        return ms_hasEdition;
    }

    public static OTDatatypeProperty hasEditor() {
        if (ms_hasEditor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasEditor");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Editor");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasEditor = property;
        }
        return ms_hasEditor;
    }

    public static OTDatatypeProperty hasISBN() {
        if (ms_hasISBN == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasISBN");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has ISBN");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasISBN = property;
        }
        return ms_hasISBN;
    }

    public static OTDatatypeProperty hasISSN() {
        if (ms_hasISSN == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasISSN");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has ISSN");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasISSN = property;
        }
        return ms_hasISSN;
    }

    public static OTDatatypeProperty hasJournal() {
        if (ms_hasJournal == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasJournal");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has journal");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasJournal = property;
        }
        return ms_hasJournal;
    }

    public static OTDatatypeProperty hasOrganization() {
        if (ms_hasOrganization == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasOrganization");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has organization");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasOrganization = property;
        }
        return ms_hasOrganization;
    }

    public static OTDatatypeProperty hasAddress() {
        if (ms_hasAddress == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAddress");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has address");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasAddress = property;
        }
        return ms_hasAddress;
    }

    public static OTDatatypeProperty hasAffiliation() {
        if (ms_hasAffiliation == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAffiliation");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has affiliation");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasAffiliation = property;
        }
        return ms_hasAffiliation;
    }

    public static OTDatatypeProperty hasAnnotation() {
        if (ms_hasAnnotation == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAnnotation");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has annotation");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasAnnotation = property;
        }
        return ms_hasAnnotation;
    }

    public static OTDatatypeProperty hasKey() {
        if (ms_hasKey == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasKey");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has key");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasKey = property;
        }
        return ms_hasKey;
    }

    public static OTDatatypeProperty hasKeywords() {
        if (ms_hasKeywords == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasKeywords");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has keywords");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasKeywords = property;
        }
        return ms_hasKeywords;
    }

    public static OTDatatypeProperty hasMonth() {
        if (ms_hasMonth == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasMonth");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has month");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasMonth = property;
        }
        return ms_hasMonth;
    }

    public static OTDatatypeProperty hasNumber() {
        if (ms_hasNumber == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasNumber");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has number");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasNumber = property;
        }
        return ms_hasNumber;
    }

    public static OTDatatypeProperty hasVolume() {
        if (ms_hasVolume == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasVolume");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has volume");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasVolume = property;
        }
        return ms_hasVolume;
    }

    public static OTDatatypeProperty hasSchool() {
        if (ms_hasSchool == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasSchool");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has school");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasSchool = property;
        }
        return ms_hasSchool;
    }

    public static OTDatatypeProperty hasPages() {
        if (ms_hasPages == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasPages");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has pages");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasPages = property;
        }
        return ms_hasPages;
    }

    public static OTDatatypeProperty hasSeries() {
        if (ms_hasSeries == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasSeries");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has series");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasSeries = property;
        }
        return ms_hasSeries;
    }

    public static OTDatatypeProperty hasYear() {
        if (ms_hasYear == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasYear");
            property.getDomain().add(KnoufBibTex.Entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has year");
            property.setNameSpace(KnoufBibTex.NS);
            ms_hasYear = property;
        }
        return ms_hasYear;
    }
}

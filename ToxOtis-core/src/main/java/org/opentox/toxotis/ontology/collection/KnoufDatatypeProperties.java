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
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.impl.OTDatatypePropertyImpl;

/**
 * A collection of all ontological datatype properties specified by the Knouf
 * BibTeX ontology.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class KnoufDatatypeProperties {

    private KnoufDatatypeProperties() {
    }
    private static OTDatatypeProperty hasAbstract;
    private static OTDatatypeProperty hasAddress;
    private static OTDatatypeProperty hasAffiliation;
    private static OTDatatypeProperty hasAnnotation;
    private static OTDatatypeProperty hasKey;
    private static OTDatatypeProperty hasKeywords;
    private static OTDatatypeProperty hasMonth;
    private static OTDatatypeProperty hasPages;
    private static OTDatatypeProperty hasNumber;
    private static OTDatatypeProperty hasSchool;
    private static OTDatatypeProperty hasSeries;
    private static OTDatatypeProperty hasTitle;
    private static OTDatatypeProperty hasURL;
    private static OTDatatypeProperty hasVolume;
    private static OTDatatypeProperty hasYear;
    private static OTDatatypeProperty hasAuthor;
    private static OTDatatypeProperty hasBookTitle;
    private static OTDatatypeProperty hasChapter;
    private static OTDatatypeProperty hasCopyright;
    private static OTDatatypeProperty hasEdition;
    private static OTDatatypeProperty hasCrossref;
    private static OTDatatypeProperty hasEditor;
    private static OTDatatypeProperty hasOrganization;
    private static OTDatatypeProperty hasISBN;
    private static OTDatatypeProperty hasISSN;
    private static OTDatatypeProperty hasJournal;
    /**
     * Cache of methods to avoid runtime reflective lookups
     */
    private static Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : KnoufDatatypeProperties.class.getDeclaredMethods()) {
                if (OTDatatypeProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OTDatatypeProperty forName(String name) throws ToxOtisException {
        initMethodCache();
        try {
            Method method = methodCache.get(name);
            if (method == null) {
                throw new ToxOtisException("KnoufDatatypePropertyNotFound: The property '"
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
        if (hasAbstract == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAbstract");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Abstract");
            property.getMetaInfo().addComment("An abstract of the work.");
            property.setNameSpace(KnoufBibTex.NS);
            hasAbstract = property;
        }
        return hasAbstract;
    }

    public static OTDatatypeProperty hasTitle() {
        if (hasTitle == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasTitle");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has title");
            property.setNameSpace(KnoufBibTex.NS);
            hasTitle = property;
        }
        return hasTitle;
    }

    public static OTDatatypeProperty hasURL() {
        if (hasURL == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasURL");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has url");
            property.setNameSpace(KnoufBibTex.NS);
            hasURL = property;
        }
        return hasURL;
    }

    public static OTDatatypeProperty hasAuthor() {
        if (hasAuthor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAuthor");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Author");
            property.setNameSpace(KnoufBibTex.NS);
            hasAuthor = property;
        }
        return hasAuthor;
    }

    public static OTDatatypeProperty hasBookTitle() {
        if (hasBookTitle == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasBookTitle");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has BookTitle");
            property.setNameSpace(KnoufBibTex.NS);
            hasBookTitle = property;
        }
        return hasBookTitle;
    }

    public static OTDatatypeProperty hasChapter() {
        if (hasChapter == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasChapter");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Chapter");
            property.setNameSpace(KnoufBibTex.NS);
            hasChapter = property;
        }
        return hasChapter;
    }

    public static OTDatatypeProperty hasCopyright() {
        if (hasCopyright == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasCopyright");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Copyright");
            property.setNameSpace(KnoufBibTex.NS);
            hasCopyright = property;
        }
        return hasCopyright;
    }

    public static OTDatatypeProperty hasCrossRef() {
        if (hasCrossref == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasCrossref");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDanyURI);
            property.getMetaInfo().addTitle("has Cross-reference");
            property.setNameSpace(KnoufBibTex.NS);
            hasCrossref = property;
        }
        return hasCrossref;
    }

    public static OTDatatypeProperty hasEdition() {
        if (hasEdition == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasEdition");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Edition");
            property.setNameSpace(KnoufBibTex.NS);
            hasEdition = property;
        }
        return hasEdition;
    }

    public static OTDatatypeProperty hasEditor() {
        if (hasEditor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasEditor");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has Editor");
            property.setNameSpace(KnoufBibTex.NS);
            hasEditor = property;
        }
        return hasEditor;
    }

    public static OTDatatypeProperty hasISBN() {
        if (hasISBN == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasISBN");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has ISBN");
            property.setNameSpace(KnoufBibTex.NS);
            hasISBN = property;
        }
        return hasISBN;
    }

    public static OTDatatypeProperty hasISSN() {
        if (hasISSN == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasISSN");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has ISSN");
            property.setNameSpace(KnoufBibTex.NS);
            hasISSN = property;
        }
        return hasISSN;
    }

    public static OTDatatypeProperty hasJournal() {
        if (hasJournal == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasJournal");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has journal");
            property.setNameSpace(KnoufBibTex.NS);
            hasJournal = property;
        }
        return hasJournal;
    }

    public static OTDatatypeProperty hasOrganization() {
        if (hasOrganization == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasOrganization");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has organization");
            property.setNameSpace(KnoufBibTex.NS);
            hasOrganization = property;
        }
        return hasOrganization;
    }

    public static OTDatatypeProperty hasAddress() {
        if (hasAddress == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAddress");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has address");
            property.setNameSpace(KnoufBibTex.NS);
            hasAddress = property;
        }
        return hasAddress;
    }

    public static OTDatatypeProperty hasAffiliation() {
        if (hasAffiliation == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAffiliation");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has affiliation");
            property.setNameSpace(KnoufBibTex.NS);
            hasAffiliation = property;
        }
        return hasAffiliation;
    }

    public static OTDatatypeProperty hasAnnotation() {
        if (hasAnnotation == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAnnotation");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has annotation");
            property.setNameSpace(KnoufBibTex.NS);
            hasAnnotation = property;
        }
        return hasAnnotation;
    }

    public static OTDatatypeProperty hasKey() {
        if (hasKey == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasKey");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has key");
            property.setNameSpace(KnoufBibTex.NS);
            hasKey = property;
        }
        return hasKey;
    }

    public static OTDatatypeProperty hasKeywords() {
        if (hasKeywords == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasKeywords");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has keywords");
            property.setNameSpace(KnoufBibTex.NS);
            hasKeywords = property;
        }
        return hasKeywords;
    }

    public static OTDatatypeProperty hasMonth() {
        if (hasMonth == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasMonth");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has month");
            property.setNameSpace(KnoufBibTex.NS);
            hasMonth = property;
        }
        return hasMonth;
    }

    public static OTDatatypeProperty hasNumber() {
        if (hasNumber == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasNumber");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has number");
            property.setNameSpace(KnoufBibTex.NS);
            hasNumber = property;
        }
        return hasNumber;
    }

    public static OTDatatypeProperty hasVolume() {
        if (hasVolume == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasVolume");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has volume");
            property.setNameSpace(KnoufBibTex.NS);
            hasVolume = property;
        }
        return hasVolume;
    }

    public static OTDatatypeProperty hasSchool() {
        if (hasSchool == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasSchool");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has school");
            property.setNameSpace(KnoufBibTex.NS);
            hasSchool = property;
        }
        return hasSchool;
    }

    public static OTDatatypeProperty hasPages() {
        if (hasPages == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasPages");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has pages");
            property.setNameSpace(KnoufBibTex.NS);
            hasPages = property;
        }
        return hasPages;
    }

    public static OTDatatypeProperty hasSeries() {
        if (hasSeries == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasSeries");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has series");
            property.setNameSpace(KnoufBibTex.NS);
            hasSeries = property;
        }
        return hasSeries;
    }

    public static OTDatatypeProperty hasYear() {
        if (hasYear == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasYear");
            property.getDomain().add(KnoufBibTex.entry());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has year");
            property.setNameSpace(KnoufBibTex.NS);
            hasYear = property;
        }
        return hasYear;
    }
}

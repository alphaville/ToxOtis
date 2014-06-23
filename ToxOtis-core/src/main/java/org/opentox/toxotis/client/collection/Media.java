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


package org.opentox.toxotis.client.collection;

/**
 * A collection of useful mediatypes. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Media {

    /**
     * The default encoding for an HTML form <code>application/x-www-form-urlencoded</code>
     * @see <a href="http://www.w3.org/MarkUp/html-spec/html-spec_8.html#SEC8.2.1">W3C specification</a>
     */
    APPLICATION_FORM_URL_ENCODED("application/x-www-form-urlencoded"),
    APPLICATION_RDF_XML("application/rdf+xml"),
    APPLICATION_RDF_TURTLE("application/x-turtle"),
    APPLICATION_RDF_XML_ABBREV("application/rdf+xml-abbrev"),
    /**
     * @see <a href="http://www.ch.ic.ac.uk/chemime/">Chemical MIMEs</a>
     */
    CHEMICAL_MDLSDF("chemical/x-mdl-sdfile"),
    /**
     * @see <a href="http://www.ch.ic.ac.uk/chemime/">Chemical MIMEs</a>
     */
    CHEMICAL_MDLMOL("chemical/x-mdl-molfile"),
    /**
     * Chemical Modelling Language (CML) MIME type.
     * @see <a href="http://www.ch.ic.ac.uk/chemime/">Chemical MIMEs</a>
     * @see <a href="http://cml.sourceforge.net/">CML project</a>
     */
    CHEMICAL_CML("chemical/x-cml"),
    /**
     * <code>Chemical/x-daylight-smiles</code> is the MIME type for the SMILE String of a chemical compound
     * @see <a href="http://www.ch.ic.ac.uk/chemime/">Chemical MIMEs</a>
     */
    CHEMICAL_SMILES("chemical/x-daylight-smiles"),
    /**
     * MIME for the weka prime data serialization format: <code>text/x-arff</code>
     * @see <a href="http://www.cs.waikato.ac.nz/~ml/weka/arff.html">Examples of ARFFs</a>
     * @see <a href="http://weka.wikispaces.com/Creating+an+ARFF+file">Create an ARFF file</a>
     * @see <a href="http://weka.wikispaces.com/ARFF+%28book+version%29">ARFF specification</a>
     */
    WEKA_ARFF("text/x-arff"),
    /**
     * MIME for YAML documents (text)
     * @see <a href="http://www.yaml.org/spec/1.2/spec.html">YAML specification</a>
     */
    TEXT_YAML("text/x-yaml"),
    /**
     * MIME for YAML documents (application)
     * @see <a href="http://www.yaml.org/spec/1.2/spec.html">YAML specification</a>
     */
    APPLICATION_YAML("application/x-yaml"),
    /**
     * The MIME <code>application/xml</code>
     * @see <a href="http://tools.ietf.org/html/rfc3023">RFC 3023</a>
     * @see <a href="http://en.wikipedia.org/wiki/XML_and_MIME">Wikipedia article</a>
     */
    APPLICATION_XML("application/xml"),
    /**
     * MIME <code>text/uri-list</code> for a list of URIs as specified in section 5 of RFC 2483.
     * @see <a href="http://www.ietf.org/rfc/rfc2483.txt">RFC 2483</a>
     */
    TEXT_URI_LIST("text/uri-list"),
    TEXT_N_TRIPLES("text/n-triples"),
    /**
     * MIME type <code>text/csv</code> for CSV (comma seperated values) documents
     * @see <a href="http://www.shaftek.org/publications/drafts/mime-csv/draft-shafranovich-mime-csv-00.html">Text/csv specification</a>
     */
    TEXT_CSV("text/csv"),
    /**
     * MIME <code>image/png</code> for PNG images
     * @see <a href="http://www.fileformat.info/info/mimetype/image/png/index.htm">Information about image/png</a>
     */
    IMAGE_PNG("image/png"),
    /**
     * MIME <code>image/gif</code> for GIF images
     * @see <a href="http://www.fileformat.info/info/mimetype/image/gif/index.htm">Information about image/gif</a>
     */
    IMAGE_GIF("image/gif"),
    /**
     * MIME <code>image/vnd.sealedmedia.softseal.jpg</code>
     * @see <a href="http://www.fileformat.info/info/mimetype/image/vnd.sealedmedia.softseal.jpg/index.htm">Information about image/vnd.sealedmedia.softseal.jpg</a>
     */
    IMAGE_VND_SEALEDMEDIA_SOFTSEAL_JPG("image/vnd.sealedmedia.softseal.jpg"),
    /**
     * MIME <code>image/jpeg</code> for JPEG images
     * @see <a href="http://www.w3schools.com/media/media_mimeref.asp">MIMEs at w3schools</a>
     * @see <a href="http://www.ietf.org/rfc/rfc2387.txt">RFC 2387</a>
     */
    IMAGE_JPEG("image/jpeg"),
    ;
    private final String mime;

    private Media(String mime) {
        this.mime = mime;
    }

    public String getMime() {
        return mime;
    }

    @Override
    public String toString() {
        return mime;
    }
}

package org.opentox.toxotis.client.collection;

/**
 * A collection of useful mediatypes. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Media {

    APPLICATION_FORM_URL_ENCODED("application/x-www-form-urlencoded"),
    APPLICATION_RDF_XML("application/rdf+xml"),
    APPLICATION_RDF_TURTLE("application/x-turtle"),
    APPLICATION_RDF_XML_ABBREV("application/rdf+xml-abbrev"),
    CHEMICAL_MDLSDF("chemical/x-mdl-sdfile"),
    CHEMICAL_MDLMOL("chemical/x-mdl-molfile"),
    CHEMICAL_CML("chemical/x-cml"),
    CHEMICAL_SMILES("chemical/x-daylight-smiles"),
    /**
     * MIME for the weka prime data serialization format: <code>text/x-arff</code>
     */
    WEKA_ARFF("text/x-arff"),
    /**
     * MIME for YAML documents (text)
     * @see http://www.yaml.org/spec/1.2/spec.html
     */
    TEXT_YAML("text/x-yaml"),
    /**
     * MIME for YAML documents (application)
     * @see http://www.yaml.org/spec/1.2/spec.html
     */
    APPLICATION_YAML("application/x-yaml"),
    APPLICATION_XML("application/xml"),
    /**
     * MIME for a list of URIs as specified in section 5 of RFC 2483
     * @see <a href="http://www.ietf.org/rfc/rfc2483.txt">http://www.ietf.org/rfc/rfc2483.txt</a>
     */
    TEXT_URI_LIST("text/uri-list"),
    TEXT_N_TRIPLES("text/n-triples"),
    /**
     * MIME type for CSV (comma seperated values) documents
     * @see <a href="http://www.shaftek.org/publications/drafts/mime-csv/draft-shafranovich-mime-csv-00.html">http://www.shaftek.org/publications/drafts/mime-csv/draft-shafranovich-mime-csv-00.html</a>
     */
    TEXT_CSV("text/csv"),
    /**
     * MIME for PNG images
     * @see <a href="http://www.fileformat.info/info/mimetype/image/png/index.htm">http://www.fileformat.info/info/mimetype/image/png/index.htm</a>
     */
    IMAGE_PNG("image/png"),
    /**
     * MIME for GIF images
     * @see <a href="http://www.fileformat.info/info/mimetype/image/gif/index.htm">http://www.fileformat.info/info/mimetype/image/gif/index.htm</a>
     */
    IMAGE_GIF("image/gif"),
    /**
     * MIME image/vnd.sealedmedia.softseal.jpg
     * @see <a href="http://www.fileformat.info/info/mimetype/image/vnd.sealedmedia.softseal.jpg/index.htm">http://www.fileformat.info/info/mimetype/image/vnd.sealedmedia.softseal.jpg/index.htm</a>
     */
    IMAGE_VND_SEALEDMEDIA_SOFTSEAL_JPG("image/vnd.sealedmedia.softseal.jpg"),
    /**
     * MIME for JPEG images
     * @see <a href="http://www.w3schools.com/media/media_mimeref.asp">http://www.w3schools.com/media/media_mimeref.asp</a>
     * @see <a href="http://www.ietf.org/rfc/rfc2387.txt">http://www.ietf.org/rfc/rfc2387.txt</a>
     */
    IMAGE_JPEG("image/jpeg"),
    ;
    private String mime;

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

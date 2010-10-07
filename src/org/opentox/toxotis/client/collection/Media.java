package org.opentox.toxotis.client.collection;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Media {

    APPLICATION_RDF_XML("application/rdf+xml"),
    APPLICATION_RDF_TURTLE("application/x-turtle"),
    APPLICATION_RDF_XML_ABBREV("application/rdf+xml-abbrev"),
    CHEMICAL_MDLSDF("chemical/x-mdl-sdfile"),
    CHEMICAL_MDLMOL("chemical/x-mdl-molfile"),
    CHEMICAL_CML("chemical/x-cml"),
    CHEMICAL_SMILES("chemical/x-daylight-smiles"),
    WEKA_ARFF("text/x-arff"),
    TEXT_YAML("text/x-yaml"),
    APPLICATION_YAML("application/x-yaml"),
    TEXT_URI_LIST("text/uri-list"),
    TEXT_N_TRIPLES("text/n-triples");

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

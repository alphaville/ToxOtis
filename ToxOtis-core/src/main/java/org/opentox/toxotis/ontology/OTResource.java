package org.opentox.toxotis.ontology;


/**
 * Interface for resources that appear in ontological represnetations in OpenTox.
 * @author Pantelis Sopasakis
 */
public interface OTResource extends java.io.Serializable {

    /**
     * The URI of the ontological resource that uniquely identifies it.
     * @return
     *      The URI of the resource as a string/
     */
    String getUri();   

}
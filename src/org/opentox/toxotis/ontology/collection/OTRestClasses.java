package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.vocabulary.OWL;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTRestClasses {

    private OTRestClasses() {
    }
    public static final String NS = "http://opentox.org/opentox-rest.owl#";
    private static OntologicalClass ms_HTTPMethod;
    private static OntologicalClass ms_HTTPStatus;
    private static OntologicalClass ms_Status_200;
    private static OntologicalClass ms_Status_201;
    private static OntologicalClass ms_Status_202;
    private static OntologicalClass ms_Status_303;
    private static OntologicalClass ms_Status_400;
    private static OntologicalClass ms_Status_401;
    private static OntologicalClass ms_Status_403;
    private static OntologicalClass ms_Status_404;
    private static OntologicalClass ms_Status_500;
    private static OntologicalClass ms_Status_502;
    private static OntologicalClass ms_Status_503;
    private static OntologicalClass ms_InputParameter;
    private static OntologicalClass ms_InputParameterCompound;
    private static OntologicalClass ms_InputParameterDataset;
    private static OntologicalClass ms_InputParameterDatasetService;
    private static OntologicalClass ms_InputParameterFeature;
    private static OntologicalClass ms_InputParameterResultDataset;
    private static OntologicalClass ms_InputParameterTarget;
    private static OntologicalClass ms_MediaType;
    private static OntologicalClass ms_mime_chemical_inchi;
    private static OntologicalClass ms_mime_chemical_mol;
    private static OntologicalClass ms_mime_chemical_sdf;
    private static OntologicalClass ms_mime_chemical_smiles;
    private static OntologicalClass ms_mime_json;
    private static OntologicalClass ms_mime_rdf_json;
    private static OntologicalClass ms_mime_rdf_n3;
    private static OntologicalClass ms_mime_rdf_turtle;
    private static OntologicalClass ms_mime_rdf_xml;
    private static OntologicalClass ms_mime_text_csv;
    private static OntologicalClass ms_mime_text_html;
    private static OntologicalClass ms_mime_text_plain;
    private static OntologicalClass ms_mime_text_uri_list;
    private static OntologicalClass ms_mime_text_weka_arff;
    private static OntologicalClass ms_RESTOperation;
    private static OntologicalClass ms_DELETE_Operation;
    private static OntologicalClass ms_DELETE_Feature;
    private static OntologicalClass ms_DELETE_Model;
    private static OntologicalClass ms_DELETE_Task;
    private static OntologicalClass ms_GET_Operation;
    private static OntologicalClass ms_GET_Algorithm;
    private static OntologicalClass ms_GET_Algorithms;
    private static OntologicalClass ms_GET_Feature;
    private static OntologicalClass ms_GET_Model;
    private static OntologicalClass ms_GET_Models;
    private static OntologicalClass ms_GET_Task;
    private static OntologicalClass ms_GET_Tasks;
    private static OntologicalClass ms_GET_OperationAlgorithm;
    private static OntologicalClass ms_POST_AlgorithmCreateModel;
    private static OntologicalClass ms_POST_AlgorithmProcessCompound;
    private static OntologicalClass ms_POST_AlgorithmProcessDataset;
    private static OntologicalClass ms_OperationFeature;
    private static OntologicalClass ms_POST_Feature;
    private static OntologicalClass ms_PUT_Feature;
    private static OntologicalClass ms_OperationInputCompound;
    private static OntologicalClass ms_OperationInputDataset;
    private static OntologicalClass ms_OperationInputFeature;
    private static OntologicalClass ms_OperationModel;
    private static OntologicalClass ms_POST_Model;
    private static OntologicalClass ms_OperationNoResult;
    private static OntologicalClass ms_OperationResultAlgorithm;
    private static OntologicalClass ms_OperationResultDataset;
    private static OntologicalClass ms_OperationResultModel;
    private static OntologicalClass ms_OperationResultTask;
    private static OntologicalClass ms_OperationTask;
    private static OntologicalClass ms_POST_Operation;
    private static OntologicalClass ms_PUT_Operation;
    private static OntologicalClass ms_SingleResourceOp;
    private static OntologicalClass ms_RESTTemplate;
    private static OntologicalClass ms_AlgorithmTemplate;
    private static OntologicalClass ms_RESTTemplateAttribute;
    private static OntologicalClass ms_MultiTaskTemplate;
    private static OntologicalClass ms_TaskTemplate;
    private static OntologicalClass ms_Thing;

    /**
     * Every individual in the OWL world is a member of the class <code>owl:Thing</code>.
     * Every ontological class of the OpenTox ontology is a subclass of <code>owl:Thing</code>.
     * @return
     *      Universal class <code>owl:Thing</code>.
     */
    public static OntologicalClass Thing() {
        if (ms_Thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            ms_Thing = clazz;
        }
        return ms_Thing;
    }

    public static OntologicalClass HTTPMethod() {
        if (ms_HTTPMethod == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPMethod", NS);
            clazz.getSuperClasses().add(Thing());
            ms_HTTPMethod = clazz;
        }
        return ms_HTTPMethod;
    }

    public static OntologicalClass HTTPStatus() {
        if (ms_HTTPStatus == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPStatus", NS);
            clazz.getSuperClasses().add(Thing());
            ms_HTTPStatus = clazz;
        }
        return ms_HTTPStatus;
    }

    public static OntologicalClass Status_200() {
        if (ms_Status_200 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_200", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_200 = clazz;
        }
        return ms_Status_200;
    }

    public static OntologicalClass Status_201() {
        if (ms_Status_201 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_201", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_201 = clazz;
        }
        return ms_Status_201;
    }

    public static OntologicalClass Status_202() {
        if (ms_Status_202 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_202", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_202 = clazz;
        }
        return ms_Status_202;
    }

    public static OntologicalClass Status_303() {
        if (ms_Status_303 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_303", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_303 = clazz;
        }
        return ms_Status_303;
    }

    public static OntologicalClass Status_400() {
        if (ms_Status_400 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_400", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_400 = clazz;
        }
        return ms_Status_400;
    }

    public static OntologicalClass Status_401() {
        if (ms_Status_401 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_401", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_401 = clazz;
        }
        return ms_Status_401;
    }

    public static OntologicalClass Status_403() {
        if (ms_Status_403 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_403", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_403 = clazz;
        }
        return ms_Status_403;
    }

    public static OntologicalClass Status_404() {
        if (ms_Status_404 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_404", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_404 = clazz;
        }
        return ms_Status_404;
    }

    public static OntologicalClass Status_500() {
        if (ms_Status_500 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_500", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_500 = clazz;
        }
        return ms_Status_500;
    }

    public static OntologicalClass Status_502() {
        if (ms_Status_502 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Status_502", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_Status_502 = clazz;
        }
        return ms_Status_502;
    }
}

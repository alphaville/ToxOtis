package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 * Collection of ontological classes needed to define the REST interface operations
 * over a URI that providesa access to web services.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTRestClasses {

    private OTRestClasses() {
    }
    
    public static final String NS = "http://opentox.org/opentox-rest.owl#";
    private static OntologicalClass ms_HTTPMethod;
    private static OntologicalClass ms_HTTPStatus;
    private static OntologicalClass ms_STATUS_200;
    private static OntologicalClass ms_STATUS_201;
    private static OntologicalClass ms_STATUS_202;
    private static OntologicalClass ms_STATUS_303;
    private static OntologicalClass ms_STATUS_400;
    private static OntologicalClass ms_STATUS_401;
    private static OntologicalClass ms_STATUS_403;
    private static OntologicalClass ms_STATUS_404;
    private static OntologicalClass ms_STATUS_500;
    private static OntologicalClass ms_STATUS_502;
    private static OntologicalClass ms_STATUS_503;
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
    private static OntologicalClass ms_mime_weka_arff;
    private static OntologicalClass ms_RESTOperation;
    private static OntologicalClass ms_DELETE_Operation;
    private static OntologicalClass ms_DELETE_Feature;
    private static OntologicalClass ms_DELETE_Model;
    private static OntologicalClass ms_DELETE_Task;
    private static OntologicalClass ms_GET_Operation;
    private static OntologicalClass ms_GET_Algorithm;
    private static OntologicalClass ms_GET_Algorithms;
    private static OntologicalClass ms_GET_Feature;
    private static OntologicalClass ms_GET_Features;
    private static OntologicalClass ms_GET_Model;
    private static OntologicalClass ms_GET_Models;
    private static OntologicalClass ms_GET_Task;
    private static OntologicalClass ms_GET_Tasks;
    private static OntologicalClass ms_OperationAlgorithm;
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
    private static OntologicalClass ms_OperationResultFeature;
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

    public static OntologicalClass STATUS_200() {
        if (ms_STATUS_200 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_200", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_200 = clazz;
        }
        return ms_STATUS_200;
    }

    public static OntologicalClass STATUS_201() {
        if (ms_STATUS_201 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_201", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_201 = clazz;
        }
        return ms_STATUS_201;
    }

    public static OntologicalClass STATUS_202() {
        if (ms_STATUS_202 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_202", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_202 = clazz;
        }
        return ms_STATUS_202;
    }

    public static OntologicalClass STATUS_303() {
        if (ms_STATUS_303 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_303", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_303 = clazz;
        }
        return ms_STATUS_303;
    }

    public static OntologicalClass STATUS_400() {
        if (ms_STATUS_400 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_400", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_400 = clazz;
        }
        return ms_STATUS_400;
    }

    public static OntologicalClass STATUS_401() {
        if (ms_STATUS_401 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_401", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_401 = clazz;
        }
        return ms_STATUS_401;
    }

    public static OntologicalClass STATUS_403() {
        if (ms_STATUS_403 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_403", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_403 = clazz;
        }
        return ms_STATUS_403;
    }

    public static OntologicalClass STATUS_404() {
        if (ms_STATUS_404 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_404", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_404 = clazz;
        }
        return ms_STATUS_404;
    }

    public static OntologicalClass STATUS_500() {
        if (ms_STATUS_500 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_500", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_500 = clazz;
        }
        return ms_STATUS_500;
    }

    public static OntologicalClass STATUS_502() {
        if (ms_STATUS_502 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_502", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_502 = clazz;
        }
        return ms_STATUS_502;
    }

    public static OntologicalClass STATUS_503() {
        if (ms_STATUS_503 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_503", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            ms_STATUS_503 = clazz;
        }
        return ms_STATUS_503;
    }

    public static OntologicalClass InputParameter() {
        if (ms_InputParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameter", NS);
            clazz.getSuperClasses().add(Thing());
            ms_InputParameter = clazz;
        }
        return ms_InputParameter;
    }

    public static OntologicalClass InputParameterCompound() {
        if (ms_InputParameterCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterCompound", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterCompound = clazz;
        }
        return ms_InputParameterCompound;
    }

    public static OntologicalClass InputParameterDataset() {
        if (ms_InputParameterDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDataset", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterDataset = clazz;
        }
        return ms_InputParameterDataset;
    }

    public static OntologicalClass InputParameterDatasetService() {
        if (ms_InputParameterDatasetService == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDatasetService", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterDatasetService = clazz;
        }
        return ms_InputParameterDatasetService;
    }

    public static OntologicalClass InputParameterFeature() {
        if (ms_InputParameterFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterFeature", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterFeature = clazz;
        }
        return ms_InputParameterFeature;
    }

    public static OntologicalClass InputParameterResultDataset() {
        if (ms_InputParameterResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterResultDataset", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterResultDataset = clazz;
        }
        return ms_InputParameterResultDataset;
    }

    public static OntologicalClass InputParameterTarget() {
        if (ms_InputParameterTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterTarget", NS);
            clazz.getSuperClasses().add(InputParameter());
            ms_InputParameterTarget = clazz;
        }
        return ms_InputParameterTarget;
    }

    public static OntologicalClass MediaType() {
        if (ms_MediaType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MediaType", NS);
            clazz.getSuperClasses().add(Thing());
            ms_MediaType = clazz;
        }
        return ms_MediaType;
    }

    public static OntologicalClass mime_chemical_inchi() {
        if (ms_mime_chemical_inchi == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_inchi", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_chemical_inchi = clazz;
        }
        return ms_mime_chemical_inchi;
    }

    public static OntologicalClass mime_chemical_mol() {
        if (ms_mime_chemical_mol == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_mol", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_chemical_mol = clazz;
        }
        return ms_mime_chemical_mol;
    }

    public static OntologicalClass mime_chemical_sdf() {
        if (ms_mime_chemical_sdf == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_sdf", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_chemical_sdf = clazz;
        }
        return ms_mime_chemical_sdf;
    }

    public static OntologicalClass mime_chemical_smiles() {
        if (ms_mime_chemical_smiles == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_smiles", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_chemical_smiles = clazz;
        }
        return ms_mime_chemical_smiles;
    }

    public static OntologicalClass mime_json() {
        if (ms_mime_json == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_json", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_json = clazz;
        }
        return ms_mime_json;
    }

    public static OntologicalClass mime_rdf_json() {
        if (ms_mime_rdf_json == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_json", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_rdf_json = clazz;
        }
        return ms_mime_rdf_json;
    }

    public static OntologicalClass mime_rdf_n3() {
        if (ms_mime_rdf_n3 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_n3", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_rdf_n3 = clazz;
        }
        return ms_mime_rdf_n3;
    }

    public static OntologicalClass mime_rdf_turtle() {
        if (ms_mime_rdf_turtle == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_turtle", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_rdf_turtle = clazz;
        }
        return ms_mime_rdf_turtle;
    }

    public static OntologicalClass mime_rdf_xml() {
        if (ms_mime_rdf_xml == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_xml", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_rdf_xml = clazz;
        }
        return ms_mime_rdf_xml;
    }

    public static OntologicalClass mime_text_csv() {
        if (ms_mime_text_csv == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_csv", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_text_csv = clazz;
        }
        return ms_mime_text_csv;
    }

    public static OntologicalClass mime_text_html() {
        if (ms_mime_text_html == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_html", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_text_html = clazz;
        }
        return ms_mime_text_html;
    }

    public static OntologicalClass mime_text_plain() {
        if (ms_mime_text_plain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_plain", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_text_plain = clazz;
        }
        return ms_mime_text_plain;
    }

    public static OntologicalClass mime_text_uri_list() {
        if (ms_mime_text_uri_list == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_uri_list", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_text_uri_list = clazz;
        }
        return ms_mime_text_uri_list;
    }

    public static OntologicalClass mime_weka_arff() {
        if (ms_mime_weka_arff == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_weka_arff", NS);
            clazz.getSuperClasses().add(MediaType());
            ms_mime_weka_arff = clazz;
        }
        return ms_mime_weka_arff;
    }

    public static OntologicalClass RESTOperation() {
        if (ms_RESTOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTOperation", NS);
            clazz.getSuperClasses().add(Thing());
            ms_RESTOperation = clazz;
        }
        return ms_RESTOperation;
    }

    public static OntologicalClass DELETE_Operation() {
        if (ms_DELETE_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_DELETE_Operation = clazz;
        }
        return ms_DELETE_Operation;
    }

    public static OntologicalClass GET_Operation() {
        if (ms_GET_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_GET_Operation = clazz;
        }
        return ms_GET_Operation;
    }

    public static OntologicalClass OperationAlgorithm() {
        if (ms_OperationAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationAlgorithm", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationAlgorithm = clazz;
        }
        return ms_OperationAlgorithm;
    }

    public static OntologicalClass OperationFeature() {
        if (ms_OperationFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationFeature = clazz;
        }
        return ms_OperationFeature;
    }

    public static OntologicalClass OperationInputCompound() {
        if (ms_OperationInputCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputCompound", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationInputCompound = clazz;
        }
        return ms_OperationInputCompound;
    }

    public static OntologicalClass OperationInputDataset() {
        if (ms_OperationInputDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputDataset", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationInputDataset = clazz;
        }
        return ms_OperationInputDataset;
    }

    public static OntologicalClass OperationInputFeature() {
        if (ms_OperationInputFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationInputFeature = clazz;
        }
        return ms_OperationAlgorithm;
    }

    public static OntologicalClass OperationModel() {
        if (ms_OperationModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationModel", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationModel = clazz;
        }
        return ms_OperationModel;
    }

    public static OntologicalClass OperationNoResult() {
        if (ms_OperationNoResult == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationNoResult", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationNoResult = clazz;
        }
        return ms_OperationNoResult;
    }

    public static OntologicalClass OperationResultAlgorithm() {
        if (ms_OperationResultAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultAlgorithm", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationResultAlgorithm = clazz;
        }
        return ms_OperationResultAlgorithm;
    }

    public static OntologicalClass OperationResultDataset() {
        if (ms_OperationResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultDataset", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationResultDataset = clazz;
        }
        return ms_OperationResultDataset;
    }

    public static OntologicalClass OperationResultModel() {
        if (ms_OperationResultModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultModel", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationResultModel = clazz;
        }
        return ms_OperationResultModel;
    }

    public static OntologicalClass OperationResultTask() {
        if (ms_OperationResultTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultTask", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationResultTask = clazz;
        }
        return ms_OperationResultTask;
    }

    public static OntologicalClass OperationResultFeature() {
        if (ms_OperationResultFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationResultFeature = clazz;
        }
        return ms_OperationResultFeature;
    }

    public static OntologicalClass OperationTask() {
        if (ms_OperationTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationTask", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_OperationTask = clazz;
        }
        return ms_OperationTask;
    }

    public static OntologicalClass POST_Operation() {
        if (ms_POST_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_POST_Operation = clazz;
        }
        return ms_POST_Operation;
    }

    public static OntologicalClass PUT_Operation() {
        if (ms_PUT_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_PUT_Operation = clazz;
        }
        return ms_PUT_Operation;
    }

    public static OntologicalClass SingleResourceOp() {
        if (ms_SingleResourceOp == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleResourceOp", NS);
            clazz.getSuperClasses().add(RESTOperation());
            ms_SingleResourceOp = clazz;
        }
        return ms_SingleResourceOp;
    }

    public static OntologicalClass DELETE_Feature() {
        if (ms_DELETE_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Feature", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(DELETE_Operation());
            ms_DELETE_Feature = clazz;
        }
        return ms_DELETE_Feature;
    }

    public static OntologicalClass DELETE_Model() {
        if (ms_DELETE_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Model", NS);
            clazz.getSuperClasses().add(OperationModel());
            clazz.getSuperClasses().add(DELETE_Operation());
            ms_DELETE_Model = clazz;
        }
        return ms_DELETE_Model;
    }

    public static OntologicalClass DELETE_Task() {
        if (ms_DELETE_Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Task", NS);
            clazz.getSuperClasses().add(OperationTask());
            clazz.getSuperClasses().add(DELETE_Operation());
            ms_DELETE_Task = clazz;
        }
        return ms_DELETE_Task;
    }

    public static OntologicalClass GET_Algorithm() {
        if (ms_GET_Algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_GET_Algorithm = clazz;
        }
        return ms_GET_Algorithm;
    }

    public static OntologicalClass GET_Algorithms() {
        if (ms_GET_Algorithms == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultAlgorithm());
            ms_GET_Algorithms = clazz;
        }
        return ms_GET_Algorithms;
    }

    public static OntologicalClass GET_Feature() {
        if (ms_GET_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Feature", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_GET_Feature = clazz;
        }
        return ms_GET_Feature;
    }

    public static OntologicalClass GET_Features() {
        if (ms_GET_Features == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Features", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            ms_GET_Features = clazz;
        }
        return ms_GET_Features;
    }

    public static OntologicalClass GET_Task() {
        if (ms_GET_Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Task", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_GET_Task = clazz;
        }
        return ms_GET_Task;
    }

    public static OntologicalClass GET_Tasks() {
        if (ms_GET_Tasks == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Tasks", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            ms_GET_Tasks = clazz;
        }
        return ms_GET_Tasks;
    }

    public static OntologicalClass POST_AlgorithmCreateModel() {
        if (ms_POST_AlgorithmCreateModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmCreateModel", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultModel());
            clazz.getSuperClasses().add(OperationInputDataset());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_POST_AlgorithmCreateModel = clazz;
        }
        return ms_POST_AlgorithmCreateModel;
    }

    public static OntologicalClass POST_AlgorithmProcessCompound() {
        if (ms_POST_AlgorithmProcessCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessCompound", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultDataset());
            clazz.getSuperClasses().add(OperationInputCompound());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_POST_AlgorithmProcessCompound = clazz;
        }
        return ms_POST_AlgorithmProcessCompound;
    }

    public static OntologicalClass POST_AlgorithmProcessDataset() {
        if (ms_POST_AlgorithmProcessDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessDataset", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultDataset());
            clazz.getSuperClasses().add(OperationInputCompound());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_POST_AlgorithmProcessDataset = clazz;
        }
        return ms_POST_AlgorithmProcessDataset;
    }

    public static OntologicalClass POST_Feature() {
        if (ms_POST_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Feature", NS);
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationFeature());
            ms_POST_Feature = clazz;
        }
        return ms_POST_Feature;
    }

    public static OntologicalClass PUT_Feature() {
        if (ms_PUT_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Feature", NS);
            clazz.getSuperClasses().add(PUT_Operation());
            clazz.getSuperClasses().add(OperationFeature());
            ms_PUT_Feature = clazz;
        }
        return ms_PUT_Feature;
    }

    public static OntologicalClass GET_Model() {
        if (ms_GET_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Model", NS);
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationModel());
            clazz.getSuperClasses().add(OperationResultModel());
            clazz.getSuperClasses().add(SingleResourceOp());
            ms_GET_Model = clazz;
        }
        return ms_GET_Model;
    }

    public static OntologicalClass GET_Models() {
        if (ms_GET_Models == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Models", NS) {

                @Override
                public OntClass inModel(OntModel model) {
                    OntClass c = super.inModel(model);
                    HasValueRestriction hvr = model.createHasValueRestriction(null, OTRestObjectProperties.result().asObjectProperty(model), c);
                    hvr.setHasValue(OTClasses.Model().inModel(model));
                    return c;
                }
            };
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationModel());
            clazz.getSuperClasses().add(OperationResultModel());
            ms_GET_Models = clazz;
        }
        return ms_GET_Models;
    }

    public static OntologicalClass RESTTemplate() {
        if (ms_RESTTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplate", NS);
            clazz.getSuperClasses().add(Thing());
            ms_RESTTemplate = clazz;
        }
        return ms_RESTTemplate;
    }

    public static OntologicalClass AlgorithmTemplate() {
        if (ms_AlgorithmTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            ms_AlgorithmTemplate = clazz;
        }
        return ms_AlgorithmTemplate;
    }

    public static OntologicalClass RESTTemplateAttribute() {
        if (ms_RESTTemplateAttribute == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplateAttribute", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            ms_RESTTemplateAttribute = clazz;
        }
        return ms_RESTTemplateAttribute;
    }

    public static OntologicalClass MultiTaskTemplate() {
        if (ms_MultiTaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiTaskTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplateAttribute());
            ms_MultiTaskTemplate = clazz;
        }
        return ms_MultiTaskTemplate;
    }

    public static OntologicalClass TaskTemplate() {
        if (ms_TaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("TaskTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            ms_TaskTemplate = clazz;
        }
        return ms_TaskTemplate;
    }

    public static OntologicalClass POST_Model() {
        if (ms_POST_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Model", NS);
            clazz.getSuperClasses().add(POST_Operation());
            ms_POST_Model = clazz;
        }
        return ms_POST_Model;
    }
}



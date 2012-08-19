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

import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * Collection of ontological classes needed to define the REST interface operations
 * over a URI that provides access to web services.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class OTRestClasses {

    private OTRestClasses() {
    }
    /**
     * NameSpace of the OpenTox ontology.
     */
    public static final String NS = "http://opentox.org/opentox-rest.owl#";
    private static OntologicalClass msHTTPMethod;
    private static OntologicalClass msHTTPStatus;
    private static OntologicalClass msSTATUS_200;
    private static OntologicalClass msSTATUS_201;
    private static OntologicalClass msSTATUS_202;
    private static OntologicalClass msSTATUS_303;
    private static OntologicalClass msSTATUS_400;
    private static OntologicalClass msSTATUS_401;
    private static OntologicalClass msSTATUS_403;
    private static OntologicalClass msSTATUS_404;
    private static OntologicalClass msSTATUS_500;
    private static OntologicalClass msSTATUS_502;
    private static OntologicalClass msSTATUS_503;
    private static OntologicalClass msInputParameter;
    private static OntologicalClass msInputParameterOpenTox;
    private static OntologicalClass msInputParameterSimple;
    private static OntologicalClass msInputParameterCompound;
    private static OntologicalClass msInputParameterDataset;
    private static OntologicalClass msInputParameterDatasetService;
    private static OntologicalClass msInputParameterFeature;
    private static OntologicalClass msInputParameterResultDataset;
    private static OntologicalClass msInputParameterTarget;
    private static OntologicalClass msHeader;
    private static OntologicalClass msAA;
    private static OntologicalClass msMediaType;
    private static OntologicalClass msmime_chemical_inchi;
    private static OntologicalClass msmime_chemical_mol;
    private static OntologicalClass msmime_chemical_sdf;
    private static OntologicalClass msmime_chemical_smiles;
    private static OntologicalClass msmime_json;
    private static OntologicalClass msmime_rdf_json;
    private static OntologicalClass msmime_rdf_n3;
    private static OntologicalClass msmime_rdf_turtle;
    private static OntologicalClass msmime_rdf_xml;
    private static OntologicalClass msmime_text_csv;
    private static OntologicalClass msmime_text_html;
    private static OntologicalClass msmime_text_plain;
    private static OntologicalClass msmime_text_uri_list;
    private static OntologicalClass msmime_weka_arff;
    private static OntologicalClass msRESTOperation;
    private static OntologicalClass msURLParameter;
    private static OntologicalClass msDELETE_Operation;
    private static OntologicalClass msDELETE_Feature;
    private static OntologicalClass msDELETE_Model;
    private static OntologicalClass msDELETE_Task;
    private static OntologicalClass msGET_Operation;
    private static OntologicalClass msGET_Algorithm;
    private static OntologicalClass msGET_Algorithms;
    private static OntologicalClass msGET_Feature;
    private static OntologicalClass msGET_Features;
    private static OntologicalClass msGET_Model;
    private static OntologicalClass msGET_Models;
    private static OntologicalClass msGET_Task;
    private static OntologicalClass msGET_Tasks;
    private static OntologicalClass msOperationAlgorithm;
    private static OntologicalClass msPOST_AlgorithmCreateModel;
    private static OntologicalClass msPOST_AlgorithmProcessCompound;
    private static OntologicalClass msPOST_AlgorithmProcessDataset;
    private static OntologicalClass msOperationFeature;
    private static OntologicalClass msPOST_Feature;
    private static OntologicalClass msPUT_Feature;
    private static OntologicalClass msOperationInputCompound;
    private static OntologicalClass msOperationInputDataset;
    private static OntologicalClass msOperationInputFeature;
    private static OntologicalClass msOperationModel;
    private static OntologicalClass msPOST_Model;
    private static OntologicalClass msOperationNoResult;
    private static OntologicalClass msOperationResultAlgorithm;
    private static OntologicalClass msOperationResultDataset;
    private static OntologicalClass msOperationResultModel;
    private static OntologicalClass msOperationResultTask;
    private static OntologicalClass msOperationResultFeature;
    private static OntologicalClass msOperationTask;
    private static OntologicalClass msPOST_Operation;
    private static OntologicalClass msPUT_Operation;
    private static OntologicalClass msSingleResourceOp;
    private static OntologicalClass msRESTTemplate;
    private static OntologicalClass msAlgorithmTemplate;
    private static OntologicalClass msRESTTemplateAttribute;
    private static OntologicalClass msMultiTaskTemplate;
    private static OntologicalClass msTaskTemplate;
    private static OntologicalClass msThing;

    /**
     * Every individual in the OWL world is a member of the class <code>owl:Thing</code>.
     * Every ontological class of the OpenTox ontology is a subclass of <code>owl:Thing</code>.
     * @return
     *      Universal class <code>owl:Thing</code>.
     */
    public static OntologicalClass Thing() {
        if (msThing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            msThing = clazz;
        }
        return msThing;
    }

    /**
     * Ontological class for an HTTP Method.
     */
    public static OntologicalClass HTTPMethod() {
        if (msHTTPMethod == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPMethod", NS);
            clazz.getSuperClasses().add(Thing());
            msHTTPMethod = clazz;
        }
        return msHTTPMethod;
    }

    /**
     * Ontological class for an HTTP Status.
     */
    public static OntologicalClass HTTPStatus() {
        if (msHTTPStatus == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPStatus", NS);
            clazz.getSuperClasses().add(Thing());
            msHTTPStatus = clazz;
        }
        return msHTTPStatus;
    }

    /**
     * Ontological class for the HTTP Status 200.
     */
    public static OntologicalClass STATUS_200() {
        if (msSTATUS_200 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_200", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_200 = clazz;
        }
        return msSTATUS_200;
    }

    /**
     * Ontological class for the HTTP Status 201.
     */
    public static OntologicalClass STATUS_201() {
        if (msSTATUS_201 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_201", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_201 = clazz;
        }
        return msSTATUS_201;
    }

    /**
     * Ontological class for the HTTP Status 202.
     */
    public static OntologicalClass STATUS_202() {
        if (msSTATUS_202 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_202", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_202 = clazz;
        }
        return msSTATUS_202;
    }

    /**
     * Ontological class for the HTTP Status 303.
     */
    public static OntologicalClass STATUS_303() {
        if (msSTATUS_303 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_303", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_303 = clazz;
        }
        return msSTATUS_303;
    }

    /**
     * Ontological class for the HTTP Status 400.
     */
    public static OntologicalClass STATUS_400() {
        if (msSTATUS_400 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_400", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_400 = clazz;
        }
        return msSTATUS_400;
    }

    /**
     * Ontological class for the HTTP Status 401.
     */
    public static OntologicalClass STATUS_401() {
        if (msSTATUS_401 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_401", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_401 = clazz;
        }
        return msSTATUS_401;
    }

    /**
     * Ontological class for the HTTP Status 403.
     */
    public static OntologicalClass STATUS_403() {
        if (msSTATUS_403 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_403", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_403 = clazz;
        }
        return msSTATUS_403;
    }

    /**
     * Ontological class for the HTTP Status 404.
     */
    public static OntologicalClass STATUS_404() {
        if (msSTATUS_404 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_404", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_404 = clazz;
        }
        return msSTATUS_404;
    }

    /**
     * Ontological class for the HTTP Status 500.
     */
    public static OntologicalClass STATUS_500() {
        if (msSTATUS_500 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_500", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_500 = clazz;
        }
        return msSTATUS_500;
    }

    /**
     * Ontological class for the HTTP Status 502.
     */
    public static OntologicalClass STATUS_502() {
        if (msSTATUS_502 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_502", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_502 = clazz;
        }
        return msSTATUS_502;
    }

    /**
     * Ontological class for the HTTP Status 503.
     */
    public static OntologicalClass STATUS_503() {
        if (msSTATUS_503 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_503", NS);
            clazz.getSuperClasses().add(HTTPStatus());
            msSTATUS_503 = clazz;
        }
        return msSTATUS_503;
    }

    /**
     * Ontological class for Input Parameters.
     */
    public static OntologicalClass InputParameter() {
        if (msInputParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameter", NS);
            clazz.getSuperClasses().add(Thing());
            msInputParameter = clazz;
        }
        return msInputParameter;
    }

    /**
     * Ontological class for OpenTox-style Input Parameters.
     */
    public static OntologicalClass InputParameterOpenTox() {
        if (msInputParameterOpenTox == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterOpenTox", NS);
            clazz.getSuperClasses().add(InputParameter());
            msInputParameterOpenTox = clazz;
        }
        return msInputParameterOpenTox;
    }

    /**
     * Ontological class for single-valued simple Input Parameters.
     */
    public static OntologicalClass InputParameterSimple() {
        if (msInputParameterSimple == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterSimple", NS);
            clazz.getSuperClasses().add(InputParameter());
            msInputParameterSimple = clazz;
        }
        return msInputParameterSimple;
    }

    /**
     * Ontological class for HTTP Headers.
     */
    public static OntologicalClass Header() {
        if (msHeader == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Header", NS);
            clazz.getSuperClasses().add(InputParameter());
            msHeader = clazz;
        }
        return msHeader;
    }

    /**
     * Ontological class for URL parameters.
     */
    public static OntologicalClass URLParameter() {
        if (msURLParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("URLParameter", NS);
            clazz.getSuperClasses().add(InputParameter());
            msURLParameter = clazz;
        }
        return msURLParameter;
    }

    /**
     * Ontological class for Authentication and Authorization.
     */
    public static OntologicalClass AA() {
        if (msAA == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AA", NS) {

                @Override
                public OntClass inModel(OntModel model) {
                    OntClass c = super.inModel(model);
                    HasValueRestriction hvr = model.createHasValueRestriction(null, OTRestObjectProperties.inputParam().asObjectProperty(model), c);
                    hvr.setHasValue(OTRestClasses.Header().inModel(model));
                    return c;
                }
            };
            clazz.getSuperClasses().add(Thing());
            msAA = clazz;
        }
        return msAA;
    }

    /**
     * Ontological class for Input Parameters that admit compound URIs as values.
     */
    public static OntologicalClass InputParameterCompound() {
        if (msInputParameterCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterCompound", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterCompound = clazz;
        }
        return msInputParameterCompound;
    }

    /**
     * Ontological class for Input Parameters that admit Dataset URIs as values.
     */
    public static OntologicalClass InputParameterDataset() {
        if (msInputParameterDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDataset", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterDataset = clazz;
        }
        return msInputParameterDataset;
    }

    /**
     * Ontological class for Input Parameters that admit dataset base URIs as values and
     * serve as dataset service URIs.
     */
    public static OntologicalClass InputParameterDatasetService() {
        if (msInputParameterDatasetService == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDatasetService", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterDatasetService = clazz;
        }
        return msInputParameterDatasetService;
    }

    /**
     * Ontological class for Input Parameters that admit feature URIs as values.
     */
    public static OntologicalClass InputParameterFeature() {
        if (msInputParameterFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterFeature", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterFeature = clazz;
        }
        return msInputParameterFeature;
    }

    /**
     * Ontological class for Input Parameters that admit (resulting) dataset URIs as values.
     */
    public static OntologicalClass InputParameterResultDataset() {
        if (msInputParameterResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterResultDataset", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterResultDataset = clazz;
        }
        return msInputParameterResultDataset;
    }

    /**
     * Ontological class for Input Parameters that admit target URIs as values.
     */
    public static OntologicalClass InputParameterTarget() {
        if (msInputParameterTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterTarget", NS);
            clazz.getSuperClasses().add(InputParameterOpenTox());
            msInputParameterTarget = clazz;
        }
        return msInputParameterTarget;
    }

    /**
     * Ontological class for MIME types.
     */
    public static OntologicalClass MediaType() {
        if (msMediaType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MediaType", NS);
            clazz.getSuperClasses().add(Thing());
            msMediaType = clazz;
        }
        return msMediaType;
    }

    /**
     * Ontological class for the InChI MIME type.
     */
    public static OntologicalClass mime_chemical_inchi() {
        if (msmime_chemical_inchi == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_inchi", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_chemical_inchi = clazz;
        }
        return msmime_chemical_inchi;
    }

    /**
     * Ontological class for the chemical/MOL MIME type.
     */
    public static OntologicalClass mime_chemical_mol() {
        if (msmime_chemical_mol == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_mol", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_chemical_mol = clazz;
        }
        return msmime_chemical_mol;
    }

    /**
     * Ontological class for the chemical/SDF MIME type.
     */
    public static OntologicalClass mime_chemical_sdf() {
        if (msmime_chemical_sdf == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_sdf", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_chemical_sdf = clazz;
        }
        return msmime_chemical_sdf;
    }

    /**
     * Ontological class for the chemical/SMILES MIME type.
     */
    public static OntologicalClass mime_chemical_smiles() {
        if (msmime_chemical_smiles == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_smiles", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_chemical_smiles = clazz;
        }
        return msmime_chemical_smiles;
    }

    /**
     * Ontological class for the JSON MIME type.
     */
    public static OntologicalClass mime_json() {
        if (msmime_json == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_json", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_json = clazz;
        }
        return msmime_json;
    }

    /**
     * Ontological class for the RDF/JSON MIME type.
     */
    public static OntologicalClass mime_rdf_json() {
        if (msmime_rdf_json == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_json", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_rdf_json = clazz;
        }
        return msmime_rdf_json;
    }

    /**
     * Ontological class for the RDF/N3 MIME type.
     */
    public static OntologicalClass mime_rdf_n3() {
        if (msmime_rdf_n3 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_n3", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_rdf_n3 = clazz;
        }
        return msmime_rdf_n3;
    }

    /**
     * Ontological class for the RDF/Turtle MIME type.
     */
    public static OntologicalClass mime_rdf_turtle() {
        if (msmime_rdf_turtle == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_turtle", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_rdf_turtle = clazz;
        }
        return msmime_rdf_turtle;
    }

    /**
     * Ontological class for the RDF/XML MIME type.
     */
    public static OntologicalClass mime_rdf_xml() {
        if (msmime_rdf_xml == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_xml", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_rdf_xml = clazz;
        }
        return msmime_rdf_xml;
    }

    /**
     * Ontological class for the text/CSV MIME type.
     */
    public static OntologicalClass mime_text_csv() {
        if (msmime_text_csv == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_csv", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_text_csv = clazz;
        }
        return msmime_text_csv;
    }

    /**
     * Ontological class for the text/HTML MIME type.
     */
    public static OntologicalClass mime_text_html() {
        if (msmime_text_html == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_html", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_text_html = clazz;
        }
        return msmime_text_html;
    }

    /**
     * Ontological class for the plain-text MIME type.
     */
    public static OntologicalClass mime_text_plain() {
        if (msmime_text_plain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_plain", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_text_plain = clazz;
        }
        return msmime_text_plain;
    }

    /**
     * Ontological class for the text/URI-list MIME type.
     */
    public static OntologicalClass mime_text_uri_list() {
        if (msmime_text_uri_list == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_uri_list", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_text_uri_list = clazz;
        }
        return msmime_text_uri_list;
    }

    /**
     * Ontological class for the Weka ARFF MIME type.
     */
    public static OntologicalClass mime_weka_arff() {
        if (msmime_weka_arff == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_weka_arff", NS);
            clazz.getSuperClasses().add(MediaType());
            msmime_weka_arff = clazz;
        }
        return msmime_weka_arff;
    }

    /**
     * Ontological class for REST operations (using HTTP methods).
     */
    public static OntologicalClass RESTOperation() {
        if (msRESTOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTOperation", NS);
            clazz.getSuperClasses().add(Thing());
            msRESTOperation = clazz;
        }
        return msRESTOperation;
    }

    /**
     * Ontological class for the DELETE REST operation.
     */
    public static OntologicalClass DELETE_Operation() {
        if (msDELETE_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msDELETE_Operation = clazz;
        }
        return msDELETE_Operation;
    }

    /**
     * Ontological class for the GET REST operation.
     */
    public static OntologicalClass GET_Operation() {
        if (msGET_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msGET_Operation = clazz;
        }
        return msGET_Operation;
    }

    /**
     * An operation on an algorithm.
     */
    public static OntologicalClass OperationAlgorithm() {
        if (msOperationAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationAlgorithm", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationAlgorithm = clazz;
        }
        return msOperationAlgorithm;
    }

    /**
     * An operation on a feature.
     */
    public static OntologicalClass OperationFeature() {
        if (msOperationFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationFeature = clazz;
        }
        return msOperationFeature;
    }

    /**
     * An operation that requires compounds as input.
     */
    public static OntologicalClass OperationInputCompound() {
        if (msOperationInputCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputCompound", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationInputCompound = clazz;
        }
        return msOperationInputCompound;
    }

    /**
     * An operation that requires datasets as input.
     */
    public static OntologicalClass OperationInputDataset() {
        if (msOperationInputDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputDataset", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationInputDataset = clazz;
        }
        return msOperationInputDataset;
    }

    /**
     * An operation that processes features or in any other way uses 
     * features as input.
     */
    public static OntologicalClass OperationInputFeature() {
        if (msOperationInputFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationInputFeature = clazz;
        }
        return msOperationAlgorithm;
    }

    /**
     * Any operation on a Model.
     */
    public static OntologicalClass OperationModel() {
        if (msOperationModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationModel", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationModel = clazz;
        }
        return msOperationModel;
    }

    /**
     * An operation that does not necessarily return some response such
     * as a DELETE operation.
     */
    public static OntologicalClass OperationNoResult() {
        if (msOperationNoResult == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationNoResult", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationNoResult = clazz;
        }
        return msOperationNoResult;
    }

    /**
     * An operation that results in an algorithm that is returned to the client.
     */
    public static OntologicalClass OperationResultAlgorithm() {
        if (msOperationResultAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultAlgorithm", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationResultAlgorithm = clazz;
        }
        return msOperationResultAlgorithm;
    }

    /**
     * An operation that results in a dataset being created.
     */
    public static OntologicalClass OperationResultDataset() {
        if (msOperationResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultDataset", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationResultDataset = clazz;
        }
        return msOperationResultDataset;
    }

    /**
     * An operation that results in a model being created.
     */
    public static OntologicalClass OperationResultModel() {
        if (msOperationResultModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultModel", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationResultModel = clazz;
        }
        return msOperationResultModel;
    }

    /**
     * An operation that results in a task being created.
     */
    public static OntologicalClass OperationResultTask() {
        if (msOperationResultTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultTask", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationResultTask = clazz;
        }
        return msOperationResultTask;
    }

    /**
     * An operation that results in a feature being created.
     */
    public static OntologicalClass OperationResultFeature() {
        if (msOperationResultFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultFeature", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationResultFeature = clazz;
        }
        return msOperationResultFeature;
    }

    /**
     * An operation on a Task resource.
     * @return 
     */
    public static OntologicalClass OperationTask() {
        if (msOperationTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationTask", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msOperationTask = clazz;
        }
        return msOperationTask;
    }

    /**
     * Ontological class for the POST REST operation.
     */
    public static OntologicalClass POST_Operation() {
        if (msPOST_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msPOST_Operation = clazz;
        }
        return msPOST_Operation;
    }

    /**
     * Ontological class for the PUT REST operation.
     */
    public static OntologicalClass PUT_Operation() {
        if (msPUT_Operation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Operation", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msPUT_Operation = clazz;
        }
        return msPUT_Operation;
    }

    /**
     * A simple single-resource operation.
     */
    public static OntologicalClass SingleResourceOp() {
        if (msSingleResourceOp == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleResourceOp", NS);
            clazz.getSuperClasses().add(RESTOperation());
            msSingleResourceOp = clazz;
        }
        return msSingleResourceOp;
    }

    /**
     * Ontological class for and operation that deletes a feature.
     */
    public static OntologicalClass DELETE_Feature() {
        if (msDELETE_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Feature", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(DELETE_Operation());
            msDELETE_Feature = clazz;
        }
        return msDELETE_Feature;
    }

    /**
     * Ontological class for and operation that deletes a model.
     */
    public static OntologicalClass DELETE_Model() {
        if (msDELETE_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Model", NS);
            clazz.getSuperClasses().add(OperationModel());
            clazz.getSuperClasses().add(DELETE_Operation());
            msDELETE_Model = clazz;
        }
        return msDELETE_Model;
    }

    /**
     * Ontological class for and operation that deletes/cancels a task.
     */
    public static OntologicalClass DELETE_Task() {
        if (msDELETE_Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Task", NS);
            clazz.getSuperClasses().add(OperationTask());
            clazz.getSuperClasses().add(DELETE_Operation());
            msDELETE_Task = clazz;
        }
        return msDELETE_Task;
    }

    /**
     * Ontological class for and operation that retrieves meta-data about an algorithm.
     */
    public static OntologicalClass GET_Algorithm() {
        if (msGET_Algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            msGET_Algorithm = clazz;
        }
        return msGET_Algorithm;
    }

    /**
     * Ontological class for and operation that retrieves a list
     * of algorithms.
     */
    public static OntologicalClass GET_Algorithms() {
        if (msGET_Algorithms == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultAlgorithm());
            msGET_Algorithms = clazz;
        }
        return msGET_Algorithms;
    }

    /**
     * Ontological class for any operation that retrieves meta-data about a feature.
     */
    public static OntologicalClass GET_Feature() {
        if (msGET_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Feature", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            clazz.getSuperClasses().add(SingleResourceOp());
            msGET_Feature = clazz;
        }
        return msGET_Feature;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of features.
     */
    public static OntologicalClass GET_Features() {
        if (msGET_Features == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Features", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            msGET_Features = clazz;
        }
        return msGET_Features;
    }

    /**
     * Ontological class for any operation that retrieves meta-data for
     * an asynchronous task.
     */
    public static OntologicalClass GET_Task() {
        if (msGET_Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Task", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(SingleResourceOp());
            msGET_Task = clazz;
        }
        return msGET_Task;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of tasks.
     */
    public static OntologicalClass GET_Tasks() {
        if (msGET_Tasks == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Tasks", NS);
            clazz.getSuperClasses().add(OperationFeature());
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationResultFeature());
            msGET_Tasks = clazz;
        }
        return msGET_Tasks;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * creates a model.
     */
    public static OntologicalClass POST_AlgorithmCreateModel() {
        if (msPOST_AlgorithmCreateModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmCreateModel", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultModel());
            clazz.getSuperClasses().add(OperationInputDataset());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            msPOST_AlgorithmCreateModel = clazz;
        }
        return msPOST_AlgorithmCreateModel;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * processes a compound.
     */
    public static OntologicalClass POST_AlgorithmProcessCompound() {
        if (msPOST_AlgorithmProcessCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessCompound", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultDataset());
            clazz.getSuperClasses().add(OperationInputCompound());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            msPOST_AlgorithmProcessCompound = clazz;
        }
        return msPOST_AlgorithmProcessCompound;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * (pre)processes a dataset.
     */
    public static OntologicalClass POST_AlgorithmProcessDataset() {
        if (msPOST_AlgorithmProcessDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessDataset", NS);
            clazz.getSuperClasses().add(OperationResultTask());
            clazz.getSuperClasses().add(OperationResultDataset());
            clazz.getSuperClasses().add(OperationInputCompound());
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationAlgorithm());
            clazz.getSuperClasses().add(SingleResourceOp());
            msPOST_AlgorithmProcessDataset = clazz;
        }
        return msPOST_AlgorithmProcessDataset;
    }

    /**
     * Ontological class for an POST operation on a feature.
     */
    public static OntologicalClass POST_Feature() {
        if (msPOST_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Feature", NS);
            clazz.getSuperClasses().add(POST_Operation());
            clazz.getSuperClasses().add(OperationFeature());
            msPOST_Feature = clazz;
        }
        return msPOST_Feature;
    }

    /**
     * A PUT operation on a feature.
     */
    public static OntologicalClass PUT_Feature() {
        if (msPUT_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Feature", NS);
            clazz.getSuperClasses().add(PUT_Operation());
            clazz.getSuperClasses().add(OperationFeature());
            msPUT_Feature = clazz;
        }
        return msPUT_Feature;
    }

    /**
     * Any GET operation on a model.
     */
    public static OntologicalClass GET_Model() {
        if (msGET_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Model", NS);
            clazz.getSuperClasses().add(GET_Operation());
            clazz.getSuperClasses().add(OperationModel());
            clazz.getSuperClasses().add(OperationResultModel());
            clazz.getSuperClasses().add(SingleResourceOp());
            msGET_Model = clazz;
        }
        return msGET_Model;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of models.
     */
    public static OntologicalClass GET_Models() {
        if (msGET_Models == null) {
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
            msGET_Models = clazz;
        }
        return msGET_Models;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template.
     */
    public static OntologicalClass RESTTemplate() {
        if (msRESTTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplate", NS);
            clazz.getSuperClasses().add(Thing());
            msRESTTemplate = clazz;
        }
        return msRESTTemplate;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for algorithms.
     */
    public static OntologicalClass AlgorithmTemplate() {
        if (msAlgorithmTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            msAlgorithmTemplate = clazz;
        }
        return msAlgorithmTemplate;
    }

    public static OntologicalClass RESTTemplateAttribute() {
        if (msRESTTemplateAttribute == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplateAttribute", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            msRESTTemplateAttribute = clazz;
        }
        return msRESTTemplateAttribute;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for Multi-Tasks.
     */
    public static OntologicalClass MultiTaskTemplate() {
        if (msMultiTaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiTaskTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplateAttribute());
            msMultiTaskTemplate = clazz;
        }
        return msMultiTaskTemplate;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for tasks.
     */
    public static OntologicalClass TaskTemplate() {
        if (msTaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("TaskTemplate", NS);
            clazz.getSuperClasses().add(RESTTemplate());
            msTaskTemplate = clazz;
        }
        return msTaskTemplate;
    }

    /**
     * Ontological class for an POST operation on a model.
     */
    public static OntologicalClass POST_Model() {
        if (msPOST_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Model", NS);
            clazz.getSuperClasses().add(POST_Operation());
            msPOST_Model = clazz;
        }
        return msPOST_Model;
    }
}

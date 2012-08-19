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
    private static OntologicalClass msSTATUS200;
    private static OntologicalClass msSTATUS201;
    private static OntologicalClass msSTATUS202;
    private static OntologicalClass msSTATUS303;
    private static OntologicalClass msSTATUS400;
    private static OntologicalClass msSTATUS401;
    private static OntologicalClass msSTATUS403;
    private static OntologicalClass msSTATUS404;
    private static OntologicalClass msSTATUS500;
    private static OntologicalClass msSTATUS502;
    private static OntologicalClass msSTATUS503;
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
    private static OntologicalClass msMimeChemicalInchi;
    private static OntologicalClass msMimeChemicalMol;
    private static OntologicalClass msMimeChemicalSdf;
    private static OntologicalClass msMimeChemicalSmiles;
    private static OntologicalClass msMimeJson;
    private static OntologicalClass msMimeRdfJson;
    private static OntologicalClass msMimeRdfN3;
    private static OntologicalClass msMimeRdfTurtle;
    private static OntologicalClass msMimeRdfXml;
    private static OntologicalClass msMimeTextCsv;
    private static OntologicalClass msmimeTextHtml;
    private static OntologicalClass msMimeTextPlain;
    private static OntologicalClass msmimeTextUriList;
    private static OntologicalClass msmimeWekaArff;
    private static OntologicalClass msRESTOperation;
    private static OntologicalClass msURLParameter;
    private static OntologicalClass msDELETEOperation;
    private static OntologicalClass msDELETEFeature;
    private static OntologicalClass msDELETEModel;
    private static OntologicalClass msDELETETask;
    private static OntologicalClass msGETOperation;
    private static OntologicalClass msGETAlgorithm;
    private static OntologicalClass msGETAlgorithms;
    private static OntologicalClass msGETFeature;
    private static OntologicalClass msGETFeatures;
    private static OntologicalClass msGETModel;
    private static OntologicalClass msGETModels;
    private static OntologicalClass msGETTask;
    private static OntologicalClass msGETTasks;
    private static OntologicalClass msOperationAlgorithm;
    private static OntologicalClass msPOSTAlgorithmCreateModel;
    private static OntologicalClass msPOSTAlgorithmProcessCompound;
    private static OntologicalClass msPOSTAlgorithmProcessDataset;
    private static OntologicalClass msOperationFeature;
    private static OntologicalClass msPOSTFeature;
    private static OntologicalClass msPUTFeature;
    private static OntologicalClass msOperationInputCompound;
    private static OntologicalClass msOperationInputDataset;
    private static OntologicalClass msOperationInputFeature;
    private static OntologicalClass msOperationModel;
    private static OntologicalClass msPOSTModel;
    private static OntologicalClass msOperationNoResult;
    private static OntologicalClass msOperationResultAlgorithm;
    private static OntologicalClass msOperationResultDataset;
    private static OntologicalClass msOperationResultModel;
    private static OntologicalClass msOperationResultTask;
    private static OntologicalClass msOperationResultFeature;
    private static OntologicalClass msOperationTask;
    private static OntologicalClass msPOSTOperation;
    private static OntologicalClass msPUTOperation;
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
    public static OntologicalClass thing() {
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
    public static OntologicalClass httpMethod() {
        if (msHTTPMethod == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPMethod", NS);
            clazz.getSuperClasses().add(thing());
            msHTTPMethod = clazz;
        }
        return msHTTPMethod;
    }

    /**
     * Ontological class for an HTTP Status.
     */
    public static OntologicalClass httpStatus() {
        if (msHTTPStatus == null) {
            OntologicalClass clazz = new OntologicalClassImpl("HTTPStatus", NS);
            clazz.getSuperClasses().add(thing());
            msHTTPStatus = clazz;
        }
        return msHTTPStatus;
    }

    /**
     * Ontological class for the HTTP Status 200.
     */
    public static OntologicalClass status200() {
        if (msSTATUS200 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_200", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS200 = clazz;
        }
        return msSTATUS200;
    }

    /**
     * Ontological class for the HTTP Status 201.
     */
    public static OntologicalClass status201() {
        if (msSTATUS201 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_201", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS201 = clazz;
        }
        return msSTATUS201;
    }

    /**
     * Ontological class for the HTTP Status 202.
     */
    public static OntologicalClass status202() {
        if (msSTATUS202 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_202", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS202 = clazz;
        }
        return msSTATUS202;
    }

    /**
     * Ontological class for the HTTP Status 303.
     */
    public static OntologicalClass status303() {
        if (msSTATUS303 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_303", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS303 = clazz;
        }
        return msSTATUS303;
    }

    /**
     * Ontological class for the HTTP Status 400.
     */
    public static OntologicalClass status400() {
        if (msSTATUS400 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_400", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS400 = clazz;
        }
        return msSTATUS400;
    }

    /**
     * Ontological class for the HTTP Status 401.
     */
    public static OntologicalClass status401() {
        if (msSTATUS401 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_401", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS401 = clazz;
        }
        return msSTATUS401;
    }

    /**
     * Ontological class for the HTTP Status 403.
     */
    public static OntologicalClass status403() {
        if (msSTATUS403 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_403", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS403 = clazz;
        }
        return msSTATUS403;
    }

    /**
     * Ontological class for the HTTP Status 404.
     */
    public static OntologicalClass status404() {
        if (msSTATUS404 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_404", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS404 = clazz;
        }
        return msSTATUS404;
    }

    /**
     * Ontological class for the HTTP Status 500.
     */
    public static OntologicalClass status500() {
        if (msSTATUS500 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_500", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS500 = clazz;
        }
        return msSTATUS500;
    }

    /**
     * Ontological class for the HTTP Status 502.
     */
    public static OntologicalClass status502() {
        if (msSTATUS502 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_502", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS502 = clazz;
        }
        return msSTATUS502;
    }

    /**
     * Ontological class for the HTTP Status 503.
     */
    public static OntologicalClass status503() {
        if (msSTATUS503 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("STATUS_503", NS);
            clazz.getSuperClasses().add(httpStatus());
            msSTATUS503 = clazz;
        }
        return msSTATUS503;
    }

    /**
     * Ontological class for Input Parameters.
     */
    public static OntologicalClass inputParameter() {
        if (msInputParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameter", NS);
            clazz.getSuperClasses().add(thing());
            msInputParameter = clazz;
        }
        return msInputParameter;
    }

    /**
     * Ontological class for OpenTox-style Input Parameters.
     */
    public static OntologicalClass inputParameterOpenTox() {
        if (msInputParameterOpenTox == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterOpenTox", NS);
            clazz.getSuperClasses().add(inputParameter());
            msInputParameterOpenTox = clazz;
        }
        return msInputParameterOpenTox;
    }

    /**
     * Ontological class for single-valued simple Input Parameters.
     */
    public static OntologicalClass inputParameterSimple() {
        if (msInputParameterSimple == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterSimple", NS);
            clazz.getSuperClasses().add(inputParameter());
            msInputParameterSimple = clazz;
        }
        return msInputParameterSimple;
    }

    /**
     * Ontological class for HTTP Headers.
     */
    public static OntologicalClass header() {
        if (msHeader == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Header", NS);
            clazz.getSuperClasses().add(inputParameter());
            msHeader = clazz;
        }
        return msHeader;
    }

    /**
     * Ontological class for URL parameters.
     */
    public static OntologicalClass urlParameter() {
        if (msURLParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("URLParameter", NS);
            clazz.getSuperClasses().add(inputParameter());
            msURLParameter = clazz;
        }
        return msURLParameter;
    }

    /**
     * Ontological class for Authentication and Authorization.
     */
    public static OntologicalClass aa() {
        if (msAA == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AA", NS) {

                @Override
                public OntClass inModel(OntModel model) {
                    OntClass c = super.inModel(model);
                    HasValueRestriction hvr = model.createHasValueRestriction(null, OTRestObjectProperties.inputParam().asObjectProperty(model), c);
                    hvr.setHasValue(OTRestClasses.header().inModel(model));
                    return c;
                }
            };
            clazz.getSuperClasses().add(thing());
            msAA = clazz;
        }
        return msAA;
    }

    /**
     * Ontological class for Input Parameters that admit compound URIs as values.
     */
    public static OntologicalClass inputParameterCompound() {
        if (msInputParameterCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterCompound", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterCompound = clazz;
        }
        return msInputParameterCompound;
    }

    /**
     * Ontological class for Input Parameters that admit Dataset URIs as values.
     */
    public static OntologicalClass inputParameterDataset() {
        if (msInputParameterDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDataset", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterDataset = clazz;
        }
        return msInputParameterDataset;
    }

    /**
     * Ontological class for Input Parameters that admit dataset base URIs as values and
     * serve as dataset service URIs.
     */
    public static OntologicalClass inputParameterDatasetService() {
        if (msInputParameterDatasetService == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterDatasetService", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterDatasetService = clazz;
        }
        return msInputParameterDatasetService;
    }

    /**
     * Ontological class for Input Parameters that admit feature URIs as values.
     */
    public static OntologicalClass inputParameterFeature() {
        if (msInputParameterFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterFeature", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterFeature = clazz;
        }
        return msInputParameterFeature;
    }

    /**
     * Ontological class for Input Parameters that admit (resulting) dataset URIs as values.
     */
    public static OntologicalClass inputParameterResultDataset() {
        if (msInputParameterResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterResultDataset", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterResultDataset = clazz;
        }
        return msInputParameterResultDataset;
    }

    /**
     * Ontological class for Input Parameters that admit target URIs as values.
     */
    public static OntologicalClass inputParameterTarget() {
        if (msInputParameterTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("InputParameterTarget", NS);
            clazz.getSuperClasses().add(inputParameterOpenTox());
            msInputParameterTarget = clazz;
        }
        return msInputParameterTarget;
    }

    /**
     * Ontological class for MIME types.
     */
    public static OntologicalClass mediaType() {
        if (msMediaType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MediaType", NS);
            clazz.getSuperClasses().add(thing());
            msMediaType = clazz;
        }
        return msMediaType;
    }

    /**
     * Ontological class for the InChI MIME type.
     */
    public static OntologicalClass mimeChemicalInchi() {
        if (msMimeChemicalInchi == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_inchi", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeChemicalInchi = clazz;
        }
        return msMimeChemicalInchi;
    }

    /**
     * Ontological class for the chemical/MOL MIME type.
     */
    public static OntologicalClass mimeChemicalMol() {
        if (msMimeChemicalMol == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_mol", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeChemicalMol = clazz;
        }
        return msMimeChemicalMol;
    }

    /**
     * Ontological class for the chemical/SDF MIME type.
     */
    public static OntologicalClass mimeChemicalSdf() {
        if (msMimeChemicalSdf == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_sdf", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeChemicalSdf = clazz;
        }
        return msMimeChemicalSdf;
    }

    /**
     * Ontological class for the chemical/SMILES MIME type.
     */
    public static OntologicalClass mimeChemicalSmiles() {
        if (msMimeChemicalSmiles == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_chemical_smiles", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeChemicalSmiles = clazz;
        }
        return msMimeChemicalSmiles;
    }

    /**
     * Ontological class for the JSON MIME type.
     */
    public static OntologicalClass mimeJson() {
        if (msMimeJson == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_json", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeJson = clazz;
        }
        return msMimeJson;
    }

    /**
     * Ontological class for the RDF/JSON MIME type.
     */
    public static OntologicalClass mimeRdfJson() {
        if (msMimeRdfJson == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_json", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeRdfJson = clazz;
        }
        return msMimeRdfJson;
    }

    /**
     * Ontological class for the RDF/N3 MIME type.
     */
    public static OntologicalClass mimeRdfN3() {
        if (msMimeRdfN3 == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_n3", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeRdfN3 = clazz;
        }
        return msMimeRdfN3;
    }

    /**
     * Ontological class for the RDF/Turtle MIME type.
     */
    public static OntologicalClass mimeRdfTurtle() {
        if (msMimeRdfTurtle == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_turtle", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeRdfTurtle = clazz;
        }
        return msMimeRdfTurtle;
    }

    /**
     * Ontological class for the RDF/XML MIME type.
     */
    public static OntologicalClass mimeRdfXml() {
        if (msMimeRdfXml == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_rdf_xml", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeRdfXml = clazz;
        }
        return msMimeRdfXml;
    }

    /**
     * Ontological class for the text/CSV MIME type.
     */
    public static OntologicalClass mimeTextCsv() {
        if (msMimeTextCsv == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_csv", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeTextCsv = clazz;
        }
        return msMimeTextCsv;
    }

    /**
     * Ontological class for the text/HTML MIME type.
     */
    public static OntologicalClass mimeTextHtml() {
        if (msmimeTextHtml == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_html", NS);
            clazz.getSuperClasses().add(mediaType());
            msmimeTextHtml = clazz;
        }
        return msmimeTextHtml;
    }

    /**
     * Ontological class for the plain-text MIME type.
     */
    public static OntologicalClass mimeTextPlain() {
        if (msMimeTextPlain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_plain", NS);
            clazz.getSuperClasses().add(mediaType());
            msMimeTextPlain = clazz;
        }
        return msMimeTextPlain;
    }

    /**
     * Ontological class for the text/URI-list MIME type.
     */
    public static OntologicalClass mimeTextUriList() {
        if (msmimeTextUriList == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_text_uri_list", NS);
            clazz.getSuperClasses().add(mediaType());
            msmimeTextUriList = clazz;
        }
        return msmimeTextUriList;
    }

    /**
     * Ontological class for the Weka ARFF MIME type.
     */
    public static OntologicalClass mimeWekaArff() {
        if (msmimeWekaArff == null) {
            OntologicalClass clazz = new OntologicalClassImpl("mime_weka_arff", NS);
            clazz.getSuperClasses().add(mediaType());
            msmimeWekaArff = clazz;
        }
        return msmimeWekaArff;
    }

    /**
     * Ontological class for REST operations (using HTTP methods).
     */
    public static OntologicalClass restOperation() {
        if (msRESTOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTOperation", NS);
            clazz.getSuperClasses().add(thing());
            msRESTOperation = clazz;
        }
        return msRESTOperation;
    }

    /**
     * Ontological class for the DELETE REST operation.
     */
    public static OntologicalClass deleteOperation() {
        if (msDELETEOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Operation", NS);
            clazz.getSuperClasses().add(restOperation());
            msDELETEOperation = clazz;
        }
        return msDELETEOperation;
    }

    /**
     * Ontological class for the GET REST operation.
     */
    public static OntologicalClass getOperation() {
        if (msGETOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Operation", NS);
            clazz.getSuperClasses().add(restOperation());
            msGETOperation = clazz;
        }
        return msGETOperation;
    }

    /**
     * An operation on an algorithm.
     */
    public static OntologicalClass operationAlgorithm() {
        if (msOperationAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationAlgorithm", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationAlgorithm = clazz;
        }
        return msOperationAlgorithm;
    }

    /**
     * An operation on a feature.
     */
    public static OntologicalClass operationFeature() {
        if (msOperationFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationFeature", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationFeature = clazz;
        }
        return msOperationFeature;
    }

    /**
     * An operation that requires compounds as input.
     */
    public static OntologicalClass operationInputCompound() {
        if (msOperationInputCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputCompound", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationInputCompound = clazz;
        }
        return msOperationInputCompound;
    }

    /**
     * An operation that requires datasets as input.
     */
    public static OntologicalClass operationInputDataset() {
        if (msOperationInputDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputDataset", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationInputDataset = clazz;
        }
        return msOperationInputDataset;
    }

    /**
     * An operation that processes features or in any other way uses 
     * features as input.
     */
    public static OntologicalClass operationInputFeature() {
        if (msOperationInputFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationInputFeature", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationInputFeature = clazz;
        }
        return msOperationAlgorithm;
    }

    /**
     * Any operation on a Model.
     */
    public static OntologicalClass operationModel() {
        if (msOperationModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationModel", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationModel = clazz;
        }
        return msOperationModel;
    }

    /**
     * An operation that does not necessarily return some response such
     * as a DELETE operation.
     */
    public static OntologicalClass operationNoResult() {
        if (msOperationNoResult == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationNoResult", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationNoResult = clazz;
        }
        return msOperationNoResult;
    }

    /**
     * An operation that results in an algorithm that is returned to the client.
     */
    public static OntologicalClass operationResultAlgorithm() {
        if (msOperationResultAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultAlgorithm", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationResultAlgorithm = clazz;
        }
        return msOperationResultAlgorithm;
    }

    /**
     * An operation that results in a dataset being created.
     */
    public static OntologicalClass operationResultDataset() {
        if (msOperationResultDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultDataset", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationResultDataset = clazz;
        }
        return msOperationResultDataset;
    }

    /**
     * An operation that results in a model being created.
     */
    public static OntologicalClass operationResultModel() {
        if (msOperationResultModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultModel", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationResultModel = clazz;
        }
        return msOperationResultModel;
    }

    /**
     * An operation that results in a task being created.
     */
    public static OntologicalClass operationResultTask() {
        if (msOperationResultTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultTask", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationResultTask = clazz;
        }
        return msOperationResultTask;
    }

    /**
     * An operation that results in a feature being created.
     */
    public static OntologicalClass operationResultFeature() {
        if (msOperationResultFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationResultFeature", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationResultFeature = clazz;
        }
        return msOperationResultFeature;
    }

    /**
     * An operation on a Task resource.
     * @return 
     */
    public static OntologicalClass operationTask() {
        if (msOperationTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OperationTask", NS);
            clazz.getSuperClasses().add(restOperation());
            msOperationTask = clazz;
        }
        return msOperationTask;
    }

    /**
     * Ontological class for the POST REST operation.
     */
    public static OntologicalClass postOperation() {
        if (msPOSTOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Operation", NS);
            clazz.getSuperClasses().add(restOperation());
            msPOSTOperation = clazz;
        }
        return msPOSTOperation;
    }

    /**
     * Ontological class for the PUT REST operation.
     */
    public static OntologicalClass putOperation() {
        if (msPUTOperation == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Operation", NS);
            clazz.getSuperClasses().add(restOperation());
            msPUTOperation = clazz;
        }
        return msPUTOperation;
    }

    /**
     * A simple single-resource operation.
     */
    public static OntologicalClass singleResourceOperation() {
        if (msSingleResourceOp == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleResourceOp", NS);
            clazz.getSuperClasses().add(restOperation());
            msSingleResourceOp = clazz;
        }
        return msSingleResourceOp;
    }

    /**
     * Ontological class for and operation that deletes a feature.
     */
    public static OntologicalClass deleteFeature() {
        if (msDELETEFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Feature", NS);
            clazz.getSuperClasses().add(operationFeature());
            clazz.getSuperClasses().add(deleteOperation());
            msDELETEFeature = clazz;
        }
        return msDELETEFeature;
    }

    /**
     * Ontological class for and operation that deletes a model.
     */
    public static OntologicalClass deleteModel() {
        if (msDELETEModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Model", NS);
            clazz.getSuperClasses().add(operationModel());
            clazz.getSuperClasses().add(deleteOperation());
            msDELETEModel = clazz;
        }
        return msDELETEModel;
    }

    /**
     * Ontological class for and operation that deletes/cancels a task.
     */
    public static OntologicalClass deleteTask() {
        if (msDELETETask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DELETE_Task", NS);
            clazz.getSuperClasses().add(operationTask());
            clazz.getSuperClasses().add(deleteOperation());
            msDELETETask = clazz;
        }
        return msDELETETask;
    }

    /**
     * Ontological class for and operation that retrieves meta-data about an algorithm.
     */
    public static OntologicalClass getAlgorithm() {
        if (msGETAlgorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(operationAlgorithm());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultAlgorithm());
            clazz.getSuperClasses().add(singleResourceOperation());
            msGETAlgorithm = clazz;
        }
        return msGETAlgorithm;
    }

    /**
     * Ontological class for and operation that retrieves a list
     * of algorithms.
     */
    public static OntologicalClass getAlgorithms() {
        if (msGETAlgorithms == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Algorithm", NS);
            clazz.getSuperClasses().add(operationAlgorithm());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultAlgorithm());
            msGETAlgorithms = clazz;
        }
        return msGETAlgorithms;
    }

    /**
     * Ontological class for any operation that retrieves meta-data about a feature.
     */
    public static OntologicalClass getFeature() {
        if (msGETFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Feature", NS);
            clazz.getSuperClasses().add(operationFeature());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultFeature());
            clazz.getSuperClasses().add(singleResourceOperation());
            msGETFeature = clazz;
        }
        return msGETFeature;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of features.
     */
    public static OntologicalClass getFeatures() {
        if (msGETFeatures == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Features", NS);
            clazz.getSuperClasses().add(operationFeature());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultFeature());
            msGETFeatures = clazz;
        }
        return msGETFeatures;
    }

    /**
     * Ontological class for any operation that retrieves meta-data for
     * an asynchronous task.
     */
    public static OntologicalClass getTask() {
        if (msGETTask == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Task", NS);
            clazz.getSuperClasses().add(operationFeature());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultTask());
            clazz.getSuperClasses().add(singleResourceOperation());
            msGETTask = clazz;
        }
        return msGETTask;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of tasks.
     */
    public static OntologicalClass getTasks() {
        if (msGETTasks == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Tasks", NS);
            clazz.getSuperClasses().add(operationFeature());
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationResultFeature());
            msGETTasks = clazz;
        }
        return msGETTasks;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * creates a model.
     */
    public static OntologicalClass postAlgorithmCreateModel() {
        if (msPOSTAlgorithmCreateModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmCreateModel", NS);
            clazz.getSuperClasses().add(operationResultTask());
            clazz.getSuperClasses().add(operationResultModel());
            clazz.getSuperClasses().add(operationInputDataset());
            clazz.getSuperClasses().add(postOperation());
            clazz.getSuperClasses().add(operationAlgorithm());
            clazz.getSuperClasses().add(singleResourceOperation());
            msPOSTAlgorithmCreateModel = clazz;
        }
        return msPOSTAlgorithmCreateModel;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * processes a compound.
     */
    public static OntologicalClass postAlgorithmProcessCompound() {
        if (msPOSTAlgorithmProcessCompound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessCompound", NS);
            clazz.getSuperClasses().add(operationResultTask());
            clazz.getSuperClasses().add(operationResultDataset());
            clazz.getSuperClasses().add(operationInputCompound());
            clazz.getSuperClasses().add(postOperation());
            clazz.getSuperClasses().add(operationAlgorithm());
            clazz.getSuperClasses().add(singleResourceOperation());
            msPOSTAlgorithmProcessCompound = clazz;
        }
        return msPOSTAlgorithmProcessCompound;
    }

    /**
     * Ontological class for a POST operation on an algorithm that
     * (pre)processes a dataset.
     */
    public static OntologicalClass postAlgorithmProcessDataset() {
        if (msPOSTAlgorithmProcessDataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_AlgorithmProcessDataset", NS);
            clazz.getSuperClasses().add(operationResultTask());
            clazz.getSuperClasses().add(operationResultDataset());
            clazz.getSuperClasses().add(operationInputCompound());
            clazz.getSuperClasses().add(postOperation());
            clazz.getSuperClasses().add(operationAlgorithm());
            clazz.getSuperClasses().add(singleResourceOperation());
            msPOSTAlgorithmProcessDataset = clazz;
        }
        return msPOSTAlgorithmProcessDataset;
    }

    /**
     * Ontological class for an POST operation on a feature.
     */
    public static OntologicalClass postFeature() {
        if (msPOSTFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Feature", NS);
            clazz.getSuperClasses().add(postOperation());
            clazz.getSuperClasses().add(operationFeature());
            msPOSTFeature = clazz;
        }
        return msPOSTFeature;
    }

    /**
     * A PUT operation on a feature.
     */
    public static OntologicalClass putFeature() {
        if (msPUTFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("PUT_Feature", NS);
            clazz.getSuperClasses().add(putOperation());
            clazz.getSuperClasses().add(operationFeature());
            msPUTFeature = clazz;
        }
        return msPUTFeature;
    }

    /**
     * Any GET operation on a model.
     */
    public static OntologicalClass getModel() {
        if (msGETModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Model", NS);
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationModel());
            clazz.getSuperClasses().add(operationResultModel());
            clazz.getSuperClasses().add(singleResourceOperation());
            msGETModel = clazz;
        }
        return msGETModel;
    }

    /**
     * Ontological class for any operation that retrieves a list
     * of models.
     */
    public static OntologicalClass getModels() {
        if (msGETModels == null) {
            OntologicalClass clazz = new OntologicalClassImpl("GET_Models", NS) {

                @Override
                public OntClass inModel(OntModel model) {
                    OntClass c = super.inModel(model);
                    HasValueRestriction hvr = model.createHasValueRestriction(null, OTRestObjectProperties.result().asObjectProperty(model), c);
                    hvr.setHasValue(OTClasses.model().inModel(model));
                    return c;
                }
            };
            clazz.getSuperClasses().add(getOperation());
            clazz.getSuperClasses().add(operationModel());
            clazz.getSuperClasses().add(operationResultModel());
            msGETModels = clazz;
        }
        return msGETModels;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template.
     */
    public static OntologicalClass restTemplate() {
        if (msRESTTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplate", NS);
            clazz.getSuperClasses().add(thing());
            msRESTTemplate = clazz;
        }
        return msRESTTemplate;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for algorithms.
     */
    public static OntologicalClass algorithmTemplate() {
        if (msAlgorithmTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmTemplate", NS);
            clazz.getSuperClasses().add(restTemplate());
            msAlgorithmTemplate = clazz;
        }
        return msAlgorithmTemplate;
    }

    public static OntologicalClass restTemplateAttribute() {
        if (msRESTTemplateAttribute == null) {
            OntologicalClass clazz = new OntologicalClassImpl("RESTTemplateAttribute", NS);
            clazz.getSuperClasses().add(restTemplate());
            msRESTTemplateAttribute = clazz;
        }
        return msRESTTemplateAttribute;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for Multi-Tasks.
     */
    public static OntologicalClass multiTaskTemplate() {
        if (msMultiTaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiTaskTemplate", NS);
            clazz.getSuperClasses().add(restTemplateAttribute());
            msMultiTaskTemplate = clazz;
        }
        return msMultiTaskTemplate;
    }

    /**
     * An OpenTox APIv1.2-compliant URI-template for tasks.
     */
    public static OntologicalClass taskTemplate() {
        if (msTaskTemplate == null) {
            OntologicalClass clazz = new OntologicalClassImpl("TaskTemplate", NS);
            clazz.getSuperClasses().add(restTemplate());
            msTaskTemplate = clazz;
        }
        return msTaskTemplate;
    }

    /**
     * Ontological class for an POST operation on a model.
     */
    public static OntologicalClass postModel() {
        if (msPOSTModel == null) {
            OntologicalClass clazz = new OntologicalClassImpl("POST_Model", NS);
            clazz.getSuperClasses().add(postOperation());
            msPOSTModel = clazz;
        }
        return msPOSTModel;
    }
}

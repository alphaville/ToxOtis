package org.opentox.toxotis.ontology.collection;

import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTEchaEndpoints {

    private static final String _NS = "http://www.opentox.org/echaEndpoints.owl#%s";
    public static final String NS = String.format(_NS, "");
    private static OntologicalClass ms_Gastrointestinal_absorption;
    private static OntologicalClass ms_Endpoints;
    private static OntologicalClass ms_PhysicoChemicalEffects;
    private static OntologicalClass ms_EnvironmentalFateParameters;
    private static OntologicalClass ms_EcotoxicEffects;
    private static OntologicalClass ms_HumanHealthEffects;
    private static OntologicalClass ms_ToxicoKinetics;
    private static OntologicalClass ms_DNA_Binding;

    public static OntologicalClass Endpoints() {
        if (ms_Endpoints == null) {
            ms_Endpoints = new OntologicalClassImpl("Endpoints", NS);
            ms_Endpoints.getMetaInfo().setTitle("Endpoints");
        }
        return ms_Endpoints;
    }

    public static OntologicalClass PhysicoChemicalEffects() {
        if (ms_PhysicoChemicalEffects == null) {
            ms_PhysicoChemicalEffects = new OntologicalClassImpl("PhysicoChemicalEffects", NS);
            ms_PhysicoChemicalEffects.getMetaInfo().setTitle("Physicochemical effects");
            ms_PhysicoChemicalEffects.getMetaInfo().setIdentifier("1");
            ms_PhysicoChemicalEffects.getSuperClasses().add(Endpoints());
        }
        return ms_PhysicoChemicalEffects;
    }

    public static OntologicalClass EnvironmentalFateParameters() {
        if (ms_EnvironmentalFateParameters == null) {
            ms_EnvironmentalFateParameters = new OntologicalClassImpl("EnvironmentalFateParameters", NS);
            ms_EnvironmentalFateParameters.getMetaInfo().setTitle("Environmental fate parameters");
            ms_EnvironmentalFateParameters.getMetaInfo().setIdentifier("2");
            ms_EnvironmentalFateParameters.getSuperClasses().add(Endpoints());
        }
        return ms_EnvironmentalFateParameters;
    }

    public static OntologicalClass EcotoxicEffects() {
        if (ms_EcotoxicEffects == null) {
            ms_EcotoxicEffects = new OntologicalClassImpl("EcotoxicEffects", NS);
            ms_EcotoxicEffects.getMetaInfo().setTitle("Ecotoxic effects");
            ms_EcotoxicEffects.getMetaInfo().setIdentifier("3");
            ms_EcotoxicEffects.getSuperClasses().add(Endpoints());
        }
        return ms_EcotoxicEffects;
    }

    public static OntologicalClass HumanHealthEffects() {
        if (ms_HumanHealthEffects == null) {
            ms_HumanHealthEffects = new OntologicalClassImpl("HumanHealthEffects", NS);
            ms_HumanHealthEffects.getMetaInfo().setTitle("Human Health effects");
            ms_HumanHealthEffects.getMetaInfo().setIdentifier("4");
            ms_HumanHealthEffects.getSuperClasses().add(Endpoints());
        }
        return ms_HumanHealthEffects;
    }

    public static OntologicalClass ToxicoKinetics() {
        if (ms_ToxicoKinetics == null) {
            ms_ToxicoKinetics = new OntologicalClassImpl("ToxicoKinetics", NS);
            ms_ToxicoKinetics.getMetaInfo().setTitle("ToxicoKinetics");
            ms_ToxicoKinetics.getMetaInfo().setIdentifier("4.5");
            ms_ToxicoKinetics.getSuperClasses().add(Endpoints());
        }
        return ms_ToxicoKinetics;
    }

    /**
     * Gastrointestinal absorption
     * @return
     */
    public static OntologicalClass Gastrointestinal_absorption() {
        if (ms_Gastrointestinal_absorption == null) {
            ms_Gastrointestinal_absorption = new OntologicalClassImpl("Gastrointestinal_absorption", NS);
            ms_Gastrointestinal_absorption.getSuperClasses().add(ToxicoKinetics());
        }
        return ms_Gastrointestinal_absorption;
    }

    /**
     * DNA-binding
     * @return
     */
    public static OntologicalClass DNA_Binding() {
        if (ms_DNA_Binding == null) {
            ms_DNA_Binding = new OntologicalClassImpl("DNA-binding", NS);
            ms_DNA_Binding.getSuperClasses().add(ToxicoKinetics());
        }
        return ms_DNA_Binding;

    }
}

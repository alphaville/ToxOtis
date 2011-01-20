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

import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * Collection of ECHA endpoints.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTEchaEndpoints {

    private static final String _NS = "http://www.opentox.org/echaEndpoints.owl#%s";
    public static final String NS = String.format(_NS, "");
    private static OntologicalClass ms_Gastrointestinal_absorption;
    private static OntologicalClass ms_Endpoints;

    private static OntologicalClass ms_PhysicoChemicalEffects,
            ms_AirWaterPartitionCoefficient,
            ms_BoilingPoint,
            ms_DissociationConstantPKa,
            ms_MeltingPoint;

    private static OntologicalClass ms_EnvironmentalFateParameters;
    private static OntologicalClass ms_EcotoxicEffects;
    
    private static OntologicalClass ms_HumanHealthEffects,
            ms_AcuteDermalToxicity,
            ms_AcuteInhalationToxicity,
            ms_AcuteOralToxicity,
            ms_AcutePhotoirritation,
            ms_Carcinogenicity,
            ms_EndocrineActivity,
            ms_Other,
            ms_ReceptorBinding,
            ms_ReceptorBindingAndGeneExpression,
            ms_EyeIrritationCorrosion,
            ms_InVitroReproductiveToxicity,
            ms_InVivoPrePeriPostNatalDevelopment,
            ms_InVivoPreNatalDevelopmentToxicity,
            ms_Mutagenicity,
            ms_PhotoCarcinogenicity,
            ms_PhotoMutagenicity,
            ms_PhotoSensitisation,
            ms_RepeatedDoseToxicity,
            ms_RespiratorySensitisation,
            ms_SkinIrritationCorrosion,
            ms_SkinSensitisation;
    
    private static OntologicalClass ms_ToxicoKinetics;
    private static OntologicalClass ms_DNA_Binding;

    public static OntologicalClass Endpoints() {
        if (ms_Endpoints == null) {
            ms_Endpoints = new OntologicalClassImpl("Endpoints", NS);
            ms_Endpoints.getMetaInfo().addTitle("Endpoints");
        }
        return ms_Endpoints;
    }

    public static OntologicalClass PhysicoChemicalEffects() {
        if (ms_PhysicoChemicalEffects == null) {
            ms_PhysicoChemicalEffects = new OntologicalClassImpl("PhysicoChemicalEffects", NS);
            ms_PhysicoChemicalEffects.getMetaInfo().addTitle("Physicochemical effects");
            ms_PhysicoChemicalEffects.getMetaInfo().addIdentifier("1");
            ms_PhysicoChemicalEffects.getSuperClasses().add(Endpoints());
        }
        return ms_PhysicoChemicalEffects;
    }

    /**
     * Air- water partition coefficient (Henry's law constant, H).
     * @return
     *      Ontological Class for <code>otee:Air-water_partition_coefficient_Henry_s_law_constant_H</code>.
     */
    public static OntologicalClass AirWaterPartitionCoefficient() {
        if (ms_AirWaterPartitionCoefficient == null) {
            ms_AirWaterPartitionCoefficient = new OntologicalClassImpl("Air-water_partition_coefficient_Henry_s_law_constant_H", NS);
            ms_AirWaterPartitionCoefficient.getMetaInfo().addTitle("Air- water partition coefficient (Henry's law constant, H)");
            ms_AirWaterPartitionCoefficient.getMetaInfo().addIdentifier("1.9");
            ms_AirWaterPartitionCoefficient.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return ms_PhysicoChemicalEffects;
    }

    public static OntologicalClass BoilingPoint() {
        if (ms_BoilingPoint == null) {
            ms_BoilingPoint = new OntologicalClassImpl("Boiling_point", NS);
            ms_BoilingPoint.getMetaInfo().addTitle("Boiling point");
            ms_BoilingPoint.getMetaInfo().addIdentifier("1.2");
            ms_BoilingPoint.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return ms_BoilingPoint;
    }

    public static OntologicalClass DissociationConstantPKa() {
        if (ms_DissociationConstantPKa == null) {
            ms_DissociationConstantPKa = new OntologicalClassImpl("Dissociation_constant_pKa", NS);
            ms_DissociationConstantPKa.getMetaInfo().addTitle("Dissociation constant (pKa)");
            ms_DissociationConstantPKa.getMetaInfo().addIdentifier("1.10");
            ms_DissociationConstantPKa.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return ms_DissociationConstantPKa;
    }

    public static OntologicalClass MeltingPoint() {
        if (ms_MeltingPoint == null) {
            ms_MeltingPoint = new OntologicalClassImpl("Melting_point", NS);
            ms_MeltingPoint.getMetaInfo().addTitle("Melting point");
            ms_MeltingPoint.getMetaInfo().addIdentifier("1.1");
            ms_MeltingPoint.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return ms_MeltingPoint;
    }


    

    public static OntologicalClass EnvironmentalFateParameters() {
        if (ms_EnvironmentalFateParameters == null) {
            ms_EnvironmentalFateParameters = new OntologicalClassImpl("EnvironmentalFateParameters", NS);
            ms_EnvironmentalFateParameters.getMetaInfo().addTitle("Environmental fate parameters");
            ms_EnvironmentalFateParameters.getMetaInfo().addIdentifier("2");
            ms_EnvironmentalFateParameters.getSuperClasses().add(Endpoints());
        }
        return ms_EnvironmentalFateParameters;
    }

    public static OntologicalClass EcotoxicEffects() {
        if (ms_EcotoxicEffects == null) {
            ms_EcotoxicEffects = new OntologicalClassImpl("EcotoxicEffects", NS);
            ms_EcotoxicEffects.getMetaInfo().addTitle("Ecotoxic effects");
            ms_EcotoxicEffects.getMetaInfo().addIdentifier("3");
            ms_EcotoxicEffects.getSuperClasses().add(Endpoints());
        }
        return ms_EcotoxicEffects;
    }

    public static OntologicalClass HumanHealthEffects() {
        if (ms_HumanHealthEffects == null) {
            ms_HumanHealthEffects = new OntologicalClassImpl("HumanHealthEffects", NS);
            ms_HumanHealthEffects.getMetaInfo().addTitle("Human Health effects");
            ms_HumanHealthEffects.getMetaInfo().addIdentifier("4");
            ms_HumanHealthEffects.getSuperClasses().add(Endpoints());
        }
        return ms_HumanHealthEffects;
    }

    public static OntologicalClass AcuteDermalToxicity() {
        if (ms_AcuteDermalToxicity == null) {
            ms_AcuteDermalToxicity = new OntologicalClassImpl("AcuteDermalToxicity", NS);
            ms_AcuteDermalToxicity.getMetaInfo().addTitle("Acute Dermal Toxicity");
            ms_AcuteDermalToxicity.getMetaInfo().addIdentifier("4.3");
            ms_AcuteDermalToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_AcuteDermalToxicity;
    }

    public static OntologicalClass AcuteInhalationToxicity() {
        if (ms_AcuteInhalationToxicity == null) {
            ms_AcuteInhalationToxicity = new OntologicalClassImpl("AcuteInhalationToxicity", NS);
            ms_AcuteInhalationToxicity.getMetaInfo().addTitle("Acute Inhalation Toxicity");
            ms_AcuteInhalationToxicity.getMetaInfo().addIdentifier("4.1");
            ms_AcuteInhalationToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_AcuteInhalationToxicity;
    }

    public static OntologicalClass AcuteOralToxicity() {
        if (ms_AcuteOralToxicity == null) {
            ms_AcuteOralToxicity = new OntologicalClassImpl("AcuteOralToxicity", NS);
            ms_AcuteOralToxicity.getMetaInfo().addTitle("Acute Oral Toxicity");
            ms_AcuteOralToxicity.getMetaInfo().addIdentifier("4.2");
            ms_AcuteOralToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_AcuteOralToxicity;
    }

    public static OntologicalClass AcutePhotoirritation() {
        if (ms_AcutePhotoirritation == null) {
            ms_AcutePhotoirritation = new OntologicalClassImpl("AcutePhotoirritation", NS);
            ms_AcutePhotoirritation.getMetaInfo().addTitle("Acute Photoirritation");
            ms_AcutePhotoirritation.getMetaInfo().addIdentifier("4.5");
            ms_AcutePhotoirritation.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_AcutePhotoirritation;
    }

    public static OntologicalClass Carcinogenicity() {
        if (ms_Carcinogenicity == null) {
            ms_Carcinogenicity = new OntologicalClassImpl("Carcinogenicity", NS);
            ms_Carcinogenicity.getMetaInfo().addTitle("Carcinogenicity");
            ms_Carcinogenicity.getMetaInfo().addIdentifier("4.12");
            ms_Carcinogenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_Carcinogenicity;
    }

    public static OntologicalClass EndocrineActivity() {
        if (ms_EndocrineActivity == null) {
            ms_EndocrineActivity = new OntologicalClassImpl("EndocrineActivity", NS);
            ms_EndocrineActivity.getMetaInfo().addTitle("Endocrine Activity");
            ms_EndocrineActivity.getMetaInfo().addIdentifier("4.18");
            ms_EndocrineActivity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_EndocrineActivity;
    }

    /**
     * Other (e.g. inhibition of specific enzymes involved in hormone synthesis
     * or regulation, specify enzyme(s) and hormone)
     * @return
     *      Ontological class for <code>otee:Other</code>
     */
    public static OntologicalClass Other(){
        if (ms_Other == null) {
            ms_Other = new OntologicalClassImpl("Other", NS);
            ms_Other.getMetaInfo().addTitle("Other (e.g. inhibition of specific enzymes involved in hormone " +
                    "synthesis or regulation, specify enzyme(s) and hormone)");
            ms_Other.getMetaInfo().addIdentifier("4.18.c");
            ms_Other.getSuperClasses().add(EndocrineActivity());
        }
        return ms_Other;
    }

    public static OntologicalClass ReceptorBinding(){
        if (ms_ReceptorBinding == null) {
            ms_ReceptorBinding = new OntologicalClassImpl("ReceptorBinding", NS);
            ms_ReceptorBinding.getMetaInfo().addTitle("Receptor-binding (specify receptor)");
            ms_ReceptorBinding.getMetaInfo().addIdentifier("4.18.a");
            ms_ReceptorBinding.getSuperClasses().add(EndocrineActivity());
        }
        return ms_ReceptorBinding;
    }

    public static OntologicalClass ReceptorBindingAndGeneExpression(){
        if (ms_ReceptorBindingAndGeneExpression == null) {
            ms_ReceptorBindingAndGeneExpression = new OntologicalClassImpl("ReceptorBindingAndGeneExpression", NS);
            ms_ReceptorBindingAndGeneExpression.getMetaInfo().addTitle("Receptor binding and gene expression (specify receptor)");
            ms_ReceptorBindingAndGeneExpression.getMetaInfo().addIdentifier("4.18.b");
            ms_ReceptorBindingAndGeneExpression.getSuperClasses().add(EndocrineActivity());
        }
        return ms_ReceptorBindingAndGeneExpression;
    }

    public static OntologicalClass EyeIrritationCorrosion(){
        if (ms_EyeIrritationCorrosion == null) {
            ms_EyeIrritationCorrosion = new OntologicalClassImpl("EyeIrritationCorrosion", NS);
            ms_EyeIrritationCorrosion.getMetaInfo().addTitle("Eye irritation/corrosion");
            ms_EyeIrritationCorrosion.getMetaInfo().addIdentifier("4.9");
            ms_EyeIrritationCorrosion.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_EyeIrritationCorrosion;
    }

    public static OntologicalClass InVitroReproductiveToxicity(){
        if (ms_InVitroReproductiveToxicity == null) {
            ms_InVitroReproductiveToxicity = new OntologicalClassImpl("InVitroReproductiveToxicity", NS);
            ms_InVitroReproductiveToxicity.getMetaInfo().addTitle("In vitro reproductive toxicity (e.g. " +
                    "embryotoxic effects in cell culture such as embryo stem cells) ");
            ms_InVitroReproductiveToxicity.getMetaInfo().addIdentifier("4.15");
            ms_InVitroReproductiveToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_InVitroReproductiveToxicity;
    }

    public static OntologicalClass InVivoPrePeriPostNatalDevelopment(){
        if (ms_InVivoPrePeriPostNatalDevelopment == null) {
            ms_InVivoPrePeriPostNatalDevelopment = new OntologicalClassImpl("InVivoPrePeriPostNatalDevelopmentAndOrFertility", NS);
            ms_InVivoPrePeriPostNatalDevelopment.getMetaInfo().addTitle("In vivo pre-, peri-, post natal development and / or " +
                    "fertility (1 or 2 gen. Study or enhanced 1 gen study) ");
            ms_InVivoPrePeriPostNatalDevelopment.getMetaInfo().addIdentifier("4.17");
            ms_InVivoPrePeriPostNatalDevelopment.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_InVivoPrePeriPostNatalDevelopment;
    }

    /**
     * In vivo pre-natal-developmental toxicity
     * @return
     *      Ontological class for <code>otee:InVivoPreNatalDevelopmentToxicity</code>
     */
    public static OntologicalClass InVivoPreNatalDevelopmentToxicity(){
        if (ms_InVivoPreNatalDevelopmentToxicity == null) {
            ms_InVivoPreNatalDevelopmentToxicity = new OntologicalClassImpl("InVivoPreNatalDevelopmentToxicity", NS);
            ms_InVivoPreNatalDevelopmentToxicity.getMetaInfo().addTitle("In vivo pre-natal-developmental toxicity");
            ms_InVivoPreNatalDevelopmentToxicity.getMetaInfo().addIdentifier("4.16");
            ms_InVivoPreNatalDevelopmentToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_InVivoPreNatalDevelopmentToxicity;
    }

    public static OntologicalClass Mutagenicity() {
        if (ms_Mutagenicity == null) {
            ms_Mutagenicity = new OntologicalClassImpl("Mutagenicity", NS);
            ms_Mutagenicity.getMetaInfo().addTitle("Mutagenicity");
            ms_Mutagenicity.getMetaInfo().addIdentifier("4.10");
            ms_Mutagenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_Mutagenicity;
    }

    public static OntologicalClass PhotoCarcinogenicity() {
        if (ms_PhotoCarcinogenicity == null) {
            ms_PhotoCarcinogenicity = new OntologicalClassImpl("PhotoCarcinogenicity", NS);
            ms_PhotoCarcinogenicity.getMetaInfo().addTitle("PhotoCarcinogenicity");
            ms_PhotoCarcinogenicity.getMetaInfo().addIdentifier("4.13");
            ms_PhotoCarcinogenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_PhotoCarcinogenicity;
    }

    public static OntologicalClass PhotoMutagenicity() {
        if (ms_PhotoMutagenicity == null) {
            ms_PhotoMutagenicity = new OntologicalClassImpl("PhotoMutagenicity", NS);
            ms_PhotoMutagenicity.getMetaInfo().addTitle("PhotoMutagenicity");
            ms_PhotoMutagenicity.getMetaInfo().addIdentifier("4.11");
            ms_PhotoMutagenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_PhotoMutagenicity;
    }

    public static OntologicalClass PhotoSensitisation() {
        if (ms_PhotoSensitisation == null) {
            ms_PhotoSensitisation = new OntologicalClassImpl("PhotoSensitisation", NS);
            ms_PhotoSensitisation.getMetaInfo().addTitle("PhotoSensitisation");
            ms_PhotoSensitisation.getMetaInfo().addIdentifier("4.8");
            ms_PhotoSensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_PhotoSensitisation;
    }

    public static OntologicalClass RepeatedDoseToxicity() {
        if (ms_RepeatedDoseToxicity == null) {
            ms_RepeatedDoseToxicity = new OntologicalClassImpl("RepeatedDoseToxicity", NS);
            ms_RepeatedDoseToxicity.getMetaInfo().addTitle("Repeated Dose Toxicity");
            ms_RepeatedDoseToxicity.getMetaInfo().addIdentifier("4.14");
            ms_RepeatedDoseToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_RepeatedDoseToxicity;
    }

    public static OntologicalClass RespiratorySensitisation() {
        if (ms_RespiratorySensitisation == null) {
            ms_RespiratorySensitisation = new OntologicalClassImpl("RespiratorySensitisation", NS);
            ms_RespiratorySensitisation.getMetaInfo().addTitle("Respiratory Sensitisation");
            ms_RespiratorySensitisation.getMetaInfo().addIdentifier("4.7");
            ms_RespiratorySensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_RespiratorySensitisation;
    }

    public static OntologicalClass SkinIrritationCorrosion() {
        if (ms_SkinIrritationCorrosion == null) {
            ms_SkinIrritationCorrosion = new OntologicalClassImpl("SkinIrritationCorrosion", NS);
            ms_SkinIrritationCorrosion.getMetaInfo().addTitle("Skin irritation /corrosion");
            ms_SkinIrritationCorrosion.getMetaInfo().addIdentifier("4.4");
            ms_SkinIrritationCorrosion.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_SkinIrritationCorrosion;
    }

    public static OntologicalClass SkinSensitisation() {
        if (ms_SkinSensitisation == null) {
            ms_SkinSensitisation = new OntologicalClassImpl("SkinSensitisation", NS);
            ms_SkinSensitisation.getMetaInfo().addTitle("Skin Sensitisation");
            ms_SkinSensitisation.getMetaInfo().addIdentifier("4.6");
            ms_SkinSensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return ms_SkinSensitisation;
    }



    public static OntologicalClass ToxicoKinetics() {
        if (ms_ToxicoKinetics == null) {
            ms_ToxicoKinetics = new OntologicalClassImpl("ToxicoKinetics", NS);
            ms_ToxicoKinetics.getMetaInfo().addTitle("ToxicoKinetics");
            ms_ToxicoKinetics.getMetaInfo().addIdentifier("4.5");
            ms_ToxicoKinetics.getSuperClasses().add(Endpoints());
        }
        return ms_ToxicoKinetics;
    }

    /**
     * Gastrointestinal absorption
     * @return
     *      Ontological class corresponding to the Gastrointestinal absorption:
     *      <code>otee:Gastrointestinal_absorption</code>
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
     *      Ontological class corresponding to the DNA binding: <code>otee:DNA-binding</code>
     *
     */
    public static OntologicalClass DNA_Binding() {
        if (ms_DNA_Binding == null) {
            ms_DNA_Binding = new OntologicalClassImpl("DNA-binding", NS);
            ms_DNA_Binding.getSuperClasses().add(ToxicoKinetics());
        }
        return ms_DNA_Binding;

    }
}

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
    private static OntologicalClass msGastrointestinal_absorption;
    private static OntologicalClass msEndpoints;

    private static OntologicalClass msPhysicoChemicalEffects,
            msAirWaterPartitionCoefficient,
            msBoilingPoint,
            msDissociationConstantPKa,
            msMeltingPoint;

    private static OntologicalClass msEnvironmentalFateParameters;
    private static OntologicalClass msEcotoxicEffects;
    
    private static OntologicalClass msHumanHealthEffects,
            msAcuteDermalToxicity,
            msAcuteInhalationToxicity,
            msAcuteOralToxicity,
            msAcutePhotoirritation,
            msCarcinogenicity,
            msEndocrineActivity,
            msOther,
            msReceptorBinding,
            msReceptorBindingAndGeneExpression,
            msEyeIrritationCorrosion,
            msInVitroReproductiveToxicity,
            msInVivoPrePeriPostNatalDevelopment,
            msInVivoPreNatalDevelopmentToxicity,
            msMutagenicity,
            msPhotoCarcinogenicity,
            msPhotoMutagenicity,
            msPhotoSensitisation,
            msRepeatedDoseToxicity,
            msRespiratorySensitisation,
            msSkinIrritationCorrosion,
            msSkinSensitisation;
    
    private static OntologicalClass msToxicoKinetics;
    private static OntologicalClass msDNA_Binding;

    public static OntologicalClass Endpoints() {
        if (msEndpoints == null) {
            msEndpoints = new OntologicalClassImpl("Endpoints", NS);
            msEndpoints.getMetaInfo().addTitle("Endpoints");
        }
        return msEndpoints;
    }

    public static OntologicalClass PhysicoChemicalEffects() {
        if (msPhysicoChemicalEffects == null) {
            msPhysicoChemicalEffects = new OntologicalClassImpl("PhysicoChemicalEffects", NS);
            msPhysicoChemicalEffects.getMetaInfo().addTitle("Physicochemical effects");
            msPhysicoChemicalEffects.getMetaInfo().addIdentifier("1");
            msPhysicoChemicalEffects.getSuperClasses().add(Endpoints());
        }
        return msPhysicoChemicalEffects;
    }

    /**
     * Air- water partition coefficient (Henry's law constant, H).
     * @return
     *      Ontological Class for <code>otee:Air-water_partition_coefficient_Henry_s_law_constant_H</code>.
     */
    public static OntologicalClass AirWaterPartitionCoefficient() {
        if (msAirWaterPartitionCoefficient == null) {
            msAirWaterPartitionCoefficient = new OntologicalClassImpl("Air-water_partition_coefficient_Henry_s_law_constant_H", NS);
            msAirWaterPartitionCoefficient.getMetaInfo().addTitle("Air- water partition coefficient (Henry's law constant, H)");
            msAirWaterPartitionCoefficient.getMetaInfo().addIdentifier("1.9");
            msAirWaterPartitionCoefficient.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return msPhysicoChemicalEffects;
    }

    public static OntologicalClass BoilingPoint() {
        if (msBoilingPoint == null) {
            msBoilingPoint = new OntologicalClassImpl("Boiling_point", NS);
            msBoilingPoint.getMetaInfo().addTitle("Boiling point");
            msBoilingPoint.getMetaInfo().addIdentifier("1.2");
            msBoilingPoint.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return msBoilingPoint;
    }

    public static OntologicalClass DissociationConstantPKa() {
        if (msDissociationConstantPKa == null) {
            msDissociationConstantPKa = new OntologicalClassImpl("Dissociation_constant_pKa", NS);
            msDissociationConstantPKa.getMetaInfo().addTitle("Dissociation constant (pKa)");
            msDissociationConstantPKa.getMetaInfo().addIdentifier("1.10");
            msDissociationConstantPKa.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return msDissociationConstantPKa;
    }

    public static OntologicalClass MeltingPoint() {
        if (msMeltingPoint == null) {
            msMeltingPoint = new OntologicalClassImpl("Melting_point", NS);
            msMeltingPoint.getMetaInfo().addTitle("Melting point");
            msMeltingPoint.getMetaInfo().addIdentifier("1.1");
            msMeltingPoint.getSuperClasses().add(PhysicoChemicalEffects());
        }
        return msMeltingPoint;
    }


    

    public static OntologicalClass EnvironmentalFateParameters() {
        if (msEnvironmentalFateParameters == null) {
            msEnvironmentalFateParameters = new OntologicalClassImpl("EnvironmentalFateParameters", NS);
            msEnvironmentalFateParameters.getMetaInfo().addTitle("Environmental fate parameters");
            msEnvironmentalFateParameters.getMetaInfo().addIdentifier("2");
            msEnvironmentalFateParameters.getSuperClasses().add(Endpoints());
        }
        return msEnvironmentalFateParameters;
    }

    public static OntologicalClass EcotoxicEffects() {
        if (msEcotoxicEffects == null) {
            msEcotoxicEffects = new OntologicalClassImpl("EcotoxicEffects", NS);
            msEcotoxicEffects.getMetaInfo().addTitle("Ecotoxic effects");
            msEcotoxicEffects.getMetaInfo().addIdentifier("3");
            msEcotoxicEffects.getSuperClasses().add(Endpoints());
        }
        return msEcotoxicEffects;
    }

    public static OntologicalClass HumanHealthEffects() {
        if (msHumanHealthEffects == null) {
            msHumanHealthEffects = new OntologicalClassImpl("HumanHealthEffects", NS);
            msHumanHealthEffects.getMetaInfo().addTitle("Human Health effects");
            msHumanHealthEffects.getMetaInfo().addIdentifier("4");
            msHumanHealthEffects.getSuperClasses().add(Endpoints());
        }
        return msHumanHealthEffects;
    }

    public static OntologicalClass AcuteDermalToxicity() {
        if (msAcuteDermalToxicity == null) {
            msAcuteDermalToxicity = new OntologicalClassImpl("AcuteDermalToxicity", NS);
            msAcuteDermalToxicity.getMetaInfo().addTitle("Acute Dermal Toxicity");
            msAcuteDermalToxicity.getMetaInfo().addIdentifier("4.3");
            msAcuteDermalToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msAcuteDermalToxicity;
    }

    public static OntologicalClass AcuteInhalationToxicity() {
        if (msAcuteInhalationToxicity == null) {
            msAcuteInhalationToxicity = new OntologicalClassImpl("AcuteInhalationToxicity", NS);
            msAcuteInhalationToxicity.getMetaInfo().addTitle("Acute Inhalation Toxicity");
            msAcuteInhalationToxicity.getMetaInfo().addIdentifier("4.1");
            msAcuteInhalationToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msAcuteInhalationToxicity;
    }

    public static OntologicalClass AcuteOralToxicity() {
        if (msAcuteOralToxicity == null) {
            msAcuteOralToxicity = new OntologicalClassImpl("AcuteOralToxicity", NS);
            msAcuteOralToxicity.getMetaInfo().addTitle("Acute Oral Toxicity");
            msAcuteOralToxicity.getMetaInfo().addIdentifier("4.2");
            msAcuteOralToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msAcuteOralToxicity;
    }

    public static OntologicalClass AcutePhotoirritation() {
        if (msAcutePhotoirritation == null) {
            msAcutePhotoirritation = new OntologicalClassImpl("AcutePhotoirritation", NS);
            msAcutePhotoirritation.getMetaInfo().addTitle("Acute Photoirritation");
            msAcutePhotoirritation.getMetaInfo().addIdentifier("4.5");
            msAcutePhotoirritation.getSuperClasses().add(HumanHealthEffects());
        }
        return msAcutePhotoirritation;
    }

    public static OntologicalClass Carcinogenicity() {
        if (msCarcinogenicity == null) {
            msCarcinogenicity = new OntologicalClassImpl("Carcinogenicity", NS);
            msCarcinogenicity.getMetaInfo().addTitle("Carcinogenicity");
            msCarcinogenicity.getMetaInfo().addIdentifier("4.12");
            msCarcinogenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msCarcinogenicity;
    }

    public static OntologicalClass EndocrineActivity() {
        if (msEndocrineActivity == null) {
            msEndocrineActivity = new OntologicalClassImpl("EndocrineActivity", NS);
            msEndocrineActivity.getMetaInfo().addTitle("Endocrine Activity");
            msEndocrineActivity.getMetaInfo().addIdentifier("4.18");
            msEndocrineActivity.getSuperClasses().add(HumanHealthEffects());
        }
        return msEndocrineActivity;
    }

    /**
     * Other (e.g. inhibition of specific enzymes involved in hormone synthesis
     * or regulation, specify enzyme(s) and hormone)
     * @return
     *      Ontological class for <code>otee:Other</code>
     */
    public static OntologicalClass Other(){
        if (msOther == null) {
            msOther = new OntologicalClassImpl("Other", NS);
            msOther.getMetaInfo().addTitle("Other (e.g. inhibition of specific enzymes involved in hormone " +
                    "synthesis or regulation, specify enzyme(s) and hormone)");
            msOther.getMetaInfo().addIdentifier("4.18.c");
            msOther.getSuperClasses().add(EndocrineActivity());
        }
        return msOther;
    }

    public static OntologicalClass ReceptorBinding(){
        if (msReceptorBinding == null) {
            msReceptorBinding = new OntologicalClassImpl("ReceptorBinding", NS);
            msReceptorBinding.getMetaInfo().addTitle("Receptor-binding (specify receptor)");
            msReceptorBinding.getMetaInfo().addIdentifier("4.18.a");
            msReceptorBinding.getSuperClasses().add(EndocrineActivity());
        }
        return msReceptorBinding;
    }

    public static OntologicalClass ReceptorBindingAndGeneExpression(){
        if (msReceptorBindingAndGeneExpression == null) {
            msReceptorBindingAndGeneExpression = new OntologicalClassImpl("ReceptorBindingAndGeneExpression", NS);
            msReceptorBindingAndGeneExpression.getMetaInfo().addTitle("Receptor binding and gene expression (specify receptor)");
            msReceptorBindingAndGeneExpression.getMetaInfo().addIdentifier("4.18.b");
            msReceptorBindingAndGeneExpression.getSuperClasses().add(EndocrineActivity());
        }
        return msReceptorBindingAndGeneExpression;
    }

    public static OntologicalClass EyeIrritationCorrosion(){
        if (msEyeIrritationCorrosion == null) {
            msEyeIrritationCorrosion = new OntologicalClassImpl("EyeIrritationCorrosion", NS);
            msEyeIrritationCorrosion.getMetaInfo().addTitle("Eye irritation/corrosion");
            msEyeIrritationCorrosion.getMetaInfo().addIdentifier("4.9");
            msEyeIrritationCorrosion.getSuperClasses().add(HumanHealthEffects());
        }
        return msEyeIrritationCorrosion;
    }

    public static OntologicalClass InVitroReproductiveToxicity(){
        if (msInVitroReproductiveToxicity == null) {
            msInVitroReproductiveToxicity = new OntologicalClassImpl("InVitroReproductiveToxicity", NS);
            msInVitroReproductiveToxicity.getMetaInfo().addTitle("In vitro reproductive toxicity (e.g. " +
                    "embryotoxic effects in cell culture such as embryo stem cells) ");
            msInVitroReproductiveToxicity.getMetaInfo().addIdentifier("4.15");
            msInVitroReproductiveToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msInVitroReproductiveToxicity;
    }

    public static OntologicalClass InVivoPrePeriPostNatalDevelopment(){
        if (msInVivoPrePeriPostNatalDevelopment == null) {
            msInVivoPrePeriPostNatalDevelopment = new OntologicalClassImpl("InVivoPrePeriPostNatalDevelopmentAndOrFertility", NS);
            msInVivoPrePeriPostNatalDevelopment.getMetaInfo().addTitle("In vivo pre-, peri-, post natal development and / or " +
                    "fertility (1 or 2 gen. Study or enhanced 1 gen study) ");
            msInVivoPrePeriPostNatalDevelopment.getMetaInfo().addIdentifier("4.17");
            msInVivoPrePeriPostNatalDevelopment.getSuperClasses().add(HumanHealthEffects());
        }
        return msInVivoPrePeriPostNatalDevelopment;
    }

    /**
     * In vivo pre-natal-developmental toxicity
     * @return
     *      Ontological class for <code>otee:InVivoPreNatalDevelopmentToxicity</code>
     */
    public static OntologicalClass InVivoPreNatalDevelopmentToxicity(){
        if (msInVivoPreNatalDevelopmentToxicity == null) {
            msInVivoPreNatalDevelopmentToxicity = new OntologicalClassImpl("InVivoPreNatalDevelopmentToxicity", NS);
            msInVivoPreNatalDevelopmentToxicity.getMetaInfo().addTitle("In vivo pre-natal-developmental toxicity");
            msInVivoPreNatalDevelopmentToxicity.getMetaInfo().addIdentifier("4.16");
            msInVivoPreNatalDevelopmentToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msInVivoPreNatalDevelopmentToxicity;
    }

    public static OntologicalClass Mutagenicity() {
        if (msMutagenicity == null) {
            msMutagenicity = new OntologicalClassImpl("Mutagenicity", NS);
            msMutagenicity.getMetaInfo().addTitle("Mutagenicity");
            msMutagenicity.getMetaInfo().addIdentifier("4.10");
            msMutagenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msMutagenicity;
    }

    public static OntologicalClass PhotoCarcinogenicity() {
        if (msPhotoCarcinogenicity == null) {
            msPhotoCarcinogenicity = new OntologicalClassImpl("PhotoCarcinogenicity", NS);
            msPhotoCarcinogenicity.getMetaInfo().addTitle("PhotoCarcinogenicity");
            msPhotoCarcinogenicity.getMetaInfo().addIdentifier("4.13");
            msPhotoCarcinogenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msPhotoCarcinogenicity;
    }

    public static OntologicalClass PhotoMutagenicity() {
        if (msPhotoMutagenicity == null) {
            msPhotoMutagenicity = new OntologicalClassImpl("PhotoMutagenicity", NS);
            msPhotoMutagenicity.getMetaInfo().addTitle("PhotoMutagenicity");
            msPhotoMutagenicity.getMetaInfo().addIdentifier("4.11");
            msPhotoMutagenicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msPhotoMutagenicity;
    }

    public static OntologicalClass PhotoSensitisation() {
        if (msPhotoSensitisation == null) {
            msPhotoSensitisation = new OntologicalClassImpl("PhotoSensitisation", NS);
            msPhotoSensitisation.getMetaInfo().addTitle("PhotoSensitisation");
            msPhotoSensitisation.getMetaInfo().addIdentifier("4.8");
            msPhotoSensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return msPhotoSensitisation;
    }

    public static OntologicalClass RepeatedDoseToxicity() {
        if (msRepeatedDoseToxicity == null) {
            msRepeatedDoseToxicity = new OntologicalClassImpl("RepeatedDoseToxicity", NS);
            msRepeatedDoseToxicity.getMetaInfo().addTitle("Repeated Dose Toxicity");
            msRepeatedDoseToxicity.getMetaInfo().addIdentifier("4.14");
            msRepeatedDoseToxicity.getSuperClasses().add(HumanHealthEffects());
        }
        return msRepeatedDoseToxicity;
    }

    public static OntologicalClass RespiratorySensitisation() {
        if (msRespiratorySensitisation == null) {
            msRespiratorySensitisation = new OntologicalClassImpl("RespiratorySensitisation", NS);
            msRespiratorySensitisation.getMetaInfo().addTitle("Respiratory Sensitisation");
            msRespiratorySensitisation.getMetaInfo().addIdentifier("4.7");
            msRespiratorySensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return msRespiratorySensitisation;
    }

    public static OntologicalClass SkinIrritationCorrosion() {
        if (msSkinIrritationCorrosion == null) {
            msSkinIrritationCorrosion = new OntologicalClassImpl("SkinIrritationCorrosion", NS);
            msSkinIrritationCorrosion.getMetaInfo().addTitle("Skin irritation /corrosion");
            msSkinIrritationCorrosion.getMetaInfo().addIdentifier("4.4");
            msSkinIrritationCorrosion.getSuperClasses().add(HumanHealthEffects());
        }
        return msSkinIrritationCorrosion;
    }

    public static OntologicalClass SkinSensitisation() {
        if (msSkinSensitisation == null) {
            msSkinSensitisation = new OntologicalClassImpl("SkinSensitisation", NS);
            msSkinSensitisation.getMetaInfo().addTitle("Skin Sensitisation");
            msSkinSensitisation.getMetaInfo().addIdentifier("4.6");
            msSkinSensitisation.getSuperClasses().add(HumanHealthEffects());
        }
        return msSkinSensitisation;
    }



    public static OntologicalClass ToxicoKinetics() {
        if (msToxicoKinetics == null) {
            msToxicoKinetics = new OntologicalClassImpl("ToxicoKinetics", NS);
            msToxicoKinetics.getMetaInfo().addTitle("ToxicoKinetics");
            msToxicoKinetics.getMetaInfo().addIdentifier("4.5");
            msToxicoKinetics.getSuperClasses().add(Endpoints());
        }
        return msToxicoKinetics;
    }

    /**
     * Gastrointestinal absorption
     * @return
     *      Ontological class corresponding to the Gastrointestinal absorption:
     *      <code>otee:Gastrointestinal_absorption</code>
     */
    public static OntologicalClass Gastrointestinal_absorption() {
        if (msGastrointestinal_absorption == null) {
            msGastrointestinal_absorption = new OntologicalClassImpl("Gastrointestinal_absorption", NS);
            msGastrointestinal_absorption.getSuperClasses().add(ToxicoKinetics());
        }
        return msGastrointestinal_absorption;
    }

    /**
     * DNA-binding
     * @return
     *      Ontological class corresponding to the DNA binding: <code>otee:DNA-binding</code>
     *
     */
    public static OntologicalClass DNA_Binding() {
        if (msDNA_Binding == null) {
            msDNA_Binding = new OntologicalClassImpl("DNA-binding", NS);
            msDNA_Binding.getSuperClasses().add(ToxicoKinetics());
        }
        return msDNA_Binding;

    }
}

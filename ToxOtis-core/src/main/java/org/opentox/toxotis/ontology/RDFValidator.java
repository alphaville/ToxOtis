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
package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author Pantelis Sopasakis
 */
public class RDFValidator {

    private final OntModel ontModel;
    private ValidityReport validityReport;

    public RDFValidator(OntModel ontModel) {
        this.ontModel = ontModel;
    }    

    public void validateLite() {
        validate(OntModelSpec.OWL_LITE_MEM);
    }

    public void validateDL() {
        validate(OntModelSpec.OWL_DL_MEM);
    }

    public void validate(OntModelSpec ontModelSpec) {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        OntModel schema = ModelFactory.createOntologyModel(ontModelSpec);
        reasoner = reasoner.bindSchema(schema);
        InfModel inf = ModelFactory.createInfModel(reasoner, ontModel);
        validityReport = inf.validate();
    }

    public boolean isValid() {
        return validityReport.isValid();
    }

    public boolean isClean() {
        return validityReport.isClean();
    }

    public void printIssues(PrintStream ps) {
        if (validityReport == null) {
            ps.println("No validity report found");
            return;
        }
        ps.println("Validity report:");
        if (validityReport.isValid()) {
            ps.println("No issues");
            return;
        }
        for (Iterator i = validityReport.getReports(); i.hasNext();) {
            ps.println(" - " + i.next());
        }
    }
}

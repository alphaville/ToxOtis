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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDF;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpStatus extends OTComponent<HttpStatus> {

    private OntologicalClass httpStatusClass;

    public HttpStatus(VRI uri) {
        super(uri);
    }

    public HttpStatus() {
    }

    public HttpStatus(OntologicalClass clazz) {
        this.httpStatusClass = clazz;
    }

    public OntologicalClass getHttpStatusClass() {
        return httpStatusClass;
    }

    public void setHttpStatusClass(OntologicalClass httpStatusClass) {
        this.httpStatusClass = httpStatusClass;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String httpStatusUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = model.createIndividual(httpStatusUri, OTRestClasses.HTTPStatus().inModel(model));
        if (getHttpStatusClass()!=null){
            indiv.addProperty(RDF.type, getHttpStatusClass().inModel(model));
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HttpStatus other = (HttpStatus) obj;
        if (this.httpStatusClass != other.httpStatusClass && (this.httpStatusClass == null || !this.httpStatusClass.equals(other.httpStatusClass))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.httpStatusClass != null ? this.httpStatusClass.hashCode() : 0);
        return hash;
    }

    
}

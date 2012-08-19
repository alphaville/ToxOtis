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
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Conformers are compounds with 3D characteristics. Conformers don't play any
 * significant role in ToxOtis till now since all chemical entities are cast
 * as Compounds.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends Compound {    

    public Conformer() {
        super();
        this.meta = null;
    }

    public Conformer(VRI uri) throws ToxOtisException {
        super();
        setUri(uri);
        this.meta = null;
    }

    
    public VRI getCompoundUri() {
        throw new UnsupportedOperationException();
    }

    public String getConformerId(){
        throw new UnsupportedOperationException();
    }        

    @Override
    public Individual asIndividual(OntModel model) {
        String conformerUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(conformerUri, OTClasses.conformer().inModel(model));
        return indiv;
    }

    @Override
    protected Conformer loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

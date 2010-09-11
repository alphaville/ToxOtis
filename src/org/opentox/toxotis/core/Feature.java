package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Iterator;
import java.util.Set;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 * A Feature is an object,representing any kind of property, assigned to a
 * Compound. The feature types are determined via their links to ontologies
 * (Feature ontologies, Decsriptor ontologies, Endpoints ontologies). OpenTox
 * has established an ontology for biological/toxicological and chemical features
 * that is <a href="http://opentox.org/dev/apis/api-1.1/feature_ontology">
 * available online</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature extends OTOnlineResource<Feature> {

    private Set<OntologicalClass> ontologies;
    private String units;

    public Feature() {
        super();
    }

    public Feature(VRI uri) {
        super(uri);
    }

    public Set<OntologicalClass> getOntologies() {
        return ontologies;
    }

    public void setOntologies(Set<OntologicalClass> ontologies) {
        this.ontologies = ontologies;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (uri != null) {
            builder.append("URI...\n");
            builder.append(uri);
            builder.append("\n\n");
        }
        if (meta != null) {
            builder.append("Meta information....\n");
            builder.append(meta);
            builder.append("\n");
        }
        if (!ontologies.isEmpty()) {
            builder.append("Ontological Classes....\n");
            Iterator<OntologicalClass> i = getOntologies().iterator();
            while (i.hasNext()) {
                builder.append(i.next().getUri());
                builder.append("\n");
            }
        }
        if (units != null && !units.isEmpty()) {
            builder.append("Units : " + units);
        }

        return new String(builder);
    }

    public Feature loadFromRemote() throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

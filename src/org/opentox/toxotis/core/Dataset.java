package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.List;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A dataset provides access to chemical compounds and their features
 * (e.g. structural, physical-chemical, biological, toxicological properties)
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Dataset extends OTOnlineResource<Dataset> {

    List<DataEntry> dataEntries;

    public Dataset(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!Dataset.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Dataset uri according to the OpenTox specifications.");
            }
        }
    }

    public Dataset() {
    }

    public Dataset(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    public List<DataEntry> getDataEntries() {
        return dataEntries;
    }

    public void setDataEntries(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String datasetUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(datasetUri, OTClasses.Dataset().inModel(model));
        /* Attach the metadata to the dataset node... */
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        /** */
        if (dataEntries != null && !dataEntries.isEmpty()) {
            ObjectProperty otdataentry = OTObjectProperties.dataEntry().asObjectProperty(model);
            for (DataEntry dataEntry : dataEntries) {
                indiv.addProperty(otdataentry, dataEntry.asIndividual(model));
            }
        }
        return indiv;
    }

    protected Dataset loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

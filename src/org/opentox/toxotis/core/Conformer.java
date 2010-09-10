package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends Compound{

    private VRI conformerUri;
    private MetaInfo conformerMetaInfo;
    private Dataset basicProperties;

    public Dataset getBasicProperties() {
        if(basicProperties == null){
            updateBasicProperties();
        }
        return basicProperties;
    }

    private void updateBasicProperties(){

    }

    public Dataset getProperty(Feature feature) throws ToxOtisException{
        for(DataEntry dataEntry: getBasicProperties().getDataEntries()){
            if(dataEntry.getCompound().equals((Compound)this)){
                for(FeatureValue featureValue : dataEntry.getFeatureValues()){
                    if(featureValue.getFeature().equals(feature)){
                        List<FeatureValue> featureValues = new ArrayList<FeatureValue>();
                        featureValues.add(featureValue);
                        List<DataEntry> dataEntries = new ArrayList<DataEntry>();
                        dataEntries.add(new DataEntry((Compound)this, featureValues));
                        return new Dataset(dataEntries);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public VRI getUri() {
        return conformerUri;
    }

    @Override
    public MetaInfo getMeta() {
        return conformerMetaInfo;
    }

    @Override
    public Compound setMeta(MetaInfo meta) {
        this.conformerMetaInfo = meta;
        return this;
    }

    @Override
    public Compound setUri(VRI uri) {
        this.conformerUri = uri;
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Conformer createFrom(OntModel model) {
        throw new UnsupportedOperationException("");
    }










}
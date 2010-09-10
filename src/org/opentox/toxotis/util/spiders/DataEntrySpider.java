package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.Compound;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.FeatureValue;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DataEntrySpider extends Tarantula<DataEntry>{

    public DataEntrySpider() {
    }

    public DataEntrySpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public DataEntry parse() throws ToxOtisException {
        DataEntry dataEntry = new DataEntry();
        StmtIterator compoundIt = model.listStatements(
                    new SimpleSelector(resource,
                    OTObjectProperties.compound().asObjectProperty(model),
                    (RDFNode)null));
            if(compoundIt.hasNext()){
                Statement compStmt = compoundIt.nextStatement();

                CompoundSpider compoundSpider = new CompoundSpider(model, compStmt.getObject().as(Resource.class).getURI());
                Compound compound = compoundSpider.parse();
                dataEntry.setCompound(compound);
            }
        StmtIterator valuesIt = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.values().asObjectProperty(model),
                (RDFNode)null));
        List<FeatureValue> featureValues = new ArrayList<FeatureValue>();
        while(valuesIt.hasNext()){
            Resource valueResource = valuesIt.nextStatement().getObject().as(Resource.class);
            FeatureValueSpider featureValueSpider = new FeatureValueSpider(valueResource, model);
            featureValues.add(featureValueSpider.parse());
        }
        dataEntry.setFeatureValues(featureValues);
        return dataEntry;
    }

}

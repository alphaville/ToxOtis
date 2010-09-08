package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MetaInfoSpider extends Tarantula {

    public MetaInfoSpider(OntModel model, String uri) {
        super();
        this.model = model;
        resource = model.getResource(uri);
    }

    public MetaInfoSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public MetaInfo parse() {
        MetaInfo dcmeta = new MetaInfoImpl();
        TypedValue temp = null;
        temp = retrieveProp(DC.title);
        dcmeta.setTitle(temp!=null?temp.getValue():null);
        temp =retrieveProp(DC.creator);
        dcmeta.setCreator(temp!=null?temp.getValue():null);
        temp = retrieveProp(DC.description);
        dcmeta.setDescription(temp!=null?temp.getValue():null);
        temp = retrieveProp(DC.publisher);
        dcmeta.setPublisher(temp!=null?temp.getValue():null);
        temp = retrieveProp(RDFS.comment);
        dcmeta.setComment(temp!=null?temp.getValue():null);
        temp = retrieveProp(OWL.versionInfo);
        dcmeta.setVersionInfo(temp!=null?temp.getValue():null);
        temp = retrieveProp(org.opentox.toxotis.ontology.collection.OTObjectProperties.hasSource().asObjectProperty(model));
        dcmeta.setHasSource(temp!=null?temp.getValue():null);
        dcmeta.getContributors().addAll(retrieveProps(DC.contributor));
        temp = retrieveProp(OWL.sameAs);
        dcmeta.setSameAs(temp!=null?temp.getValue():null);
        temp =retrieveProp(RDFS.seeAlso);
        dcmeta.setSeeAlso(temp!=null?temp.getValue():null);
        temp = retrieveProp(DC.identifier);
        dcmeta.setIdentifier(temp!=null?temp.getValue():null);
        //TODO: audiences
        return dcmeta;
    }
}

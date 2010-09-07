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
        dcmeta.setTitle(retrieveProp(DC.title));
        dcmeta.setCreator(retrieveProp(DC.creator));
        dcmeta.setDescription(retrieveProp(DC.description));
        dcmeta.setPublisher(retrieveProp(DC.publisher));
        dcmeta.setComment(retrieveProp(RDFS.comment));
        dcmeta.setVersionInfo(retrieveProp(OWL.versionInfo));
        dcmeta.setHasSource(retrieveProp(org.opentox.toxotis.ontology.collection.OTObjectProperties.hasSource().asObjectProperty(model)));
        dcmeta.getContributors().addAll(retrieveProps(DC.contributor));
        dcmeta.setSameAs(retrieveProp(OWL.sameAs));
        dcmeta.setSeeAlso(retrieveProp(RDFS.seeAlso));
        dcmeta.setIdentifier(retrieveProp(DC.identifier));
        //TODO: audiences
        return dcmeta;
    }
}

package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.Date;
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
        TypedValue<String> temp = null;
        /* title */
        temp = retrieveProp(DC.title);
        dcmeta.setTitle(temp != null ? temp.getValue() : null);
        /* subject */
        temp = retrieveProp(DC.subject);
        dcmeta.setSubject(temp != null ? temp.getValue() : null);
        /* creator */
        temp = retrieveProp(DC.creator);
        dcmeta.setCreator(temp != null ? temp.getValue() : null);
        /* description */
        temp = retrieveProp(DC.description);
        dcmeta.setDescription(temp != null ? temp.getValue() : null);
        /* publidher */
        temp = retrieveProp(DC.publisher);
        dcmeta.setPublisher(temp != null ? temp.getValue() : null);
        /* comment */
        temp = retrieveProp(RDFS.comment);
        dcmeta.setComment(temp != null ? temp.getValue() : null);
        /* version info */
        temp = retrieveProp(OWL.versionInfo);
        dcmeta.setVersionInfo(temp != null ? temp.getValue() : null);
        /* contributors */
        temp = retrieveProp(org.opentox.toxotis.ontology.collection.OTObjectProperties.hasSource().asObjectProperty(model));
        dcmeta.setHasSource(temp != null ? temp.getValue() : null);
        dcmeta.getContributors().addAll(retrieveProps(DC.contributor));
        /* sameAs */
        temp = retrieveProp(OWL.sameAs);
        dcmeta.setSameAs(temp != null ? temp.getValue() : null);
        /* see also... */
        temp = retrieveProp(RDFS.seeAlso);
        dcmeta.setSeeAlso(temp != null ? temp.getValue() : null);
        /* identifier */
        temp = retrieveProp(DC.identifier);
        dcmeta.setIdentifier(temp != null ? temp.getValue() : null);
        /* date */
        TypedValue<Date> date = (TypedValue<Date>) retrieveProp(DC.date);
        dcmeta.setDate(date != null ? date.getValue() : null);
        //TODO: add audiences
        return dcmeta;
    }
}

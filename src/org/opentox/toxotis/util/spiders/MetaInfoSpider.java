package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MetaInfoSpider extends Tarantula<MetaInfo> {

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
        Set<LiteralValue> temp = null;
        Set<ResourceValue> temp2 = null;

        /* title */
        temp = retrievePropertyLiterals(DC.title);
        dcmeta.setTitles(temp);
        /* subject */
        temp = retrievePropertyLiterals(DC.subject);
        dcmeta.setSubjects(temp);
        /* creator */
        temp = retrievePropertyLiterals(DC.creator);
        dcmeta.setCreators(temp);
        /* description */
        temp = retrievePropertyLiterals(DC.description);
        dcmeta.setDescriptions(temp);
        /* publidher */
        temp = retrievePropertyLiterals(DC.publisher);
        dcmeta.setPublishers(temp);
        /* comment */
        temp = retrievePropertyLiterals(RDFS.comment);
        dcmeta.setComments(temp);
        /* contributors */
        temp = retrievePropertyLiterals(DC.contributor);
        dcmeta.setContributors(temp);
        /* identifier */
        temp = retrievePropertyLiterals(DC.identifier);
        dcmeta.setIdentifiers(temp != null ? temp : null);
        /* date */
        temp = retrievePropertyLiterals(DC.date);
        if (temp != null && !temp.isEmpty()) {// date declaration found!
            dcmeta.setDate(temp.iterator().next());
        }


        /* version info */
//        temp = retrieveProp(OWL.versionInfo);
//        dcmeta.setVersionInfo(temp != null ? temp.getValue() : null);


        /* hasSource */
        temp2 = retrievePropertyNodes(org.opentox.toxotis.ontology.collection.OTObjectProperties.hasSource().asObjectProperty(model));
        dcmeta.setHasSources(temp2);
        /* sameAs */
        temp2 = retrievePropertyNodes(OWL.sameAs);
        dcmeta.setSameAs(temp2);
        /* see also... */
        temp2 = retrievePropertyNodes(RDFS.seeAlso);
        dcmeta.setSeeAlso(temp2);


        //TODO: add audiences
        return dcmeta;
    }
}

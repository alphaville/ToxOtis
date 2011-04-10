package org.opentox.toxotis.database.engine;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 * Random Object Generator
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ROG {

    private static final Random RNG = new SecureRandom();

    public ROG() {
        RNG.setSeed(System.currentTimeMillis() * 19 + 71);
    }

    private UUID nextUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public BibTeX nextBibTeX() {
        try {
            BibTeX random = new BibTeX(Services.anonymous().augment("bibtex", nextUuid()));
            random.setAbstract(nextString(255));
            random.setAddress(nextString(255));
            random.setAnnotation(nextString(255));
            random.setAuthor(nextString(255));
            random.setBibType(BibTeX.BIB_TYPE.Article);
            random.setBookTitle(nextString(255));
            random.setChapter(null);
            random.setCopyright(null);
            random.setCreatedBy(User.GUEST);
            random.setCrossref(nextString(255));
            random.setCrossref(null);
            random.setEdition(null);
            random.setEditor(null);
            random.setEnabled(true);
            random.setIsbn(null);
            random.setIssn(null);
            random.setJournal(null);
            random.setKey(null);
            random.setKeywords(null);
            random.setMeta(
                    new MetaInfoImpl().addAudience(nextString(500)).
                    addComment(nextString(500)).
                    addContributor(nextString(100)).
                    addCreator(nextString(500)).
                    addDescription(nextString(1000)).
                    addHasSource(new ResourceValue(Services.opentox().augment("model", RNG.nextInt()), OTClasses.Model())).
                    addIdentifier(random.getUri().toString()).
                    addPublisher(nextString(600)).
                    addRights(nextString(2500)).
                    addSubject(nextString(4000)).
                    addTitle(nextString(8000)).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark",RNG.nextInt()), OTClasses.Compound())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment(nextString(50),RNG.nextLong()), OTClasses.FeatureValueString())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark",RNG.nextInt()), OTClasses.Algorithm()))
                    );

            random.setNumber(RNG.nextInt());
            random.setPages(RNG.nextInt()+" to "+RNG.nextInt());
            random.setSeries(nextString(255));
            random.setTitle(nextString(255));
            random.setUrl(nextString(255));
            random.setVolume(RNG.nextInt());
            random.setYear(RNG.nextInt());
            return random;
        } catch (ToxOtisException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String nextString(int len) {
        String str = new String("#. aF$0b9338nH94&cLdU|K2eHfJgTP8XhiFj61DOk.lNm9n/BoI5pGqYVrs3C tSuMZvwWx4yE7zR");
        StringBuffer sb = new StringBuffer();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(62);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }
}

package org.opentox.toxotis.database.engine;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.core.component.qprf.QprfReportMeta;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
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
    private static final String STRING_SEED = getSeed(255);

    public ROG() {
        RNG.setSeed(System.currentTimeMillis() * 19 + 71);
    }

    private UUID nextUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public User nextUser() {
        User random = new User();
        random.setName(nextString(255));
        try {
            random.setMail(RNG.nextLong() + "@mail.here.org");
        } catch (ToxOtisException ex) {
            throw new RuntimeException(ex);
        }
        random.setHashedPass(nextString(50));
        random.setUid(nextString(234) + "@opensso.in-silico.ch");
        return random;
    }

    public Model nextModel() {
        Model nextModel = new Model(Services.ntua().augment("model", nextUuid()));
        nextModel.setCreatedBy(User.GUEST);
        return nextModel;
    }

    public QprfReport nextReport(int nAuthors) {
        QprfReport random = new QprfReport(Services.anonymous().augment("report", "qprf", nextUuid()));
        QprfReportMeta randomReportMeta = new QprfReportMeta();
        random.setApplicabilityDomainResult(new LiteralValue(RNG.nextBoolean(), XSDDatatype.XSDboolean));
        HashSet<VRI> authrors = new HashSet<VRI>();
        for (int i = 0; i < nAuthors; i++) {
            authrors.add(Services.anonymous().augment("foaf", nextString(80)));
        }
        random.setCreatedBy(User.GUEST);
        random.setAuthors(authrors);
        random.setCompoundUri(Services.anonymous().augment("compound", nextString(80)));
        random.setDatasetStructuralAnalogues(Services.anonymous().augment("dataset", Math.abs(RNG.nextInt())));
        randomReportMeta.setDescriptorDomain(nextString(10000));
        random.setDoaUri(Services.anonymous().augment("model", nextUuid()));
        random.setExperimentalResult(new LiteralValue(RNG.nextFloat(), XSDDatatype.XSDfloat));
        random.setKeywords(nextString(20));
        randomReportMeta.setMechanismDomain(nextString(2000));
        randomReportMeta.setMetabolicDomain(nextString(2000));
        random.setModelUri(Services.anonymous().augment("model", nextUuid()));
        randomReportMeta.setModelVersion(nextString(2000));
        random.setModelDate(System.currentTimeMillis() / 2);
        random.setPredictionResult(new LiteralValue(RNG.nextFloat(), XSDDatatype.XSDfloat));
        randomReportMeta.setQMRFReportDiscussion(nextString(2000));
        random.setQMRFreference(nextString(255));
        random.setReportDate(System.currentTimeMillis());
        randomReportMeta.setSec_3_2_e(nextString(5000));
        randomReportMeta.setSec_3_3_c(nextString(5000));
        randomReportMeta.setSec_3_4(nextString(5000));
        randomReportMeta.setSec_3_5(nextString(5000));
        randomReportMeta.setSec_4_1(nextString(5000));
        randomReportMeta.setSec_4_2(nextString(10000));
        randomReportMeta.setSec_4_3(nextString(10000));
        randomReportMeta.setSec_4_4(nextString(12000));
        randomReportMeta.setStereoFeatures(nextString(2000));
        randomReportMeta.setStructuralDomain(nextString(2000));
        random.setReportMeta(randomReportMeta);
        return random;
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
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark", RNG.nextInt()), OTClasses.Compound())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment(nextString(50), RNG.nextLong()), OTClasses.FeatureValueString())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark", RNG.nextInt()), OTClasses.Algorithm())));
            random.setNumber(RNG.nextInt());
            random.setPages(RNG.nextInt() + " to " + RNG.nextInt());
            random.setSeries(nextString(255));
            random.setTitle(nextString(255));
            random.setUrl(Services.opentox().augment("bibtex2", nextString(100)).toString());
            random.setVolume(RNG.nextInt());
            random.setYear(RNG.nextInt());
            return random;
        } catch (ToxOtisException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getSeed(int len){
        String str = new String("#. aF$0b9338nH94&cLdU|K2eHfJgTP8XhiFj61DOk.lNm9n/BoI5pGqYVrs3C tSuMZvwWx4yE7zR");
        StringBuffer sb = new StringBuffer();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(62);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }

    public String nextString(int len) {
        String str = STRING_SEED;
        StringBuffer sb = new StringBuffer();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(62);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }
}

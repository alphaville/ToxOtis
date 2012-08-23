/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.util;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.NotSerializableException;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.core.component.qprf.QprfReportMeta;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
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
    private static final String STRING_SEED = getSeed(255);
    private static final int SEED_MOD = 19, SEED_OFF = 71,
            ACTOR_LN = 255, DETAILS_LN = 2000,
            ERROR_CODE_LENGTH = 255, HTTP_STATUS_MAX = 50000,
            VRI_LEN = 100, MAIL_LN = 234,
            AUDIENCE_BIG = 255, SMALL_AUDIENCE = 2,
            BIG_COMMENT = 1000, SMALL_COMMENT = 145, SEEDING_FRAC = 62,
            STD_LN = 255, CTRB_LEN = 1050, CREATOR_LN = 120, CREATOR_LN_BIG = 580,
            RIGHTS_LN = 5046, ISBN_LN = 15, KEY_LN = 41, HUGE_LN = 8503,
            PAR0=2031, PAR1=4998, PAR2=10403, PAR3=12000, HTTP_STATUS = 407;

    public ROG() {
        RNG.setSeed(System.currentTimeMillis() * SEED_MOD + SEED_OFF);
    }

    private UUID nextUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public Task.Status nextTaskStatus() {
        int randomChoice = RNG.nextInt(Task.Status.values().length);
        return Task.Status.values()[randomChoice];
    }

    public ErrorReport nextErrorReport(int nTrace) {
        ErrorReport er = new ErrorReport(Services.anonymous().augment("error", nextUuid().toString()));
        er.setActor(nextString(ACTOR_LN));
        er.setDetails(nextString(DETAILS_LN));
        er.setErrorCode(nextString(ERROR_CODE_LENGTH));
        er.setHttpStatus(RNG.nextInt(HTTP_STATUS_MAX));
        if (nTrace >= 2) {
            er.setErrorCause(nextErrorReport(nTrace - 1));
        }
        return er;
    }

    public long nextLong() {
        return RNG.nextLong();
    }

    public int nextInt(int i) {
        return RNG.nextInt(i);
    }

    public int nextInt() {
        return RNG.nextInt();
    }

    public synchronized double nextGaussian() {
        return RNG.nextGaussian();
    }

    public float nextFloat() {
        return RNG.nextFloat();
    }

    public double nextDouble() {
        return RNG.nextDouble();
    }

    public void nextBytes(byte[] bytes) {
        RNG.nextBytes(bytes);
    }

    public boolean nextBoolean() {
        return RNG.nextBoolean();
    }

    public VRI nextVri() {
        return Services.anonymous().augment("rnd", nextString(VRI_LEN));
    }

    public MetaInfo nextMeta() {
        MetaInfo mi = new MetaInfoImpl();
        mi.addAudience(nextString(AUDIENCE_BIG)).addAudience(nextString(SMALL_AUDIENCE)).
                addComment(nextString(SMALL_COMMENT)).addComment(nextString(BIG_COMMENT)).
                addContributor(nextString(CTRB_LEN)).addCreator(nextString(CREATOR_LN)).
                addDescription(nextString(BIG_COMMENT)).addDescription(nextString(BIG_COMMENT)).
                addIdentifier(nextString(SMALL_COMMENT)).addPublisher(nextString(BIG_COMMENT)).
                addRights(nextString(RIGHTS_LN)).addSubject(nextString(SMALL_COMMENT)).
                addSubject(nextString(SMALL_COMMENT)).addSubject(nextString(SMALL_COMMENT)).
                addSubject(nextString(SMALL_COMMENT)).addSubject(nextString(SMALL_COMMENT)).
                addHasSource(new ResourceValue(
                Services.anonymous().augment("x", nextString(VRI_LEN)),
                OTClasses.featureValueNominal())).
                addSameAs(new ResourceValue(
                Services.anonymous().augment("x", nextString(VRI_LEN)),
                null)).
                addSeeAlso(new ResourceValue(
                Services.anonymous().augment("x", nextString(VRI_LEN)),
                OTClasses.compound()));

        return mi;
    }

    public Task nextTask(int nTrace) {
        Task t = new Task(Services.ntua().augment("task", UUID.randomUUID()));
        t.setErrorReport(nextErrorReport(nTrace));
        t.setPercentageCompleted(0);
        t.setHttpStatus(HTTP_STATUS);
        t.setStatus(Task.Status.ERROR);
        t.setMeta(nextMeta());
        t.setResultUri(Services.ntua().augment("model", nextUuid().toString()));
        return t;
    }

    public User nextUser() {
        User random = new User();
        random.setName(nextString(STD_LN));
        try {
            random.setMail(RNG.nextLong() + "@mail.here.org");
        } catch (ToxOtisException ex) {
            throw new IllegalArgumentException(ex);
        }
        random.setHashedPass(nextString(50));
        random.setUid(nextString(MAIL_LN) + "@opensso.in-silico.ch");
        return random;
    }

    public Model nextModel() {
        Model m = new Model(Services.ntua().augment("model", nextUuid()));
        m.setCreatedBy(User.GUEST);
        VRI datasetUri;
        try {
            datasetUri = new VRI("http://otherServer.com:7000/dataset/1");


            VRI f1 = new VRI("http://otherServer.com:7000/feature/1");
            VRI f2 = new VRI("http://otherServer.com:7000/feature/2");
            VRI f3 = new VRI("http://otherServer.com:7000/feature/3");


            Parameter p = new Parameter();
            p.setName("alpha");
            p.setScope(Parameter.ParameterScope.OPTIONAL);
            p.setTypedValue(new LiteralValue(RNG.nextInt(), XSDDatatype.XSDint));
            p.setUri(Services.ntua().augment("parameter", nextUuid()));

            Parameter p2 = new Parameter();
            p2.setName("beta");
            p2.setScope(Parameter.ParameterScope.MANDATORY);
            p2.setTypedValue(new LiteralValue(5, XSDDatatype.XSDint));
            p2.setUri(Services.ntua().augment("parameter", nextUuid()));

            m.setParameters(new HashSet<Parameter>());
            m.getParameters().add(p);
            m.getParameters().add(p2);
            m.setDataset(datasetUri);

            m.setDependentFeatures(new ArrayList<Feature>());
            m.setIndependentFeatures(new ArrayList<Feature>());

            m.getIndependentFeatures().add(new Feature(f1));
            m.getDependentFeatures().add(new Feature(f1));
            m.getDependentFeatures().add(new Feature(f2));
            m.getDependentFeatures().add(new Feature(f3));
            m.setCreatedBy(User.GUEST);
            m.setActualModel(new MetaInfoImpl());// just for the sake to write something in there!
            m.setLocalCode(UUID.randomUUID().toString());
            m.setAlgorithm(new Algorithm("http://algorithm.server.co.uk:9000/algorithm/mlr"));
            m.setMeta(nextMeta());
        } catch (NotSerializableException ex) {
            Logger.getLogger(ROG.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ROG.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }

    public QprfReport nextReport(int nAuthors) {
        QprfReport random = new QprfReport(Services.anonymous().augment("report", "qprf", nextUuid()));
        QprfReportMeta randomReportMeta = new QprfReportMeta();
        random.setApplicabilityDomainResult(nextString(100));
        HashSet<VRI> authrors = new HashSet<VRI>();
        for (int i = 0; i < nAuthors; i++) {
            authrors.add(Services.anonymous().augment("foaf", nextString(VRI_LEN)));
        }
        randomReportMeta.setDescriptorDomain(nextString(PAR2));
        randomReportMeta.setMechanismDomain(nextString(PAR0));
        randomReportMeta.setMetabolicDomain(nextString(PAR0));
        randomReportMeta.setModelVersion(nextString(PAR0));
        random.setModelDate(System.currentTimeMillis() / 2);
        randomReportMeta.setQMRFReportDiscussion(nextString(PAR0));
        random.setQMRFreference(nextString(STD_LN));
        random.setReportDate(System.currentTimeMillis());
        randomReportMeta.setSec32e(nextString(PAR1));
        randomReportMeta.setSec33c(nextString(PAR1));
        randomReportMeta.setSec34(nextString(PAR1));
        randomReportMeta.setSec35(nextString(PAR1));
        randomReportMeta.setSec41(nextString(PAR1));
        randomReportMeta.setSec42(nextString(PAR2));
        randomReportMeta.setSec43(nextString(PAR2));
        randomReportMeta.setSec44(nextString(PAR3));
        randomReportMeta.setStereoFeatures(nextString(PAR0));
        randomReportMeta.setStructuralDomain(nextString(PAR0));
        random.setReportMeta(randomReportMeta);
        return random;
    }

    public BibTeX nextBibTeX() {
        try {
            BibTeX random = new BibTeX(Services.anonymous().augment("bibtex", nextUuid()));
            random.setAbstract(nextString(STD_LN));
            random.setAddress(nextString(STD_LN));
            random.setAnnotation(nextString(STD_LN));
            random.setAuthor(nextString(STD_LN));
            random.setBibType(BibTeX.BibTYPE.Article);
            random.setBookTitle(nextString(STD_LN));
            random.setChapter(nextString(STD_LN));
            random.setCopyright(nextString(STD_LN));
            random.setCreatedBy(User.GUEST);
            random.setCrossref(nextString(STD_LN));
            random.setCrossref(nextString(STD_LN));
            random.setEdition(nextString(STD_LN));
            random.setEditor(nextString(STD_LN));
            random.setEnabled(true);
            random.setIsbn(nextString(ISBN_LN));
            random.setIssn(nextString(ISBN_LN));
            random.setJournal(nextString(STD_LN));
            random.setKey(nextString(KEY_LN));
            random.setKeywords(nextString(STD_LN));
            random.setMeta(
                    new MetaInfoImpl().addAudience(nextString(AUDIENCE_BIG)).
                    addComment(nextString(BIG_COMMENT)).
                    addContributor(nextString(CTRB_LEN)).
                    addCreator(nextString(CREATOR_LN_BIG)).
                    addDescription(nextString(BIG_COMMENT)).
                    addHasSource(new ResourceValue(Services.opentox().augment("model", RNG.nextInt()),
                    OTClasses.model())).
                    addIdentifier(random.getUri().toString()).
                    addPublisher(nextString(BIG_COMMENT)).
                    addRights(nextString(BIG_COMMENT)).
                    addSubject(nextString(BIG_COMMENT)).
                    addTitle(nextString(HUGE_LN)).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark", RNG.nextInt()),
                    OTClasses.compound())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment(nextString(50), RNG.nextLong()),
                    OTClasses.featureValueString())).
                    addSeeAlso(new ResourceValue(Services.anonymous().augment("bookmark", RNG.nextInt()),
                    OTClasses.algorithm())));
            random.setNumber(RNG.nextInt());
            random.setPages(RNG.nextInt() + " to " + RNG.nextInt());
            random.setSeries(nextString(STD_LN));
            random.setTitle(nextString(STD_LN));
            random.setUrl(Services.opentox().augment("bibtex2", nextString(VRI_LEN)).toString());
            random.setVolume(RNG.nextInt());
            random.setYear(RNG.nextInt());
            return random;
        } catch (ToxOtisException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static String getSeed(int len) {
        String str = "#. aF$0b9338nH94&cLdU|K2eHfJgTP8XhiFj61DOk.lNm9n/BoI5pGqYVrs3C tSuMZvwWx4yE7zR";
        StringBuilder sb = new StringBuilder();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(SEEDING_FRAC);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }

    public String nextString(int len) {
        String str = STRING_SEED;
        StringBuilder sb = new StringBuilder();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(SEEDING_FRAC);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }
}
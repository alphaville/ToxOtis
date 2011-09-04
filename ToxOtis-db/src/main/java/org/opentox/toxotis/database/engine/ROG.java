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

package org.opentox.toxotis.database.engine;

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

    public ROG() {
        RNG.setSeed(System.currentTimeMillis() * 19 + 71);
    }

    private UUID nextUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public Task.Status nextTaskStatus() {
        int randomChoice = RNG.nextInt(5) + 1;
        return Task.Status.values()[randomChoice];
    }

    public ErrorReport nextErrorReport(int nTrace) {
        ErrorReport er = new ErrorReport(Services.anonymous().augment("error", nextUuid().toString()));
        er.setActor(nextString(255));
        er.setDetails(nextString(2000));
        er.setErrorCode(nextString(255));
        er.setHttpStatus(RNG.nextInt(50000));
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
        return Services.anonymous().augment("rnd", nextString(100));
    }

    public MetaInfo nextMeta() {
        MetaInfo mi = new MetaInfoImpl();
        mi.addAudience(nextString(255)).addAudience(nextString(2)).
                addComment(nextString(100)).addComment(nextString(1000)).
                addContributor(nextString(1000)).addCreator(nextString(1000)).
                addDescription(nextString(3000)).addDescription(nextString(1000)).
                addIdentifier(nextString(500)).addPublisher(nextString(1000)).
                addRights(nextString(5000)).addSubject(nextString(100)).
                addSubject(nextString(100)).addSubject(nextString(100)).
                addSubject(nextString(100)).addSubject(nextString(100)).
                addHasSource(new ResourceValue(
                Services.anonymous().augment("x", nextString(100)),
                OTClasses.FeatureValueNominal())).
                addSameAs(new ResourceValue(
                Services.anonymous().augment("x", nextString(100)),
                null)).
                addSeeAlso(new ResourceValue(
                Services.anonymous().augment("x", nextString(100)),
                OTClasses.Compound()));

        return mi;
    }

    public Task nextTask(int nTrace) {
        Task t = new Task(Services.ntua().augment("task", UUID.randomUUID()));
        t.setErrorReport(nextErrorReport(nTrace));
        t.setPercentageCompleted(0);
        t.setHttpStatus(407);
        t.setStatus(Task.Status.ERROR);
        t.setMeta(nextMeta());
        t.setResultUri(Services.ntua().augment("model", nextUuid().toString()));
        return t;
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
            authrors.add(Services.anonymous().augment("foaf", nextString(80)));
        }
//        random.setCreatedBy(User.GUEST);
//        random.setAuthors(authrors);
//        random.setCompoundUri(Services.anonymous().augment("compound", nextString(80)));
//        random.setDatasetStructuralAnalogues(Services.anonymous().augment("dataset", Math.abs(RNG.nextInt())));
        randomReportMeta.setDescriptorDomain(nextString(10000));
//        random.setDoaUri(Services.anonymous().augment("model", nextUuid()));
//        random.setExperimentalResult(new LiteralValue(RNG.nextFloat(), XSDDatatype.XSDfloat));
//        random.setKeywords(nextString(20));
        randomReportMeta.setMechanismDomain(nextString(2000));
        randomReportMeta.setMetabolicDomain(nextString(2000));
//        random.setModelUri(Services.anonymous().augment("model", nextUuid()));
        randomReportMeta.setModelVersion(nextString(2000));
        random.setModelDate(System.currentTimeMillis() / 2);
//        random.setPredictionResult(new LiteralValue(RNG.nextFloat(), XSDDatatype.XSDfloat));
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

    private static String getSeed(int len) {
        String str = "#. aF$0b9338nH94&cLdU|K2eHfJgTP8XhiFj61DOk.lNm9n/BoI5pGqYVrs3C tSuMZvwWx4yE7zR";
        StringBuilder sb = new StringBuilder();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(62);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }

    public String nextString(int len) {
        String str = STRING_SEED;
        StringBuilder sb = new StringBuilder();
        int te = 0;
        for (int i = 1; i <= len; i++) {
            te = RNG.nextInt(62);
            sb.append(str.charAt(te));
        }
        return sb.toString();
    }
}

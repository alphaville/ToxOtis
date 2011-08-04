package org.opentox.toxotis.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.CompoundSpider;

/**
 *
 * @author chung
 */
public class SimilarityRetriever {

    private final double similarity;
    private final Compound compound;
    private AuthenticationToken token;
    private boolean retrieveDepiction = false;
    private boolean retrieveConformers = true;
    private String depictionService = null;

    public boolean isRetrieveConformers() {
        return retrieveConformers;
    }

    public void setRetrieveConformers(boolean retrieveConformers) {
        this.retrieveConformers = retrieveConformers;
    }

    public String getDepictionService() {
        return depictionService;
    }

    public void setDepictionService(String depictionService) {
        this.depictionService = depictionService;
    }

    public boolean isRetrieveDepiction() {
        return retrieveDepiction;
    }

    public void setRetrieveDepiction(boolean retrieveDepiction) {
        this.retrieveDepiction = retrieveDepiction;
    }

    public SimilarityRetriever(double similarity, Compound compound) {
        this.similarity = similarity;
        this.compound = compound;
    }

    public void authenticate(AuthenticationToken token) {
        this.token = token;
    }

    public ArrayList<Compound> similarCompounds() throws ServiceInvocationException {
        System.out.println("Similarity Search engine started");
        Set<VRI> similarCompoundVRIs = compound.getSimilar(similarity, token);
        System.out.println("  L similar compounds found (listing uris)");
        for (VRI v : similarCompoundVRIs) {
            System.out.println("        L  o --> " + v);
        }
        Compound current;
        ArrayList<Compound> similarCompounds = new ArrayList<Compound>(similarCompoundVRIs.size());
        for (VRI sVri : similarCompoundVRIs) {
            try {
                current = new CompoundSpider(sVri.toString(), null).parse();
                if (retrieveDepiction) {
                    current.getDepiction(depictionService);
                }
                if (retrieveConformers){
                    current.listConformers(token);
                }
                similarCompounds.add(current);
            } catch (ToxOtisException ex) {
                Logger.getLogger(SimilarityRetriever.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("xxx : Compound download failure for " + sVri);
            }
        }
        System.out.println("Similar compounds downloaded!");
        return similarCompounds;
    }
}

package org.opentox.toxotis.collection;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum OpenToxAlgorithms {

    NTUA_MLR(Services.NTUA.augment("algorithm","mlr")),
    NTUA_LEVERAGES(Services.NTUA.augment("algorithm","leverages")),
    NTUA_SVM(Services.NTUA.augment("algorithm","svm")),
    NTUA_FILTER(Services.NTUA.augment("algorithm","filter")),
    AMBIT_LR(Services.AMBIT_UNI_PLOVDIV.augment("algorithm","LR")),
    TUM_KNN_CLASSIFICATION(Services.TUM_DEV.augment("algorithm","kNNclassification"));
    private static final Date LAST_UPDATE;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.SEPTEMBER, 10, 18, 00);
        LAST_UPDATE = cal.getTime();
    }
    private String serviceURI;

    private OpenToxAlgorithms(VRI serviceURI) {
        this.serviceURI = serviceURI.getStringNoQuery();
    }

    private OpenToxAlgorithms(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public static final Date getLastUpdated() {
        return LAST_UPDATE;
    }

    public String getServiceUri() {
        return serviceURI;
    }

    public VRI getServiceVri() {
        try {
            return new VRI(serviceURI);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}

package org.opentox.toxotis.collection;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum OpenToxAlgorithms {

    NTUA_MLR(String.format(Services.NTUA_SERVICES, "algorithm/mlr")),
    NTUA_LEVERAGES(String.format(Services.NTUA_SERVICES, "algorithm/leverages")),
    NTUA_SVM(String.format(Services.NTUA_SERVICES, "algorithm/svm")),
    NTUA_FILTER(String.format(Services.NTUA_SERVICES, "algorithm/filter"));
    private static final Date LAST_UPDATE;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.SEPTEMBER, 5, 18, 10);
        LAST_UPDATE = cal.getTime();
    }
    private String serviceURI;

    private OpenToxAlgorithms(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public static final Date getLastUpdated() {
        return LAST_UPDATE;
    }

    public String getServiceUri() {
        return serviceURI;
    }
}

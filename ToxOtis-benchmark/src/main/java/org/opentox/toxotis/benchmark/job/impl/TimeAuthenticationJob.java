package org.opentox.toxotis.benchmark.job.impl;

import org.opentox.toxotis.benchmark.gauge.GaugeFactory;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TimeAuthenticationJob extends Job {

    private String encryptedPassFile;
    public static final String DEFAULT_GAUGE_NAME = "MilliTime Gauge";
    private String milliTimeGaugeName = DEFAULT_GAUGE_NAME;

    public TimeAuthenticationJob(Comparable title, Comparable parameter) {
        super(title, parameter);
        TimeGauge gauge = GaugeFactory.milliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    public TimeAuthenticationJob(Comparable title, Comparable parameter, String milliTimeGaugeName) {
        super(title, parameter);
        this.milliTimeGaugeName = milliTimeGaugeName;
        TimeGauge gauge = GaugeFactory.milliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    public void setEncryptedPassFile(String encryptedPassFile) {
        this.encryptedPassFile = encryptedPassFile;
    }

    @Override
    public void work() throws Exception {
        TimeGauge timeGauge = (TimeGauge) getGaugeForName(milliTimeGaugeName);
        timeGauge.start();
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile(encryptedPassFile);
        timeGauge.stop();
    }
}

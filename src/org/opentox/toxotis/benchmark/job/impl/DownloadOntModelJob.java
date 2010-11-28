package org.opentox.toxotis.benchmark.job.impl;

import org.opentox.toxotis.benchmark.gauge.GaugeFactory;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DownloadOntModelJob extends Job {

    private String templatedUri;
    public static final String DEFAULT_GAUGE_NAME = "MilliTime Gauge";
    private String milliTimeGaugeName = DEFAULT_GAUGE_NAME;

    public String getService() {
        return templatedUri;
    }

    public void setService(String service) {
        this.templatedUri = service;
    }

    public DownloadOntModelJob(Comparable title, Comparable parameter) {
        super(title, parameter);
        TimeGauge gauge = GaugeFactory.milliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    public DownloadOntModelJob(Comparable title, Comparable parameter, String milliTimeGaugeName) {
        super(title, parameter);
        this.milliTimeGaugeName = milliTimeGaugeName;
        TimeGauge gauge = GaugeFactory.milliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    @Override
    public void work() throws Exception {
        TimeGauge timeGauge = (TimeGauge) getGaugeForName(milliTimeGaugeName);
        timeGauge.start();
        VRI uri = new VRI(String.format(templatedUri, parameter.toString()));
        System.out.println(uri);
        GetHttpClient client = new GetHttpClient(uri);
        client.getResponseOntModel();
        client.close();
        timeGauge.stop();
    }
}

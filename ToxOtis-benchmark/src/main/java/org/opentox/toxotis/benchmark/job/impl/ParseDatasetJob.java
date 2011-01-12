package org.opentox.toxotis.benchmark.job.impl;

import org.opentox.toxotis.benchmark.gauge.GaugeFactory;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Dataset;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ParseDatasetJob extends Job {

    public static final String DOWNLOAD_TIMER_NAME = "download time";
    public static final String WEKA_TIMER_NAME = "parsing time";
    private String downloadTimeName = DOWNLOAD_TIMER_NAME;
    private String wekaTimeName = DOWNLOAD_TIMER_NAME;

    String templatedUri;

    public String getService() {
        return templatedUri;
    }

    public void setService(String service) {
        this.templatedUri = service;
    }

    public ParseDatasetJob(Comparable title, Comparable parameter) {
        super(title, parameter);
        TimeGauge timer1 = GaugeFactory.milliTimeGauge(downloadTimeName);
        TimeGauge timer2 = GaugeFactory.milliTimeGauge(wekaTimeName);
        addGauge(timer1);
        addGauge(timer2);
    }

    public ParseDatasetJob(Comparable title, Comparable parameter, String downloadTimerGaugeName, String wekaTimerGaugeName) {
        super(title, parameter);
        TimeGauge timer1 = GaugeFactory.milliTimeGauge(downloadTimeName);
        TimeGauge timer2 = GaugeFactory.milliTimeGauge(wekaTimeName);
        addGauge(timer1);
        addGauge(timer2);
    }

    @Override
    public void work() throws Exception {
        TimeGauge t1 = (TimeGauge) getGaugeForName(DOWNLOAD_TIMER_NAME);
        TimeGauge t2 = (TimeGauge) getGaugeForName(WEKA_TIMER_NAME);
        t1.start();
        Dataset ds = new Dataset(new VRI(String.format(templatedUri, parameter.toString())));
        ds.loadFromRemote();
        t1.stop();
        t2.start();
        ds.getInstances();
        t2.stop();
    }
}

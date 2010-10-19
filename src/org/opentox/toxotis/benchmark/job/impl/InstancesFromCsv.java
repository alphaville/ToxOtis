package org.opentox.toxotis.benchmark.job.impl;

import java.io.InputStreamReader;
import org.opentox.toxotis.benchmark.gauge.GaugeFactory;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InstancesFromCsv extends Job {

    private String templatedUri;
    public static final String DEFAULT_DOWNLOAD_GAUGE_NAME = "time";
    private String milliTimeDownloadGaugeName = DEFAULT_DOWNLOAD_GAUGE_NAME;

    public String getService() {
        return templatedUri;
    }

    public void setService(String service) {
        this.templatedUri = service;
    }

    public InstancesFromCsv(Comparable title, Comparable parameter) {
        super(title, parameter);
        TimeGauge gaugeDownload = GaugeFactory.milliTimeGauge(milliTimeDownloadGaugeName);
        addGauge(gaugeDownload);
    }

    public InstancesFromCsv(Comparable title, Comparable parameter, String downloadGaugeName) {
        super(title, parameter);
        this.milliTimeDownloadGaugeName = downloadGaugeName;
        TimeGauge gaugeDownload = GaugeFactory.milliTimeGauge(downloadGaugeName);
        addGauge(gaugeDownload);
    }

    @Override
    public void work() throws Exception {
        TimeGauge d_timeGauge = (TimeGauge) getGaugeForName(milliTimeDownloadGaugeName);        
        GetClient client = null;
        try {
            d_timeGauge.start();
            VRI uri = new VRI(String.format(templatedUri, parameter.toString()));
            client = new GetClient(uri);
            client.setMediaType(Media.TEXT_CSV);            
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(client.getRemoteStream());
            Instances output = csvLoader.getDataSet();
            d_timeGauge.stop();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }    

}

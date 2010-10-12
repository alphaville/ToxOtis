package org.opentox.toxotis.benchmark.job;

import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.benchmark.gauge.MilliTimeGauge;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DownloadOntModelJob extends Job {

    String templatedUri;
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
        MilliTimeGauge gauge = new MilliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    public DownloadOntModelJob(Comparable title, Comparable parameter, String milliTimeGaugeName) {        
        super(title, parameter);
        this.milliTimeGaugeName = milliTimeGaugeName;
        MilliTimeGauge gauge = new MilliTimeGauge(milliTimeGaugeName);
        addGauge(gauge);
    }

    @Override
    public void work() throws Exception {
        TimeGauge timeGauge = (TimeGauge) getGaugeForName(milliTimeGaugeName);
        timeGauge.start();
        GetClient client = new GetClient();
        client.setUri(String.format(templatedUri, parameter.toString())).setMediaType(Media.APPLICATION_RDF_XML);
        client.getResponseOntModel();
        timeGauge.stop();
    }
}

package org.opentox.toxotis.benchmark.job;

import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.benchmark.gauge.Gauge;
import org.opentox.toxotis.benchmark.gauge.MilliTimeGauge;
import org.opentox.toxotis.benchmark.gauge.TimeGauge;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DownloadOntModelJob extends Job {

    VRI service;

    public VRI getService() {
        return service;
    }

    public void setService(VRI service) {
        this.service = service;
    }

    

    public DownloadOntModelJob(Comparable title, Comparable parameter) {
        super(title, parameter);
        MilliTimeGauge gauge = new MilliTimeGauge();
        addGauge(gauge);
    }

    @Override
    public void work() throws Exception {
        
        //TESTING PURPOSES - WILL FIND ANOTHER WAY
        ((TimeGauge)getCounters().get(0)).start();
        System.out.println((getCounters().get(0).getTitle()));
        GetClient client = new GetClient();
        System.out.println(parameter);
        client.setUri(service.augment(parameter.toString())).setMediaType("application/rdf+xml");
        OntModel model = client.getResponseOntModel();
        ((TimeGauge)getCounters().get(0)).stop();
    }
}

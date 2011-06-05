package org.opentox.toxotis.benchmark.job.impl;

import org.opentox.toxotis.benchmark.gauge.GaugeFactory;
import org.opentox.toxotis.benchmark.gauge.MemoryConsumptionGauge;
import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.arff.GetArff;

/**
 *
 * @author Pantelis Sopasakis 
 */
public class DatasetMemoryJob extends Job{

    private String templatedUri;
    public static final String DEFAULT_GAUGE_NAME = "Memory Gauge";
    private String memoryGaugeName = DEFAULT_GAUGE_NAME;

    public DatasetMemoryJob(Comparable title, Comparable parameter) {
        super(title, parameter);
        MemoryConsumptionGauge gauge = new MemoryConsumptionGauge(memoryGaugeName);
        addGauge(gauge);
    }

    public DatasetMemoryJob(Comparable title, Comparable parameter, String memoryGaugeName) {
        super(title, parameter);
        this.memoryGaugeName = memoryGaugeName;
        MemoryConsumptionGauge gauge = new MemoryConsumptionGauge(memoryGaugeName);
        addGauge(gauge);
    }

    public String getService() {
        return templatedUri;
    }

    public void setService(String service) {
        this.templatedUri = service;
    }

    @Override
    public void work() throws Exception {        
        MemoryConsumptionGauge gauge = ((MemoryConsumptionGauge)getGaugeForName(memoryGaugeName));
        gauge.sampleStart();
        VRI uri = new VRI(String.format(templatedUri, parameter.toString()));
        System.out.println(uri);
        GetArff getter = new GetArff(uri);
        getter.getInstances();
        gauge.sampleEnd();    
    }

}
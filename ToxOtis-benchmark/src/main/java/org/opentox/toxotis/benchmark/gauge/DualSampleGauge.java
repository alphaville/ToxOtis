package org.opentox.toxotis.benchmark.gauge;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DualSampleGauge extends Gauge {

    public DualSampleGauge(String title) {
        super(title);
    }

    public DualSampleGauge() {
    }

    public abstract void sampleStart();

    public abstract void sampleEnd();
}

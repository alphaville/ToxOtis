package org.opentox.toxotis.benchmark.gauge;

/**
 * Gauge used to measure the system's CPU load.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class SystemLoadGauge extends Gauge{

    public SystemLoadGauge(String title) {
        super(title);
    }

    public SystemLoadGauge() {
        super();
    }

    public abstract void sample();

}
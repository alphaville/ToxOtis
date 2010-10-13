package org.opentox.toxotis.benchmark.gauge;

/**
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
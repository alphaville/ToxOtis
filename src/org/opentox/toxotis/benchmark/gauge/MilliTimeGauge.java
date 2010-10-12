package org.opentox.toxotis.benchmark.gauge;

import org.opentox.toxotis.benchmark.gauge.Gauge;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MilliTimeGauge extends Gauge implements TimeGauge {

    public MilliTimeGauge() {
        super("Time (milliseconds)");
    }

    public void start() {
        setMeasurement(System.currentTimeMillis());
    }

    public void stop() {
        setMeasurement(System.currentTimeMillis() - (Long) getMeasurement());
    }
}

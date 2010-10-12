package org.opentox.toxotis.benchmark.gauge;

import org.opentox.toxotis.benchmark.gauge.Gauge;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class NanoTimeGauge extends Gauge implements TimeGauge{

    public NanoTimeGauge() {
        super("Time (nanoseconds)");
    }

    public void start(){
        setMeasurement(System.nanoTime());
    }

    public void stop(){
        setMeasurement(System.nanoTime()-(Long)getMeasurement());
    }

}

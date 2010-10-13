package org.opentox.toxotis.benchmark.gauge;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
@Deprecated
public class NanoTimeGauge extends TimeGauge {

    public NanoTimeGauge() {
        super("Time (nanoseconds)");
    }

    public void start() {
        setMeasurement(System.nanoTime());
    }

    public void stop() {
        setMeasurement(System.nanoTime() - (Long) getMeasurement());
    }
}

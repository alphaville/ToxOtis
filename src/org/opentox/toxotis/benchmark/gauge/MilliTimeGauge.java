package org.opentox.toxotis.benchmark.gauge;

/**
 * A Gauge used to time jobs in milliseconds. The methods <code>start()</code> and
 * <code>stop()</code> should be invoked by running jobs to time the elapsed time
 * thus measuring the computational time needed for the job to complete.
 * 
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MilliTimeGauge extends  TimeGauge {

    public static final String DEFAULT_GAUGE_NAME = "MilliTime Gauge";

    public MilliTimeGauge() {
        super("MilliTime Gauge");
    }

    public MilliTimeGauge(String title) {
        super(title);
    }
    

    public void start() {
        setMeasurement(System.currentTimeMillis());
    }

    public void stop() {
        setMeasurement(System.currentTimeMillis() - (Long) getMeasurement());
    }
}

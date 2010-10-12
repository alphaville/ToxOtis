package org.opentox.toxotis.benchmark.gauge;

/**
 * The TimeGauge Interface declares timing functionalities
 * in a Gauge. <br> A start() and stop() method must be implemented
 * in order for the counter to be able to measure time.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public interface TimeGauge {

    /**
     * By invoking this method one starts the gauge's inner timer.
     */
    public void start();

    /**
     * By invoking this method one stops the gauge's inner timer and
     * stores the timing result.
     */
    public void stop();
}

package org.opentox.toxotis.benchmark.gauge;

/**
 * A Gauge is used to provide a desired measurement in any Job.
 * This class must be extended for beauty purposes. The setMeasurement() method
 * must be used in any implementation in order for the gauge to store a measurement.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public abstract class Gauge {

    private String title;
    private Number measurement;
    private Number stdev;

    protected Gauge(){
        
    }

    /**
     * Constructs a new Gauge using a given Title.
     * @param title
     */
    public Gauge(String title) {
        this.title = title;
    }

    /**
     * Gets this Gauge's title.
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets this Gauge's title.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets this Gauge's measurement value. This method is used by a Benchmark
     * object to derive a measurement stored in the Gauge.
     * @return
     */
    public Number getMeasurement() {
        return measurement;
    }

    /**
     * Sets a measurement value in the Gauge. Every implementation of Gauge must
     * use this method to finaly store whatever measurement is needed in the Gauge.
     * 
     * @param measurement
     */
    public void setMeasurement(Number measurement) {
        this.measurement = measurement;
    }

    /**
     * Gets the Standard Deviation value for a Job's sum of executions on this
     * Gauge's measurement. This method is used by a Benchmark object to
     * construct a statistical chart.
     *
     * @return
     */
    public Number getStdev() {
        return stdev;
    }

    /**
     * Sets the Standard Deviation value for a Job's sum of executions on this
     * Gauge's measurement. This value is calculated by the Job holding this Gauge.
     *
     * @param stdev
     */
    public void setStdev(Number stdev) {
        this.stdev = stdev;
    }
    
}

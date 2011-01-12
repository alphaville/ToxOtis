package org.opentox.toxotis.benchmark.job;

import org.opentox.toxotis.benchmark.gauge.Gauge;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jfree.data.statistics.Statistics;

/**
 * A Job represents a series of code lines that can be executed and benched.
 * A Job can use one or more Gauges in order to obtain the desired measurements.
 * In order for it to be able to function, the work() method must be implemented.
 * A Job has a Title and a Parameter. Those two fields uniquely identify a Job.
 * If another Job shares the same values those two are considered equal.
 * The parameter value is important and should be used in the implementation of
 * work() if possible, because it's value is used as x-axis coordinates in any
 * generated chart.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public abstract class Job implements Runnable {

    private Comparable title;
    protected Comparable parameter;
    private int iterations;
    private List<Gauge> gauges;

    /**
     * This method must be implemented for the Job to be functional. One can
     * execute any code in the work() method, and use whatever Gauges suit them
     * to obtain the required measurements. One must not forget to add Gauges
     * they used in the Job, by using addGauge() or setGauges() methods.
     * 
     * @throws Exception
     */
    public abstract void work() throws Exception;

    public Job(Comparable title, Comparable parameter) {
        this.title = title;
        this.parameter = parameter;
        this.iterations = 1;
        this.gauges = new ArrayList<Gauge>();
    }

    public void run() {
        try {
            /* Constructs Two-Dimensinal List that will hold all measurements from all Gauges
             * and initializes it with the correct sizes.
             */
            ArrayList<ArrayList<Number>> allValues = new ArrayList<ArrayList<Number>>(gauges.size());
            for (int i = 0; i < gauges.size(); i++) {
                allValues.add(i, new ArrayList<Number>(iterations));
            }


            /*
             * Running work() method as many times as the accuracy field dictates
             * and saving all results in the Two-Dimensinal List.
             */
            work(); //Ignoring first run
            for (int i = 0; i < iterations; i++) {
                work();
                for (int y = 0; y < gauges.size(); y++) {
                    allValues.get(y).add(i, gauges.get(y).getMeasurement());
                }
            }

            /*
             * Calculates Mean and Standard Deviation seperately for each Gauge
             * and saves the values to the same Gauge. This happens because we
             * don't really need to hold all values from all cycles, just the
             * statistic results.
             */
            for (int i = 0; i < gauges.size(); i++) {
                System.out.println(allValues.get(i));
                double mean = Statistics.calculateMean(allValues.get(i));
                double stdev = Statistics.getStdDev(
                        allValues.get(i).toArray(
                        new Number[allValues.get(i).size()]));
                gauges.get(i).setMeasurement(mean);
                gauges.get(i).setStdev(stdev);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the first Gauge in the Job whose title matches the given String.
     * @param name a Gauge title
     * @return a Gauge existing in the Job.
     */
    public Gauge getGaugeForName(String name) {
        for (Gauge g : gauges) {
            if (name.equals(g.getTitle())) {
                return g;
            }
        }
        return null;
    }

    /**
     * Adds a Gauge in the Job. If a Gauge is used but not added in the Job, it's
     * results will be ignored.
     * @param gauge A Gauge to be added in the Job.
     * @return
     *  <code>true</code> as specified by {@link Collection#add(java.lang.Object) }.
     */
    public boolean addGauge(Gauge e) {
        return gauges.add(e);
    }

    /**
     * Returns a List of Gauges contained in this Job.
     * @return
     *      List of measuring gauges in the current job or <code>null</code> if no
     *      gauges have been added.
     */
    public List<Gauge> getGauges() {
        return gauges;
    }

    /**
     * Sets a List of Gauges to replace the current Job's Gauges.
     *
     * @param counters
     *      List of gauges for this job.
     */
    public void setGauges(List<Gauge> counters) {
        this.gauges = counters;
    }

    /**
     * Gets this Job's parameter value.
     * @return
     *      A comparable parameter that has been specified for this job.
     */
    public Comparable getParameter() {
        return parameter;
    }

    /**
     * Sets this Job's parameter value.
     * The parameter is important and should be used in the implementation of
     * work() if possible, because it's value is used as x-axis coordinates in any
     * generated chart.
     * @param parameter
     */
    public void setParameter(Comparable parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets this Job's title value.
     * @return
     *      Comparable title for this particular job.
     */
    public Comparable getTitle() {
        return title;
    }

    /**
     * Sets this Job's title value. A Job's title and Parameter are able to
     * uniquely identify a Job, meaning that another Job with same values
     * is considered equal to this.
     *
     * @param title
     */
    public void setTitle(Comparable title) {
        this.title = title;
    }

    /**
     * Gets the number of times this Job's work method must be run.
     * @return
     *      Number of iterations
     */
    public int getNumberIterations() {
        return iterations;
    }

    /**
     * Sets the number of times this Job's work method must be run.
     * The final measurement will occur as a Mean of all partial executions.
     * It should be noted that there is always an extra startup run of the work()
     * method whose results are not counted for initialization purposes.
     *
     * @param nIter
     *      Number of iterations for this job.
     */
    public void setNumberIterations(int nIter) {
        this.iterations = nIter;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Job)) {
            return false;
        }
        Job other = (Job) obj;
        if (this.getTitle().compareTo(other.getTitle()) == 0
                && this.getParameter().compareTo(other.getParameter()) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 89 * hash + (this.parameter != null ? this.parameter.hashCode() : 0);
        return hash;
    }
}

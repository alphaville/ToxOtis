package org.opentox.toxotis.benchmark.job;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.benchmark.gauge.Gauge;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.statistics.Statistics;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public abstract class Job implements Runnable, Cloneable {

    private Comparable title;
    protected Comparable parameter;
    private int accuracy;
    private List<Gauge> counters;

    public abstract void work() throws Exception;

    public Job(Comparable title, Comparable parameter) {
        this.title = title;
        this.parameter = parameter;
        this.accuracy = 1;
        this.counters = new ArrayList<Gauge>();
    }

    public void run() {
        try {
            /* Constructs Two-Dimensinal List that will hold all measurements from all Gauges
             * and initializes it with the correct sizes.
             */
            ArrayList<ArrayList<Number>> allValues = new ArrayList<ArrayList<Number>>(counters.size());
            for (int i = 0; i < counters.size(); i++) {
                allValues.add(i,new ArrayList<Number>(accuracy));
            }


            /*
             * Running work() method as many times as the accuracy field dictates
             * and saving all results in the Two-Dimensinal List.
             */
            work(); //Ignoring first run
            for (int i = 0; i < accuracy; i++) {
                work();
                for (int y = 0; y < counters.size(); y++) {
                    allValues.get(y).add(i, counters.get(y).getMeasurement());
                }
            }

            /*
             * Calculates Mean and Standard Deviation seperately for each Gauge
             * and saves the values to the same Gauge. This happens because we
             * don't really need to hold all values from all cycles, just the
             * statistic results.
             */
            for (int i = 0; i < counters.size(); i++) {
                System.out.println(allValues.get(i));
                double mean = Statistics.calculateMean(allValues.get(i));
                double stdev = Statistics.getStdDev(
                        allValues.get(i).toArray(
                        new Number[allValues.get(i).size()]));
                counters.get(i).setMeasurement(mean);
                counters.get(i).setStdev(stdev);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public boolean addGauge(Gauge e) {
        return counters.add(e);
    }

    public List<Gauge> getCounters() {
        return counters;
    }

    public void setCounters(List<Gauge> counters) {
        this.counters = counters;
    }

    public Comparable getParameter() {
        return parameter;
    }

    public void setParameter(Comparable parameter) {
        this.parameter = parameter;
    }

    public Comparable getTitle() {
        return title;
    }

    public void setTitle(Comparable title) {
        this.title = title;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public Job clone() throws CloneNotSupportedException {
        Job newJob = (Job) super.clone();
        newJob.setCounters(new ArrayList<Gauge>());
        for(Gauge gauge : this.getCounters()){
            try {
                System.out.println(gauge.getTitle());
                newJob.getCounters().add(gauge.clone());
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
            }
        }
        newJob.title = this.getTitle();
        newJob.parameter = this.getParameter();
        newJob.accuracy = this.getAccuracy();
        return newJob;
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

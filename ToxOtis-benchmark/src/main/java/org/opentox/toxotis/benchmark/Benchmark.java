package org.opentox.toxotis.benchmark;

import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.benchmark.gauge.Gauge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;


/**
 * This is the core of a ToxOtis Benchmark. One can use this class to execute a
 * series of Jobs in order to get the desired benchmarking measurements. <br>
 * One can also use the automatic chart features this class provides, in order
 * to get their results in one or many of various charting styles supported.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class Benchmark {

    private String title;
    private Status status;
    private List<Job> jobs = new ArrayList<Job>();
    private String horizontalAxisTitle = "parameter";
    private String verticalAxisTitle = "benchmarked measure";

    /**
     * Gets the Horizontal Axis title. This title will be used in any chart
     * that this Benchmark provides.
     *
     * @return a String representing the Horizontal Axis title.
     */
    public String getHorizontalAxisTitle() {
        return horizontalAxisTitle;
    }

    /**
     * Sets the Horizontal Axis title. This title will be used in any chart
     * that this Benchmark provides.
     *
     * @param horizontalAxisTitle a String representing the Horizontal Axis title.
     */
    public void setHorizontalAxisTitle(String horizontalAxisTitle) {
        this.horizontalAxisTitle = horizontalAxisTitle;
    }

    /**
     * Gets the Vertical Axis title. This title will be used in any chart
     * that this Benchmark provides.
     *
     * @return a String representing the Vertical Axis title.
     */
    public String getVerticalAxisTitle() {
        return verticalAxisTitle;
    }

    /**
     * Sets the Vertical Axis title. This title will be used in any chart
     * that this Benchmark provides.
     *
     * @param verticalAxisTitle a String representing the Vertical Axis title.
     */
    public void setVerticalAxisTitle(String verticalAxisTitle) {
        this.verticalAxisTitle = verticalAxisTitle;
    }

    /**
     * Status of a Benchmark object. One cannot insert new Jobs in a RUNNING or
     * a COMPLETED Benchmark, and results cannot be acquired from an IDLE or
     * a RUNNING Benchmark.
     */
    public enum Status {

        RUNNING,
        IDLE,
        COMPLETED
    }

    /**
     * Constructs a new Benchmark object by defining it's title. This title will
     * be used as a header in any chart provided by the object.
     *
     * @param title a String representing the Benchmark's title.
     */
    public Benchmark(String title) {
        jobs = new ArrayList<Job>();
        status = Status.IDLE;
        this.title = title;
    }

    /**
     * Adds a Job to be executed in the Benchmark. An Exception is thrown if
     * the Benchmark is not in IDLE state.
     *
     * @param job the Job to be executed.
     * @throws ToxOtisException
     */
    public void addJob(Job job) throws ToxOtisException {
        if (!status.equals(Status.IDLE)) {
            throw new ToxOtisException("Benchmark:" + title + " is not in IDLE state and cannot accept more Jobs");
        }
        jobs.add(job);
    }

    /**
     * Sets the Jobs to be executed in the Benchmark. An Exception is thrown if
     * the Benchmark is not in IDLE state.
     *
     * @param jobs the Jobs to be executed.
     * @throws ToxOtisException
     */
    public void setJobs(List<Job> jobs) throws ToxOtisException {
        if (!status.equals(Status.IDLE)) {
            throw new ToxOtisException("Benchmark:" + title + " is not in IDLE state and cannot accept more Jobs");
        }
        this.jobs = jobs;
    }


    public void start(int coreThreadPoolSize, int maxThreadPoolSize, long keepAliveTime, TimeUnit keepAliveUnits, int queueSize) throws ToxOtisException {
        start(new ThreadPoolExecutor(coreThreadPoolSize, maxThreadPoolSize, keepAliveTime, keepAliveUnits, new ArrayBlockingQueue<Runnable>(queueSize)));
    }

    /**
     * Adds Jobs to be executed in the Benchmark. An Exception is thrown if
     * the Benchmark is not in IDLE state.
     *
     * @param jobs the Jobs to be executed.
     * @throws ToxOtisException
     */
    public void addJobs(List<Job> jobs){
        this.jobs.addAll(jobs);
    }

    /**
     * Starts the sequential execution of Jobs in the Benchmark. The Benchmark's
     * State must be IDLE or an exception is thrown. While executing, state
     * turns to RUNNING. This method blocks current thread until all Jobs finish
     * execution. Afterwards the State is set to COMPLETED.
     * 
     * @throws ToxOtisException
     */
    public void start() throws ToxOtisException {
        start(Executors.newSingleThreadExecutor());
    }

    public void start(ExecutorService executor) throws ToxOtisException {
        if (!status.equals(Status.IDLE)) {
            throw new ToxOtisException("Benchmark:" + title + " is not in IDLE state and cannot be executed");
        }
        status = Status.RUNNING;
        for (Job job : jobs) {
            job.run();
        }
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            throw new ToxOtisException(ex);
        }
        status = Status.COMPLETED;
    }

    /**
     * Gets this Benchmark's Status.
     * @return a Status representing this Benchmark's status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the Benchmark's title. This title will
     * be used as a header in any chart provided by the object.
     *
     * @return a String representing this Benchmark's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Constructs a new Bar Chart with Error Bars using the job results as input.
     * The Array of <code>Class </code>objects asked is used to define which types
     * of Gauge results will be used in the Chart's creation. Since a job may contain
     * many gauges and take measurements for various things, this method provides
     * a way to define which of those will be present in the required chart. If
     * none are provided all will be used.
     *
     * @param counters Class objects that extend Gauge class.
     * @return A new Bar Chart as a JFreeChart object.
     */
    public JFreeChart getBarChart(Class<? extends Gauge>... counters) {
        return getChart(new StatisticalBarRenderer(), counters);
    }

    /**
     * Constructs a new Bar Chart with Error Bars using the job results as input.
     * The Array of Strings asked is used to define which exactly
     * Gauge results will be used in the Chart's creation. Since a job may contain
     * many gauges and take measurements for various things, this method provides
     * a way to define which of those will be present in the required chart. If
     * none are provided all will be used.
     *
     * @param counters Strings that represent Gauge titles contained in this
     * Benchmark's Jobs.
     *
     * @return A new Bar Chart as a JFreeChart object.
     */
    public JFreeChart getBarChart(String... counters) {
        return getChart(new StatisticalBarRenderer(), counters);
    }

    /**
     * Constructs a new Line Chart with Error Bars using the job results as input.
     * The Array of <code>Class </code>objects asked is used to define which types
     * of Gauge results will be used in the Chart's creation. Since a job may contain
     * many gauges and take measurements for various things, this method provides
     * a way to define which of those will be present in the required chart. If
     * none are provided all will be used.
     *
     * @param counters Class objects that extend Gauge class.
     * @return A new Line Chart as a JFreeChart object.
     */
    public JFreeChart getLineChart(Class<? extends Gauge>... counters) {
        return getChart(new StatisticalLineAndShapeRenderer(true, true), counters);
    }

    /**
     * Constructs a new Line Chart with Error Bars using the job results as input.
     * The Array of Strings asked is used to define which exactly
     * Gauge results will be used in the Chart's creation. Since a job may contain
     * many gauges and take measurements for various things, this method provides
     * a way to define which of those will be present in the required chart. If
     * none are provided all will be used.
     *
     * @param counters Strings that represent Gauge titles contained in this
     * Benchmark's Jobs.
     *
     * @return A new Line Chart as a JFreeChart object.
     */
    public JFreeChart getLineChart(String... counters) {
        return getChart(new StatisticalLineAndShapeRenderer(true, true), counters);
    }

    /*
     * This private method actually constructs the chart.
     */
    private JFreeChart getChart(CategoryItemRenderer renderer, Class<? extends Gauge>... counters) {
        List<Class<? extends Gauge>> gaugesToInclude = Arrays.asList(counters);
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        /*
         * Runs through all stored jobs and if any of their gauges are included
         * in the counters array the gauge results are added in the chart's dataset.
         */
        for (Job job : jobs) {
            for (Gauge g : job.getGauges()) {
                if (gaugesToInclude.contains(g.getClass())) {
                    dataset.add(g.getMeasurement(), g.getStdev(), g.getTitle(), job.getParameter());
                }
            }
        }
        CategoryAxis categoryAxis = new CategoryAxis(horizontalAxisTitle);
        ValueAxis valueAxis = new NumberAxis(verticalAxisTitle);
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart c = new JFreeChart(title, plot);
        return c;
    }

    /*
     * This private method actually constructs the chart.
     */
    private JFreeChart getChart(CategoryItemRenderer renderer, String... counters) {
        List<String> gaugesToInclude = Arrays.asList(counters);
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        /*
         * Runs through all stored jobs and if any of their gauges are included
         * in the counters array the gauge results are added in the chart's dataset.
         */
        for (Job job : jobs) {
            for (Gauge g : job.getGauges()) {
                if (gaugesToInclude.contains(g.getTitle())) {                    
                    dataset.add(g.getMeasurement(), g.getStdev(), g.getTitle(), job.getParameter());
                }
            }
        }
        CategoryAxis categoryAxis = new CategoryAxis(horizontalAxisTitle);
        ValueAxis valueAxis = new NumberAxis(verticalAxisTitle);
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart c = new JFreeChart(title, plot);
        return c;
    }


}

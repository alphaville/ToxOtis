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
import org.opentox.toxotis.ToxOtisException;

/**
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

    public String getHorizontalAxisTitle() {
        return horizontalAxisTitle;
    }

    public void setHorizontalAxisTitle(String horizontalAxisTitle) {
        this.horizontalAxisTitle = horizontalAxisTitle;
    }

    public String getVerticalAxisTitle() {
        return verticalAxisTitle;
    }

    public void setVerticalAxisTitle(String verticalAxisTitle) {
        this.verticalAxisTitle = verticalAxisTitle;
    }

    public enum Status {

        RUNNING,
        IDLE,
        COMPLETED
    }

    public Benchmark(String title) {
        jobs = new ArrayList<Job>();
        status = Status.IDLE;
        this.title = title;
    }

    public void addJob(Job job) throws ToxOtisException {
        if (!status.equals(Status.IDLE)) {
            throw new ToxOtisException("Benchmark:" + title + " is not in IDLE state and cannot accept more Jobs");
        }
        jobs.add(job);
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public void addJobs(List<Job> jobs) {
        this.jobs.addAll(jobs);
    }


    public void start(int coreThreadPoolSize, int maxThreadPoolSize, long keepAliveTime, TimeUnit keepAliveUnits, int queueSize) throws ToxOtisException {
        start(new ThreadPoolExecutor(coreThreadPoolSize, maxThreadPoolSize, keepAliveTime, keepAliveUnits, new ArrayBlockingQueue<Runnable>(queueSize)));
    }

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

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public JFreeChart getBarChart(Class<? extends Gauge>... counters) {
        return getChart(new StatisticalBarRenderer(), counters);
    }

    public JFreeChart getBarChart(String... counters) {
        return getChart(new StatisticalBarRenderer(), counters);
    }

    public JFreeChart getLineChart(Class<? extends Gauge>... counters) {
        return getChart(new StatisticalLineAndShapeRenderer(true, true), counters);
    }

    public JFreeChart getLineChart(String... counters) {
        return getChart(new StatisticalLineAndShapeRenderer(true, true), counters);
    }

    private JFreeChart getChart(CategoryItemRenderer renderer, Class<? extends Gauge>... counters) {
        List<Class<? extends Gauge>> gaugesToInclude = Arrays.asList(counters);
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
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

    private JFreeChart getChart(CategoryItemRenderer renderer, String... counters) {
        List<String> gaugesToInclude = Arrays.asList(counters);
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
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

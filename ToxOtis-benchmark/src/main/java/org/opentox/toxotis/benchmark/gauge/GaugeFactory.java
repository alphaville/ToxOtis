package org.opentox.toxotis.benchmark.gauge;

import java.lang.management.ManagementFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GaugeFactory {

    public static SystemLoadGauge heapUsageGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(Double.parseDouble(Long.toString(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1000)) / 1000);// in MB
            }
        };
        return g;
    }

    public static SystemLoadGauge sysLoadGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement((double) (ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 50));// as %
            }
        };
        return g;
    }

    public static SystemLoadGauge freeMemGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(Double.parseDouble(Long.toString(Runtime.getRuntime().freeMemory() / 1000)) / 1000);// in MB
            }
        };
        return g;
    }

    public static SystemLoadGauge maxMemGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(Double.parseDouble(Long.toString(Runtime.getRuntime().maxMemory() / 1000)) / 1000);// in MB
            }
        };
        return g;
    }

    public static SystemLoadGauge totMemGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(Double.parseDouble(Long.toString(Runtime.getRuntime().totalMemory() / 1000)) / 1000);// in MB
            }
        };
        return g;
    }

    public static SystemLoadGauge loadedClassesGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount());
            }
        };
        return g;
    }

    public static SystemLoadGauge threadCountGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(ManagementFactory.getThreadMXBean().getThreadCount());
            }
        };
        return g;
    }

    public static SystemLoadGauge peakThreadCountGauge(String title) {
        SystemLoadGauge g = new SystemLoadGauge(title) {

            @Override
            public void sample() {
                setMeasurement(ManagementFactory.getThreadMXBean().getPeakThreadCount());
            }
        };
        return g;
    }

    /**
     * A Gauge used to time jobs in milliseconds. The methods <code>start()</code> and
     * <code>stop()</code> should be invoked by running jobs to time the elapsed time
     * thus measuring the computational time needed for the job to complete. A milliTimeGauge
     * is a {@link TimeGauge }
     * @param title 
     *      Title used to identify this time gauge.
     * @return
     * 
     */
    public static TimeGauge milliTimeGauge(String title) {
        TimeGauge milliTimeGauge = new TimeGauge(title) {

            @Override
            public void start() {
                setMeasurement(System.currentTimeMillis());
            }

            @Override
            public void stop() {
                setMeasurement(System.currentTimeMillis() - (Long) getMeasurement());
            }
        };
        return milliTimeGauge;
    }

    /**
     *
     * @return
     */
    public static TimeGauge nanoTimeGauge() {
        return nanoTimeGauge("");
    }

    public static TimeGauge nanoTimeGauge(String title) {
        TimeGauge nanoTimeGauge = new TimeGauge(title) {

            @Override
            public void start() {
                setMeasurement(System.nanoTime());
            }

            @Override
            public void stop() {
                setMeasurement(System.nanoTime() - (Long) getMeasurement());
            }
        };
        return nanoTimeGauge;
    }
}

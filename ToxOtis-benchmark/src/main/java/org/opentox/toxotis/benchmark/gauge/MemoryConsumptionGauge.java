package org.opentox.toxotis.benchmark.gauge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class MemoryConsumptionGauge extends DualSampleGauge {

    private volatile long start;
    private long currentMeasurement;
    private volatile boolean doRun = true;    
    private ExecutorService executor;

    public MemoryConsumptionGauge() {
        super();
    }

    public MemoryConsumptionGauge(String title) {
        super(title);
    }

    @Override
    public void sampleStart() {                
        Thread thread = new Thread() {

            @Override
            public void run() {
                doRun = true;
                start = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                while (doRun) {
                    currentMeasurement = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    if (currentMeasurement > start) {
                        setMeasurement(currentMeasurement - start);
                    }
                }
            }
        };
        executor = Executors.newSingleThreadExecutor();
        executor.submit(thread);
    }

    @Override
    public void sampleEnd() {
        doRun = false;
        executor.shutdownNow();
    }
}

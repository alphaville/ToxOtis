package org.opentox.toxotis.benchmark.job;

import java.util.ArrayList;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class JobFactory {

    public static ArrayList<Job> createDownloadOntModelJobs(String title, int start, int stop, int step, VRI service, int accuracy){
        ArrayList<Job> jobs = new ArrayList<Job>();
        for(int i=start; i<=stop ; i+=step){
            DownloadOntModelJob job = new DownloadOntModelJob(""+i, i);
            job.setService(service);
            job.setAccuracy(accuracy);
            jobs.add(job);
        }
        return jobs;
    }

}

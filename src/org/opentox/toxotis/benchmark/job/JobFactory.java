package org.opentox.toxotis.benchmark.job;

import org.opentox.toxotis.benchmark.job.impl.ParseDatasetJob;
import java.util.ArrayList;
import org.opentox.toxotis.benchmark.job.impl.DownloadOntModelJob;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class JobFactory {

    /**
     *
     * @param title
     *      Provide a title for the created jobs. This title does not have some
     *      special functionality but can be used to facilitate the identification
     *      of the object, so in most of the cases can be set to <code>null</code>.
     * @param start
     *      Initial value for the parameter
     * @param stop
     *      Final Value for the parameter
     * @param step
     *      The step with which the parameter increases
     * @param templatedServiceUrl
     *      Templated service allowing for easy parametrization. Use the character
     *      <code>%s</code> to locate the paramter. For example
     *      <code>http://someserver.com/service/1?param=%s</code>
     *      or <code>http://server.com/service/%s/metadata</code>.
     * @param iterations
     *      The number of iterations that each measurement is repeated. Consider
     *      using adequately large number of iterations to obtain statistical validity.
     * @return
     *      An array list of jobs parametrized with the specified range of
     *      values, i.e. <code>from START to STOP step STEP</code>.
     */
    public static ArrayList<Job> createDownloadOntModelJobs(String title, int start, int stop, int step, String templatedServiceUrl, int iterations, String gaugeName){
        ArrayList<Job> jobs = new ArrayList<Job>();
        for(int i=start; i<=stop ; i+=step){
            DownloadOntModelJob job = new DownloadOntModelJob(i, i, gaugeName);
            job.setService(templatedServiceUrl);
            job.setNumberIterations(iterations);
            job.setTitle(title);
            jobs.add(job);
        }
        return jobs;
    }

    public static ArrayList<Job> createParseDatasetJobs(String title, int start, int stop, int step, String templatedServiceUrl, int iterations){
        ArrayList<Job> jobs = new ArrayList<Job>();
        for(int i=start; i<=stop ; i+=step){
            ParseDatasetJob job = new ParseDatasetJob(i, i);
            job.setService(templatedServiceUrl);
            job.setNumberIterations(iterations);
            job.setTitle(title);
            jobs.add(job);
        }
        return jobs;
    }

}

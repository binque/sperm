package com.dirlt.java.mr;

import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobStatus;

import java.io.IOException;

public class ClusterSummary {
    public static void main(String[] args) throws IOException {
        JobConf jobConf = new JobConf();
        JobClient jobClient = new JobClient(jobConf);
        JobStatus[] jobStatuses = jobClient.getAllJobs();
        for (JobStatus jobStatus : jobStatuses) {
            System.out.println("JOB=jobId:" + jobStatus.getJobID().toString() + ",setup:"
                    + jobStatus.setupProgress() + ",cleanup:" + jobStatus.cleanupProgress()
                    + ",map:" + jobStatus.mapProgress() + ",reduce:"
                    + jobStatus.reduceProgress() + ",status:"
                    + JobStatus.getJobRunState(jobStatus.getRunState()));
        }
        ClusterStatus cs = jobClient.getClusterStatus(true);
        System.out.println("CSR=map-cap:" + cs.getMaxMapTasks() + ",map-used:"
                + cs.getMapTasks() + ",reduce-cap:" + cs.getMaxReduceTasks()
                + ",reduce-used:" + cs.getReduceTasks());
    }
}

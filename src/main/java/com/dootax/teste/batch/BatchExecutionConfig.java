package com.dootax.teste.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
public class BatchExecutionConfig {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BatchProperties properties;

    @Async
    @Scheduled(fixedRateString = "${app.batch.tempo-execucao-job-milisegundos}")
    public void jobScheduler() throws Exception {
        if (properties.isHabilitarProcesso()) {
            Date startDate = new Date();
            JobExecution execution = jobLauncher
                    .run(job, new JobParametersBuilder()
                    .addDate("launch date",  startDate)
                    .toJobParameters());
        }
    }

}

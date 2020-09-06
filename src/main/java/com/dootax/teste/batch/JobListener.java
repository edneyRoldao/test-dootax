package com.dootax.teste.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("iniciando job ID={} NOME={}.", jobExecution.getJobId(), jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Finalizando job ID={} NOME={}. O job levou {} segundos(s) para completar.",
                jobExecution.getJobId(),
                jobExecution.getJobInstance().getJobName(),
                (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime())/1000
        );

        log.info("Finalizando job ID={} NOME={}.", jobExecution.getJobId(), jobExecution.getJobInstance().getJobName());
    }
}

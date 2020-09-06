package com.dootax.teste.batch;

import com.dootax.teste.batch.tasklet.LerArquivosTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BatchJobConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobListener jobListener;

    @Bean
    public Job job() {
        return jobs
                .get("processarChavesDocumentosJob")
                .start(processarArquivos())
                .listener(jobListener)
                .build();
    }

    @Bean
    protected Step processarArquivos() {
        return steps
                .get("processarArquivos")
                .tasklet(lerArquivosTask())
                .build();
    }

    @Bean
    public LerArquivosTask lerArquivosTask() {
        return new LerArquivosTask();
    }

}

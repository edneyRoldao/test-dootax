package com.dootax.teste.batch;

import com.dootax.teste.batch.tasklet.LeitorArquivoTasklet;
import com.dootax.teste.batch.tasklet.LeitorLinhasArquivosTasklet;
import com.dootax.teste.batch.tasklet.PersistirLinhasTasklet;
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
                .next(processarLinhas())
                .next(processarPersistenciaLinhas())
                .listener(jobListener)
                .build();
    }

    @Bean
    protected Step processarArquivos() {
        return steps
                .get("processarArquivos")
                .tasklet(leitorArquivoTasklet())
                .build();
    }

    @Bean
    protected Step processarLinhas() {
        return steps
                .get("processarLinhas")
                .tasklet(leitorLinhasArquivosTasklet())
                .build();
    }

    @Bean
    protected Step processarPersistenciaLinhas() {
        return steps
                .get("processarPersistenciaLinhas")
                .tasklet(persistirLinhasTasklet())
                .build();
    }

    @Bean
    public LeitorArquivoTasklet leitorArquivoTasklet() {
        return new LeitorArquivoTasklet();
    }

    @Bean
    public LeitorLinhasArquivosTasklet leitorLinhasArquivosTasklet() {
        return new LeitorLinhasArquivosTasklet();
    }

    @Bean
    public PersistirLinhasTasklet persistirLinhasTasklet() {
        return new PersistirLinhasTasklet();
    }

}

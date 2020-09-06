package com.dootax.teste.batch;

import com.dootax.teste.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Slf4j
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
        try {
            Set<Path> paths = IOUtil.getFilteredPaths(properties.getCaminhoDiretorio(), properties.getExtensoesArquivo());

            if (properties.isHabilitarProcesso() && Objects.nonNull(paths) && !paths.isEmpty()) {
                Date startDate = new Date();
                JobExecution execution = jobLauncher
                        .run(job, new JobParametersBuilder()
                                .addDate("launch date",  startDate)
                                .toJobParameters());
            }

        } catch (IOException e) {
            log.error("ocorreu um erro no caminho do diretório informado");
        }

    }

}

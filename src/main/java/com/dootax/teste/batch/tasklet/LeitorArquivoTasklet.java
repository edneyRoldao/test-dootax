package com.dootax.teste.batch.tasklet;

import com.dootax.teste.batch.BatchProperties;
import com.dootax.teste.service.ProcessamentoArquivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import static com.dootax.teste.util.IOUtil.getFilteredPaths;

@Slf4j
public class LeitorArquivoTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private BatchProperties properties;

    @Autowired
    private ProcessamentoArquivoService arquivoService;


    private List<Future<Map<Path, Set<String>>>> processos;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        processos = new ArrayList<>();
    }


    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext chunk) throws Exception {
        log.info("LerArquivosTask.execute inicio");

        // obtendo arquivos
        String caminhoPasta = properties.getCaminhoDiretorio();
        List<String> extensoesFiltro = properties.getExtensoesArquivo();
        Set<Path> pathsProcessar = getFilteredPaths(caminhoPasta, extensoesFiltro);
        List<Path> pathsList = new ArrayList<>(pathsProcessar);

        processos = processarArquivosAsync(pathsList);

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("processos", processos);

        return ExitStatus.COMPLETED;
    }


    private List<Future<Map<Path, Set<String>>>> processarArquivosAsync(List<Path> paths) {
        log.info("LerArquivosTask.processarArquivosAsync");

        int contadorProcessos = 0;
        int totalItems = paths.size();
        boolean isContinuarChamandoAsync = true;
        List<Future<Map<Path, Set<String>>>> processos = new ArrayList<>();
        int totalItensPorProcesso = properties.getTotalArquivosPorProcesso();

        while (isContinuarChamandoAsync) {
            Future<Map<Path, Set<String>>> processo;

            if (totalItems > totalItensPorProcesso) {
                int inicio = contadorProcessos * totalItensPorProcesso;
                int fim = inicio + totalItensPorProcesso;
                totalItems -= totalItensPorProcesso;
                contadorProcessos++;
                processo = arquivoService.processarArquivosAsync(paths.subList(inicio, fim));

            } else {
                int fim = paths.size();
                int inicio = contadorProcessos * totalItensPorProcesso;
                isContinuarChamandoAsync = false;
                processo = arquivoService.processarArquivosAsync(paths.subList(inicio, fim));
            }

            log.info("total processos processamento arquivos {}", contadorProcessos);
            processos.add(processo);
        }

        return processos;
    }

}

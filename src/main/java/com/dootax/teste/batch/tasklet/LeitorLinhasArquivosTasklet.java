package com.dootax.teste.batch.tasklet;

import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.service.ChaveDocumentoService;
import com.dootax.teste.service.ProcessamentoArquivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Future;

@Slf4j
public class LeitorLinhasArquivosTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private ProcessamentoArquivoService arquivoService;

    @Autowired
    private ChaveDocumentoService chaveDocumentoService;

    private List<Future<Map<Path, Set<String>>>> processosArquivos;
    private Map<Integer, Set<String>> mapaLinhasPorArquivo;
    private List<Future<List<ChaveDocumento>>> processosLinhas;

    @Override
    @SuppressWarnings("unchecked")
    public void beforeStep(StepExecution stepExecution) {
        processosLinhas = new ArrayList<>();
        mapaLinhasPorArquivo = new HashMap<>();
        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
        processosArquivos = (List<Future<Map<Path, Set<String>>>>) context.get("processos");
    }

    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext chunk) throws Exception {
        log.info("LeitorLinhasArquivosTasklet.execute inicio");

        processarArquivosFinalizados();
        processarLinhasAsync();

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("processosLinhas", processosLinhas);

        return ExitStatus.COMPLETED;
    }

    private void processarLinhasAsync() {
        mapaLinhasPorArquivo.forEach((k, v) -> {
            Future<List<ChaveDocumento>> processo = chaveDocumentoService.chavesDocumentosBuilder(v);
            processosLinhas.add(processo);
        });
    }

    private void processarArquivosFinalizados() throws Exception {
        while (true) {
            if (processosArquivos.stream().allMatch(Future::isDone)) break;
        }

        int contadorProcessos = 1;
        for (Future<Map<Path, Set<String>>> processo : processosArquivos) {
            Map<Path, Set<String>> mapaArquivos = processo.get();
            Set<Path> paths = mapaArquivos.keySet();

            arquivoService.deletarArquivosProcessadosValidos(paths);

            // adicionando as linhas de cada arquivo em um map
            int contadorPaths = 1;
            for (Path path : paths) {
                Integer numeroArquivo = contadorPaths + contadorProcessos;
                Set<String> linhas = mapaArquivos.get(path);
                mapaLinhasPorArquivo.put(numeroArquivo, linhas);
                contadorPaths++;
            }

            contadorProcessos++;
        }
    }

}

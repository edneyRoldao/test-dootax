package com.dootax.teste.batch.tasklet;

import com.dootax.teste.batch.BatchProperties;
import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.service.ChaveDocumentoService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
public class PersistirLinhasTasklet implements Tasklet, StepExecutionListener {

    @Autowired
    private ChaveDocumentoService chaveDocumentoService;

    @Autowired
    private BatchProperties properties;

    private List<Future<List<ChaveDocumento>>> processosLinhas;

    @Override
    @SuppressWarnings("unchecked")
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext context = stepExecution.getJobExecution().getExecutionContext();
        processosLinhas = (List<Future<List<ChaveDocumento>>>) context.get("processosLinhas");
    }

    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext chunk) throws Exception {
        log.info("PersistirLinhasTasklet.execute inicio");

        while (true) {
            if (processosLinhas.stream().allMatch(Future::isDone)) break;
        }

        List<Future<Integer>> persistenciaProcessos = new ArrayList<>();

        for (Future<List<ChaveDocumento>> processo : processosLinhas) {
            List<ChaveDocumento> docs = processo.get();
            List<Future<Integer>> subProcessos = processarChavesDocumentosAsync(docs);
            persistenciaProcessos.addAll(subProcessos);
        }

        while (true) {
            if (persistenciaProcessos.stream().allMatch(Future::isDone)) break;
        }

        Integer totalChavesPersistidas = 0;
        for (Future<Integer> p : persistenciaProcessos) {
            totalChavesPersistidas += p.get();
        }

        log.info("O total do chaves documentos persistidos foi: {}", totalChavesPersistidas);

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    private List<Future<Integer>> processarChavesDocumentosAsync(List<ChaveDocumento> docs) {
        log.info("LerArquivosTask.processarChavesDocumentosAsync");

        int contadorProcessos = 0;
        int totalItems = docs.size();
        boolean isContinuarChamandoAsync = true;
        List<Future<Integer>> processos = new ArrayList<>();
        int totalItensPorProcesso = properties.getTotalLinhasPorProcesso();

        while (isContinuarChamandoAsync) {
            Future<Integer> processo;

            if (totalItems > totalItensPorProcesso) {
                int inicio = contadorProcessos * totalItensPorProcesso;
                int fim = inicio + totalItensPorProcesso;
                totalItems -= totalItensPorProcesso;
                contadorProcessos++;
                processo = chaveDocumentoService.salvarTodosAsync(docs.subList(inicio, fim));

            } else {
                int fim = docs.size();
                int inicio = contadorProcessos * totalItensPorProcesso;
                isContinuarChamandoAsync = false;
                processo = chaveDocumentoService.salvarTodosAsync(docs.subList(inicio, fim));
            }

            log.info("total processos persistencia chaves documentos {}", contadorProcessos);
            processos.add(processo);
        }

        return processos;
    }

}

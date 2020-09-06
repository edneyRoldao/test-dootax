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

        if (Objects.isNull(processosArquivos))
            processosArquivos = new ArrayList<>();
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
        log.info("Arquivos sendo processados. total processos: {}", processosArquivos.size());

        while (true) {
            if (processosArquivos.stream().allMatch(Future::isDone)) break;
        }

        log.info("Arquivos processados com sucesso");

        Map<Path, Set<String>> mapaArquivos = new HashMap<>();

        for (Future<Map<Path, Set<String>>> processosArquivo : processosArquivos) {
            mapaArquivos.putAll(processosArquivo.get());
        }

        Integer total = 1;
        Set<Path> paths = mapaArquivos.keySet();

        for (Path path : paths) {
            mapaLinhasPorArquivo.put(total, mapaArquivos.get(path));
            total++;
        }

        arquivoService.deletarArquivosProcessadosValidos(paths);
    }

}

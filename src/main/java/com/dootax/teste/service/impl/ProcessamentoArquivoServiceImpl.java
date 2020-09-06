package com.dootax.teste.service.impl;

import com.dootax.teste.service.ProcessamentoArquivoService;
import com.dootax.teste.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessamentoArquivoServiceImpl implements ProcessamentoArquivoService {

    @Value("${app.validador.formato-linha}")
    private String formatoLinhaRegex;

    @Override
    public boolean isFormatoValido(String primeiraLinha) {
        log.info("ProcessamentoArquivoServiceImpl.isFormatoArquivo");
        return Objects.nonNull(primeiraLinha) && primeiraLinha.matches(formatoLinhaRegex);
    }

    @Async
    @Override
    public Future<Map<Path, Set<String>>> processarArquivosAsync(List<Path> arquivosPath) {
        log.info("ProcessamentoArquivoServiceImpl.processarArquivos");

        Map<Path, Set<String>> pathAndLinesMap = new HashMap<>();

        arquivosPath.forEach(path -> {
            try {
                BufferedReader reader = Files.newBufferedReader(path);
                Set<String> linesRead = reader.lines().collect(Collectors.toSet());

                if (linesRead.isEmpty()) {
                    log.error("O arquivo {} está vazio", path.toString());
                } else if (isFormatoInvalido(linesRead.stream().findFirst().get())) {
                    log.error("O formato do arquivo {} é inválido", path.toString());
                } else {
                    log.info("O arquivo {} é válido", path.toString());
                    pathAndLinesMap.put(path, linesRead);
                }

            } catch (IOException e) {
                log.error("Ocorreu um erro no processamento do arquivo {}", path.toString());
            }
        });

        return new AsyncResult<>(pathAndLinesMap);
    }

    @Override
    public void deletarArquivosProcessadosValidos(Set<Path> paths) {
        log.info("ProcessamentoArquivoServiceImpl.deletarArquivosProcessadosValidos");
        paths.forEach(p -> {
            try {
                boolean isRemovido = IOUtil.deletarArquivo(p);
                if (isRemovido)
                    log.info("o arquivo: {} foi removido", p.toString());

            } catch (IOException e) {
                log.error("ocorreu um erro e não foi possível deletar o arquivo {}", p.toString());
                e.printStackTrace();
            }
        });
    }

    private boolean isFormatoInvalido(String primeiraLinha) {
        return !isFormatoValido(primeiraLinha);
    }

}

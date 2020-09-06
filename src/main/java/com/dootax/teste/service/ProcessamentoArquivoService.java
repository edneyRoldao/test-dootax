package com.dootax.teste.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public interface ProcessamentoArquivoService {

    boolean isFormatoValido(String primeiraLinha);

    Future<Map<Path, Set<String>>> processarArquivosAsync(List<Path> arquivosPath);

    void deletarArquivosProcessadosValidos(Set<Path> paths);


}

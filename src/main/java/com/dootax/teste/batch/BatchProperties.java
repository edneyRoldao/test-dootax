package com.dootax.teste.batch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.batch")
public class BatchProperties {

    private String caminhoDiretorio;
    private boolean habilitarProcesso;
    private int totalLinhasPorProcesso;
    private int tempoExecucaoJobSegundos;
    private int totalArquivosPorProcesso;
    private List<String> extensoesArquivo;

}

package com.dootax.teste.service.impl;

import com.dootax.teste.service.ValidadorChaveDocumentoService;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ValidadorChaveDocumentoServiceImpl implements ValidadorChaveDocumentoService {

    @Value("${app.validador.total-chaves-validacao}")
    private int totalChavesValidarPorVez;

    @Value("${app.validador.total-lista-chaves}")
    private int totalListaChavesPesquisa;

    @Override
    public String validadorChavesRequest(int idEmpresa, List<ChaveDocumentoRequestVO> chaves) {
        if (idEmpresa <= 0)
            return "o id da empresa deve ser informado";

        if (Objects.isNull(chaves) || chaves.isEmpty())
            return "pelo menos uma chave deve ser informada";

        if (chaves.size() > totalChavesValidarPorVez)
            return String.format("O total chaves para ser validado por vez é:%s", totalChavesValidarPorVez);

        return null;
    }

    @Override
    public String validadorListaChavesRequest(int pageSize) {
        if (pageSize <= 0)
            return " a quantidade de ítens por página deve ser maior que zero";

        if (pageSize > 20)
            return String.format("O valor máximo da lista de chaves é: %s", totalListaChavesPesquisa);

        return null;
    }

}

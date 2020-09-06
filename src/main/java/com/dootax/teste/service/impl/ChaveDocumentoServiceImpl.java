package com.dootax.teste.service.impl;

import com.dootax.teste.enums.StatusChave;
import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.repository.ChaveDocumentoRepository;
import com.dootax.teste.service.ChaveDocumentoService;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChaveDocumentoServiceImpl implements ChaveDocumentoService {

    @Value("${app.validador.chave}")
    private String validadorChaveRegex;

    @Value("${app.validador.formato-linha}")
    private String formatoLinhaRegex;

    private final ChaveDocumentoRepository repository;

    @Override
    public void salvarTodos(List<ChaveDocumento> chaves) {
        log.info("## ChaveDocumentoServiceImpl.salvarTodos ##");
        List<ChaveDocumento> chavesPersistidas = repository.saveAll(chaves);
        log.info("total chaves adicionadas no banco: {}", chavesPersistidas.size());
    }

    @Async
    @Override
    public Future<Integer> salvarTodosAsync(List<ChaveDocumento> chaves) {
        log.info("## ChaveDocumentoServiceImpl.salvarTodos ##");
        List<ChaveDocumento> chavesPersistidas = repository.saveAll(chaves);
        log.info("total chaves adicionadas no banco: {}", chavesPersistidas.size());
        return new AsyncResult<>(chavesPersistidas.size());
    }

    @Override
    public List<ChaveDocumentoResponseVO> validarChaves(int idEmpresa, List<ChaveDocumentoRequestVO> chaves) {
        log.info("## ChaveDocumentoServiceImpl.validarChaves ##");

        List<BigInteger> chavesRequest = chaves
                .stream()
                .map(ChaveDocumentoRequestVO::getValorChave)
                .collect(Collectors.toList());

        List<ChaveDocumento> chavesEncontradas = repository.findAllByIdEmpresaAndValorChaveIn(idEmpresa, chavesRequest);
        chavesEncontradas.forEach(chave -> chave.setStatus(StatusChave.VALIDADO));
        salvarTodos(chavesEncontradas);

        log.info("total chaves validadas {}.", chavesEncontradas.size());

        return chavesDocumentosResponseBuilder(chavesRequest, chavesEncontradas);
    }

    @Async
    @Override
    public Future<List<ChaveDocumento>> chavesDocumentosBuilder(Set<String> linhas) {
        log.info("## ChaveDocumentoServiceImpl.chavesDocumentosBuilder ##");

        List<ChaveDocumento> docs = new ArrayList<>();

        linhas.forEach(linha -> {
            if (linha.matches(formatoLinhaRegex)) {
                String[] token = linha.split(";");
                docs.add(ChaveDocumento.chaveDocumentoBuider(token[0], token[1]));
            }
        });

        return new AsyncResult<>(docs);
    }

    @Override
    public Page<ChaveDocumento> buscarDocumentosPaginado(int idEmpresa, BigInteger chave, int pageSize, int pageNumber) {
        log.info("## ChaveDocumentoServiceImpl.buscarDocumentosPaginado ##");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());

        if (idEmpresa != 0 && Objects.nonNull(chave)) {
            log.info(" buscarPorChaveAndIdEmmpresa. empresa: {}, chave: {}", idEmpresa, chave);
            return repository.buscarPorChaveAndIdEmmpresa(idEmpresa, chave, pageRequest);
        }

        if (idEmpresa != 0) {
            log.info(" buscarPorIdEmpresa. empresa: {}", idEmpresa);
            return repository.buscarPorIdEmpresa(idEmpresa, pageRequest);
        }

        if (Objects.nonNull(chave)) {
            log.info(" buscarPorChave. chave: {}", chave);
            return repository.buscarPorChave(chave, pageRequest);
        }

        return repository.buscarTodos(pageRequest);
    }

    private List<ChaveDocumentoResponseVO> chavesDocumentosResponseBuilder(List<BigInteger> chavesRequest, List<ChaveDocumento> chavesValidas) {
        log.info("## ChaveDocumentoServiceImpl.chavesDocumentosResponseBuilder ##");

        List<ChaveDocumentoResponseVO> chavesResponse = new ArrayList<>();

        chavesRequest.forEach(chave -> {
            ChaveDocumentoResponseVO chaveResponse = new ChaveDocumentoResponseVO(chave);

            if (chavesValidas.stream().anyMatch(cv -> cv.getValorChave().equals(chave))) {
                chaveResponse.setStatus(StatusChave.VALIDADO);
            } else {
                chaveResponse.setStatus(StatusChave.VALIDADO);
                String erroMsg = obterMensagemErro(chave);
                chaveResponse.setErroValidaoMsg(erroMsg);
            }

            chavesResponse.add(chaveResponse);
        });

        log.info(" chaves request validadas");

        return chavesResponse;
    }

    private String obterMensagemErro(BigInteger chave) {
        log.info("## ChaveDocumentoServiceImpl.obterMensagemErro ##");

        if (Objects.isNull(chave) || chave.toString().length() == 0)
            return "chave não informada (vazia)";

        if (chave.toString().matches(this.validadorChaveRegex))
            return "chave não existe";

        return "formato da chave inválida";
    }

}

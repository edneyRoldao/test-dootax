package com.dootax.teste.service;

import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public interface ChaveDocumentoService {

    void salvarTodos(List<ChaveDocumento> chaves);

    Future<Integer> salvarTodosAsync(List<ChaveDocumento> chaves);

    List<ChaveDocumentoResponseVO> validarChaves(int companyId, List<ChaveDocumentoRequestVO> chaves);

    Future<List<ChaveDocumento>> chavesDocumentosBuilder(Set<String> linhas);

    Page<ChaveDocumento> buscarDocumentosPaginado(int idEmpresa, BigInteger chave, int pageSize, int pageNumber);

}

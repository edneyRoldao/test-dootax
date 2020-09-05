package com.dootax.teste.service;

import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface ChaveDocumentoService {

    void salvarTodos(List<ChaveDocumento> chaves);

    List<ChaveDocumentoResponseVO> validarChaves(int companyId, List<ChaveDocumentoRequestVO> chaves);

    Page<ChaveDocumento> buscarDocumentosPaginado(int idEmpresa, BigInteger chave, int pageSize, int pageNumber);

}

package com.dootax.teste.service;

import com.dootax.teste.vo.ChaveDocumentoRequestVO;

import java.util.List;

public interface ValidadorChaveDocumentoService {

    String validadorChavesRequest(int idEmpresa, List<ChaveDocumentoRequestVO> chaves);

    String validadorListaChavesRequest(int pageSize);

}

package com.dootax.teste.service;

import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.repository.ChaveDocumentoRepository;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChaveDocumentoServiceImpl implements ChaveDocumentoService {

    private final ChaveDocumentoRepository repository;

    @Override
    public void salvarTodos(List<ChaveDocumento> chaves) {

    }

    @Override
    public List<ChaveDocumentoResponseVO> validarChaves(int companyId, List<ChaveDocumentoRequestVO> chaves) {
        return null;
    }

    @Override
    public Page<ChaveDocumento> buscarDocumentosPaginado(int idEmpresa, BigInteger chave, int pageSize, int pageNumber) {
        return null;
    }

}

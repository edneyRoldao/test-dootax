package com.dootax.teste.rest;

import com.dootax.teste.exception.BadRequestException;
import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.service.ChaveDocumentoService;
import com.dootax.teste.service.ValidadorChaveDocumentoService;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChaveDocumentoController implements ChaveDocumentoAPI {

    private final ChaveDocumentoService service;
    private final ValidadorChaveDocumentoService validadorChave;

    @Override
    public ResponseEntity<List<ChaveDocumentoResponseVO>> validarChaves(int idEmpresa, List<ChaveDocumentoRequestVO> chaves) {
        String mensageErro = validadorChave.validadorChavesRequest(idEmpresa, chaves);

        if (Objects.nonNull(mensageErro))
            throw new BadRequestException(mensageErro);

        List<ChaveDocumentoResponseVO> keysValidated = service.validarChaves(idEmpresa, chaves);

        return ResponseEntity.status(HttpStatus.OK).body(keysValidated);
    }

    @Override
    public ResponseEntity<Page<ChaveDocumento>> listarChaves(int idEmpresa, BigInteger chaveDocumento, int pageSize, int pageNumber) {
        String mensageErro = validadorChave.validadorListaChavesRequest(pageSize);

        if (Objects.nonNull(mensageErro))
            throw new BadRequestException(mensageErro);

        Page<ChaveDocumento> keysList = service.buscarDocumentosPaginado(idEmpresa, chaveDocumento, pageSize, pageNumber);

        return ResponseEntity.status(HttpStatus.OK).body(keysList);
    }

}

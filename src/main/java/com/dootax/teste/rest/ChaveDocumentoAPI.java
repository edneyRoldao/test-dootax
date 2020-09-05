package com.dootax.teste.rest;

import com.dootax.teste.model.ChaveDocumento;
import com.dootax.teste.vo.ChaveDocumentoRequestVO;
import com.dootax.teste.vo.ChaveDocumentoResponseVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static com.dootax.teste.util.MensagensSwagger.*;

@RequestMapping("api/chave-documento")
public interface ChaveDocumentoAPI {

    @PutMapping("{idEmpresa}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = UPDATE_DOCS_CHAVE, notes = UPDATE_DOCS_CHAVE_NOTE)
    ResponseEntity<List<ChaveDocumentoResponseVO>> validarChaves(
            @PathVariable int idEmpresa,
            @RequestBody List<ChaveDocumentoRequestVO> chaves);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = GET_DOCS_CHAVE, notes = GET_DOCS_CHAVE_NOTE)
    ResponseEntity<Page<ChaveDocumento>> listarChaves(
            @RequestParam(required = false) int idEmpresa,
            @RequestParam(required = false) BigInteger chaveDocumento,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int pageNumber);

}

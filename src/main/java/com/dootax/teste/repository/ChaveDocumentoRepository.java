package com.dootax.teste.repository;

import com.dootax.teste.model.ChaveDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ChaveDocumentoRepository extends PagingAndSortingRepository<ChaveDocumento, Long>, JpaRepository<ChaveDocumento, Long> {
    String SQL_DOCS_TODOS  = "SELECT cd.* FROM chave_documento cd";
    String SQL_DOCS_POR_CHAVE  = "SELECT cd.* FROM chave_documento cd WHERE cd.valor_chave = :chave";
    String SQL_DOCS_POR_EMPRESA  = "SELECT cd.* FROM chave_documento cd WHERE cd.id_empresa = :idEmpresa";
    String SQL_DOCS_POR_EMPRESA_AND_CHAVE  = "SELECT cd.* FROM chave_documento cd WHERE cd.id_empresa = :idEmpresa AND cd.valor_chave = :chave";

    List<ChaveDocumento> findAllByIdEmpresaAndValorChaveIn(int idEmpresa, List<BigInteger> chaves);

    @Query(value = SQL_DOCS_TODOS, nativeQuery = true)
    Page<ChaveDocumento> buscarTodos(Pageable pageable);

    @Query(value = SQL_DOCS_POR_CHAVE, nativeQuery = true)
    Page<ChaveDocumento> buscarPorChave(@Param("chave") BigInteger chave, Pageable pageable);

    @Query(value = SQL_DOCS_POR_EMPRESA, nativeQuery = true)
    Page<ChaveDocumento> buscarPorIdEmpresa(@Param("idEmpresa") int idEmpresa, Pageable pageable);

    @Query(value = SQL_DOCS_POR_EMPRESA_AND_CHAVE, nativeQuery = true)
    Page<ChaveDocumento> buscarPorChaveAndIdEmmpresa(@Param("idEmpresa") int idEmpresa, @Param("chave") BigInteger chave, Pageable pageable);

}

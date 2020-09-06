package com.dootax.teste.model;

import com.dootax.teste.enums.StatusChave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
@Table(name = "chave_documento")
public class ChaveDocumento {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 6, nullable = false)
    private int idEmpresa;

    @Column(nullable = false, precision = 44)
    private BigInteger valorChave;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 10, nullable = false)
    private StatusChave status;

    public static ChaveDocumento chaveDocumentoBuider(String idEmpresa, String valorChave) {
        return ChaveDocumento
                .builder()
                .idEmpresa(new Integer(idEmpresa))
                .valorChave(new BigInteger(valorChave))
                .status(StatusChave.CRIADO)
                .build();
    }

}

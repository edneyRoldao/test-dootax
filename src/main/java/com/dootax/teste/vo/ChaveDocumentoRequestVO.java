package com.dootax.teste.vo;

import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChaveDocumentoRequestVO {

    private BigInteger valorChave;

}

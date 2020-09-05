package com.dootax.teste.vo;

import com.dootax.teste.enums.StatusChave;
import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChaveDocumentoResponseVO {

    private BigInteger valorChave;
    private StatusChave status;
    private String erroValidaoMsg;

    public ChaveDocumentoResponseVO(BigInteger valorChave) {
        this.valorChave = valorChave;
    }

}

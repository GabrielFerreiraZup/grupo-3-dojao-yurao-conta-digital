package br.com.zup.edu.dojaoyurao.model.dto;

import br.com.zup.edu.dojaoyurao.model.Conta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class DtoTransacaoEntrada {


    @Positive
    private BigDecimal valor;
    @NotNull
    private Long idCliente;

    public DtoTransacaoEntrada(BigDecimal valor, Long idCliente) {
        this.valor = valor;
        this.idCliente = idCliente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Long getIdCliente() {
        return idCliente;
    }
}

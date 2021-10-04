package br.com.zup.edu.dojaoyurao.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String numeroConta;
    @NotNull
    private Long idCliente;
    @NotNull
    private BigDecimal saldo;

    @Deprecated
    public Conta() {
    }

    public Conta(String numeroConta, Long idCliente, BigDecimal saldo) {
        this.numeroConta = numeroConta;
        this.idCliente = idCliente;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
    public void deposita(BigDecimal valor){
        this.saldo = this.saldo.add(valor);
    }
    public void debita(BigDecimal valor){
        this.saldo = this.saldo.subtract(valor);
    }
}

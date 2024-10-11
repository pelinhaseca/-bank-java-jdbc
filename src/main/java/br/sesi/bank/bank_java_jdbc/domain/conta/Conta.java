package br.sesi.bank.bank_java_jdbc.domain.conta;

import br.sesi.bank.bank_java_jdbc.domain.cliente.Cliente;

import java.math.BigDecimal;

public class Conta {
    private Integer numero;
    private BigDecimal valor;
    private Cliente titular;

    public Conta(Integer numero , BigDecimal saldo,Cliente titular) {
    }
    public boolean possuiSaldo(){
        return true;
    }
    public void sacar(BigDecimal valor){
    }
    public void depositar(BigDecimal valor){
    }
    public Integer getNumero(){
        return numero;
    }
    public BigDecimal getSaldo(){
        return valor;
    }
    public Cliente getTitular(){
        return titular;
    }
   public boolean equais(Object o){
        return true;
   }
    public int hashCode() {
        return 0;
    }
}

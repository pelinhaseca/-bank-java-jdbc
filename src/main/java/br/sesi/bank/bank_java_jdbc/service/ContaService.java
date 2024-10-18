package br.sesi.bank.bank_java_jdbc.service;

import br.sesi.bank.bank_java_jdbc.domain.cliente.Cliente;
import br.sesi.bank.bank_java_jdbc.domain.conta.Conta;
import br.sesi.bank.bank_java_jdbc.domain.conta.DadosAberturaConta;
import br.sesi.bank.bank_java_jdbc.exeptions.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Locale.filter;

public class ContaService {

    private Set<Conta> contas = new HashSet<>();

    public ContaService(){}
    public  Set<Conta> listarContasAbertas(){
        return this.contas;
    }
    public BigDecimal consultarSaldo(Integer numeroDaConta){
        Conta conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }
    public void abrir(DadosAberturaConta dadosDaConta) throws SQLException {
       Cliente cliente = new Cliente(dadosDaConta.dadosCliente);
       Conta conta = new Conta(dadosDaConta.numero, BigDecimal.ZERO, cliente);
       if (contas.contains(conta)){
           throw new RegraDeNegocioException("Já existe outra conta aberta com o mesmo número!");
       }
       contas.add(conta);
    }
    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
       var conta = buscarContaPorNumero(numeroDaConta);
       if (valor.compareTo(BigDecimal.ZERO) <= 0){
           throw new RegraDeNegocioException("valor do saque deve ser superior a zero!");
       }
       if (valor.compareTo(conta.getSaldo()) > 0){
           throw new RegraDeNegocioException("saldo insuficiente!");
       }
       conta.sacar(valor);
    }
    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
      var conta = buscarContaPorNumero(numeroDaConta);
      if (valor.compareTo(BigDecimal.ZERO) <=0){
          throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
      }
      contas.remove(conta);
    }
    public void realizaTransferencia(Integer numeroContaOrigem, Integer numeroContaDestino, BigDecimal valor){

    }
    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()){
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo");
        }

        contas.remove(conta);
    }
    private Conta buscarContaPorNumero(Integer numero) {
        return contas
                .stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("não existe conta cadastrada com esse número!"));
    }
    private void alterar(Integer numeroDaConta, BigDecimal valor) { }


}

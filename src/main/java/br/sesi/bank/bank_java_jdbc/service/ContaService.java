package br.sesi.bank.bank_java_jdbc.service;

import br.sesi.bank.bank_java_jdbc.config.ConnectionFactory;
import br.sesi.bank.bank_java_jdbc.domain.cliente.Cliente;
import br.sesi.bank.bank_java_jdbc.domain.conta.Conta;
import br.sesi.bank.bank_java_jdbc.domain.conta.ContaDAO;
import br.sesi.bank.bank_java_jdbc.domain.conta.DadosAberturaConta;
import br.sesi.bank.bank_java_jdbc.exeptions.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Locale.filter;

public class ContaService {

    private ConnectionFactory connection;

    public ContaService() {
        this.connection = new ConnectionFactory();
    }

    public Set<Conta> listarContasAbertas() {
        Connection conn = connection.recuperarConexao();
        return new ContaDAO(conn).listar();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        Conta conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) throws SQLException {
        Connection conn = connection.recuperarConexao();
        new ContaDAO(conn).salvar(dadosDaConta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroDaConta);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("saldo insuficiente!");
        }

        if (!conta.isAtivo()) {
            throw new RegraDeNegocioException("Conta não está ativa!");
        }

        BigDecimal novoValor = conta.getSaldo().subtract(valor);
        alterar(conta, novoValor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
       var conta = buscarContaPorNumero(numeroDaConta);

       if(valor.compareTo(BigDecimal.ZERO) <=0 ){
           throw new RegraDeNegocioException("valor do deposito deve ser superior a zero!");
       }
       if(!conta.isAtivo()){
           throw new RegraDeNegocioException("Conta não está ativa!");
       }

       BigDecimal novoValor = conta.getSaldo().add(valor);
       alterar(conta, novoValor);
    }

    public void realizaTransferencia(Integer numeroContaOrigem, Integer numeroContaDestino, BigDecimal valor){
        this.realizarSaque(numeroContaOrigem, valor);
        this.realizarDeposito(numeroContaDestino, valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()){
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo");
        }

        Connection conn = connection.recuperarConexao();

        new ContaDAO(conn).deletar(numeroDaConta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        Connection conn = connection.recuperarConexao();
        Conta conta = new ContaDAO(conn).listarporNumeros(numero);
        if(conta != null){
            return conta;
        }else{
            throw new RegraDeNegocioException("não existe conta cadastrada com esse número!");
        }
    }

    private void alterar(Conta conta, BigDecimal valor) {
        Connection conn = connection.recuperarConexao();
        new ContaDAO(conn).alterarSaldo(conta.getNumero(), valor);
    }
}

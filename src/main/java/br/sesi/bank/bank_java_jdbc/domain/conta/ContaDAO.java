package br.sesi.bank.bank_java_jdbc.domain.conta;

import br.sesi.bank.bank_java_jdbc.domain.cliente.Cliente;
import br.sesi.bank.bank_java_jdbc.domain.cliente.DadosCadastroCliente;
import ch.qos.logback.core.net.server.Client;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private Connection con;

    public ContaDAO(Connection con) {
        this.con = con;
    }

    public void salvar(DadosAberturaConta dadosDaConta){
        Cliente cliente = new Cliente(dadosDaConta.dadosCliente);
        Conta conta = new Conta(dadosDaConta.numero, BigDecimal.ZERO, cliente, true);
        String sql = "INSERT INTO conta (numero,saldo,cliente_nome, cliente_cpf,cliente_email, ativo)" +
                "VALUES (?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente.nome);
            preparedStatement.setString(4, dadosDaConta.dadosCliente.cpf);
            preparedStatement.setString(5, dadosDaConta.dadosCliente.email);
            preparedStatement.setBoolean(6, conta.isAtivo());
            preparedStatement.execute();
            preparedStatement.close();
            con.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar(){
       Set<Conta> contas = new HashSet<>();
       String sql = "select * from conta";
       try{
           PreparedStatement ps = con.prepareStatement(sql);
           ResultSet resultSet = ps.executeQuery();
           while(resultSet.next()){
               Integer numero = resultSet.getInt(2);
               BigDecimal saldo = resultSet.getBigDecimal(3);
               String nome = resultSet.getString(4);
               String cpf = resultSet.getString(5);
               String email = resultSet.getString(6);
               Boolean ativo = resultSet.getBoolean(7);
               Cliente cliente = new Cliente(new DadosCadastroCliente(nome,cpf,email));
               Conta conta = new Conta(numero,saldo,cliente,ativo);
               contas.add(conta);
           }
           resultSet.close();
           ps.close();
           con.close();
       }catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return contas;
    }
    public Conta listarporNumeros(Integer numero){
        String sql = "SELECT * FROM conta WHERE numero = " + numero + " and ativo = true";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try{
            ps = con.prepareStatement(sql);
            //ps.setInt(1,numero);
            resultSet = ps.executeQuery();

            while (resultSet.next()){
                Integer numeroRecuperado = resultSet.getInt(2);
                BigDecimal saldo = resultSet.getBigDecimal(3);
                String nome = resultSet.getString(4);
                String cpf = resultSet.getString(5);
                String email = resultSet.getString(6);
                Boolean estaAtiva = resultSet.getBoolean(7);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf,email);
                Cliente cliente = new Cliente (dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            con.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void alterarSaldo(Integer numero,BigDecimal valor){
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";
        PreparedStatement ps;
        try{
            ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, valor);
            ps.setInt(2, numero);
            ps.execute();
            ps.close();
            con.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Integer numeroConta){
        String sql = "DELETE FROM contas WHERE numero = ?";
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1,numeroConta);
            ps.execute();
            ps.close();
            con.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}



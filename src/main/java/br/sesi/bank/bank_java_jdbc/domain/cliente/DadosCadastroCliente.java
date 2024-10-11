package br.sesi.bank.bank_java_jdbc.domain.cliente;

import javax.lang.model.element.NestingKind;

public class DadosCadastroCliente {
    public String nome;
    public String email;
    public String cpf;

    public DadosCadastroCliente(String nome,String email, String cpf){
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }
}

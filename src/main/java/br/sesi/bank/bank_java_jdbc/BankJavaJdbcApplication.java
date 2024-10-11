package br.sesi.bank.bank_java_jdbc;

import br.sesi.bank.bank_java_jdbc.controller.BankJavaController;
import br.sesi.bank.bank_java_jdbc.domain.cliente.DadosCadastroCliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankJavaJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankJavaJdbcApplication.class, args);
	}

}

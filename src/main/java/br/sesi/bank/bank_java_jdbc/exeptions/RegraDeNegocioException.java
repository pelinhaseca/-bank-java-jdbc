package br.sesi.bank.bank_java_jdbc.exeptions;

public class RegraDeNegocioException extends RuntimeException{
    public RegraDeNegocioException(String mensagem){
        super(mensagem);
    }
}

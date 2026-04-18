package br.gov.caixa.gerenciadorfila.exception;

public class PessoaJaAtendidaException extends RuntimeException {
    public PessoaJaAtendidaException(Long id) {
        super("Pessoa com ID " + id + " já foi atendida e não pode ser modificada");
    }
}


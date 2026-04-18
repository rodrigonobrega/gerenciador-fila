package br.gov.caixa.gerenciadorfila.exception;

public class PessoaNaoEncontradaException extends RuntimeException {
    public PessoaNaoEncontradaException(Long id) {
        super("Pessoa com ID " + id + " não encontrada na fila");
    }
}


package br.gov.caixa.gerenciadorfila.exception;

public class NaoHaProximoAtendimentoException extends RuntimeException {
    public NaoHaProximoAtendimentoException() {
        super("Não mais há pessoas na fila para atendimento após a pessoa com ID ");
    }
}


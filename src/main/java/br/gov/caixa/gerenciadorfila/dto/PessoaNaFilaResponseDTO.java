package br.gov.caixa.gerenciadorfila.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.gov.caixa.gerenciadorfila.enums.StatusAtendimento;
import br.gov.caixa.gerenciadorfila.enums.TipoAtendimento;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PessoaNaFilaResponseDTO {

    private Long id;
    private String nome;
    private TipoAtendimento tipoAtendimento;
    private LocalDateTime horaEntrada;
    private StatusAtendimento status;
    private Integer posicaoNaFila;
}

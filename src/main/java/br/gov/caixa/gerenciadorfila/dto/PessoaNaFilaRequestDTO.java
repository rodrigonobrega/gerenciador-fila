package br.gov.caixa.gerenciadorfila.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.gov.caixa.gerenciadorfila.enums.TipoAtendimento;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaNaFilaRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "O tipo de atendimento é obrigatório")
    private TipoAtendimento tipoAtendimento;
}



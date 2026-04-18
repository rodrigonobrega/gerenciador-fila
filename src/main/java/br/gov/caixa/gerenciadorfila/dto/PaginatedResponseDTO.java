package br.gov.caixa.gerenciadorfila.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO<T> {

    private int totalPaginas;
    private long totalElementos;
    private int quantidadeItensPorPagina;
    private int pagina;
    private List<T> pessoas;
}


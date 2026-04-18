package br.gov.caixa.gerenciadorfila.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import br.gov.caixa.gerenciadorfila.dto.PaginatedResponseDTO;
import br.gov.caixa.gerenciadorfila.dto.PessoaNaFilaRequestDTO;
import br.gov.caixa.gerenciadorfila.dto.PessoaNaFilaResponseDTO;
import br.gov.caixa.gerenciadorfila.service.PessoaNaFilaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/fila")
@RequiredArgsConstructor
public class PessoaNaFilaController {

    private final PessoaNaFilaService service;

    @PostMapping
    public ResponseEntity<PessoaNaFilaResponseDTO> adicionarNaFila(
            @Valid @RequestBody PessoaNaFilaRequestDTO requestDTO) {
        PessoaNaFilaResponseDTO response = service.adicionarNaFila(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PessoaNaFilaResponseDTO>> listarFila(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        PaginatedResponseDTO<PessoaNaFilaResponseDTO> response = service.listarFila(pagina, tamanho);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aguardando")
    public ResponseEntity<PaginatedResponseDTO<PessoaNaFilaResponseDTO>> listarFilaAguardando(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        PaginatedResponseDTO<PessoaNaFilaResponseDTO> response = service.listarFilaAguardando(pagina, tamanho);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaNaFilaResponseDTO> buscarPorId(@PathVariable Long id) {
        PessoaNaFilaResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaNaFilaResponseDTO> atualizarPessoa(
            @PathVariable Long id,
            @Valid @RequestBody PessoaNaFilaRequestDTO requestDTO) {
        PessoaNaFilaResponseDTO response = service.atualizarPessoa(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerDaFila(@PathVariable Long id) {
        service.removerDaFila(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/atender")
    public ResponseEntity<PessoaNaFilaResponseDTO> atenderProximaPessoa() {
        PessoaNaFilaResponseDTO response = service.atenderProximaPessoa();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> estatisticas = service.obterEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}

package br.gov.caixa.gerenciadorfila.service;

import br.gov.caixa.gerenciadorfila.exception.NaoHaProximoAtendimentoException;
import lombok.RequiredArgsConstructor;
import br.gov.caixa.gerenciadorfila.dto.PaginatedResponseDTO;
import br.gov.caixa.gerenciadorfila.dto.PessoaNaFilaRequestDTO;
import br.gov.caixa.gerenciadorfila.dto.PessoaNaFilaResponseDTO;
import br.gov.caixa.gerenciadorfila.entity.PessoaNaFila;
import br.gov.caixa.gerenciadorfila.enums.StatusAtendimento;
import br.gov.caixa.gerenciadorfila.exception.PessoaJaAtendidaException;
import br.gov.caixa.gerenciadorfila.exception.PessoaNaoEncontradaException;
import br.gov.caixa.gerenciadorfila.repository.PessoaNaFilaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PessoaNaFilaService {


    private final PessoaNaFilaRepository repository;

    @Transactional
    @CacheEvict(value = {"fila", "filaPaginada", "estatisticasFila"}, allEntries = true)
    public PessoaNaFilaResponseDTO adicionarNaFila(PessoaNaFilaRequestDTO requestDTO) {
        PessoaNaFila pessoa = new PessoaNaFila();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setTipoAtendimento(requestDTO.getTipoAtendimento());
        pessoa.setStatus(StatusAtendimento.AGUARDANDO);

        PessoaNaFila pessoaSalva = repository.save(pessoa);
        return converterParaResponseDTO(pessoaSalva);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "filaPaginada", key = "#pagina + '-' + #tamanho")
    public PaginatedResponseDTO<PessoaNaFilaResponseDTO> listarFila(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho,
            Sort.by(Sort.Order.desc("tipoAtendimento"), Sort.Order.asc("horaEntrada")));

        Page<PessoaNaFila> page = repository.findAll(pageable);

        List<PessoaNaFilaResponseDTO> pessoas = page.getContent().stream()
                .map(this::converterParaResponseDTO)
                .toList();

        PaginatedResponseDTO<PessoaNaFilaResponseDTO> response = new PaginatedResponseDTO<>();
        response.setTotalPaginas(page.getTotalPages());
        response.setTotalElementos(page.getTotalElements());
        response.setQuantidadeItensPorPagina(page.getSize());
        response.setPagina(page.getNumber());
        response.setPessoas(pessoas);

        return response;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "fila", key = "#id")
    public PessoaNaFilaResponseDTO buscarPorId(Long id) {
        PessoaNaFila pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(id));
        return converterParaResponseDTO(pessoa);
    }

    @Transactional
    @CacheEvict(value = {"fila", "filaPaginada", "estatisticasFila"}, allEntries = true)
    public PessoaNaFilaResponseDTO atualizarPessoa(Long id, PessoaNaFilaRequestDTO requestDTO) {
        PessoaNaFila pessoa = repository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(id));

        if (pessoa.getStatus() == StatusAtendimento.ATENDIDO) {
            throw new PessoaJaAtendidaException(id);
        }

        pessoa.setNome(requestDTO.getNome());
        pessoa.setTipoAtendimento(requestDTO.getTipoAtendimento());

        PessoaNaFila pessoaAtualizada = repository.save(pessoa);
        return converterParaResponseDTO(pessoaAtualizada);
    }

    @Transactional
    @CacheEvict(value = {"fila", "filaPaginada", "estatisticasFila"}, allEntries = true)
    public void removerDaFila(Long id) {
        if (!repository.existsById(id)) {
            throw new PessoaNaoEncontradaException(id);
        }
        repository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"fila", "filaPaginada", "estatisticasFila"}, allEntries = true)
    public PessoaNaFilaResponseDTO atenderProximaPessoa() {
        List<PessoaNaFila> filaOrdenada = repository.findByStatusOrderByPriorityAndTime(StatusAtendimento.AGUARDANDO);

        if (filaOrdenada.isEmpty()) {
            throw new NaoHaProximoAtendimentoException();
        }

        PessoaNaFila proximaPessoa = filaOrdenada.get(0);
        proximaPessoa.setStatus(StatusAtendimento.ATENDIDO);

        PessoaNaFila pessoaAtendida = repository.save(proximaPessoa);
        return converterParaResponseDTO(pessoaAtendida);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "estatisticasFila")
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        long totalAguardando = repository.countByStatus(StatusAtendimento.AGUARDANDO);
        long totalAtendidos = repository.countByStatus(StatusAtendimento.ATENDIDO);
        long totalCancelados = repository.countByStatus(StatusAtendimento.CANCELADO);
        
        estatisticas.put("totalAguardando", totalAguardando);
        estatisticas.put("totalAtendidos", totalAtendidos);
        estatisticas.put("totalCancelados", totalCancelados);
        estatisticas.put("totalGeral", repository.count());

        return estatisticas;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<PessoaNaFilaResponseDTO> listarFilaAguardando(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho,
            Sort.by(Sort.Order.desc("tipoAtendimento"), Sort.Order.asc("horaEntrada")));

        Page<PessoaNaFila> page = repository.findByStatus(StatusAtendimento.AGUARDANDO, pageable);

        List<PessoaNaFilaResponseDTO> pessoas = page.getContent().stream()
                .map(this::converterParaResponseDTOComPosicao)
                .toList();

        PaginatedResponseDTO<PessoaNaFilaResponseDTO> response = new PaginatedResponseDTO<>();
        response.setTotalPaginas(page.getTotalPages());
        response.setTotalElementos(page.getTotalElements());
        response.setQuantidadeItensPorPagina(page.getSize());
        response.setPagina(page.getNumber());
        response.setPessoas(pessoas);

        return response;
    }

    private PessoaNaFilaResponseDTO converterParaResponseDTO(PessoaNaFila pessoa) {
        PessoaNaFilaResponseDTO dto = new PessoaNaFilaResponseDTO();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setTipoAtendimento(pessoa.getTipoAtendimento());
        dto.setHoraEntrada(pessoa.getHoraEntrada());
        dto.setStatus(pessoa.getStatus());
        return dto;
    }

    private PessoaNaFilaResponseDTO converterParaResponseDTOComPosicao(PessoaNaFila pessoa) {
        PessoaNaFilaResponseDTO dto = converterParaResponseDTO(pessoa);
        
        if (pessoa.getStatus() == StatusAtendimento.AGUARDANDO) {
            List<PessoaNaFila> filaOrdenada = repository.findByStatusOrderByPriorityAndTime(StatusAtendimento.AGUARDANDO);
            int posicao = filaOrdenada.indexOf(pessoa) + 1;
            dto.setPosicaoNaFila(posicao);
        }
        
        return dto;
    }
}


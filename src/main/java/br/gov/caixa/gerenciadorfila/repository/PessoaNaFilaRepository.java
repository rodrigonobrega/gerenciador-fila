package br.gov.caixa.gerenciadorfila.repository;

import br.gov.caixa.gerenciadorfila.entity.PessoaNaFila;
import br.gov.caixa.gerenciadorfila.enums.StatusAtendimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaNaFilaRepository extends JpaRepository<PessoaNaFila, Long> {

    Page<PessoaNaFila> findByStatus(StatusAtendimento status, Pageable pageable);

    @Query("SELECT p FROM PessoaNaFila p WHERE p.status = :status ORDER BY " +
           "CASE WHEN p.tipoAtendimento = 'PRIORITARIO' THEN 0 ELSE 1 END, " +
           "p.horaEntrada ASC")
    List<PessoaNaFila> findByStatusOrderByPriorityAndTime(StatusAtendimento status);

    long countByStatus(StatusAtendimento status);
}


package br.gov.caixa.gerenciadorfila.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.gov.caixa.gerenciadorfila.enums.StatusAtendimento;
import br.gov.caixa.gerenciadorfila.enums.TipoAtendimento;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "pessoa_na_fila")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaNaFila {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O tipo de atendimento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtendimento tipoAtendimento;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime horaEntrada;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAtendimento status = StatusAtendimento.AGUARDANDO;
}

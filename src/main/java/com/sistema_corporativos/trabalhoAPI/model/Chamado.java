package com.sistema_corporativos.trabalhoAPI.model;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chamados")
@Data
@NoArgsConstructor
public class Chamado {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 180)
  private String titulo;

  @Column(nullable = false, length = 3000)
  private String descricao;

  @Column(nullable = false, length = 180)
  private String local;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Priority prioridade;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 25)
  private TicketStatus status;

  @Column(name = "data_abertura", nullable = false)
  private LocalDateTime dataAbertura;

  @Column(name = "data_fechamento")
  private LocalDateTime dataFechamento;

  @Column(length = 3000)
  private String solucao;

  @ManyToOne(optional = false)
  @JoinColumn(name = "solicitante_id", nullable = false)
  private Usuario solicitante;

  @ManyToOne(optional = false)
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categoria categoria;

  @ManyToOne
  @JoinColumn(name = "tecnico_id")
  private Usuario tecnico;

  @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("dataHora ASC")
  private List<Interacao> interacoes = new java.util.ArrayList<>();
}

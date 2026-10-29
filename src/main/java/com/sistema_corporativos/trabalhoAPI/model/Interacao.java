package com.sistema_corporativos.trabalhoAPI.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interacoes")
@Data
@NoArgsConstructor
public class Interacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 2000)
  private String texto;

  @Column(name = "data_hora", nullable = false)
  private LocalDateTime dataHora;

  @ManyToOne(optional = false)
  @JoinColumn(name = "chamado_id", nullable = false)
  private Chamado chamado;

  @ManyToOne(optional = false)
  @JoinColumn(name = "autor_id", nullable = false)
  private Usuario autor;
}

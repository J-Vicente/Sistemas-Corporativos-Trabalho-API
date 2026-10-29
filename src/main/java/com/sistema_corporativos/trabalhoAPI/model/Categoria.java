package com.sistema_corporativos.trabalhoAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
public class Categoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(length = 500)
  private String descricao;

  @ManyToOne(optional = false)
  @JoinColumn(name = "setor_id", nullable = false)
  private Setor setor;
}

package com.sistema_corporativos.trabalhoAPI.model;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 160)
  private String nome;

  @Column(nullable = false, unique = true, length = 180)
  private String email;

  @Column(nullable = false, unique = true, length = 30)
  private String matricula;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserType tipo;

  @ManyToOne
  @JoinColumn(name = "setor_id")
  private Setor setor;
}

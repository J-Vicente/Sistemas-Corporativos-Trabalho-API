package com.sistema_corporativos.trabalhoAPI.model;

public final class DomainEnums {
  private DomainEnums() {}

  public enum UserType {
    SOLICITANTE,
    TECNICO
  }

  public enum Priority {
    BAIXA,
    MEDIA,
    ALTA
  }

  public enum TicketStatus {
    ABERTO,
    EM_ATENDIMENTO,
    RESOLVIDO,
    CANCELADO
  }
}

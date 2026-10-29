package com.sistema_corporativos.trabalhoAPI.dto.response;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.Priority;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ChamadoResponse(
    Long id,
    String titulo,
    String descricao,
    String local,
    Priority prioridade,
    TicketStatus status,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    String solucao,
    CategoriaResponse categoria,
    UsuarioResponse solicitante,
    UsuarioResponse tecnico,
    List<InteracaoResponse> interacoes) {}

package com.sistema_corporativos.trabalhoAPI.dto.response;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.Priority;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.TicketStatus;
import java.time.LocalDateTime;

public record ChamadoResumoResponse(
    Long id,
    String titulo,
    String local,
    Priority prioridade,
    TicketStatus status,
    LocalDateTime dataAbertura,
    CategoriaResumoResponse categoria,
    UsuarioResumoResponse solicitante,
    UsuarioResumoResponse tecnico) {}

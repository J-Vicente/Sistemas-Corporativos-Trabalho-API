package com.sistema_corporativos.trabalhoAPI.dto.request;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChamadoCreateRequest(
    @NotBlank @Size(max = 180) String titulo,
    @NotBlank @Size(max = 3000) String descricao,
    @NotBlank @Size(max = 180) String local,
    Priority prioridade,
    @NotNull @Positive Long solicitanteId,
    @NotNull @Positive Long categoriaId) {}

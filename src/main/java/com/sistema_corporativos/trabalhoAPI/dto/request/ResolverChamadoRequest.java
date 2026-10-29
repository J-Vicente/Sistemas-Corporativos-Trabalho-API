package com.sistema_corporativos.trabalhoAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ResolverChamadoRequest(
    @NotNull @Positive Long tecnicoId, @NotBlank @Size(max = 3000) String solucao) {}

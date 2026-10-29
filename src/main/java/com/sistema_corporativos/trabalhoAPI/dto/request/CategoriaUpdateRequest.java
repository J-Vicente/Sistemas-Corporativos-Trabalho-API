package com.sistema_corporativos.trabalhoAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CategoriaUpdateRequest(
    @NotBlank @Size(max = 120) String nome,
    @Size(max = 500) String descricao,
    @NotNull @Positive Long setorId) {}

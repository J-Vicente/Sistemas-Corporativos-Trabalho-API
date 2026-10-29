package com.sistema_corporativos.trabalhoAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorUpdateRequest(
    @NotBlank @Size(max = 120) String nome, @NotBlank @Size(max = 20) String sigla) {}

package com.sistema_corporativos.trabalhoAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorCreateRequest(
    @NotBlank @Size(max = 120) String nome, @NotBlank @Size(max = 20) String sigla) {}

package com.sistema_corporativos.trabalhoAPI.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssumirChamadoRequest(@NotNull @Positive Long tecnicoId) {}

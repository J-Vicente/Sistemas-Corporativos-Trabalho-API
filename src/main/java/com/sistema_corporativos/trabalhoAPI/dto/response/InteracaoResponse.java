package com.sistema_corporativos.trabalhoAPI.dto.response;

import java.time.LocalDateTime;

public record InteracaoResponse(
    Long id, String texto, LocalDateTime dataHora, UsuarioResumoResponse autor) {}

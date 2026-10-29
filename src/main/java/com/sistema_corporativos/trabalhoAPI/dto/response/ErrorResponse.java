package com.sistema_corporativos.trabalhoAPI.dto.response;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String mensagem, String caminho) {}

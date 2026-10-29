package com.sistema_corporativos.trabalhoAPI.dto.response;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;

public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    String matricula,
    UserType tipo,
    SetorResumoResponse setor) {}

package com.sistema_corporativos.trabalhoAPI.dto.response;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;

public record UsuarioResumoResponse(
    Long id, String nome, String email, UserType tipo, SetorResumoResponse setor) {}

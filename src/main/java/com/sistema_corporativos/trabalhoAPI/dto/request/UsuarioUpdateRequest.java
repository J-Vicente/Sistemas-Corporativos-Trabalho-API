package com.sistema_corporativos.trabalhoAPI.dto.request;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
    @NotBlank @Size(max = 160) String nome,
    @NotBlank @Email @Size(max = 180) String email,
    @NotBlank @Size(max = 30) String matricula,
    @NotNull UserType tipo,
    Long setorId) {}

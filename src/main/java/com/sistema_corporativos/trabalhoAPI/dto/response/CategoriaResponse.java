package com.sistema_corporativos.trabalhoAPI.dto.response;

public record CategoriaResponse(
    Long id, String nome, String descricao, SetorResumoResponse setor) {}

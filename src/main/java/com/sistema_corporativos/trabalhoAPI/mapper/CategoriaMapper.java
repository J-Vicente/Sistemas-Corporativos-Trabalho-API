package com.sistema_corporativos.trabalhoAPI.mapper;

import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResumoResponse;
import com.sistema_corporativos.trabalhoAPI.model.Categoria;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = SetorMapper.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoriaMapper {
  CategoriaResponse toResponse(Categoria category);

  CategoriaResumoResponse toSummary(Categoria category);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "nome", source = "request.nome")
  @Mapping(target = "descricao", source = "request.descricao")
  @Mapping(target = "setor", source = "sector")
  Categoria toEntity(CategoriaCreateRequest request, Setor sector);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "nome", source = "request.nome")
  @Mapping(target = "descricao", source = "request.descricao")
  @Mapping(target = "setor", source = "sector")
  Categoria toEntity(CategoriaUpdateRequest request, Long id, Setor sector);
}

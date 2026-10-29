package com.sistema_corporativos.trabalhoAPI.mapper;

import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResumoResponse;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import com.sistema_corporativos.trabalhoAPI.model.Usuario;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = SetorMapper.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UsuarioMapper {
  UsuarioResponse toResponse(Usuario user);

  UsuarioResumoResponse toSummary(Usuario user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "nome", source = "request.nome")
  @Mapping(target = "email", source = "request.email")
  @Mapping(target = "matricula", source = "request.matricula")
  @Mapping(target = "tipo", source = "request.tipo")
  @Mapping(target = "setor", source = "sector")
  Usuario toEntity(UsuarioCreateRequest request, Setor sector);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "nome", source = "request.nome")
  @Mapping(target = "email", source = "request.email")
  @Mapping(target = "matricula", source = "request.matricula")
  @Mapping(target = "tipo", source = "request.tipo")
  @Mapping(target = "setor", source = "sector")
  Usuario toEntity(UsuarioUpdateRequest request, Long id, Setor sector);
}

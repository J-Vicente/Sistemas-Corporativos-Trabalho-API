package com.sistema_corporativos.trabalhoAPI.mapper;

import com.sistema_corporativos.trabalhoAPI.dto.request.ChamadoCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.ChamadoResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.ChamadoResumoResponse;
import com.sistema_corporativos.trabalhoAPI.model.Categoria;
import com.sistema_corporativos.trabalhoAPI.model.Chamado;
import com.sistema_corporativos.trabalhoAPI.model.Usuario;
import java.time.LocalDateTime;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {CategoriaMapper.class, UsuarioMapper.class, InteracaoMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ChamadoMapper {
  ChamadoResponse toResponse(Chamado ticket);

  ChamadoResumoResponse toSummary(Chamado ticket);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "titulo", source = "request.titulo")
  @Mapping(target = "descricao", source = "request.descricao")
  @Mapping(target = "local", source = "request.local")
  @Mapping(target = "solicitante", source = "requester")
  @Mapping(target = "categoria", source = "category")
  @Mapping(target = "status", constant = "ABERTO")
  @Mapping(
      target = "prioridade",
      expression =
          "java(request.prioridade() == null ?"
              + " com.sistema_corporativos.trabalhoAPI.model.DomainEnums.Priority.MEDIA :"
              + " request.prioridade())")
  @Mapping(target = "dataAbertura", source = "openedAt")
  @Mapping(target = "dataFechamento", ignore = true)
  @Mapping(target = "solucao", ignore = true)
  @Mapping(target = "tecnico", ignore = true)
  @Mapping(target = "interacoes", ignore = true)
  Chamado toEntity(
      ChamadoCreateRequest request, Usuario requester, Categoria category, LocalDateTime openedAt);
}

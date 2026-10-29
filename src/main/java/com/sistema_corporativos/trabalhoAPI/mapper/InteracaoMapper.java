package com.sistema_corporativos.trabalhoAPI.mapper;

import com.sistema_corporativos.trabalhoAPI.dto.request.InteracaoCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.InteracaoResponse;
import com.sistema_corporativos.trabalhoAPI.model.Chamado;
import com.sistema_corporativos.trabalhoAPI.model.Interacao;
import com.sistema_corporativos.trabalhoAPI.model.Usuario;
import java.time.LocalDateTime;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = UsuarioMapper.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface InteracaoMapper {
  InteracaoResponse toResponse(Interacao interaction);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "texto", source = "request.texto")
  @Mapping(target = "chamado", source = "ticket")
  @Mapping(target = "autor", source = "author")
  @Mapping(target = "dataHora", source = "occurredAt")
  Interacao toEntity(
      InteracaoCreateRequest request, Chamado ticket, Usuario author, LocalDateTime occurredAt);
}

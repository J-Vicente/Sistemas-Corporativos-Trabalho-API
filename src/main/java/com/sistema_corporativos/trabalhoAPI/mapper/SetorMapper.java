package com.sistema_corporativos.trabalhoAPI.mapper;

import com.sistema_corporativos.trabalhoAPI.dto.request.SetorCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.SetorUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResumoResponse;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SetorMapper {
  SetorResponse toResponse(Setor sector);

  SetorResumoResponse toSummary(Setor sector);

  Setor toEntity(SetorCreateRequest request);

  @Mapping(target = "id", source = "id")
  Setor toEntity(SetorUpdateRequest request, Long id);
}

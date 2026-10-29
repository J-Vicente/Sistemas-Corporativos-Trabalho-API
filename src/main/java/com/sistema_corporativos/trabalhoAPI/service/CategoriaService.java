package com.sistema_corporativos.trabalhoAPI.service;

import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResumoResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.exception.BusinessRuleException;
import com.sistema_corporativos.trabalhoAPI.exception.ResourceNotFoundException;
import com.sistema_corporativos.trabalhoAPI.mapper.CategoriaMapper;
import com.sistema_corporativos.trabalhoAPI.model.Categoria;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import com.sistema_corporativos.trabalhoAPI.repository.CategoriaRepository;
import com.sistema_corporativos.trabalhoAPI.repository.ChamadoRepository;
import com.sistema_corporativos.trabalhoAPI.repository.SetorRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaService {
  private final CategoriaRepository categorias;
  private final SetorRepository setores;
  private final ChamadoRepository chamados;
  private final CategoriaMapper mapper;

  public CategoriaService(
      CategoriaRepository categorias,
      SetorRepository setores,
      ChamadoRepository chamados,
      CategoriaMapper mapper) {
    this.categorias = categorias;
    this.setores = setores;
    this.chamados = chamados;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageResponse<CategoriaResumoResponse> list(String name, Long sectorId, Pageable pageable) {
    validatePage(pageable);
    validateSectorWhenProvided(sectorId);
    return PageResponse.from(
        categorias
            .searchByNameAndSector(name != null, name, sectorId != null, sectorId, pageable)
            .map(mapper::toSummary));
  }

  @Transactional(readOnly = true)
  public PageResponse<CategoriaResumoResponse> listBySector(Long sectorId, Pageable pageable) {
    validatePage(pageable);
    getSector(sectorId);
    return PageResponse.from(
        categorias
            .searchByNameAndSector(false, null, true, sectorId, pageable)
            .map(mapper::toSummary));
  }

  @Transactional(readOnly = true)
  public CategoriaResponse find(Long id) {
    return mapper.toResponse(get(id));
  }

  public CategoriaResponse create(CategoriaCreateRequest request) {
    validateText(request.nome(), "Nome");
    Setor sector = getSector(request.setorId());
    Categoria category = mapper.toEntity(request, sector);
    return mapper.toResponse(categorias.save(category));
  }

  public CategoriaResponse update(Long id, CategoriaUpdateRequest request) {
    get(id);
    validateText(request.nome(), "Nome");
    Setor sector = getSector(request.setorId());
    Categoria category = mapper.toEntity(request, id, sector);
    return mapper.toResponse(categorias.save(category));
  }

  public void delete(Long id) {
    get(id);
    if (chamados.hasTicketsForCategory(id)) {
      throw new BusinessRuleException("Não é possível excluir categoria com chamados vinculados");
    }
    categorias.deleteById(id);
  }

  private Categoria get(Long id) {
    return categorias.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria"));
  }

  private Setor getSector(Long id) {
    return setores.findById(id).orElseThrow(() -> new ResourceNotFoundException("Setor"));
  }

  private void validateSectorWhenProvided(Long sectorId) {
    if (sectorId != null) {
      getSector(sectorId);
    }
  }

  private void validatePage(Pageable pageable) {
    if (pageable.getPageSize() > 50) {
      throw new IllegalArgumentException("O tamanho máximo da página é 50");
    }
  }

  private void validateText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(field + " é obrigatório");
    }
  }
}

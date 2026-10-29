package com.sistema_corporativos.trabalhoAPI.service;

import com.sistema_corporativos.trabalhoAPI.dto.request.SetorCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.SetorUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResumoResponse;
import com.sistema_corporativos.trabalhoAPI.exception.BusinessRuleException;
import com.sistema_corporativos.trabalhoAPI.exception.ResourceNotFoundException;
import com.sistema_corporativos.trabalhoAPI.mapper.SetorMapper;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import com.sistema_corporativos.trabalhoAPI.repository.CategoriaRepository;
import com.sistema_corporativos.trabalhoAPI.repository.ChamadoRepository;
import com.sistema_corporativos.trabalhoAPI.repository.SetorRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SetorService {
  private final SetorRepository setores;
  private final CategoriaRepository categorias;
  private final ChamadoRepository chamados;
  private final SetorMapper mapper;

  public SetorService(
      SetorRepository setores,
      CategoriaRepository categorias,
      ChamadoRepository chamados,
      SetorMapper mapper) {
    this.setores = setores;
    this.categorias = categorias;
    this.chamados = chamados;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageResponse<SetorResumoResponse> list(String name, Pageable pageable) {
    validatePage(pageable);
    return PageResponse.from(
        setores.searchByName(name != null, name, pageable).map(mapper::toSummary));
  }

  @Transactional(readOnly = true)
  public SetorResponse find(Long id) {
    return mapper.toResponse(get(id));
  }

  public SetorResponse create(SetorCreateRequest request) {
    validateText(request.nome(), "Nome");
    validateText(request.sigla(), "Sigla");
    if (setores.hasSectorWithCode(request.sigla())) {
      throw new BusinessRuleException("Já existe setor com esta sigla");
    }
    return mapper.toResponse(setores.save(mapper.toEntity(request)));
  }

  public SetorResponse update(Long id, SetorUpdateRequest request) {
    get(id);
    validateText(request.nome(), "Nome");
    validateText(request.sigla(), "Sigla");
    if (setores.hasOtherSectorWithCode(request.sigla(), id)) {
      throw new BusinessRuleException("Já existe setor com esta sigla");
    }
    return mapper.toResponse(setores.save(mapper.toEntity(request, id)));
  }

  public void delete(Long id) {
    get(id);
    if (chamados.hasTicketsForSector(id)) {
      throw new BusinessRuleException("Não é possível excluir setor com chamados vinculados");
    }
    if (categorias.hasCategoriesForSector(id)) {
      throw new BusinessRuleException("Remova as categorias vinculadas antes de excluir o setor");
    }
    setores.deleteById(id);
  }

  private Setor get(Long id) {
    return setores.findById(id).orElseThrow(() -> new ResourceNotFoundException("Setor"));
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

package com.sistema_corporativos.trabalhoAPI.service;

import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResumoResponse;
import com.sistema_corporativos.trabalhoAPI.exception.BusinessRuleException;
import com.sistema_corporativos.trabalhoAPI.exception.ResourceNotFoundException;
import com.sistema_corporativos.trabalhoAPI.mapper.UsuarioMapper;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;
import com.sistema_corporativos.trabalhoAPI.model.Setor;
import com.sistema_corporativos.trabalhoAPI.model.Usuario;
import com.sistema_corporativos.trabalhoAPI.repository.SetorRepository;
import com.sistema_corporativos.trabalhoAPI.repository.UsuarioRepository;
import java.util.regex.Pattern;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {
  private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  private final UsuarioRepository usuarios;
  private final SetorRepository setores;
  private final UsuarioMapper mapper;

  public UsuarioService(UsuarioRepository usuarios, SetorRepository setores, UsuarioMapper mapper) {
    this.usuarios = usuarios;
    this.setores = setores;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageResponse<UsuarioResumoResponse> list(
      String name, UserType userType, Long sectorId, Pageable pageable) {
    validatePage(pageable);
    if (sectorId != null) {
      getSector(sectorId);
    }
    return PageResponse.from(
        usuarios
            .searchByCriteria(
                name != null,
                name,
                userType != null,
                userType,
                sectorId != null,
                sectorId,
                pageable)
            .map(mapper::toSummary));
  }

  @Transactional(readOnly = true)
  public UsuarioResponse find(Long id) {
    return mapper.toResponse(get(id));
  }

  public UsuarioResponse create(UsuarioCreateRequest request) {
    validateUser(request.nome(), request.email(), request.matricula(), request.tipo());
    validateUniqueness(request.email(), request.matricula(), null);
    Setor sector = validateAndGetSector(request.tipo(), request.setorId());
    Usuario user = mapper.toEntity(request, sector);
    return mapper.toResponse(usuarios.save(user));
  }

  public UsuarioResponse update(Long id, UsuarioUpdateRequest request) {
    get(id);
    validateUser(request.nome(), request.email(), request.matricula(), request.tipo());
    validateUniqueness(request.email(), request.matricula(), id);
    Setor sector = validateAndGetSector(request.tipo(), request.setorId());
    Usuario user = mapper.toEntity(request, id, sector);
    return mapper.toResponse(usuarios.save(user));
  }

  private Usuario get(Long id) {
    return usuarios.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário"));
  }

  private Setor validateAndGetSector(UserType userType, Long sectorId) {
    if (userType == UserType.TECNICO && sectorId == null) {
      throw new IllegalArgumentException("Técnico deve pertencer a um setor");
    }
    if (userType == UserType.SOLICITANTE && sectorId != null) {
      throw new IllegalArgumentException("Solicitante não deve possuir setor");
    }
    return sectorId == null ? null : getSector(sectorId);
  }

  private Setor getSector(Long id) {
    return setores.findById(id).orElseThrow(() -> new ResourceNotFoundException("Setor"));
  }

  private void validateUniqueness(String email, String registration, Long userId) {
    boolean emailExists =
        userId == null
            ? usuarios.hasUserWithEmail(email)
            : usuarios.hasOtherUserWithEmail(email, userId);
    if (emailExists) {
      throw new BusinessRuleException("E-mail já cadastrado");
    }
    boolean registrationExists =
        userId == null
            ? usuarios.hasUserWithRegistration(registration)
            : usuarios.hasOtherUserWithRegistration(registration, userId);
    if (registrationExists) {
      throw new BusinessRuleException("Matrícula já cadastrada");
    }
  }

  private void validateUser(String name, String email, String registration, UserType userType) {
    validateText(name, "Nome");
    validateText(email, "E-mail");
    validateText(registration, "Matrícula");
    if (!EMAIL.matcher(email.trim()).matches()) {
      throw new IllegalArgumentException("E-mail inválido");
    }
    if (userType == null) {
      throw new IllegalArgumentException("Tipo é obrigatório");
    }
  }

  private void validateText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(field + " é obrigatório");
    }
  }

  private void validatePage(Pageable pageable) {
    if (pageable.getPageSize() > 50) {
      throw new IllegalArgumentException("O tamanho máximo da página é 50");
    }
  }
}

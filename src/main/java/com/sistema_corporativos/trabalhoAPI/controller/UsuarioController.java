package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.UsuarioUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.UsuarioResumoResponse;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;
import com.sistema_corporativos.trabalhoAPI.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
  private final UsuarioService service;

  public UsuarioController(UsuarioService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioResponse create(@Valid @RequestBody UsuarioCreateRequest request) {
    return service.create(request);
  }

  @GetMapping
  public PageResponse<UsuarioResumoResponse> list(
      @RequestParam(name = "nome", required = false) String name,
      @RequestParam(name = "tipo", required = false) UserType userType,
      @RequestParam(name = "setorId", required = false) Long sectorId,
      @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
    return service.list(name, userType, sectorId, pageable);
  }

  @GetMapping("/{id}")
  public UsuarioResponse get(@PathVariable Long id) {
    return service.find(id);
  }

  @PutMapping("/{id}")
  public UsuarioResponse update(
      @PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
    return service.update(id, request);
  }
}

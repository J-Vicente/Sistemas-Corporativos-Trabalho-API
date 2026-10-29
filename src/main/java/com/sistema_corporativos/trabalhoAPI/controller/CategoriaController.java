package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.CategoriaUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResumoResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
  private final CategoriaService service;

  public CategoriaController(CategoriaService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoriaResponse create(@Valid @RequestBody CategoriaCreateRequest request) {
    return service.create(request);
  }

  @GetMapping
  public PageResponse<CategoriaResumoResponse> list(
      @RequestParam(name = "nome", required = false) String name,
      @RequestParam(name = "setorId", required = false) Long sectorId,
      @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
    return service.list(name, sectorId, pageable);
  }

  @GetMapping("/{id}")
  public CategoriaResponse get(@PathVariable Long id) {
    return service.find(id);
  }

  @PutMapping("/{id}")
  public CategoriaResponse update(
      @PathVariable Long id, @Valid @RequestBody CategoriaUpdateRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}

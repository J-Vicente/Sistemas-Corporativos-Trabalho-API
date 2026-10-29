package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.request.SetorCreateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.request.SetorUpdateRequest;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.SetorResumoResponse;
import com.sistema_corporativos.trabalhoAPI.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setores")
public class SetorController {
  private final SetorService service;

  public SetorController(SetorService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SetorResponse create(@Valid @RequestBody SetorCreateRequest request) {
    return service.create(request);
  }

  @GetMapping
  public PageResponse<SetorResumoResponse> list(
      @RequestParam(name = "nome", required = false) String name,
      @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
    return service.list(name, pageable);
  }

  @GetMapping("/{id}")
  public SetorResponse get(@PathVariable Long id) {
    return service.find(id);
  }

  @PutMapping("/{id}")
  public SetorResponse update(
      @PathVariable Long id, @Valid @RequestBody SetorUpdateRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}

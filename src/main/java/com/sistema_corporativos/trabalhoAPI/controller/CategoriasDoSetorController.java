package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.response.CategoriaResumoResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.service.CategoriaService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setores/{id}/categorias")
public class CategoriasDoSetorController {
  private final CategoriaService service;

  public CategoriasDoSetorController(CategoriaService service) {
    this.service = service;
  }

  @GetMapping
  public PageResponse<CategoriaResumoResponse> list(
      @PathVariable Long id, @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
    return service.listBySector(id, pageable);
  }
}

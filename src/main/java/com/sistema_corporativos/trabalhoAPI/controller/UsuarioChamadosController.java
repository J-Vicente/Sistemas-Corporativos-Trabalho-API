package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.response.ChamadoResumoResponse;
import com.sistema_corporativos.trabalhoAPI.dto.response.PageResponse;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.TicketStatus;
import com.sistema_corporativos.trabalhoAPI.service.ChamadoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{id}/chamados")
public class UsuarioChamadosController {
  private final ChamadoService service;

  public UsuarioChamadosController(ChamadoService service) {
    this.service = service;
  }

  @GetMapping
  public PageResponse<ChamadoResumoResponse> list(
      @PathVariable Long id,
      @RequestParam(required = false) TicketStatus status,
      @PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return service.findByUser(id, status, pageable);
  }
}

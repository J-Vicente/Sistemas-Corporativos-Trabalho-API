package com.sistema_corporativos.trabalhoAPI.controller;

import com.sistema_corporativos.trabalhoAPI.dto.request.*;
import com.sistema_corporativos.trabalhoAPI.dto.response.*;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.*;
import com.sistema_corporativos.trabalhoAPI.service.ChamadoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {
  private final ChamadoService service;

  public ChamadoController(ChamadoService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ChamadoResponse open(@Valid @RequestBody ChamadoCreateRequest request) {
    return service.open(request);
  }

  @GetMapping
  public PageResponse<ChamadoResumoResponse> list(
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(name = "prioridade", required = false) Priority priority,
      @RequestParam(name = "setorId", required = false) Long sectorId,
      @RequestParam(name = "categoriaId", required = false) Long categoryId,
      @RequestParam(name = "solicitanteId", required = false) Long requesterId,
      @RequestParam(name = "tecnicoId", required = false) Long technicianId,
      @RequestParam(name = "dataInicio", required = false) LocalDate startDate,
      @RequestParam(name = "dataFim", required = false) LocalDate endDate,
      @PageableDefault(size = 10, sort = "dataAbertura", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return service.list(
        status,
        priority,
        sectorId,
        categoryId,
        requesterId,
        technicianId,
        startDate,
        endDate,
        pageable);
  }

  @GetMapping("/{id}")
  public ChamadoResponse findById(@PathVariable Long id) {
    return service.find(id);
  }

  @PatchMapping("/{id}/assumir")
  public ChamadoResponse assign(
      @PathVariable Long id, @Valid @RequestBody AssumirChamadoRequest request) {
    return service.assign(id, request);
  }

  @PatchMapping("/{id}/resolver")
  public ChamadoResponse resolve(
      @PathVariable Long id, @Valid @RequestBody ResolverChamadoRequest request) {
    return service.resolve(id, request);
  }

  @PatchMapping("/{id}/cancelar")
  public ChamadoResponse cancel(
      @PathVariable Long id, @Valid @RequestBody CancelarChamadoRequest request) {
    return service.cancel(id, request);
  }

  @PostMapping("/{id}/interacoes")
  @ResponseStatus(HttpStatus.CREATED)
  public InteracaoResponse createInteraction(
      @PathVariable Long id, @Valid @RequestBody InteracaoCreateRequest request) {
    return service.addInteraction(id, request);
  }
}

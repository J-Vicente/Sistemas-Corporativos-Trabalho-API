package com.sistema_corporativos.trabalhoAPI.service;

import com.sistema_corporativos.trabalhoAPI.dto.request.*;
import com.sistema_corporativos.trabalhoAPI.dto.response.*;
import com.sistema_corporativos.trabalhoAPI.exception.*;
import com.sistema_corporativos.trabalhoAPI.mapper.ChamadoMapper;
import com.sistema_corporativos.trabalhoAPI.mapper.InteracaoMapper;
import com.sistema_corporativos.trabalhoAPI.model.*;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.*;
import com.sistema_corporativos.trabalhoAPI.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChamadoService {
  private final ChamadoRepository chamados;
  private final UsuarioRepository usuarios;
  private final CategoriaRepository categorias;
  private final ChamadoMapper chamadoMapper;
  private final InteracaoMapper interacaoMapper;

  public ChamadoService(
      ChamadoRepository ticketRepository,
      UsuarioRepository userRepository,
      CategoriaRepository categoryRepository,
      ChamadoMapper chamadoMapper,
      InteracaoMapper interacaoMapper) {
    chamados = ticketRepository;
    usuarios = userRepository;
    categorias = categoryRepository;
    this.chamadoMapper = chamadoMapper;
    this.interacaoMapper = interacaoMapper;
  }

  private <T> T get(java.util.Optional<T> value, String resource) {
    return value.orElseThrow(() -> new ResourceNotFoundException(resource));
  }

  public ChamadoResponse open(ChamadoCreateRequest request) {
    Usuario requester = get(usuarios.findById(request.solicitanteId()), "Usuário");
    if (requester.getTipo() != UserType.SOLICITANTE)
      throw new BusinessRuleException("Apenas solicitantes podem abrir chamados");
    Chamado ticket =
        chamadoMapper.toEntity(
            request,
            requester,
            get(categorias.findById(request.categoriaId()), "Categoria"),
            LocalDateTime.now());
    return chamadoMapper.toResponse(chamados.save(ticket));
  }

  public ChamadoResponse assign(Long id, AssumirChamadoRequest request) {
    Chamado ticket = get(chamados.findById(id), "Chamado");
    Usuario technician = get(usuarios.findById(request.tecnicoId()), "Usuário");
    if (technician.getTipo() != UserType.TECNICO)
      throw new BusinessRuleException("Usuário não é técnico");
    if (ticket.getStatus() != TicketStatus.ABERTO)
      throw new BusinessRuleException("Somente chamados abertos podem ser assumidos");
    if (technician.getSetor() == null
        || !technician.getSetor().getId().equals(ticket.getCategoria().getSetor().getId()))
      throw new BusinessRuleException("Técnico não pertence ao setor responsável");
    ticket.setTecnico(technician);
    ticket.setStatus(TicketStatus.EM_ATENDIMENTO);
    return chamadoMapper.toResponse(ticket);
  }

  public ChamadoResponse resolve(Long id, ResolverChamadoRequest request) {
    Chamado ticket = get(chamados.findById(id), "Chamado");
    if (ticket.getStatus() != TicketStatus.EM_ATENDIMENTO
        || ticket.getTecnico() == null
        || !ticket.getTecnico().getId().equals(request.tecnicoId()))
      throw new BusinessRuleException(
          "Somente o técnico atribuído pode resolver chamado em atendimento");
    if (request.solucao() == null || request.solucao().isBlank())
      throw new BusinessRuleException("Solução é obrigatória");
    ticket.setSolucao(request.solucao().trim());
    ticket.setStatus(TicketStatus.RESOLVIDO);
    ticket.setDataFechamento(LocalDateTime.now());
    return chamadoMapper.toResponse(ticket);
  }

  public ChamadoResponse cancel(Long id, CancelarChamadoRequest request) {
    Chamado ticket = get(chamados.findById(id), "Chamado");
    if (ticket.getStatus() != TicketStatus.ABERTO
        || !ticket.getSolicitante().getId().equals(request.solicitanteId()))
      throw new BusinessRuleException("Somente o solicitante pode cancelar chamado aberto");
    ticket.setStatus(TicketStatus.CANCELADO);
    ticket.setDataFechamento(LocalDateTime.now());
    return chamadoMapper.toResponse(ticket);
  }

  public InteracaoResponse addInteraction(Long id, InteracaoCreateRequest request) {
    Chamado ticket = get(chamados.findById(id), "Chamado");
    Usuario author = get(usuarios.findById(request.autorId()), "Usuário");
    if (ticket.getStatus() == TicketStatus.RESOLVIDO
        || ticket.getStatus() == TicketStatus.CANCELADO)
      throw new BusinessRuleException("Chamado encerrado não aceita interações");
    if (!ticket.getSolicitante().getId().equals(request.autorId())
        && (ticket.getTecnico() == null || !ticket.getTecnico().getId().equals(request.autorId())))
      throw new BusinessRuleException("Usuário não participa deste chamado");
    if (request.texto() == null || request.texto().isBlank())
      throw new BusinessRuleException("Texto é obrigatório");
    Interacao interaction = interacaoMapper.toEntity(request, ticket, author, LocalDateTime.now());
    ticket.getInteracoes().add(interaction);
    chamados.saveAndFlush(ticket);
    return interacaoMapper.toResponse(interaction);
  }

  @Transactional(readOnly = true)
  public ChamadoResponse find(Long id) {
    return chamadoMapper.toResponse(get(chamados.findById(id), "Chamado"));
  }

  @Transactional(readOnly = true)
  public PageResponse<ChamadoResumoResponse> list(
      TicketStatus status,
      Priority priority,
      Long sectorId,
      Long categoryId,
      Long requesterId,
      Long technicianId,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable) {
    validatePage(pageable);
    if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("dataFim deve ser igual ou posterior a dataInicio");
    }
    LocalDateTime startDateTime = startDate == null ? null : startDate.atStartOfDay();
    LocalDateTime endDateTime =
        endDate == null ? null : endDate.plusDays(1).atStartOfDay().minusNanos(1);
    return PageResponse.from(
        chamados
            .searchByCriteria(
                status != null,
                status,
                priority != null,
                priority,
                sectorId != null,
                sectorId,
                categoryId != null,
                categoryId,
                requesterId != null,
                requesterId,
                technicianId != null,
                technicianId,
                startDateTime != null,
                startDateTime,
                endDateTime != null,
                endDateTime,
                pageable)
            .map(chamadoMapper::toSummary));
  }

  @Transactional(readOnly = true)
  public PageResponse<ChamadoResumoResponse> findByUser(
      Long userId, TicketStatus status, Pageable pageable) {
    validatePage(pageable);
    if (!usuarios.existsById(userId)) throw new ResourceNotFoundException("Usuário");
    return PageResponse.from(
        chamados
            .findByUser(userId, status != null, status, pageable)
            .map(chamadoMapper::toSummary));
  }

  private void validatePage(Pageable pageable) {
    if (pageable.getPageSize() > 50) {
      throw new IllegalArgumentException("O tamanho máximo da página é 50");
    }
  }
}

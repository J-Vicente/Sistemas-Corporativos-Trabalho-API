package com.sistema_corporativos.trabalhoAPI.repository;

import com.sistema_corporativos.trabalhoAPI.model.Chamado;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.Priority;
import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.TicketStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
  @Query(
      "select c from Chamado c where (:filterByStatus = false or c.status = :status)"
          + " and (:filterByPriority = false or c.prioridade = :priority)"
          + " and (:filterBySector = false or c.categoria.setor.id = :sectorId)"
          + " and (:filterByCategory = false or c.categoria.id = :categoryId)"
          + " and (:filterByRequester = false or c.solicitante.id = :requesterId)"
          + " and (:filterByTechnician = false or c.tecnico.id = :technicianId)"
          + " and (:filterByStart = false or c.dataAbertura >= :startDate)"
          + " and (:filterByEnd = false or c.dataAbertura <= :endDate)")
  Page<Chamado> searchByCriteria(
      @Param("filterByStatus") boolean filterByStatus,
      @Param("status") TicketStatus status,
      @Param("filterByPriority") boolean filterByPriority,
      @Param("priority") Priority priority,
      @Param("filterBySector") boolean filterBySector,
      @Param("sectorId") Long sectorId,
      @Param("filterByCategory") boolean filterByCategory,
      @Param("categoryId") Long categoryId,
      @Param("filterByRequester") boolean filterByRequester,
      @Param("requesterId") Long requesterId,
      @Param("filterByTechnician") boolean filterByTechnician,
      @Param("technicianId") Long technicianId,
      @Param("filterByStart") boolean filterByStart,
      @Param("startDate") LocalDateTime startDate,
      @Param("filterByEnd") boolean filterByEnd,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  @Query(
      "select c from Chamado c where (:filterByStatus = false or c.status = :status) and"
          + " (c.solicitante.id=:userId or c.tecnico.id=:userId)")
  Page<Chamado> findByUser(
      @Param("userId") Long userId,
      @Param("filterByStatus") boolean filterByStatus,
      @Param("status") TicketStatus status,
      Pageable pageable);

  @Query(
      "select case when count(c) > 0 then true else false end from Chamado c"
          + " where c.categoria.id = :categoryId")
  boolean hasTicketsForCategory(@Param("categoryId") Long categoryId);

  @Query(
      "select case when count(c) > 0 then true else false end from Chamado c"
          + " where c.categoria.setor.id = :sectorId")
  boolean hasTicketsForSector(@Param("sectorId") Long sectorId);
}

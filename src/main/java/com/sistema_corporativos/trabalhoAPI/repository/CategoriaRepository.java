package com.sistema_corporativos.trabalhoAPI.repository;

import com.sistema_corporativos.trabalhoAPI.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  @Query(
      "select c from Categoria c where (:filterByName = false or lower(c.nome) like"
          + " lower(concat('%',:name,'%'))) and (:filterBySector = false or c.setor.id=:sectorId)")
  Page<Categoria> searchByNameAndSector(
      @Param("filterByName") boolean filterByName,
      @Param("name") String name,
      @Param("filterBySector") boolean filterBySector,
      @Param("sectorId") Long sectorId,
      Pageable pageable);

  @Query(
      "select case when count(c) > 0 then true else false end from Categoria c where c.setor.id ="
          + " :sectorId")
  boolean hasCategoriesForSector(@Param("sectorId") Long sectorId);
}

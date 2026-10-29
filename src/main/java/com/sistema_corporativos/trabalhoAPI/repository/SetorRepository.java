package com.sistema_corporativos.trabalhoAPI.repository;

import com.sistema_corporativos.trabalhoAPI.model.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
  @Query(
      "select case when count(s) > 0 then true else false end from Setor s where lower(s.sigla) ="
          + " lower(:code)")
  boolean hasSectorWithCode(@Param("code") String code);

  @Query(
      "select case when count(s) > 0 then true else false end from Setor s where lower(s.sigla) ="
          + " lower(:code) and s.id <> :sectorId")
  boolean hasOtherSectorWithCode(@Param("code") String code, @Param("sectorId") Long sectorId);

  @Query(
      "select s from Setor s where :filterByName = false or lower(s.nome) like"
          + " lower(concat('%',:name,'%'))")
  Page<Setor> searchByName(
      @Param("filterByName") boolean filterByName, @Param("name") String name, Pageable pageable);
}

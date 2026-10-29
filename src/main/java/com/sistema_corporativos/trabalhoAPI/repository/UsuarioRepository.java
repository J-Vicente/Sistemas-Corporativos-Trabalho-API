package com.sistema_corporativos.trabalhoAPI.repository;

import com.sistema_corporativos.trabalhoAPI.model.DomainEnums.UserType;
import com.sistema_corporativos.trabalhoAPI.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  @Query(
      "select case when count(u) > 0 then true else false end from Usuario u where lower(u.email) ="
          + " lower(:email)")
  boolean hasUserWithEmail(@Param("email") String email);

  @Query(
      "select case when count(u) > 0 then true else false end from Usuario u where lower(u.email) ="
          + " lower(:email) and u.id <> :userId")
  boolean hasOtherUserWithEmail(@Param("email") String email, @Param("userId") Long userId);

  @Query(
      "select case when count(u) > 0 then true else false end from Usuario u where"
          + " lower(u.matricula) = lower(:registration)")
  boolean hasUserWithRegistration(@Param("registration") String registration);

  @Query(
      "select case when count(u) > 0 then true else false end from Usuario u where"
          + " lower(u.matricula) = lower(:registration) and u.id <> :userId")
  boolean hasOtherUserWithRegistration(
      @Param("registration") String registration, @Param("userId") Long userId);

  @Query(
      "select u from Usuario u where (:filterByName = false or lower(u.nome) like"
          + " lower(concat('%',:name,'%'))) and (:filterByType = false or u.tipo=:userType) and"
          + " (:filterBySector = false or u.setor.id=:sectorId)")
  Page<Usuario> searchByCriteria(
      @Param("filterByName") boolean filterByName,
      @Param("name") String name,
      @Param("filterByType") boolean filterByType,
      @Param("userType") UserType userType,
      @Param("filterBySector") boolean filterBySector,
      @Param("sectorId") Long sectorId,
      Pageable pageable);
}

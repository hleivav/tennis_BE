package com.example.demo.repository;

import com.example.demo.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByNameContaining(String name);
    List<Tournament> findByCreatedById(Long adminId); // valfritt, för admin
    List<Tournament> findByStartDateBetween(LocalDate start, LocalDate end); // valfritt, för datumintervall
    
    @Query("SELECT DISTINCT t FROM Tournament t " +
           "LEFT JOIN FETCH t.matches m " +
           "LEFT JOIN FETCH m.prev1Match " +
           "LEFT JOIN FETCH m.prev2Match " +
           "WHERE t.id = :id")
    Optional<Tournament> findByIdWithMatchesAndPrevMatches(@Param("id") Long id);
}
package com.example.demo.repository;

import com.example.demo.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByNameContaining(String name);
    List<Tournament> findByCreatedById(Long adminId); // valfritt, för admin
    List<Tournament> findByStartDateBetween(LocalDate start, LocalDate end); // valfritt, för datumintervall
}
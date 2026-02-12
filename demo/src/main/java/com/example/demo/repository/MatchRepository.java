package com.example.demo.repository;

import com.example.demo.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, String> {
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findByTournamentIdOrderByMatchNumber(Long tournamentId);
    List<Match> findByRound(String round);
    List<Match> findByTournamentIdAndRound(Long tournamentId, String round);
    List<Match> findByPrev1MatchId(String prev1MatchId);
    List<Match> findByPrev2MatchId(String prev2MatchId);
    List<Match> findByIsByeTrue();
    
    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.prev1Match LEFT JOIN FETCH m.prev2Match WHERE m.id = :id")
    Optional<Match> findByIdWithPrevMatches(@Param("id") String id);
}
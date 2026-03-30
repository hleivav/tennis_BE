package com.example.demo.service;

import com.example.demo.dto.MatchDto;
import com.example.demo.dto.PlayerDto;
import com.example.demo.entity.Match;
import com.example.demo.entity.Tournament;
import com.example.demo.repository.MatchRepository;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository repo;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public MatchService(MatchRepository repo, TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.repo = repo;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    public List<MatchDto> getByTournament(Long tournamentId) {
        return repo.findByTournamentId(tournamentId).stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(MatchDto::getRoundTitle)
                        .thenComparing(MatchDto::getMatchNumber))
                .collect(Collectors.toList());
    }

    public MatchDto create(MatchDto matchDto) {
        Match match = new Match();
        match.setRound(matchDto.getRoundTitle());
        match.setMatchNumber(matchDto.getMatchNumber());
        match.setScore(matchDto.getScore());

        Tournament tournament = tournamentRepository.findById(matchDto.getTournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        match.setTournament(tournament);

        return toDto(repo.save(match));
    }

    public List<MatchDto> bulkUpdateSchedules(List<MatchDto> scheduleUpdates) {
        return scheduleUpdates.stream().map(update -> {
            Match match = repo.findById(update.getId())
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Match not found: " + update.getId()));
            
            if (update.getScheduleDate() != null && !update.getScheduleDate().isEmpty()) {
                match.setScheduleDate(java.time.LocalDate.parse(update.getScheduleDate()));
            } else {
                match.setScheduleDate(null);
            }
            match.setScheduleTime(update.getScheduleTime());
            
            System.out.println("[MatchService] Updated schedule for match " + match.getId() + 
                    ": date=" + match.getScheduleDate() + ", time=" + match.getScheduleTime());
            
            return toDto(repo.save(match));
        }).collect(Collectors.toList());
    }

    @Transactional
    public MatchDto updateMatchResult(String matchId, Map<String, Object> updates) {
        Match match = repo.findByIdWithPrevMatches(matchId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Match not found: " + matchId));

        // Uppdatera score
        if (updates.containsKey("score")) {
            String score = (String) updates.get("score");
            match.setScore(score);
            System.out.println("[MatchService] Setting score: " + score);
        }

        // Uppdatera winner
        if (updates.containsKey("winner")) {
            Object winnerObj = updates.get("winner");

            // Explicit nollställning
            if (winnerObj == null) {
                match.setWinner(null);
                System.out.println("[MatchService] Clearing winner (reset)");
            } else {
                final String winnerName;

                // Hantera både String och Map (JSON objekt)
                if (winnerObj instanceof String) {
                    winnerName = (String) winnerObj;
                } else if (winnerObj instanceof java.util.Map) {
                    java.util.Map<?, ?> winnerMap = (java.util.Map<?, ?>) winnerObj;
                    winnerName = (String) winnerMap.get("name");
                } else {
                    winnerName = null;
                }

                if (winnerName != null && !winnerName.isEmpty()) {
                    // Kontrollera matchens egna p1/p2 direkt först (snabbare och mer robust)
                    com.example.demo.entity.Player winner = null;
                    if (match.getP1() != null && winnerName.trim().equals(match.getP1().getName() != null ? match.getP1().getName().trim() : "")) {
                        winner = match.getP1();
                    } else if (match.getP2() != null && winnerName.trim().equals(match.getP2().getName() != null ? match.getP2().getName().trim() : "")) {
                        winner = match.getP2();
                    } else {
                        // Fallback: sök bland alla turnerings-spelare
                        Long tournamentId = match.getTournament().getId();
                        winner = playerRepository.findByTournamentId(tournamentId)
                                .stream()
                                .filter(p -> p.getName() != null && p.getName().trim().equals(winnerName.trim()))
                                .findFirst()
                                .orElse(null);
                    }

                    if (winner != null) {
                        match.setWinner(winner);
                        System.out.println("[MatchService] Setting winner: " + winner.getName());
                    } else {
                        System.out.println("[MatchService] Warning: Winner not found for name: " + winnerName
                            + " (p1=" + (match.getP1() != null ? match.getP1().getName() : "null")
                            + ", p2=" + (match.getP2() != null ? match.getP2().getName() : "null") + ")");
                    }
                } else {
                    System.out.println("[MatchService] Warning: Could not extract winner name from: " + winnerObj);
                }
            }
        }

        // Uppdatera spelarnamn i B-klass-matcher (vid nollställning av A-klass-resultat)
        if (updates.containsKey("p1Name")) {
            Object p1NameObj = updates.get("p1Name");
            if (match.getP1() != null) {
                String newName = p1NameObj != null ? (String) p1NameObj : "";
                match.getP1().setName(newName);
                playerRepository.save(match.getP1());
                System.out.println("[MatchService] Updated p1 name to: " + newName);
            }
        }

        if (updates.containsKey("p2Name")) {
            Object p2NameObj = updates.get("p2Name");
            if (match.getP2() != null) {
                String newName = p2NameObj != null ? (String) p2NameObj : "";
                match.getP2().setName(newName);
                playerRepository.save(match.getP2());
                System.out.println("[MatchService] Updated p2 name to: " + newName);
            }
        }

        Match saved = repo.save(match);
        System.out.println("[MatchService] Match updated: " + saved.getId());

        return toDto(saved);
    }

    private MatchDto toDto(Match m) {
        MatchDto dto = new MatchDto();
        dto.setId(m.getId());
        dto.setRoundTitle(m.getRound());
        dto.setMatchNumber(m.getMatchNumber());
        dto.setScore(m.getScore());
        dto.setP1Id(m.getP1() != null ? m.getP1().getId() : null);
        dto.setP2Id(m.getP2() != null ? m.getP2().getId() : null);
        dto.setWinnerId(m.getWinner() != null ? m.getWinner().getId() : null);
        dto.setTournamentId(m.getTournament().getId());
        dto.setPrev1MatchId(m.getPrev1Match() != null ? m.getPrev1Match().getId() : null);
        dto.setPrev2MatchId(m.getPrev2Match() != null ? m.getPrev2Match().getId() : null);
        dto.setIsBye(m.getIsBye());
        
        // Mappa schedule-fälten
        dto.setScheduleDate(m.getScheduleDate() != null ? m.getScheduleDate().toString() : null);
        dto.setScheduleTime(m.getScheduleTime());

        if (m.getP1() != null) {
            PlayerDto p1Dto = new PlayerDto();
            p1Dto.setId(m.getP1().getId());
            p1Dto.setName(m.getP1().getName());
            dto.setP1(p1Dto);
        }

        if (m.getP2() != null) {
            PlayerDto p2Dto = new PlayerDto();
            p2Dto.setId(m.getP2().getId());
            p2Dto.setName(m.getP2().getName());
            dto.setP2(p2Dto);
        }

        if (m.getWinner() != null) {
            PlayerDto winnerDto = new PlayerDto();
            winnerDto.setId(m.getWinner().getId());
            winnerDto.setName(m.getWinner().getName());
            dto.setWinner(winnerDto);
        }

        return dto;
    }
}
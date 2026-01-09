package com.example.demo.service;

import com.example.demo.dto.MatchDto;
import com.example.demo.dto.PlayerDto;
import com.example.demo.entity.Match;
import com.example.demo.entity.Tournament;
import com.example.demo.repository.MatchRepository;
import com.example.demo.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository repo;
    private final TournamentRepository tournamentRepository;

    public MatchService(MatchRepository repo, TournamentRepository tournamentRepository) {
        this.repo = repo;
        this.tournamentRepository = tournamentRepository;
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

    public MatchDto updateMatchResult(String matchId, Map<String, Object> updates) {
        Match match = repo.findById(matchId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Match not found: " + matchId));

        // Uppdatera score
        if (updates.containsKey("score")) {
            String score = (String) updates.get("score");
            match.setScore(score);
            System.out.println("[MatchService] Setting score: " + score);
        }

        // Uppdatera winner
        if (updates.containsKey("winner")) {
            String winnerName = (String) updates.get("winner");

            // Hitta spelaren genom att matcha namn i samma turnering
            com.example.demo.entity.Player winner = match.getTournament().getPlayers().stream()
                    .filter(p -> p.getName().equals(winnerName))
                    .findFirst()
                    .orElse(null);

            if (winner != null) {
                match.setWinner(winner);
                System.out.println("[MatchService] Setting winner: " + winner.getName());
            } else {
                System.out.println("[MatchService] Warning: Winner not found: " + winnerName);
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
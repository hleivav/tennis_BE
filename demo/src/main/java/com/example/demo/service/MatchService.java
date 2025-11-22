package com.example.demo.service;

import com.example.demo.dto.MatchDto;
import com.example.demo.entity.Match;
import com.example.demo.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository repo;

    public MatchService(MatchRepository repo) {
        this.repo = repo;
    }

    public List<MatchDto> getByTournament(Long tournamentId) {
        return repo.findByTournamentId(tournamentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MatchDto create(Match match) {
        return toDto(repo.save(match));
    }

    private MatchDto toDto(Match m) {
        MatchDto dto = new MatchDto();
        dto.setId(m.getId());
        dto.setRound(m.getRound());
        dto.setMatchNumber(m.getMatchNumber());
        dto.setScore(m.getScore());
        dto.setP1Id(m.getP1() != null ? m.getP1().getId() : null);
        dto.setP2Id(m.getP2() != null ? m.getP2().getId() : null);
        dto.setWinnerId(m.getWinner() != null ? m.getWinner().getId() : null);
        dto.setTournamentId(m.getTournament().getId());
        return dto;
    }
}

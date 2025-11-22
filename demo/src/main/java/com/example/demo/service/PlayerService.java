package com.example.demo.service;

import com.example.demo.dto.PlayerDto;
import com.example.demo.entity.Player;
import com.example.demo.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository repo;

    public PlayerService(PlayerRepository repo) {
        this.repo = repo;
    }

    public List<PlayerDto> getByTournament(Long tournamentId) {
        return repo.findByTournamentId(tournamentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PlayerDto create(Player player) {
        return toDto(repo.save(player));
    }

    private PlayerDto toDto(Player p) {
        PlayerDto dto = new PlayerDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setTournamentId(p.getTournament().getId());
        return dto;
    }
}

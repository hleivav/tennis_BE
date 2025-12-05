package com.example.demo.service;

import com.example.demo.dto.PlayerDto;
import com.example.demo.entity.Player;
import com.example.demo.entity.Tournament;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository repo;
    private TournamentRepository tournamentRepo;

    @Autowired
    public PlayerService(PlayerRepository repo, TournamentRepository tournamentRepo) {
        this.tournamentRepo = tournamentRepo;
        this.repo = repo;
    }

    public List<PlayerDto> getByTournament(Long tournamentId) {
        return repo.findByTournamentId(tournamentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PlayerDto create(PlayerDto dto) {
        Player p = new Player();
        p.setName(dto.getName());
        p.setSeed(dto.getSeed());
        p.setPlayerOrder(dto.getPlayerOrder());
        Tournament t = tournamentRepo.findById(dto.getTournamentId()).orElseThrow();
        p.setTournament(t);
        Player saved = repo.save(p);
        return toDto(saved);
    }

    private PlayerDto toDto(Player p) {
        PlayerDto dto = new PlayerDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setTournamentId(p.getTournament().getId());
        dto.setSeed(p.getSeed());
        dto.setPlayerOrder(p.getPlayerOrder());
        return dto;
    }
}

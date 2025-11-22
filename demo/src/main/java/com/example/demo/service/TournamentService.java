package com.example.demo.service;

import com.example.demo.dto.TournamentDto;
import com.example.demo.entity.Tournament;
import com.example.demo.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private final TournamentRepository repo;

    public TournamentService(TournamentRepository repo) {
        this.repo = repo;
    }

    public List<TournamentDto> getAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TournamentDto create(Tournament tournament) {
        return toDto(repo.save(tournament));
    }

    public TournamentDto findById(Long id) {
        return repo.findById(id).map(this::toDto).orElse(null);
    }

    private TournamentDto toDto(Tournament t) {
        TournamentDto dto = new TournamentDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());
        dto.setSeedCount(t.getSeedCount());
        dto.setClubName(t.getCreatedBy().getClub());
        return dto;
    }
}

package com.example.demo.controller;

import com.example.demo.dto.TournamentDto;
import com.example.demo.entity.Tournament;
import com.example.demo.service.TournamentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    private final TournamentService service;

    public TournamentController(TournamentService service) {
        this.service = service;
    }

    @GetMapping
    public List<TournamentDto> getAllTournaments() {
        return service.getAll();
    }

    @PostMapping
    public TournamentDto createTournament(@RequestBody Tournament tournament) {
        return service.create(tournament);
    }

    @GetMapping("/{id}")
    public TournamentDto getTournament(@PathVariable Long id) {
        return service.findById(id);
    }
}

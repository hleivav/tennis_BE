package com.example.demo.controller;

import com.example.demo.dto.MatchDto;
import com.example.demo.entity.Match;
import com.example.demo.service.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService service;

    public MatchController(MatchService service) {
        this.service = service;
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<MatchDto> getMatchesByTournament(@PathVariable Long tournamentId) {
        return service.getByTournament(tournamentId);
    }

    @PostMapping
    public MatchDto createMatch(@RequestBody Match match) {
        return service.create(match);
    }
}

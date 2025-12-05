package com.example.demo.controller;

import com.example.demo.dto.MatchDto;
import com.example.demo.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matches")
@CrossOrigin
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
    public MatchDto createMatch(@RequestBody MatchDto matchDto) {
        System.out.println("[DEBUG] Incoming MatchDto: " + matchDto);
        System.out.println("[DEBUG] tournamentId: " + matchDto.getTournamentId());
        return service.create(matchDto);
    }

    @PatchMapping("/{matchId}")
    public ResponseEntity<?> updateMatchResult(
            @PathVariable String matchId,
            @RequestBody Map<String, Object> updates
    ) {
        System.out.println("[MatchController] Updating match: " + matchId);
        System.out.println("[MatchController] Updates: " + updates);

        try {
            MatchDto updated = service.updateMatchResult(matchId, updates);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Match not found: " + matchId);
        }
    }
}
package com.example.demo.controller;

import com.example.demo.dto.TournamentDto;
import com.example.demo.entity.Tournament;
import com.example.demo.service.TournamentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
@CrossOrigin // Enables CORS for frontend-backend communication
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
    public TournamentDto createTournament(@RequestBody TournamentDto dto) {
        return service.create(dto);
    }

    @PostMapping("/fullBracket")
    public ResponseEntity<?> saveFullBracket(@RequestBody TournamentDto dto) {
        System.out.println("[FE->BE] Mottaget TournamentDto: " + dto); // Logga hela objektet
        TournamentDto saved = service.create(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public TournamentDto getTournament(@PathVariable Long id) {
        TournamentDto dto = service.findById(id);

        // Debug: skriv ut ordningen på spelare och matcher
        System.out.println("[DEBUG] Players order:");
        if (dto.getPlayers() != null) {
            for (var p : dto.getPlayers()) {
                System.out.println("  " + p.getId() + ": " + p.getName());
            }
        }

        System.out.println("[DEBUG] Matches order:");
        if (dto.getMatches() != null) {
            for (var m : dto.getMatches()) {
                System.out.println("  " + m.getId() + ": " + m.getRoundTitle() + " #" + m.getMatchNumber() +
                        " p1=" + (m.getP1Id() != null ? m.getP1Id() : "null") +
                        " p2=" + (m.getP2Id() != null ? m.getP2Id() : "null"));
            }
        }

        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournament(
            @PathVariable Long id,
            @RequestParam Long adminId
    ) {
        try {
            service.deleteTournament(id, adminId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Du får bara ta bort dina egna turneringar.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(
            @PathVariable Long id,
            @RequestBody TournamentDto updatedDto,
            @RequestParam Long adminId
    ) {
        try {
            TournamentDto updated = service.updateTournament(id, updatedDto, adminId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Du får bara ändra dina egna turneringar.");
        }
    }
}
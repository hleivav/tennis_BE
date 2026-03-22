package com.example.demo.controller;

import com.example.demo.dto.PlayerDto;
import com.example.demo.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
@CrossOrigin // Enables CORS for frontend-backend communication
public class PlayerController {
    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<PlayerDto> getPlayersByTournament(@PathVariable Long tournamentId) {
        return service.getByTournament(tournamentId);
    }

    @PostMapping
    public PlayerDto createPlayer(@RequestBody PlayerDto dto) {
        System.out.println("[DEBUG] Incoming PlayerDto: " + dto);
        return service.create(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> renamePlayer(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newName = body.get("name");
        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be empty");
        }
        PlayerDto updated = service.rename(id, newName.trim());
        return ResponseEntity.ok(updated);
    }
}
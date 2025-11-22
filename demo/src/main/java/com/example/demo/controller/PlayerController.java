package com.example.demo.controller;

import com.example.demo.dto.PlayerDto;
import com.example.demo.entity.Player;
import com.example.demo.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
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
    public PlayerDto createPlayer(@RequestBody Player player) {
        return service.create(player);
    }
}

package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDto {
    private Long id;
    private String name;
    private Long tournamentId;
    private Integer seed;
    private Integer playerOrder;

    public PlayerDto() {}

    public PlayerDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PlayerDto(Long id, String name, Long tournamentId, Integer seed, Integer playerOrder) {
        this.id = id;
        this.name = name;
        this.tournamentId = tournamentId;
        this.seed = seed;
        this.playerOrder = playerOrder;
    }
}

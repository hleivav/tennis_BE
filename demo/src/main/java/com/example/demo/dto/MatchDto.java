package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDto {
    private Long id;
    private String round;
    private Integer matchNumber;
    private Long p1Id;
    private Long p2Id;
    private Long winnerId;
    private String score;
    private Long tournamentId;
}

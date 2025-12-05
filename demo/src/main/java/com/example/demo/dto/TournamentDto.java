package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TournamentDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer seedCount;
    private String reportPassword;
    private Long createdBy;
    private LocalDate createdAt; // valfritt
    private List<PlayerDto> players;
    private List<MatchDto> matches;
    private List<List<MatchDto>> rounds; // valfritt, om du vill skicka bracket-struktur direkt


}
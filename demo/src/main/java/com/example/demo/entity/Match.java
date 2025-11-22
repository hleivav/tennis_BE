package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String round;
    private Integer matchNumber;
    private String score;

    // Koppling till Tournament
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    // Spelare 1
    @ManyToOne
    @JoinColumn(name = "p1_id")
    private Player p1;

    // Spelare 2
    @ManyToOne
    @JoinColumn(name = "p2_id")
    private Player p2;

    // Vinnare
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    // getters och setters
}

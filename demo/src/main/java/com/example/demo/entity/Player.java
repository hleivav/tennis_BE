package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Koppling till Tournament
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    // Relation till matcher (som p1, p2 eller winner)
    @OneToMany(mappedBy = "p1")
    private List<Match> matchesAsP1;

    @OneToMany(mappedBy = "p2")
    private List<Match> matchesAsP2;

    @OneToMany(mappedBy = "winner")
    private List<Match> matchesWon;

    // getters och setters
}

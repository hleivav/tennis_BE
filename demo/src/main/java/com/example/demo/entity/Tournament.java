package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private Integer seedCount;
    private String reportPassword;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Koppling till Admin
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Admin createdBy;

    // Relationer till players och matches
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches;

    // getters och setters
}

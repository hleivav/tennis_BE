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
    @Column(length = 20) // eller längre vid behov
    private String id;

    private String round;
    private Integer matchNumber;
    private String score;

    @Column(name = "schedule_date")
    private java.time.LocalDate scheduleDate;

    @Column(name = "schedule_time")
    private String scheduleTime;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "p1_id")
    private Player p1;

    @ManyToOne
    @JoinColumn(name = "p2_id")
    private Player p2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @ManyToOne
    @JoinColumn(name = "prev1_match_id")
    private Match prev1Match;

    @ManyToOne
    @JoinColumn(name = "prev2_match_id")
    private Match prev2Match;

    @Column(name = "is_bye")
    private Boolean isBye;

    // Valfritt: extra attribut
    // @Column(columnDefinition = "TEXT")
    // private String extra;
}
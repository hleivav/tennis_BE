package com.example.demo.dto;

public class MatchDto {
    private String id;
    private String roundTitle;
    private Integer matchNumber;
    private String score;
    private Long tournamentId;
    private Long p1Id;
    private Long p2Id;
    private Long winnerId;
    private String prev1MatchId;
    private String prev2MatchId;
    private Boolean isBye;
    private PlayerDto p1;
    private PlayerDto p2;
    private PlayerDto winner;
    private String scheduleDate;
    private String scheduleTime;

    public String getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(String scheduleDate) { this.scheduleDate = scheduleDate; }

    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoundTitle() { return roundTitle; }
    public void setRoundTitle(String roundTitle) { this.roundTitle = roundTitle; }

    public Integer getMatchNumber() { return matchNumber; }
    public void setMatchNumber(Integer matchNumber) { this.matchNumber = matchNumber; }

    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }

    public Long getTournamentId() { return tournamentId; }
    public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }

    public Long getP1Id() { return p1Id; }
    public void setP1Id(Long p1Id) { this.p1Id = p1Id; }

    public Long getP2Id() { return p2Id; }
    public void setP2Id(Long p2Id) { this.p2Id = p2Id; }

    public Long getWinnerId() { return winnerId; }
    public void setWinnerId(Long winnerId) { this.winnerId = winnerId; }

    public String getPrev1MatchId() { return prev1MatchId; }
    public void setPrev1MatchId(String prev1MatchId) { this.prev1MatchId = prev1MatchId; }

    public String getPrev2MatchId() { return prev2MatchId; }
    public void setPrev2MatchId(String prev2MatchId) { this.prev2MatchId = prev2MatchId; }

    public Boolean getIsBye() { return isBye; }
    public void setIsBye(Boolean isBye) { this.isBye = isBye; }

    public PlayerDto getP1() { return p1; }
    public void setP1(PlayerDto p1) { this.p1 = p1; }

    public PlayerDto getP2() { return p2; }
    public void setP2(PlayerDto p2) { this.p2 = p2; }

    public PlayerDto getWinner() { return winner; }
    public void setWinner(PlayerDto winner) { this.winner = winner; }
}
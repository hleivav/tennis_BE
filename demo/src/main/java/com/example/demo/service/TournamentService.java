package com.example.demo.service;

import com.example.demo.dto.MatchDto;
import com.example.demo.dto.PlayerDto;
import com.example.demo.dto.TournamentDto;
import com.example.demo.entity.Tournament;
import com.example.demo.entity.Player;
import com.example.demo.entity.Match;
import com.example.demo.entity.Admin;
import com.example.demo.repository.MatchRepository;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TournamentRepository;
import com.example.demo.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TournamentRepository tournamentRepository;

    private int getRoundOrder(String round) {
        try {
            return Integer.parseInt(round); // Om round är "1", "2", osv.
        } catch (NumberFormatException e) {
            switch (round.toLowerCase()) {
                case "final": return 100;
                case "semi": return 99;
                case "quarter": return 98;
                default: return 999;
            }
        }
    }

    public List<TournamentDto> getAll() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        return tournaments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TournamentDto create(TournamentDto dto) {
        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setSeedCount(dto.getSeedCount());
        tournament.setReportPassword(dto.getReportPassword());

        Admin admin = adminRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        tournament.setCreatedBy(admin);

        // Spara turneringen först!
        Tournament savedTournament = tournamentRepository.save(tournament);

        // Spara spelare
        List<Player> savedPlayers = new ArrayList<>();
        if (dto.getPlayers() != null) {
            for (PlayerDto pd : dto.getPlayers()) {
                Player player = new Player();
                player.setName(pd.getName());
                player.setSeed(pd.getSeed());
                player.setPlayerOrder(pd.getPlayerOrder());
                player.setTournament(savedTournament);
                savedPlayers.add(playerRepository.save(player));
            }
        }
        savedTournament.setPlayers(savedPlayers);

        // Spara matcher
        List<Match> savedMatches = new ArrayList<>();
        Map<String, Match> matchIdMap = new HashMap<>(); // String-id!
        if (dto.getMatches() != null) {
            for (MatchDto md : dto.getMatches()) {
                Match match = new Match();
                match.setId(md.getId()); // Sätt id från DTO!
                match.setTournament(savedTournament);
                match.setMatchNumber(md.getMatchNumber());
                match.setRound(md.getRoundTitle());
                match.setIsBye(md.getIsBye());
                match.setScore(md.getScore());

// ERSÄTT RADERNA 95-106 I TournamentService.java MED DENNA KOD:

                // Koppla spelare via id ELLER namn
                if (md.getP1Id() != null) {
                    Player p1 = savedPlayers.stream()
                            .filter(p -> p.getId().equals(md.getP1Id()))
                            .findFirst().orElse(null);
                    match.setP1(p1);
                } else if (md.getP1() != null && md.getP1().getName() != null) {
                    // Matcha på namn om ID saknas (från frontend)
                    Player p1 = savedPlayers.stream()
                            .filter(p -> p.getName().equals(md.getP1().getName()))
                            .findFirst().orElse(null);
                    match.setP1(p1);
                }

                if (md.getP2Id() != null) {
                    Player p2 = savedPlayers.stream()
                            .filter(p -> p.getId().equals(md.getP2Id()))
                            .findFirst().orElse(null);
                    match.setP2(p2);
                } else if (md.getP2() != null && md.getP2().getName() != null) {
                    // Matcha på namn om ID saknas (från frontend)
                    Player p2 = savedPlayers.stream()
                            .filter(p -> p.getName().equals(md.getP2().getName()))
                            .findFirst().orElse(null);
                    match.setP2(p2);
                }

                if (md.getWinnerId() != null) {
                    Player winner = savedPlayers.stream()
                            .filter(p -> p.getId().equals(md.getWinnerId()))
                            .findFirst().orElse(null);
                    match.setWinner(winner);
                } else if (md.getWinner() != null && md.getWinner().getName() != null) {
                    // Matcha på namn om ID saknas (från frontend)
                    Player winner = savedPlayers.stream()
                            .filter(p -> p.getName().equals(md.getWinner().getName()))
                            .findFirst().orElse(null);
                    match.setWinner(winner);
                }

// EFTER ÄNDRINGEN:
// 1. Spara TournamentService.java
// 2. Starta om backend
// 3. Skapa en NY turnering från frontend
// 4. Klicka på turneringen i listan - nu ska samma lottning visas!

                savedMatches.add(matchRepository.save(match));
                matchIdMap.put(md.getId(), match); // Spara med String-id
            }
            // Koppla prev1/prev2 efter att alla matcher är sparade
            for (MatchDto md : dto.getMatches()) {
                Match match = matchIdMap.get(md.getId());
                if (md.getPrev1MatchId() != null) {
                    Match prev1 = matchIdMap.get(md.getPrev1MatchId());
                    match.setPrev1Match(prev1);
                }
                if (md.getPrev2MatchId() != null) {
                    Match prev2 = matchIdMap.get(md.getPrev2MatchId());
                    match.setPrev2Match(prev2);
                }
                matchRepository.save(match);
            }
        }
        savedTournament.setMatches(savedMatches);

        Tournament saved = tournamentRepository.save(savedTournament);
        return mapToDto(saved);
    }

    public void deleteTournament(Long id, Long adminId) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));

        // Tillåt superadmin (id 1) hantera alla turneringar
        if (!tournament.getCreatedBy().getId().equals(adminId) && adminId != 1) {
            throw new SecurityException("Du får bara ta bort dina egna turneringar.");
        }

        tournamentRepository.deleteById(id);
    }

    public TournamentDto updateTournament(Long id, TournamentDto updatedDto, Long adminId) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found"));

        // Tillåt superadmin (id 1) hantera alla turneringar
        if (!tournament.getCreatedBy().getId().equals(adminId) && adminId != 1) {
            throw new SecurityException("Du får bara ändra dina egna turneringar.");
        }

        tournament.setName(updatedDto.getName());
        tournament.setStartDate(updatedDto.getStartDate());
        tournament.setEndDate(updatedDto.getEndDate());
        // Lägg till fler fält vid behov

        Tournament saved = tournamentRepository.save(tournament);
        return mapToDto(saved);
    }

    public TournamentDto findById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        return mapToDto(tournament);
    }

    private TournamentDto mapToDto(Tournament tournament) {
        TournamentDto dto = new TournamentDto();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setStartDate(tournament.getStartDate());
        dto.setEndDate(tournament.getEndDate());
        dto.setSeedCount(tournament.getSeedCount());
        dto.setReportPassword(tournament.getReportPassword());
        dto.setCreatedBy(tournament.getCreatedBy() != null ? tournament.getCreatedBy().getId() : null);

        // Mappa och sortera spelare
        List<PlayerDto> playerDtos = null;
        if (tournament.getPlayers() != null) {
            playerDtos = tournament.getPlayers().stream()
                    .sorted(Comparator.comparing(Player::getPlayerOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(player -> {
                        PlayerDto pd = new PlayerDto();
                        pd.setId(player.getId());
                        pd.setName(player.getName());
                        pd.setTournamentId(tournament.getId());
                        pd.setSeed(player.getSeed());
                        pd.setPlayerOrder(player.getPlayerOrder());
                        return pd;
                    })
                    .collect(Collectors.toList());
            dto.setPlayers(playerDtos);
        }

        // Bygg en map för snabb lookup
        Map<Long, PlayerDto> playerDtoMap = playerDtos != null
                ? playerDtos.stream().collect(Collectors.toMap(PlayerDto::getId, pd -> pd))
                : java.util.Collections.emptyMap();

        // Mappa och sortera matcher
        if (tournament.getMatches() != null) {
            dto.setMatches(
                    tournament.getMatches().stream()
                            .sorted(Comparator.comparing((Match m) -> getRoundOrder(m.getRound()))
                                    .thenComparing(Match::getMatchNumber))
                            .map(match -> {
                                MatchDto md = new MatchDto();
                                md.setId(match.getId());
                                md.setRoundTitle(match.getRound());
                                md.setMatchNumber(match.getMatchNumber());
                                md.setScore(match.getScore());

                                if (match.getScore() != null || match.getWinner() != null) {
                                    System.out.println("[TournamentService] Match " + match.getId() +
                                            " har score=" + match.getScore() +
                                            ", winner=" + (match.getWinner() != null ? match.getWinner().getName() : "null"));
                                }

                                md.setTournamentId(tournament.getId());
                                md.setP1Id(match.getP1() != null ? match.getP1().getId() : null);
                                md.setP2Id(match.getP2() != null ? match.getP2().getId() : null);
                                md.setWinnerId(match.getWinner() != null ? match.getWinner().getId() : null);
                                
                                // Sätt hela spelarobjekten, inte bara ID:n
                                if (match.getP1() != null) {
                                    md.setP1(playerDtoMap.get(match.getP1().getId()));
                                }
                                if (match.getP2() != null) {
                                    md.setP2(playerDtoMap.get(match.getP2().getId()));
                                }
                                if (match.getWinner() != null) {
                                    md.setWinner(playerDtoMap.get(match.getWinner().getId()));
                                }
                                
                                md.setIsBye(match.getIsBye());
                                md.setPrev1MatchId(match.getPrev1Match() != null ? match.getPrev1Match().getId() : null);
                                md.setPrev2MatchId(match.getPrev2Match() != null ? match.getPrev2Match().getId() : null);
                                return md;
                            })
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
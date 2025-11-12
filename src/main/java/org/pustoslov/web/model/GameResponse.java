package org.pustoslov.web.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GameResponse(
    UUID gameId,
    Instant timestamp,
    UUID xPlayerId,
    UUID oPlayerId,
    UUID currentTurnId,
    List<List<Integer>> board) {}

package org.pustoslov.web.model;

import java.util.List;
import java.util.UUID;

public record GameResponse(
    UUID gameId, UUID xPlayerId, UUID oPlayerId, UUID currentTurnId, List<List<Integer>> board) {}

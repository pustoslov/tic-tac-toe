package org.pustoslov.web.mapper;

import org.pustoslov.domain.model.RatingStats;
import org.pustoslov.web.model.RatingStatsResponse;
import org.springframework.stereotype.Component;

@Component
public class RatingStatsMapper {
  public RatingStatsResponse toDTO(RatingStats stats) {
    return new RatingStatsResponse(stats.id(), stats.winRatio());
  }
}

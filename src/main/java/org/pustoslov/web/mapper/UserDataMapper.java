package org.pustoslov.web.mapper;

import org.pustoslov.domain.model.User;
import org.pustoslov.web.model.responses.UserDataResponse;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {
  public UserDataResponse toDTO(User user) {
    return new UserDataResponse(user.getId().toString(), user.getUsername());
  }
}

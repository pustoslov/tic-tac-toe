package org.pustoslov.datasource.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.pustoslov.datasource.model.UserEntity;
import org.pustoslov.domain.model.Role;
import org.pustoslov.domain.model.RoleName;
import org.pustoslov.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserEntity toDataEntity(User user) {
    Set<RoleName> roleNames =
        user.getAuthorities().stream()
            .map(authority -> (Role) authority)
            .map(Role::getName)
            .collect(Collectors.toSet());
    return new UserEntity(user.getUsername(), user.getPassword(), roleNames);
  }

  public User toDomain(UserEntity userEntity) {
    Set<Role> roles =
        userEntity.getRoleNames().stream()
            .map(roleName -> new Role(roleName))
            .collect(Collectors.toSet());

    User user = new User(userEntity.getUsername(), userEntity.getPassword(), roles);
    user.setId(userEntity.getId());
    return user;
  }
}

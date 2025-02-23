package com.github.maitmus.springrole.repository.user;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndStatus(Long id, EntityStatus status);
}

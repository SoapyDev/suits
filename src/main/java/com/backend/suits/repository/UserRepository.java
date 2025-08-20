package com.backend.suits.repository;

import com.backend.suits.entity.User;
import jakarta.validation.constraints.Email;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(@Email @NonNull String username);
}

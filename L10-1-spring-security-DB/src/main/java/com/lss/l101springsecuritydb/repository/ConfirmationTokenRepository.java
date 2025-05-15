package com.lss.l101springsecuritydb.repository;

import com.lss.l101springsecuritydb.entity.ConfirmationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
}

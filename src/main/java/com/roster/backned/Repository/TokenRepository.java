package com.roster.backned.Repository;

import com.roster.backned.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query("select t from Token t inner join User u on t.user.userId = u.userId where t.user.userId = :userId and t.loggedOut = false ")
    List<Token> findAllAccessTokensByUser(Long userId);

    Optional<Token> findByAccessTokenAndLoggedOutFalse(String accessToken);

    Optional<Token> findByRefreshTokenAndLoggedOutFalse(String refreshToken);
}

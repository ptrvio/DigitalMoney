package com.homeBanking.usersservice.repository;

import com.homeBanking.usersservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email);

    Optional<User> findByUserName (String email);

    Optional<User> findByAlias(String alias);

    Optional<User> findByCvu(String alias);

    Optional<User> findByKeycloakId(String id);

}

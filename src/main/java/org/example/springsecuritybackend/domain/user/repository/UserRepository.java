package org.example.springsecuritybackend.domain.user.repository;

import org.example.springsecuritybackend.domain.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUsername(String username);
}

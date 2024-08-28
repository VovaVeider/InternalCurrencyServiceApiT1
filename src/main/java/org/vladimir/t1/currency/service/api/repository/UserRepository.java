package org.vladimir.t1.currency.service.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vladimir.t1.currency.service.api.entity.User;
import org.vladimir.t1.currency.service.api.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Page<User> findAllByUsernameStartingWithAndRoleIn(Pageable pageable, @Param("username") String username, List<UserRole> roles);

    Page<User> findAllByEmailStartingWithAndRoleIn(Pageable pageable, @Param("username") String username, List<UserRole> roles);

    @Query("SELECT u from User u where u.account.accountNumber = :accountNumber")
    Optional<User> findByAccountNumber(String accountNumber);
}

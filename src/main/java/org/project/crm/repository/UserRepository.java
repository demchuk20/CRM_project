package org.project.crm.repository;

import org.project.crm.entity.Audit;
import org.project.crm.repository.jpa.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Audit, Long>, UserRepositoryCustom {
    Optional<Audit> findByUsername(String username);
}

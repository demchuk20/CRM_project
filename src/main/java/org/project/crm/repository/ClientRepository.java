package org.project.crm.repository;

import org.project.crm.entity.Client;
import org.project.crm.repository.jpa.ClientRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryCustom {
}

package org.project.crm.repository;

import org.project.crm.entity.Contact;
import org.project.crm.repository.jpa.ContactRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {
}

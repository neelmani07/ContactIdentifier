package com.contact.identifier.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact.identifier.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	List<Contact> findByLinkedContact(Contact primaryContact);

	Optional<Contact> findByEmailOrPhoneNumber(Object email, Object phoneNumber);

	Optional<Contact> findByEmail(String email);

	Optional<Contact> findByPhoneNumber(String phoneNumber);
	
    Optional<Contact> findFirstByEmailOrderByCreatedAtAsc(String email);

    Optional<Contact> findFirstByPhoneNumberOrderByCreatedAtAsc(String phoneNumber);

    
    // Custom query methods for specific operations, if needed
    
}

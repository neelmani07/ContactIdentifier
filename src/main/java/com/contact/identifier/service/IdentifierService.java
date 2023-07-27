package com.contact.identifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contact.identifier.entity.Contact;
import com.contact.identifier.repository.ContactRepository;
import com.contact.identifier.request.ContactRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IdentifierService {

	@Autowired
    ContactRepository contactRepository;

	public IdentifierResult identifyContact(ContactRequest contactRequest) {
	    
		String email = contactRequest.getEmail();
	    String phoneNumber = contactRequest.getPhoneNumber();

	    boolean hasEmail = email != null && !email.isEmpty();
	    boolean hasPhoneNumber = phoneNumber != null && !phoneNumber.isEmpty();
	    
	    if(!hasEmail && !hasPhoneNumber)
	    	return null;
	    
	    Optional<Contact> existingContactByEmail = hasEmail ? contactRepository.findFirstByEmailOrderByCreatedAtAsc(email) : Optional.empty();
	    Optional<Contact> existingContactByPhoneNumber = hasPhoneNumber ? contactRepository.findFirstByPhoneNumberOrderByCreatedAtAsc(phoneNumber) : Optional.empty();
	    Contact newContact = null;
        IdentifierResult result = null;
        
        //Both email and phone number matched and point to same contact.
	    if (existingContactByEmail.isPresent() && existingContactByPhoneNumber.isPresent() &&
	        existingContactByEmail.get().equals(existingContactByPhoneNumber.get())) {
	        result = createIdentifierResult(existingContactByEmail.get());
	    }
	    
	    //Both matched but point to different contact.
	    else if (existingContactByEmail.isPresent() && existingContactByPhoneNumber.isPresent()) {
	        Contact c1 = existingContactByEmail.get();
	        Contact c2 = existingContactByPhoneNumber.get();
	        
	        //both matched to primary contacts. newer one has to be changed to secondary.
	        if (c1.getLinkPrecedence().equals("primary") && c2.getLinkPrecedence().equals("primary")) {
	            if (c2.getCreatedAt().isBefore(c1.getCreatedAt())) {
	            	changePrimaryToSecondary(c2, c1);
	            	result = createIdentifierResult(c2);
	            } else {
	            	changePrimaryToSecondary(c1, c2);
	                result = createIdentifierResult(c1);
	            }

	        //one matched to a secondary and another to a primary and vice versa.
	        } else if (c1.getLinkPrecedence().equals("secondary") && c2.getLinkPrecedence().equals("primary")) {
	            result = createIdentifierResult(c2);
	        } else if (c1.getLinkPrecedence().equals("primary") && c2.getLinkPrecedence().equals("secondary")) {
	            result = createIdentifierResult(c1);
	        }
	    }

	    //only email matched.
	    else if (existingContactByEmail.isPresent()) {
	        Contact c1 = existingContactByEmail.get();
	        
	        //matched with a secondary contact.
	        if(c1.getLinkPrecedence().equals("secondary")) { 
	        	if(hasPhoneNumber) newContact = saveNewContact(email, phoneNumber, "secondary", c1.getLinkedContact());
	        	result = createIdentifierResult(c1.getLinkedContact());
	        }
	        //matched with a primary contact.
	        else {
	        	if(hasPhoneNumber) newContact = saveNewContact(email, phoneNumber, "secondary", c1);
	            result = createIdentifierResult(c1);
	        }
	    }
	    
	    //only phone matched.
	    else if (existingContactByPhoneNumber.isPresent()) {
	        Contact c2 = existingContactByPhoneNumber.get();
	        
	        //matched with a secondary contact.
	        if (c2.getLinkPrecedence().equals("secondary")) {
	            if(hasEmail) newContact = saveNewContact(email, phoneNumber, "secondary", c2.getLinkedContact());
	            result = createIdentifierResult(c2.getLinkedContact());
	        }
	        //matched with a primary contact.
	        else {
	        	if(hasEmail) newContact = saveNewContact(email, phoneNumber, "secondary", c2);
	            result = createIdentifierResult(c2);
	        }
	    } 
	    //nothing matched.
	    else {
	    	newContact = saveNewContact(email, phoneNumber, "primary", null);
		    result = createIdentifierResult(newContact);
	    }
	    return result;
	}

	private void changePrimaryToSecondary(Contact primaryContact, Contact newSecondaryContact) {
		
		newSecondaryContact.setLinkPrecedence("secondary");
		newSecondaryContact.setUpdatedAt(LocalDateTime.now());
		newSecondaryContact.setLinkedContact(primaryContact);
        newSecondaryContact = contactRepository.save(newSecondaryContact);
		List<Contact> secondaryContacts = contactRepository.findByLinkedContact(newSecondaryContact);
		for(Contact sContact:secondaryContacts) {
			sContact.setLinkedContact(primaryContact);
			sContact.setUpdatedAt(LocalDateTime.now());
		}
	}

	private Contact saveNewContact(String email, String phoneNumber, String string, Contact linkedContact) {
		
		Contact newContact = new Contact();
		newContact.setEmail(email);
		newContact.setPhoneNumber(phoneNumber);
		newContact.setCreatedAt(LocalDateTime.now());
		newContact.setUpdatedAt(LocalDateTime.now());
		newContact.setLinkPrecedence(string);
		newContact.setLinkedContact(linkedContact);
		return contactRepository.save(newContact);
	}

    private IdentifierResult createIdentifierResult(Contact primaryContact) {
    	
        List<Contact> secondaryContacts = contactRepository.findByLinkedContact(primaryContact);
        Set<String> emails = new HashSet<>();
        emails.add(primaryContact.getEmail());
        Set<String> phoneNumbers = new HashSet<>();
        phoneNumbers.add(primaryContact.getPhoneNumber());
        List<Long> secondaryContactIds = new ArrayList<>();

        for (Contact contact : secondaryContacts) {            
        	if (contact.getEmail() != null) {
                emails.add((String) contact.getEmail());
            }
            if (contact.getPhoneNumber() != null) {
                phoneNumbers.add((String) contact.getPhoneNumber());
            }
            secondaryContactIds.add(contact.getId());
        }

        return new IdentifierResult(primaryContact.getId(), emails, phoneNumbers, secondaryContactIds);
    }
}

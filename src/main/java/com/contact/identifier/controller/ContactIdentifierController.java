package com.contact.identifier.controller;

import org.springframework.web.bind.annotation.RestController;

import com.contact.identifier.request.ContactRequest;
import com.contact.identifier.service.ContactResponse;
import com.contact.identifier.service.IdentifierResult;
import com.contact.identifier.service.IdentifierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactIdentifierController {
	
	@Autowired
    IdentifierService IdentifierService;

	@PostMapping(value = "/identify")
	public ResponseEntity<ContactResponse> identifyContact(@RequestBody ContactRequest contact) {
	    IdentifierResult result = IdentifierService.identifyContact(contact);
	    if (result != null) {
	        ContactResponse response = new ContactResponse(result);
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.badRequest().build();
	    }
	}

}

package com.contact.identifier.service;

public class ContactResponse {
	//This class is designed to basically encapsulate the contact response in JSON from. 
    private IdentifierResult contact;

    public ContactResponse(IdentifierResult contact) {
        this.contact = contact;
    }

    public IdentifierResult getContact() {
        return contact;
    }

    public void setContact(IdentifierResult contact) {
        this.contact = contact;
    }
}

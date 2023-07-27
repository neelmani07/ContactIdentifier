package com.contact.identifier.service;

public class ContactResponse {
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

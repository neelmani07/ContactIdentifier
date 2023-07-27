package com.contact.identifier.service;

import java.util.List;
import java.util.Set;

public class IdentifierResult {
	
	private Long primaryContactId;
    private Set<String> emails;
    private Set<String> phoneNumbers;
    private List<Long> secondaryContactIds;
	
    public IdentifierResult(Long primaryContactId, Set<String> emails, Set<String> phoneNumbers,
			List<Long> secondaryContactIds) {
		super();
		this.primaryContactId = primaryContactId;
		this.emails = emails;
		this.phoneNumbers = phoneNumbers;
		this.secondaryContactIds = secondaryContactIds;
	}

	public IdentifierResult(Integer id, List<String> emails2, List<String> phoneNumbers2,
			List<Long> secondaryContactIds2) {
		// TODO Auto-generated constructor stub
	}

	public Long getPrimaryContactId() {
		return primaryContactId;
	}

	public void setPrimaryContactId(Long primaryContactId) {
		this.primaryContactId = primaryContactId;
	}

	public Set<String> getEmails() {
		return emails;
	}

	public void setEmails(Set<String> emails) {
		this.emails = emails;
	}

	public Set<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<Long> getSecondaryContactIds() {
		return secondaryContactIds;
	}

	public void setSecondaryContactIds(List<Long> secondaryContactIds) {
		this.secondaryContactIds = secondaryContactIds;
	}
}

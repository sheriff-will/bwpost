package com.application.iserv.ui.participants.models;

public class ReferenceModel {

    Long referenceId, participantId;
    String firstname, lastname, identityNumber, primaryMobile, postalAddress, reference;

    public ReferenceModel() {

    }

    public ReferenceModel(Long referenceId, Long participantId, String firstname, String lastname,
                          String identityNumber, String primaryMobile, String postalAddress) {
        this.referenceId = referenceId;
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.primaryMobile = primaryMobile;
        this.postalAddress = postalAddress;
    }

    public ReferenceModel(Long referenceId, Long participantId, String firstname, String lastname,
                          String identityNumber, String primaryMobile, String postalAddress, String reference) {
        this.referenceId = referenceId;
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.primaryMobile = primaryMobile;
        this.postalAddress = postalAddress;
        this.reference = reference;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "ReferenceModel{" +
                "referenceId=" + referenceId +
                ", participantId=" + participantId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", primaryMobile='" + primaryMobile + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

}

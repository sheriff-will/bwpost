package com.application.iserv.ui.agents.models;

public class NomineesModel {

    Long nomineeId, participantId;
    String firstname, lastname, identityNumber,
            relationship, primaryMobile, postalAddress, nominee;

    public NomineesModel() {

    }

    public NomineesModel(Long nomineeId, Long participantId, String firstname, String lastname, String identityNumber,
                         String relationship, String primaryMobile, String postalAddress) {
        this.nomineeId = nomineeId;
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.relationship = relationship;
        this.primaryMobile = primaryMobile;
        this.postalAddress = postalAddress;
    }

    public NomineesModel(Long nomineeId, Long participantId, String firstname,
                         String lastname, String identityNumber, String relationship,
                         String primaryMobile, String postalAddress, String nominee) {
        this.nomineeId = nomineeId;
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.relationship = relationship;
        this.primaryMobile = primaryMobile;
        this.postalAddress = postalAddress;
        this.nominee = nominee;
    }

    public Long getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(Long nomineeId) {
        this.nomineeId = nomineeId;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    @Override
    public String toString() {
        return "NomineesModel{" +
                "nomineeId=" + nomineeId +
                ", participantId=" + participantId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", relationship='" + relationship + '\'' +
                ", primaryMobile='" + primaryMobile + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", nominee='" + nominee + '\'' +
                '}';
    }

}

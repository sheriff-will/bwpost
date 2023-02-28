package com.application.iserv.ui.participants.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceModel {

    Long referenceId, participantId;
    String firstname, lastname, identityNumber, relationship, primaryMobile, postalAddress, reference;

    public ReferenceModel(Long referenceId, Long participantId, String firstname, String lastname,
                          String identityNumber, String relationship, String primaryMobile, String postalAddress) {
        this.referenceId = referenceId;
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.identityNumber = identityNumber;
        this.relationship = relationship;
        this.primaryMobile = primaryMobile;
        this.postalAddress = postalAddress;
    }

}

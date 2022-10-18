package com.application.iserv.security.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegisterAgentModel {

    private Long application_user_id;
    private String firstname, lastname, identity_number,
            username, password, role, district, village, service;
    private int is_account_expired, is_account_locked, is_credentials_expired, is_account_disabled;

    public RegisterAgentModel(String firstname, String lastname, String identity_number, String username,
                              String password, String role, String district, String village, String service,
                              int is_account_expired, int is_account_locked, int is_credentials_expired,
                              int is_account_disabled) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.identity_number = identity_number;
        this.username = username;
        this.password = password;
        this.role = role;
        this.district = district;
        this.village = village;
        this.service = service;
        this.is_account_expired = is_account_expired;
        this.is_account_locked = is_account_locked;
        this.is_credentials_expired = is_credentials_expired;
        this.is_account_disabled = is_account_disabled;
    }

}

package com.application.iserv.backend.auth;

import com.application.iserv.security.services.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationUserService implements UserDetailsService {

    private final CredentialsService credentialsService;
    private final ApplicationUserDao applicationUserDao;

    @Autowired
    public ApplicationUserService(CredentialsService credentialsService, ApplicationUserDao applicationUserDao) {
        this.credentialsService = credentialsService;
        this.applicationUserDao = applicationUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao
                .selectApplicationUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }

}

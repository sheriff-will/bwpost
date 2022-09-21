package com.application.iserv.backend.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.application.iserv.security.enums.ApplicationUserRole.FIELD_AGENT;
import static com.application.iserv.security.enums.ApplicationUserRole.SUPERVISOR;

@Repository
public class ApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers(username)
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers(String name) {

        ApplicationUser user1 = new ApplicationUser(
                "user1",
                passwordEncoder.encode("pass1"),
                SUPERVISOR.getGrantedAuthorities(),
                true,
                true,
                true,
                true

        );

        ApplicationUser user2 = new ApplicationUser(
                "user2",
                passwordEncoder.encode("pass2"),
                FIELD_AGENT.getGrantedAuthorities(),
                true,
                true,
                true,
                true

        );

        return Lists.newArrayList(
                user1,
                user2
        );

    }

}
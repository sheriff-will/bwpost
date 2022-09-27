package com.application.iserv.security.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.application.iserv.security.enums.ApplicationUserPermission.FIELD_AGENT_READ;
import static com.application.iserv.security.enums.ApplicationUserPermission.SUPERVISOR_READ;

public enum ApplicationUserRole {
    SUPERVISOR(Sets.newHashSet(SUPERVISOR_READ, SUPERVISOR_READ, FIELD_AGENT_READ, FIELD_AGENT_READ)),
    FIELD_AGENT(Sets.newHashSet(FIELD_AGENT_READ, FIELD_AGENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}

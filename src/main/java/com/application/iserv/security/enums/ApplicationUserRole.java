package com.application.iserv.security.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.application.iserv.security.enums.ApplicationUserPermission.*;

public enum ApplicationUserRole {

    SUPERVISOR(Sets.newHashSet(SUPERVISOR_READ, SUPERVISOR_WRITE)),
    ADMIN(Sets.newHashSet(ADMIN_READ, ADMIN_WRITE, SUPERVISOR_READ, SUPERVISOR_WRITE)),
    ROOT(Sets.newHashSet(ROOT_READ, ROOT_WRITE, ADMIN_READ, ADMIN_WRITE, SUPERVISOR_READ, SUPERVISOR_WRITE));

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

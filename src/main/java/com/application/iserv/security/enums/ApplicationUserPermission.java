package com.application.iserv.security.enums;

public enum ApplicationUserPermission {

    ROOT_READ("root:read"),
    ROOT_WRITE("root:write"),

    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),

    SUPERVISOR_READ("supervisor:read"),
    SUPERVISOR_WRITE("supervisor:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

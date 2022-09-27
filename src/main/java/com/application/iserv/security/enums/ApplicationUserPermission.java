package com.application.iserv.security.enums;

public enum ApplicationUserPermission {
    SUPERVISOR_READ("supervisor:read"),
    SUPERVISOR_WRITE("supervisor:write"),
    FIELD_AGENT_READ("field_agent:read"),
    FIELD_AGENT_WRITE("field_agent:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

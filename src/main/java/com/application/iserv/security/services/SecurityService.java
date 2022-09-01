package com.application.iserv.security.services;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    public void logout() {
        UI.getCurrent().getPage().setLocation("/"); // TODO Take a look at this path user is sent to after logout
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler
                .logout(VaadinServletRequest
                        .getCurrent()
                        .getHttpServletRequest(),
                        null,
                        null);
    }

}

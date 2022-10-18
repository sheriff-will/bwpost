package com.application.iserv.security.services;


import com.application.iserv.ui.utils.SessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    public void logout() {
        SessionManager sessionManager = new SessionManager();
        sessionManager.clearApplicationUser();

        UI.getCurrent().getPage().setLocation("/");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler
                .logout(VaadinServletRequest
                        .getCurrent()
                        .getHttpServletRequest(),
                        null,
                        null);
    }

}

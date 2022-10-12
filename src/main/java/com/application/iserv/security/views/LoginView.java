package com.application.iserv.security.views;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | iServ")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginOverlay loginOverlay = new LoginOverlay();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n loginI18n = LoginI18n.createDefault();
        LoginI18n.Form loginI18nForm = loginI18n.getForm();
        loginI18n.setForm(loginI18nForm);
        loginI18nForm.setForgotPassword("I don't have an account");
        loginI18n.setAdditionalInformation(
                "Please, contact maintenance@iServ.com if you're experiencing issues logging into your account");


        loginOverlay.setTitle("iServ");
        loginOverlay.setDescription("Serving the nation ");
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login");
        loginOverlay.setForgotPasswordButtonVisible(true);
        loginOverlay.setI18n(loginI18n);

        loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> {
            getUI().ifPresent(ui -> ui.navigate("signup"));
        });

        add(
                loginOverlay
        );

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent
                .getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginOverlay.setError(true);
        }

    }

}

package com.application.iserv.security.views;


import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | iServ")
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    LoginForm loginForm = new LoginForm();

    LoginOverlay loginOverlay = new LoginOverlay();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n loginI18n = LoginI18n.createDefault();
        LoginI18n.Form loginI18nForm = loginI18n.getForm();
        loginI18nForm.setTitle("Login");
        loginI18n.setForm(loginI18nForm);

        loginForm.setAction("login");
        loginForm.setI18n(loginI18n);
        loginForm.setForgotPasswordButtonVisible(false);


        Image logo = new Image();
        logo.setWidth("15%");
        logo.setSrc("images/logo.png");

        loginOverlay.setTitle("iServ");
        loginOverlay.setDescription("Serving the nation ");
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login");
        loginOverlay.setForgotPasswordButtonVisible(false);

        add(
                logo,
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

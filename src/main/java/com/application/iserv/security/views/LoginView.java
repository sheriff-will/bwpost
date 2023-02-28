package com.application.iserv.security.views;

import com.application.iserv.security.services.CredentialsService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("login")
@PageTitle("Login | iServ")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    // LoginOverlay
    LoginOverlay loginOverlay = new LoginOverlay();

    // PasswordField
    PasswordField password = new PasswordField("Password");
    PasswordField confirmPassword = new PasswordField("Confirm Password");

    // TextField
    TextField phoneNumber = new TextField("Phone number");

    // Dialog
    Dialog signupDialog = new Dialog();

    // Buttons
    Button verify = new Button("Continue");

    // Strings
    String username;

    // Services
    private final PasswordEncoder passwordEncoder;
    private final CredentialsService credentialsService;

    @Autowired
    public LoginView(PasswordEncoder passwordEncoder, CredentialsService credentialsService) {

        this.passwordEncoder = passwordEncoder;
        this.credentialsService = credentialsService;

        addClassName("login-view");
        setSizeFull();

        checkScreenSize();
        configureButtons();
        configureDialogs();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n loginI18n = LoginI18n.createDefault();
        LoginI18n.Form loginI18nForm = loginI18n.getForm();
        loginI18n.setForm(loginI18nForm);
       // loginI18nForm.setForgotPassword("I don't have an account");
        loginI18n.setAdditionalInformation("username: admin, password: qwerty");
        loginI18nForm.setUsername("Username");

        loginOverlay.setTitle("BotswanaPost");
        loginOverlay.setDescription("We deliver, whatever wherever");
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login");
        loginOverlay.setForgotPasswordButtonVisible(true);
        loginOverlay.setI18n(loginI18n);

        loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> {
            /*verify.setEnabled(true);
            phoneNumber.clear();

            password.setVisible(false);
            confirmPassword.setVisible(false);

            configureDialogs();
            phoneNumber.setLabel("Username");
            phoneNumber.setVisible(true);
            verify.setText("Continue");

            phoneNumber.setPlaceholder("");


            signupDialog.open();*/

            Notification notification = new Notification("username: admin ~ password: qwerty");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            notification.setDuration(5000);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.open();
        });

        add(
                loginOverlay
        );

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                signupDialog.setWidth("40%");
            }
            else {
                signupDialog.setWidth("90%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                signupDialog.setWidth("40%");
            }
            else {
                signupDialog.setWidth("90%");
            }

        });

    }

    private void configureButtons() {

        verify.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        verify.addClickListener(click -> {
            if (verify.getText().equalsIgnoreCase("OK")) {
                signupDialog.close();
            }
            else if (phoneNumber.getValue().isEmpty()) {
                if (verify.getText().equalsIgnoreCase("Continue")) {
                    Notification notification = new Notification("Enter your phone number");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();
                }
                else if (verify.getText().equalsIgnoreCase("Verify")) {
                    Notification notification = new Notification("Enter verification code");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();
                }

            }
            else if (verify.getText().equalsIgnoreCase("Verify")) {

                // TODO Hook Up Twilio API and don't forget to to disable verify
                if (phoneNumber.getValue().equalsIgnoreCase("123")) {

                    password.setMinLength(6);
                    password.setMaxLength(30);
                    confirmPassword.setMinLength(6);
                    confirmPassword.setMaxLength(30);

                    FormLayout formLayout = new FormLayout(
                            password,
                            confirmPassword
                    );

                    verify.setEnabled(true);
                    verify.setText("Done");

                    phoneNumber.setVisible(false);
                    password.setVisible(true);
                    confirmPassword.setVisible(true);
                    signupDialog.add(formLayout);

                    signupDialog.setHeaderTitle("Set up your password");

                }
                else {
                    Notification notification = new Notification("Verification Incorrect");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();
                }

            }
            else if (verify.getText().equalsIgnoreCase("Done")) {

                password.setInvalid(false);
                confirmPassword.setInvalid(false);

                if (password.getValue().isEmpty()) {
                    password.setErrorMessage("Enter Password");
                    password.setInvalid(true);
                }
                else if (confirmPassword.getValue().isEmpty()) {
                    confirmPassword.setErrorMessage("Confirm your password");
                    confirmPassword.setInvalid(true);
                }
                else if (password.getValue().length() < 6) {
                    password.setErrorMessage("Password too short");
                    password.setInvalid(true);
                }
                else if (password.getValue().length() > 30) {
                    password.setErrorMessage("Password too long");
                    password.setInvalid(true);
                }
                else if (confirmPassword.getValue().length() < 6) {
                    confirmPassword.setErrorMessage("Confirm Password too short");
                    confirmPassword.setInvalid(true);
                }
                else if (confirmPassword.getValue().length() > 30) {
                    confirmPassword.setErrorMessage("Confirm Password too long");
                    confirmPassword.setInvalid(true);
                }
                else if (!password.getValue().equals(confirmPassword.getValue())) {
                    password.setErrorMessage("Passwords do not match");
                    confirmPassword.setErrorMessage("Passwords do not match");
                    password.setInvalid(true);
                    confirmPassword.setInvalid(true);
                }
                else {

                    credentialsService.updatePassword(
                            username,
                            passwordEncoder.encode(password.getValue())
                    );

                    Notification notification = new Notification("Successful, Try to Log In");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(15000);
                    notification.open();

                    signupDialog.close();
                }

            }
            else {
                verify.setEnabled(false);

                String response = credentialsService.checkForUser(phoneNumber.getValue());

                if (response.contains("Successful")) {
                    String[] getName = response.split("-");

                    verify.setEnabled(true);
                    verify.setText("Verify");

                    username = phoneNumber.getValue();
                    phoneNumber.setLabel("Enter code sent to: "+phoneNumber.getValue());
                    phoneNumber.setPlaceholder("Verification code");

                    phoneNumber.clear();
                    signupDialog.setHeaderTitle("Hello, "+getName[1]);
                    signupDialog.getFooter().add(verify);

                }
                else {

                    verify.setEnabled(true);
                    verify.setText("OK");

                    signupDialog.setHeaderTitle("Oops!");
                    signupDialog.add(new H2(response));
                    phoneNumber.setVisible(false);
                    signupDialog.getFooter().add(verify);

                }

            }
        });

    }

    private void configureDialogs() {

        signupDialog = new Dialog();

        checkScreenSize();

        FormLayout formLayout = new FormLayout(
                phoneNumber
        );

        signupDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> signupDialog.close())
        );

        signupDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            signupDialog.close();
        });

        signupDialog.setCloseOnEsc(false);
        signupDialog.setCloseOnOutsideClick(false);

        signupDialog.setHeaderTitle("Sign Up");
        signupDialog.add(formLayout);
        signupDialog.getFooter().add(verify);

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

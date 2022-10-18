package com.application.iserv.security.views;

import com.application.iserv.security.models.RegisterAgentModel;
import com.application.iserv.security.services.CredentialsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@RolesAllowed({"ADMIN"})
@Route("register")
@PageTitle("Register | iServ")
public class RegisterAgentView extends VerticalLayout {

    // TextField
    TextField firstname = new TextField(FIRSTNAME);
    TextField lastname = new TextField(LASTNAME);
    TextField identityNumber = new TextField(ID_NUMBER);
    TextField phoneNumber = new TextField("Phone number");
    TextField password = new TextField("Password");
    TextField district = new TextField("District");
    TextField village = new TextField("Village");
    TextField service = new TextField("Service");


    // ComboBox
    ComboBox<String> role = new ComboBox<>("Role");
    ComboBox<String> accountNotExpired = new ComboBox<>("Account not expired");
    ComboBox<String> accountNotLocked = new ComboBox<>("Account not locked");
    ComboBox<String> credentialsNotExpired = new ComboBox<>("Credentials not Expired");
    ComboBox<String> accountNotDisabled = new ComboBox<>("Account not disabled");

    // Buttons
    Button register = new Button("Register");

    // FormLayout
    FormLayout formLayout;

    // Services
    private final PasswordEncoder passwordEncoder;
    private final CredentialsService credentialsService;

    @Autowired
    public RegisterAgentView(PasswordEncoder passwordEncoder, CredentialsService credentialsService) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsService = credentialsService;

        setSizeFull();

        configureButtons();
        configureComboBoxItems();

        H2 h2 = new H2("Register Agent");

        formLayout = new FormLayout(
                h2,
                firstname,
                lastname,
                identityNumber,
                phoneNumber,
                password,
                role,
                district,
                village,
                service,
                accountNotExpired,
                accountNotLocked,
                credentialsNotExpired,
                accountNotDisabled,
                register
        );

        formLayout.setColspan(h2, 2);
        formLayout.setColspan(register, 2);

        add(
                formLayout
        );

    }

    private void configureButtons() {

        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        register.addClickListener(click -> validateFields());

    }

    private void configureComboBoxItems() {

        role.setItems(List.of("ADMIN", "SUPERVISOR"));
        role.setItemLabelGenerator(String::toString);

        accountNotExpired.setItems(List.of("True", "False"));
        accountNotExpired.setItemLabelGenerator(String::toString);
        accountNotExpired.setValue("True");

        accountNotLocked.setItems(List.of("True", "False"));
        accountNotLocked.setItemLabelGenerator(String::toString);
        accountNotLocked.setValue("True");

        credentialsNotExpired.setItems(List.of("True", "False"));
        credentialsNotExpired.setItemLabelGenerator(String::toString);
        credentialsNotExpired.setValue("True");

        accountNotDisabled.setItems(List.of("True", "False"));
        accountNotDisabled.setItemLabelGenerator(String::toString);
        accountNotDisabled.setValue("True");

    }

    private void validateFields() {

        if (firstname.getValue().isEmpty()) {
            Notification notification = new Notification("Enter Firstname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (lastname.getValue().isEmpty()) {
            Notification notification = new Notification("Enter Lastname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (identityNumber.getValue().isEmpty()) {
            Notification notification = new Notification("Enter ID Number");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (phoneNumber.getValue().isEmpty()) {
            Notification notification = new Notification("Enter Username");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (role.getValue() == null) {
            Notification notification = new Notification("Enter Role");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (district.getValue().isEmpty()) {
            Notification notification = new Notification("Enter District");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (village.getValue().isEmpty()) {
            Notification notification = new Notification("Enter Village");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (service.getValue().isEmpty()) {
            Notification notification = new Notification("Enter Service");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (accountNotExpired.getValue() == null) {
            Notification notification = new Notification("Is account expired");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (accountNotLocked.getValue() == null) {
            Notification notification = new Notification("Is account locked");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (credentialsNotExpired.getValue() == null) {
            Notification notification = new Notification("Is credentials expired");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (accountNotDisabled.getValue() == null) {
            Notification notification = new Notification("Is account disabled");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else {

            int isAccountNotExpired = 1;
            int isAccountNotLocked = 1;
            int isCredentialsNotExpired = 1;
            int isAccountNotDisabled = 1;

            if (accountNotExpired.getValue().equalsIgnoreCase("False")) {
                isAccountNotExpired = 0;
            }

            if (accountNotLocked.getValue().equalsIgnoreCase("False")) {
                isAccountNotLocked = 0;
            }

            if (credentialsNotExpired.getValue().equalsIgnoreCase("False")) {
                isCredentialsNotExpired = 0;
            }

            if (accountNotDisabled.getValue().equalsIgnoreCase("False")) {
                isAccountNotDisabled = 0;
            }

            String password_str;
            if (password.getValue().isEmpty()) {
               password_str = "null";
            }
            else {
                password_str = passwordEncoder.encode(password.getValue());
            }

            RegisterAgentModel registerAgentModel = new RegisterAgentModel(
                    firstname.getValue(),
                    lastname.getValue(),
                    identityNumber.getValue(),
                    phoneNumber.getValue(),
                    password_str,
                    role.getValue(),
                    district.getValue(),
                    village.getValue(),
                    service.getValue(),
                    isAccountNotExpired,
                    isAccountNotLocked,
                    isCredentialsNotExpired,
                    isAccountNotDisabled
            );

            credentialsService.registerAgent(registerAgentModel);

            Notification notification = new Notification("Agent Successfully Added");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

        }

    }

}

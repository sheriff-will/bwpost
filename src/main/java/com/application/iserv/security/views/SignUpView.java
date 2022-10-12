package com.application.iserv.security.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("signup")
@PageTitle("Sign Up | iServ")
public class SignUpView extends VerticalLayout {

    // FormLayout
    FormLayout formLayout;

    // TextFields
    TextField phoneNumber = new TextField();

    public SignUpView() {

        setSizeFull();

        H2 h2 = new H2("Enter your phone number");

        formLayout = new FormLayout(
                h2,
                phoneNumber
        );

        VerticalLayout v1 = new VerticalLayout(formLayout);
        v1.setAlignItems(Alignment.CENTER);

        v1.setHorizontalComponentAlignment(Alignment.CENTER);
        v1.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        phoneNumber.setLabel("Phone number");

        formLayout.setColspan(h2, 2);
        formLayout.setColspan(phoneNumber, 2);

        add(
                v1
        );

    }

}

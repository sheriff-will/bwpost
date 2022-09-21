package com.application.iserv.ui.parameters.views;

import com.application.iserv.tests.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("iServ | Parameters")
@Route(value = "parameters", layout = MainLayout.class)
@PermitAll
public class ParametersView extends VerticalLayout {

    // IntegerField
    IntegerField workingDays = new IntegerField();

    // NumberField
    NumberField ratePerDay = new NumberField();

    public ParametersView() {

        workingDays.setLabel("Working Days");
        workingDays.setHasControls(true);
        workingDays.setMin(0);

        ratePerDay.setLabel("Rate Per Day");

        FormLayout formLayout = new FormLayout(
                workingDays,
                ratePerDay
        );

        add(
                formLayout
        );

    }

}

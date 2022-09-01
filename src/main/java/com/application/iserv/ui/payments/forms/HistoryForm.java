package com.application.iserv.ui.payments.forms;


import com.application.iserv.ui.payments.models.AgentPaymentsModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import static com.application.iserv.ui.utils.Constants.*;

public class HistoryForm extends FormLayout {

    // TextFields
    TextField ratePerDay = new TextField();
    TextField daysWorked = new TextField();
    TextField baseSalary = new TextField();
    TextField bonus = new TextField();
    TextField deduction = new TextField();
    TextField totalSalary = new TextField();
    TextField approval = new TextField();
    TextField claimed = new TextField();
    TextField paymentMode = new TextField();
    TextField provider = new TextField();

    // Buttons
    Button back = new Button(BACK);
    Button exportStatements = new Button(EXPORT_STATEMENTS);

    // Binder
    Binder<AgentPaymentsModel> binder = new Binder<>(AgentPaymentsModel.class);

    private AgentPaymentsModel agentPaymentsModel;

    public HistoryForm() {

        configureButtons();

        setSizeFull();

        binder.bindInstanceFields(this);

        ratePerDay.setReadOnly(true);
        ratePerDay.setLabel(RATE);
        ratePerDay.setValue("P15.00");

        daysWorked.setReadOnly(true);
        daysWorked.setLabel(DAYS_WORKED);
        daysWorked.setValue("18");

        baseSalary.setReadOnly(true);
        baseSalary.setLabel(BASE_SALARY);
        baseSalary.setValue("P270.00");

        bonus.setReadOnly(true);
        bonus.setLabel(BONUS);
        bonus.setValue("P0.00");

        deduction.setReadOnly(true);
        deduction.setLabel(DEDUCTION);
        deduction.setValue("P0.00");

        totalSalary.setReadOnly(true);
        totalSalary.setLabel(TOTAL_SALARY);
        totalSalary.setValue("P270.00");

        approval.setReadOnly(true);
        approval.setLabel(APPROVAL);
        approval.setValue("Approved");

        claimed.setReadOnly(true);
        claimed.setLabel(CLAIMED);
        claimed.setValue("Yes");

        paymentMode.setReadOnly(true);
        paymentMode.setLabel(PAYMENT_MODE);
        paymentMode.setValue("Cash");

        provider.setReadOnly(true);
        provider.setLabel("Provider");
        provider.setValue(PROVIDER);

        exportStatements.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        HorizontalLayout exportBackLayout = new HorizontalLayout(exportStatements, back);
        exportBackLayout.setFlexGrow(2, exportStatements);
        exportBackLayout.setFlexGrow(1, back);
        exportBackLayout.getStyle()
                .set("margin-bottom", "var(--lumo-space-xl");


        // TODO Temporary fix
        Hr hr = new Hr();
        hr.getStyle()
                .set("margin-top", "var(--lumo-space-xl");

        add(ratePerDay, daysWorked, baseSalary, bonus, deduction,
                totalSalary, approval, claimed, paymentMode, provider, exportBackLayout, hr);

      //  scrollIntoView();

    }

    public void setHistory(AgentPaymentsModel agentPaymentsModel) {
        this.agentPaymentsModel = agentPaymentsModel;
        binder.readBean(agentPaymentsModel);
    }

    private void configureButtons() {

        exportStatements.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        back.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

    }

}

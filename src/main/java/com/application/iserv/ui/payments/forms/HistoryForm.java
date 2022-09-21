package com.application.iserv.ui.payments.forms;


import com.application.iserv.ui.payments.models.HistoryModel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import static com.application.iserv.ui.utils.Constants.*;

public class HistoryForm extends VerticalLayout {

    // TextFields
    TextField ratePerDay = new TextField();
    TextField daysWorked = new TextField();
    TextField baseSalary = new TextField();
    TextField bonus = new TextField();
    TextField deduction = new TextField();
    TextField totalSalary = new TextField();
    TextField status = new TextField();
    TextField claimed = new TextField();
    TextField paymentMode = new TextField();
    TextField provider = new TextField();

    // Buttons
    Button back = new Button(BACK);
    Button exportStatements = new Button(EXPORT_STATEMENTS);

    // Binder
    Binder<HistoryModel> binder = new Binder<>(HistoryModel.class);

    private HistoryModel historyModel;

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

        status.setReadOnly(true);
        status.setLabel(APPROVAL);
        status.setValue("Approved");

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

        FormLayout formLayout = new FormLayout(
                ratePerDay, daysWorked, baseSalary, bonus, deduction,
                totalSalary, status, claimed, paymentMode, provider
        );

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("300px", 2)
        );

        VerticalLayout buttonsVerticalLayout = new VerticalLayout(exportBackLayout);
        buttonsVerticalLayout.setPadding(false);
        buttonsVerticalLayout.setMargin(false);

        add(
                formLayout,
                buttonsVerticalLayout
        );

    }

    public void setHistory(HistoryModel historyModel) {
        this.historyModel = historyModel;
        binder.readBean(historyModel);
    }

    private void configureButtons() {

        exportStatements.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        back.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

        back.addClickListener(click -> fireEvent(new CloseHistoryFormEvent(this)));

    }

    // Events
    public static abstract class HistoryFormEvent extends ComponentEvent<HistoryForm> {

        private HistoryModel historyModel;

        protected HistoryFormEvent(HistoryForm source, HistoryModel historyModel) {
            super(source, false);
            this.historyModel = historyModel;
        }

        public HistoryModel getHistory() {
            return historyModel;
        }

    }

    public static class ExportStatementsEvent extends HistoryFormEvent {
        ExportStatementsEvent(HistoryForm source) {
            super(source, null);
        }
    }

    public static class CloseHistoryFormEvent extends HistoryFormEvent {
        CloseHistoryFormEvent(HistoryForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

package com.application.iserv.ui.payments.forms;


import com.application.iserv.ui.payments.models.AgentPaymentsModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import static com.application.iserv.ui.utils.Constants.*;

public class AuthorizeForm extends FormLayout {

    // TextFields
    TextField bonus = new TextField();
    TextField bonusReason = new TextField();
    TextField deduction = new TextField();
    TextField deductionReason = new TextField();

    // Selects
    Select<String> status = new Select<>();

    // Buttons
    Button save = new Button(SAVE);
    Button back = new Button(BACK);

    // Binder
    Binder<AgentPaymentsModel> binder = new Binder<>(AgentPaymentsModel.class);
    private AgentPaymentsModel agentPaymentsModel;

    public AuthorizeForm() {

        binder.bindInstanceFields(this);

        configureButtons();
        configureLayouts();

        add(
                status,
                bonus,
                bonusReason,
                deduction,
                deductionReason,
                new HorizontalLayout(save, back)
        );

    }

    public void setAuthorize(AgentPaymentsModel agentPaymentsModel) {
        this.agentPaymentsModel = agentPaymentsModel;
        binder.readBean(agentPaymentsModel);

        readStatusBean(agentPaymentsModel);

    }

    private void readStatusBean(AgentPaymentsModel agentPaymentsModel) {

        if (agentPaymentsModel.getApproval().equalsIgnoreCase(APPROVED)
                && agentPaymentsModel.getClaimed().equalsIgnoreCase(YES)) {
            status.setEnabled(false);
        }
        else {
            status.setEnabled(true);
        }

        if (agentPaymentsModel.getApproval().equalsIgnoreCase(APPROVED)) {
            status.setValue(APPROVE);
        }
        else if (agentPaymentsModel.getApproval().equalsIgnoreCase(DENIED)) {
            status.setValue(DENY);
        }
        else if (agentPaymentsModel.getApproval().equalsIgnoreCase(PENDING)) {
            status.setValue(PEND);
        }
        else if (agentPaymentsModel.getApproval().equalsIgnoreCase("")) {
            status.clear();
        }
    }

    private void configureLayouts() {

        status.setLabel(STATUS);
        status.setItems(getStatuses());
        status.setPlaceholder(SELECT_STATUS);

        bonus.setLabel(BONUS);
        bonusReason.setLabel(REASON);
        bonusReason.setPlaceholder(REASON_FOR_BONUS);
        bonus.addValueChangeListener(bonusValueChangeEvent -> {
            if (bonus.getValue().isEmpty()) {
                deduction.setEnabled(true);
                deductionReason.setEnabled(true);
            }
            else {
                deduction.setEnabled(false);
                deductionReason.setEnabled(false);
            }
        });

        deduction.setLabel(DEDUCTION);
        deductionReason.setLabel(REASON);
        deductionReason.setPlaceholder(REASON_FOR_DEDUCTION);
        deduction.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {

            if (deduction.getValue().isEmpty()) {
                bonus.setEnabled(true);
                bonusReason.setEnabled(true);
            }
            else {
                bonus.setEnabled(false);
                bonusReason.setEnabled(false);
            }

        });

    }

    private void configureButtons() {

        save.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        back.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

    }

}

package com.application.iserv.ui.payments.forms;


import com.application.iserv.backend.services.AuthorizeService;
import com.application.iserv.ui.payments.models.AuthorizeModel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import static com.application.iserv.ui.utils.Constants.*;

public class AuthorizeForm extends FormLayout {

    // TextFields
    TextField bonusAmount = new TextField();
    TextField bonusReason = new TextField();
    TextField deductionAmount = new TextField();
    TextField deductionReason = new TextField();
    TextField statusReason = new TextField(REASON);


    // Selects
    Select<String> status = new Select<>();

    // Buttons
    Button save = new Button(SAVE);
    Button back = new Button(BACK);

    // Binder
    Binder<AuthorizeModel> binder = new Binder<>(AuthorizeModel.class);
    private AuthorizeModel authorizeModel;

    private final AuthorizeService authorizeService;

    @Autowired
    public AuthorizeForm(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;

        binder.bindInstanceFields(this);

        configureButtons();
        configureLayouts();

        add(
                status,
                statusReason,
                bonusAmount,
                bonusReason,
                deductionAmount,
                deductionReason,
                new HorizontalLayout(save, back)
        );

        status.addValueChangeListener(statusValueChangedEvent -> {
            if (statusValueChangedEvent.getValue() != null) {
                if (statusValueChangedEvent.getValue().equalsIgnoreCase(DENY)) {
                    if (!statusReason.isEmpty()
                            && statusReason.getValue().equalsIgnoreCase("null")) {
                        statusReason.clear();
                    }

                    statusReason.setVisible(true);
                }
                else {
                    statusReason.setVisible(false);
                    statusReason.clear();
                }
            }
        });

    }

    public void hideStatusReason(boolean isHiding) {
        if (isHiding) {
            statusReason.setVisible(true);
        }
        else {
            statusReason.setVisible(false);
        }
    }

    public void setAuthorize(AuthorizeModel authorizeModel) {
        this.authorizeModel = authorizeModel;

        binder.readBean(authorizeModel);

        if (!authorizeModel.getStatusReason().equalsIgnoreCase("null")) {
            statusReason.setValue(authorizeModel.getStatusReason());
        }

        if (authorizeModel.getStatus().equalsIgnoreCase(APPROVED)) {
            status.setValue(APPROVE);
        }
        else if (authorizeModel.getStatus().equalsIgnoreCase(DENIED)) {
            status.setValue(DENY);
        }
        else if (authorizeModel.getStatus().equalsIgnoreCase(HOLD)) {
            status.setValue(HOLD);
        }
        else if (authorizeModel.getStatus().equalsIgnoreCase(PENDING)) {
            status.setValue(PEND);
        }

        if (bonusAmount.getValue().equals("0")) {
            bonusAmount.clear();
            bonusReason.clear();
        }

        if (deductionAmount.getValue().equals("0")) {
            deductionAmount.clear();
            deductionReason.clear();
        }

        if (authorizeModel.getClaimed().equalsIgnoreCase("Yes")) {
            status.setEnabled(false);
            save.setEnabled(false);
            bonusAmount.setEnabled(false);
            bonusReason.setEnabled(false);
            deductionAmount.setEnabled(false);
            deductionReason.setEnabled(false);
        }
        else if (authorizeModel.getClaimed().equalsIgnoreCase("No")) {
            status.setEnabled(true);
            save.setEnabled(true);
            bonusAmount.setEnabled(true);
            bonusReason.setEnabled(true);
            deductionAmount.setEnabled(true);
            deductionReason.setEnabled(true);
        }

        if (bonusAmount.getValue() != null && !bonusAmount.isEmpty()) {
            deductionAmount.setEnabled(false);
            deductionReason.setEnabled(false);
        }
        else if (deductionAmount.getValue() != null && !deductionAmount.isEmpty()) {
            bonusAmount.setEnabled(false);
            bonusReason.setEnabled(false);
        }
    }

    private void configureLayouts() {

        statusReason.setPlaceholder("Reason for denying");

        status.setLabel(STATUS);
        status.setItems(getStatuses());
        status.setPlaceholder(SELECT_STATUS);

        bonusAmount.setLabel(BONUS);
        bonusReason.setLabel(REASON);
        bonusReason.setPlaceholder(REASON_FOR_BONUS);
        bonusAmount.addValueChangeListener(bonusValueChangeEvent -> {
            if (bonusAmount.getValue() == null) {
                deductionAmount.setEnabled(true);
                deductionReason.setEnabled(true);
            }
            else {
                deductionAmount.setEnabled(false);
                deductionReason.setEnabled(false);
            }
        });

        bonusReason.addValueChangeListener(bonusValueChangeEvent -> {
            if (bonusReason.getValue().isEmpty()) {
                deductionAmount.setEnabled(true);
                deductionReason.setEnabled(true);
            }
            else {
                deductionAmount.setEnabled(false);
                deductionReason.setEnabled(false);
            }
        });

        deductionAmount.setLabel(DEDUCTION);
        deductionReason.setLabel(REASON);
        deductionReason.setPlaceholder(REASON_FOR_DEDUCTION);
        deductionAmount.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {

            if (deductionAmount.getValue() == null) {
                bonusAmount.setEnabled(true);
                bonusReason.setEnabled(true);
            }
            else {
                bonusAmount.setEnabled(false);
                bonusReason.setEnabled(false);
            }

        });

        deductionReason.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {

            if (deductionReason.getValue().isEmpty()) {
                bonusAmount.setEnabled(true);
                bonusReason.setEnabled(true);
            }
            else {
                bonusAmount.setEnabled(false);
                bonusReason.setEnabled(false);
            }

        });

    }

    private void configureButtons() {

        save.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        save.addClickListener(click -> validateFields());

        back.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

        back.addClickListener(click -> fireEvent(new CloseAuthorizeFormEvent(this)));

    }

    private void validateFields() {
        if (status.getValue() != null) {
            if (status.getValue().equalsIgnoreCase(DENIED)) {
                if (statusReason.isEmpty()) {
                    Notification notification = new Notification("Enter reason for denial");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();

                    statusReason.setInvalid(true);
                    save.setEnabled(true);
                }
                else {
                    updateRemuneration();
                }
            }
            else if (bonusAmount != null && !bonusAmount.isEmpty()
                    && bonusReason != null && bonusReason.isEmpty()) {
                Notification notification = new Notification("Enter reason for bonus");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(5000);
                notification.open();

                bonusReason.setInvalid(true);
                save.setEnabled(true);
            }
            else if (deductionAmount != null && !deductionAmount.isEmpty()
                    && deductionReason != null && deductionReason.isEmpty()) {
                Notification notification = new Notification("Enter reason for deduction");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(5000);
                notification.open();

                deductionReason.setInvalid(true);
                save.setEnabled(true);
            }
            else {
                updateRemuneration();
            }
        }
    }

    private void updateRemuneration() {
        save.setDisableOnClick(true);

        String statusReason_str;

        if (statusReason.isEmpty()) {
            statusReason_str = "null";
        }
        else {
            statusReason_str = statusReason.getValue();
        }

        String bonusAmount_str;
        if (bonusAmount.isEmpty()) {
            bonusAmount_str = "0";
        }
        else {
            // todo validate bonusAmount
            bonusAmount_str = bonusAmount.getValue();
        }

        String bonusAmountReason_str;
        if (bonusReason.getValue().isEmpty()) {
            bonusAmountReason_str = "null";
        }
        else {
            bonusAmountReason_str = bonusReason.getValue();
        }

        String deductionAmount_str;
        if (deductionAmount.isEmpty()) {
            deductionAmount_str = "0";
        }
        else {
            // todo validate deductionAmount
            deductionAmount_str = deductionAmount.getValue();
        }

        String deductionAmountReason_str;
        if (deductionReason.isEmpty()) {
            deductionAmountReason_str = "null";
        }
        else {
            deductionAmountReason_str = deductionReason.getValue();
        }

        String statusValue = status.getValue();

        if (status.getValue().equalsIgnoreCase(APPROVE)) {
            statusValue = APPROVED;
        }
        else if (status.getValue().equalsIgnoreCase(DENY)) {
            statusValue = DENIED;
        }
        else if (status.getValue().equalsIgnoreCase(HOLD)) {
            statusValue = HOLD;
        }
        else if (status.getValue().equalsIgnoreCase(PEND)) {
            statusValue = PENDING;
        }

        System.err.println("value: "+statusReason.getValue());

        if (statusValue.equalsIgnoreCase(DENIED)
                && statusReason.getValue().isEmpty()) {
            statusReason.setInvalid(true);
            save.setEnabled(true);
        }
        else {
            authorizeService.updateRemuneration(new AuthorizeModel(
                    bonusAmount_str,
                    deductionAmount_str,
                    authorizeModel.getRemunerationHistoryId(),
                    authorizeModel.getParticipantId(),
                    statusValue,
                    statusReason_str,
                    authorizeModel.getClaimed(),
                    bonusAmountReason_str,
                    deductionAmountReason_str
            ));

            save.setEnabled(true);

            fireEvent(new RemunerationUpdatedEvent(this));

        }

    }

    // Events
    public static abstract class AuthorizeFormEvent extends ComponentEvent<AuthorizeForm> {

        private AuthorizeModel authorizeModel;

        protected AuthorizeFormEvent(AuthorizeForm source, AuthorizeModel authorizeModel) {
            super(source, false);
            this.authorizeModel = authorizeModel;
        }

        public AuthorizeModel getAgents() {
            return authorizeModel;
        }

    }

    public static class RemunerationUpdatedEvent extends AuthorizeFormEvent {
        RemunerationUpdatedEvent(AuthorizeForm source) {
            super(source, null);
        }
    }

    public static class CloseAuthorizeFormEvent extends AuthorizeFormEvent {
        CloseAuthorizeFormEvent(AuthorizeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

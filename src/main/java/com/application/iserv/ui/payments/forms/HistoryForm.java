package com.application.iserv.ui.payments.forms;


import com.application.iserv.backend.services.HistoryService;
import com.application.iserv.ui.payments.models.HistoryModel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;

import static com.application.iserv.ui.utils.Constants.*;

public class HistoryForm extends VerticalLayout {

    // TextFields
    TextField ratePerDay = new TextField();
    TextField daysWorked = new TextField();
    TextField baseSalary = new TextField();
    TextField bonusAmount = new TextField();
    TextField bonusReason = new TextField();
    TextField deductionAmount = new TextField();
    TextField deductionReason = new TextField();
    TextField totalSalary = new TextField();
    TextField status = new TextField();
    TextField claimed = new TextField();
    TextField paymentMode = new TextField();
    TextField provider = new TextField();

    // Buttons
    Button OK = new Button("OK");
    Button back = new Button(BACK);
    Button exportStatements = new Button(EXPORT_STATEMENTS);

    // Binder
    Binder<HistoryModel> binder = new Binder<>(HistoryModel.class);

    // Forms
    FormLayout formLayout;

    // Dialogs
    Dialog statementDialog = new Dialog();

    private HistoryModel historyModel;

    // Services
    private final HistoryService historyService;

    // Longs
    Long participantId;

    // Strings
    String names;
    String firstname;

    @Autowired
    public HistoryForm(HistoryService historyService) {
        this.historyService = historyService;

        checkScreenSize();
        configureButtons();

        setSizeFull();

        binder.bindInstanceFields(this);

        ratePerDay.setReadOnly(true);
        ratePerDay.setLabel(RATE_PER_DAY);

        daysWorked.setReadOnly(true);
        daysWorked.setLabel(DAYS_WORKED);

        baseSalary.setReadOnly(true);
        baseSalary.setLabel(BASE_SALARY);

        bonusAmount.setReadOnly(true);
        bonusAmount.setLabel(BONUS);

        bonusReason.setReadOnly(true);
        bonusReason.setLabel(REASON_FOR_BONUS);

        deductionAmount.setReadOnly(true);
        deductionAmount.setLabel(DEDUCTION);

        deductionReason.setReadOnly(true);
        deductionReason.setLabel(REASON_FOR_DEDUCTION);

        totalSalary.setReadOnly(true);
        totalSalary.setLabel(TOTAL_SALARY);

        status.setReadOnly(true);
        status.setLabel(APPROVAL);

        claimed.setReadOnly(true);
        claimed.setLabel(CLAIMED);

        paymentMode.setReadOnly(true);
        paymentMode.setLabel(PAYMENT_MODE);

        provider.setReadOnly(true);
        provider.setLabel("Provider");


        HorizontalLayout exportBackLayout = new HorizontalLayout(exportStatements, back);
        exportBackLayout.setFlexGrow(2, exportStatements);
        exportBackLayout.setFlexGrow(1, back);
        exportBackLayout.getStyle()
                .set("margin-bottom", "var(--lumo-space-xl");

        formLayout = new FormLayout(
                ratePerDay,
                daysWorked,
                baseSalary,
                bonusAmount,
                bonusReason,
                deductionAmount,
                deductionReason,
                totalSalary,
                status,
                claimed,
                paymentMode,
                provider
        );

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("300px", 2)
        );

        formLayout.setColspan(bonusReason, 2);
        formLayout.setColspan(deductionReason, 2);

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

        String[] getFirstname = historyModel.getParticipant().split(" ");

        firstname = getFirstname[0];
        names = historyModel.getParticipant();
        participantId = historyModel.getParticipantId();

        if (historyModel.getBonusAmount().equalsIgnoreCase("0")) {
            bonusAmount.setVisible(false);
            bonusReason.setVisible(false);

            formLayout.setColspan(provider, 1);

        }
        else {
            bonusAmount.setVisible(true);
            bonusReason.setVisible(true);

            deductionAmount.setVisible(false);
            deductionReason.setVisible(false);

            formLayout.setColspan(provider, 2);

        }

        if (historyModel.getDeductionAmount().equalsIgnoreCase("0")) {
            deductionAmount.setVisible(false);
            deductionReason.setVisible(false);

            formLayout.setColspan(provider, 1);

        }
        else {
            deductionAmount.setVisible(true);
            deductionReason.setVisible(true);

            bonusAmount.setVisible(false);
            bonusReason.setVisible(false);

            formLayout.setColspan(provider, 2);

        }

    }

    private void configureButtons() {

        exportStatements.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        exportStatements.setDisableOnClick(true);

        exportStatements.addClickListener(click -> {

            try {
                historyService.exportReport(names, participantId);
            } catch (FileNotFoundException | JRException e) {
                throw new RuntimeException(e);
            }

            configureDialogs();

            statementDialog.open();

        });

        back.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

        back.addClickListener(click -> fireEvent(new CloseHistoryFormEvent(this)));

    }

    private void configureDialogs() {

        statementDialog = new Dialog();

        H2 h2 = new H2(firstname+"'s statement has been successfully exported");

        FormLayout formLayout1 = new FormLayout(
                h2
        );

        formLayout1.setColspan(h2, 2);

        statementDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> {
                    statementDialog.close();
                })
        );

        statementDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            statementDialog.close();
            exportStatements.setEnabled(true);
            fireEvent(new CloseHistoryFormEvent(this));
        });

        OK.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        OK.addClickListener(click -> {
            statementDialog.close();
            exportStatements.setEnabled(true);
            fireEvent(new CloseHistoryFormEvent(this));
        });

        statementDialog.setHeaderTitle("Exported");
        statementDialog.add(formLayout1);
        statementDialog.getFooter().add(OK);

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                statementDialog.setWidth("70%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                statementDialog.setWidth("70%");
            }

        });

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

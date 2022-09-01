package com.application.iserv.ui.payments.views;


import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.forms.HistoryForm;
import com.application.iserv.ui.payments.models.AgentPaymentsModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@Route(value = HISTORY_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class HistoryView extends VerticalLayout {


    // TextFields
    TextField searchAgent = new TextField();

    // DatePickers
    DatePicker datePicker = new DatePicker();
    DatePicker datePicker1 = new DatePicker();

    // Forms
    HistoryForm historyForm;

    // Booleans
    boolean isDateSelected = false;

    // Grid
    Grid<AgentPaymentsModel> paymentsGrid = new Grid<>(AgentPaymentsModel.class);

    public HistoryView() {

        addClassName(HISTORY_PAYMENTS_VIEW);
        setSizeFull();


        configureHistoryForm();
        configureHistoryGrid();

        add(
                getToolbar(),
                getContent()
        );

        updateAgentsPaymentsList();
        closeComponents();

    }

    private void closeComponents() {

        historyForm.setVisible(false);

        removeClassName(VIEWING_HISTORY);
    }

    private Component getToolbar() {

        searchAgent.setClearButtonVisible(true);
        searchAgent.setPlaceholder("Search agent...");
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName("history-search-agent");

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat("dd-MM-yyyy");

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder("Date");
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            configureHistoryGrid();
            updateAgentsPaymentsList();
        });

        datePicker1.setI18n(dateFormat);
        datePicker1.setPlaceholder("Date");
        datePicker1.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            configureHistoryGrid();
            updateAgentsPaymentsList();
        });

        datePicker1.setVisible(false);

        HorizontalLayout searchDateLayout = new HorizontalLayout(searchAgent, datePicker);

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() <= 500) {
                datePicker.setVisible(false);
                datePicker1.setVisible(true);

                if (datePicker.getValue() != null) {
                    datePicker1.setValue(datePicker.getValue());
                }

            }
            else {
                datePicker.setVisible(true);
                datePicker1.setVisible(false);

                if (datePicker1.getValue() != null) {
                    datePicker.setValue(datePicker1.getValue());
                }

            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() <= 500) {
                datePicker.setVisible(false);
                datePicker1.setVisible(true);
            }
            else {
                datePicker.setVisible(true);
                datePicker1.setVisible(false);
            }
        });

        VerticalLayout searchDateTimeLayout = new VerticalLayout(searchDateLayout, datePicker1);
        searchDateTimeLayout.addClassName(SEARCH_DATE_TIME_LAYOUT);

        return searchDateTimeLayout;

    }

    private void configureHistoryGrid() {

        paymentsGrid.addClassName(PAYMENTS_HISTORY_GRID);
        paymentsGrid.setSizeFull();

        if (isDateSelected) {
            paymentsGrid.setColumns(AGENT, AMOUNT);

            paymentsGrid.addComponentColumn(
                    claimed -> createBadge(claimed.getClaimed())).setHeader(CLAIMED);

            paymentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        } else {
            paymentsGrid.setColumns(AGENT);

            paymentsGrid.addComponentColumn(
                    claimed -> createBadge(claimed.getAmount())).setHeader(AMOUNT);

            paymentsGrid.addComponentColumn(
                    claimed -> createBadge(claimed.getClaimed())).setHeader(CLAIMED);

            paymentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }

        paymentsGrid.asSingleSelect().addValueChangeListener(e -> {
            if (isDateSelected) {
                addClassName(VIEWING_HISTORY);
                viewHistory(e.getValue());
            }
        });

    }

    private Component createBadge(String claimed) {

        Span label;

        if (claimed.equalsIgnoreCase(YES)) {
            label = new Span(YES);
            label.getElement().getThemeList().add(BADGE_SUCCESSFUL);
        }
        else if (claimed.equalsIgnoreCase(NO)) {
            label = new Span(NO);
            label.getElement().getThemeList().add(BADGE_ERROR);
        }
        else {
            label = new Span("-");
            label.getElement().getThemeList().add(BADGE_CONTRAST);
        }

        return label;
    }

    private void updateAgentsPaymentsList() {

        paymentsGrid.asSingleSelect().clear();

        if (isDateSelected) {
            paymentsGrid.setItems(getTestPaymentAgents());
        }
        else {
            paymentsGrid.setItems(getEmptyTestPaymentAgents());
        }
    }

    private void configureHistoryForm() {
        historyForm = new HistoryForm();
        historyForm.setWidth(EM_30);
    }

    private Component getContent() {

        HorizontalLayout historyHorizontalLayout = new HorizontalLayout(paymentsGrid, historyForm);
        historyHorizontalLayout.setFlexGrow(2, paymentsGrid);
        historyHorizontalLayout.setFlexGrow(1, historyForm);
        historyHorizontalLayout.setSizeFull();

        return historyHorizontalLayout;
    }

    private void viewHistory(AgentPaymentsModel agentPaymentsModel) {

        if (agentPaymentsModel == null) {
            closeComponents();
        }
        else {
            addClassName(VIEWING_HISTORY);
            historyForm.setHistory(agentPaymentsModel);
            historyForm.setVisible(true);
        }

    }

}

package com.application.iserv.ui.payments.views;


import com.application.iserv.backend.services.HistoryService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.forms.HistoryForm;
import com.application.iserv.ui.payments.models.HistoryModel;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    Grid<HistoryModel> historyGrid = new Grid<>(HistoryModel.class);

    // Services
    private final HistoryService historyService;

    // Strings
    String date;

    @Autowired
    public HistoryView(HistoryService historyService) {
        this.historyService = historyService;

        addClassName(HISTORY_PAYMENTS_VIEW);
        setSizeFull();

        configureHistoryForm();
        configureHistoryGrid();

        add(
                getToolbar(),
                getContent()
        );

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

        date = LocalDate.now().format(dateFormatter);

        updateAgentsPaymentsList();
        closeComponents();

    }

    private void closeComponents() {

        historyForm.setVisible(false);

        historyGrid.asSingleSelect().clear();

        removeClassName(VIEWING_HISTORY);
    }

    private Component getToolbar() {

        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.addValueChangeListener(searchAgentValueChanged -> historyGrid.setItems(
                historyService.searchHistory(searchAgent.getValue(), date)
        ));


        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder("Date");
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            configureHistoryGrid();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            String date_str = datePicker.getValue().format(dateTimeFormatter);
            String[] getDatePickerDate = date_str.split("-");

            LocalDate datePickerLocalDate = LocalDate.of(
                    Integer.parseInt(getDatePickerDate[2]),
                    Integer.parseInt(getDatePickerDate[1]),
                    Integer.parseInt(getDatePickerDate[0])
            );

            date = datePickerLocalDate.format(dateFormatter);

            updateAgentsPaymentsList();

        });

        datePicker1.setI18n(dateFormat);
        datePicker1.setPlaceholder("Date");
        datePicker1.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            configureHistoryGrid();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            String date_str = datePicker1.getValue().format(dateTimeFormatter);
            String[] getDatePickerDate = date_str.split("-");

            LocalDate datePickerLocalDate = LocalDate.of(
                    Integer.parseInt(getDatePickerDate[2]),
                    Integer.parseInt(getDatePickerDate[1]),
                    Integer.parseInt(getDatePickerDate[0])
            );

            date = datePickerLocalDate.format(dateFormatter);

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

        historyGrid.addClassName(PAYMENTS_HISTORY_GRID);
        historyGrid.setSizeFull();

        if (isDateSelected) {
            historyGrid.setColumns(PARTICIPANT, AMOUNT);

            historyGrid.addComponentColumn(
                    claimed -> createBadge(claimed.getClaimed())).setHeader(CLAIMED);

            historyGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        } else {
            historyGrid.setColumns(PARTICIPANT);

            historyGrid.addComponentColumn(
                    claimed -> createBadge("-")).setHeader(CAPS_AMOUNT);

            historyGrid.addComponentColumn(
                    claimed -> createBadge("-")).setHeader(CLAIMED);

            historyGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }

        historyGrid.asSingleSelect().addValueChangeListener(e -> {
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

        historyGrid.asSingleSelect().clear();

        if (isDateSelected) {
            historyGrid.setItems(historyService.getAllHistory(date));
        }
        else {
            historyGrid.setItems(historyService.getAllHistory(date));
        }
    }

    private void configureHistoryForm() {
        historyForm = new HistoryForm(historyService);
        historyForm.setWidth("70%");

        historyForm.addListener(HistoryForm.CloseHistoryFormEvent.class, e -> closeComponents());
    }

    private Component getContent() {

        HorizontalLayout historyHorizontalLayout = new HorizontalLayout(historyGrid, historyForm);
        historyHorizontalLayout.setFlexGrow(2, historyGrid);
        historyHorizontalLayout.setFlexGrow(1, historyForm);
        historyHorizontalLayout.setSizeFull();

        return historyHorizontalLayout;
    }

    private void viewHistory(HistoryModel historyModel) {

        if (historyModel == null) {
            closeComponents();
        }
        else {
            addClassName(VIEWING_HISTORY);
            historyForm.setHistory(historyModel);
            historyForm.setVisible(true);
        }

    }

}

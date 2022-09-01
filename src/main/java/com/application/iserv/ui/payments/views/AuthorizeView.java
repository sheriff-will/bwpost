package com.application.iserv.ui.payments.views;


import com.application.iserv.security.services.SecurityService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.tests.components.navigation.bar.AppBar;
import com.application.iserv.ui.payments.forms.AuthorizeForm;
import com.application.iserv.ui.payments.models.AgentPaymentsModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@Route(value = AUTHORIZE_LOWER_CASE, layout = MainLayout.class)
@PermitAll
public class AuthorizeView extends VerticalLayout {

    // Tabs
    Tabs tabs;
    Tab authorize = new Tab(AUTHORIZE);
    Tab reconcile = new Tab(RECONCILE);
    Tab history = new Tab(HISTORY);

    // TextFields
    TextField searchAgent = new TextField();

    // DatePickers
    DatePicker datePicker = new DatePicker();
    DatePicker datePicker1 = new DatePicker();

    // Forms
    AuthorizeForm authorizeForm;

    // Booleans
    boolean isDateSelected = false;

    // Grids
    Grid<AgentPaymentsModel> paymentsGrid = new Grid<>(AgentPaymentsModel.class);

    private final SecurityService securityService;

    public AuthorizeView(SecurityService securityService) {
        this.securityService = securityService;

        addClassName(AUTHORIZE_PAYMENTS_VIEW);
        setSizeFull();

        tabs = new Tabs(authorize, reconcile, history);

        AppBar appBar = new AppBar("", this.securityService);

        configureAuthorizeForm();
        configureAuthorizeGrid();

        add(
                getToolbar(),
                getContent()
        );

        updateAgentsPaymentsList();
        closeComponents();

    }

    private void closeComponents() {
        authorizeForm.setVisible(false);

        removeClassName(VIEWING_AUTHORIZE);
    }

    private Component getToolbar() {

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        MenuItem options = menuBar.addItem(VaadinIcon.OPTIONS.create());
        SubMenu subMenu = options.getSubMenu();
        subMenu.addItem("Download");
        subMenu.add(new Hr());
        subMenu.addItem("Approve All");
        menuBar.setEnabled(false);

        searchAgent.setClearButtonVisible(true);
        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            menuBar.setEnabled(true);
            configureAuthorizeGrid();
            updateAgentsPaymentsList();
        });

        datePicker1.setI18n(dateFormat);
        datePicker1.setPlaceholder(DATE);
        datePicker1.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            menuBar.setEnabled(true);
            configureAuthorizeGrid();
            updateAgentsPaymentsList();
        });

        datePicker1.setVisible(false);

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

        HorizontalLayout searchDateMenuLayout = new HorizontalLayout(searchAgent, datePicker, menuBar);
        searchDateMenuLayout.setAlignItems(Alignment.BASELINE);
        searchDateMenuLayout.addClassName(SEARCH_DATE_MENU_LAYOUT);

        VerticalLayout searchDateTimeLayout = new VerticalLayout(searchDateMenuLayout, datePicker1);
        searchDateTimeLayout.addClassName(SEARCH_DATE_TIME_LAYOUT);

        return new HorizontalLayout(searchDateTimeLayout);

    }

    private void configureAuthorizeGrid() {

        if (isDateSelected) {
            paymentsGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            paymentsGrid.setSizeFull();

            paymentsGrid.setColumns(AGENT, AMOUNT);

            paymentsGrid.addComponentColumn(
                    approval -> createBadge(approval.getApproval())).setHeader(STATUS);

            paymentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }
        else {
            paymentsGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            paymentsGrid.setSizeFull();

            paymentsGrid.setColumns(AGENT);

            paymentsGrid.addComponentColumn(
                    amount -> createBadge(amount.getAmount())).setHeader(AMOUNT);

            paymentsGrid.addComponentColumn(
                    status -> createBadge(status.getApproval())).setHeader(STATUS);

            paymentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }

        paymentsGrid.asSingleSelect().addValueChangeListener(e -> {
            if (isDateSelected) {
                addClassName(VIEWING_AUTHORIZE);
                showAuthorizeForm(e.getValue());
            }
        });

    }

    private Component createBadge(String approval) {

        Span label;

        if (approval.equalsIgnoreCase(APPROVED)) {
            label = new Span(APPROVED);
            label.getElement().getThemeList().add(BADGE_SUCCESSFUL);
        }
        else if (approval.equalsIgnoreCase(DENIED)) {
            label = new Span(DENIED);
            label.getElement().getThemeList().add(BADGE_ERROR);
        }
        else if (approval.equalsIgnoreCase(PENDING)) {
            label = new Span(PENDING);
            label.getElement().getThemeList().add(BADGE);
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

    private void configureAuthorizeForm() {
        authorizeForm = new AuthorizeForm();
        authorizeForm.setWidth(EM_30);
    }

    private Component getContent() {

        HorizontalLayout historyHorizontalLayout = new HorizontalLayout(paymentsGrid, authorizeForm);
        historyHorizontalLayout.setFlexGrow(2, paymentsGrid);
        historyHorizontalLayout.setFlexGrow(1, authorizeForm);
        historyHorizontalLayout.setSizeFull();

        return historyHorizontalLayout;
    }

    private void showAuthorizeForm(AgentPaymentsModel agentPaymentsModel) {

        if (agentPaymentsModel == null) {
            closeComponents();
        }
        else {
            addClassName(VIEWING_AUTHORIZE);
            authorizeForm.setAuthorize(agentPaymentsModel);
            authorizeForm.setVisible(true);
        }

    }

}
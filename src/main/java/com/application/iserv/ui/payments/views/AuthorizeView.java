package com.application.iserv.ui.payments.views;


import com.application.iserv.backend.services.AuthorizeService;
import com.application.iserv.security.services.SecurityService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.forms.AuthorizeForm;
import com.application.iserv.ui.payments.models.AuthorizeModel;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

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
    Grid<AuthorizeModel> authorizeGrid = new Grid<>(AuthorizeModel.class);

    // Arrays
    List<AuthorizeModel> authorizeModelList = new ArrayList<>();

    private final SecurityService securityService;
    private final AuthorizeService authorizeService;

    @Autowired
    public AuthorizeView(SecurityService securityService, AuthorizeService authorizeService) {
        this.securityService = securityService;
        this.authorizeService = authorizeService;

        addClassName(AUTHORIZE_PAYMENTS_VIEW);
        setSizeFull();

        tabs = new Tabs(authorize, reconcile, history);

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

        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.addValueChangeListener(searchAgentValueChanged -> authorizeGrid.setItems(
                authorizeService.searchRemunerationAgents(searchAgent.getValue(), 0L)
        ));


        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

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
            authorizeGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            authorizeGrid.setSizeFull();

            authorizeGrid.setColumns(AGENT, AMOUNT);

            authorizeGrid.addComponentColumn(
                    status -> createBadge(status.getStatus())).setHeader(STATUS);

            authorizeGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }
        else {
            authorizeGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            authorizeGrid.setSizeFull();

            authorizeGrid.setColumns(AGENT);

            authorizeGrid.addComponentColumn(
                    status -> createBadge("-")).setHeader(AMOUNT);

            authorizeGrid.addComponentColumn(
                    status -> createBadge("-")).setHeader(STATUS);

            authorizeGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }

        authorizeGrid.asSingleSelect().addValueChangeListener(e -> {
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
        else if (approval.equalsIgnoreCase(DECLINED)) {
            label = new Span(DECLINED);
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

        authorizeGrid.asSingleSelect().clear();

        authorizeModelList = authorizeService.getAllRemunerationHistory();

        if (isDateSelected) {
            authorizeGrid.setItems(authorizeModelList);
        }
        else {
            authorizeGrid.setItems(authorizeModelList);
        }

    }

    private void configureAuthorizeForm() {
        authorizeForm = new AuthorizeForm(authorizeService);
        authorizeForm.setWidth(EM_30);

        authorizeForm.addListener(AuthorizeForm.CloseAuthorizeFormEvent.class, e -> {
            closeComponents();
            authorizeGrid.asSingleSelect().clear();
        });

        authorizeForm.addListener(AuthorizeForm.RemunerationUpdatedEvent.class, e -> {
            Notification notification = new Notification("Updated");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            authorizeGrid.setItems(authorizeService.getAllRemunerationHistory());

        });

    }

    private Component getContent() {

        HorizontalLayout historyHorizontalLayout = new HorizontalLayout(authorizeGrid, authorizeForm);
        historyHorizontalLayout.setFlexGrow(2, authorizeGrid);
        historyHorizontalLayout.setFlexGrow(1, authorizeForm);
        historyHorizontalLayout.setSizeFull();

        return historyHorizontalLayout;
    }

    private void showAuthorizeForm(AuthorizeModel authorizeModel) {

        if (authorizeModel == null) {
            closeComponents();
        }
        else {
            addClassName(VIEWING_AUTHORIZE);

            if (authorizeModel.getStatus().equalsIgnoreCase(DECLINED)) {
                authorizeForm.hideStatusReason(true);
            }
            else {
                authorizeForm.hideStatusReason(false);
            }

            authorizeForm.setAuthorize(authorizeModel);
            authorizeForm.setVisible(true);
        }

    }

}
package com.application.iserv.ui.payments.views;

import com.application.iserv.backend.services.AuthorizeService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.forms.AuthorizeForm;
import com.application.iserv.ui.payments.models.AuthorizeModel;
import com.opencsv.CSVWriter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@RolesAllowed({"ROOT", "ADMIN"})
@Route(value = AUTHORIZE_LOWER_CASE, layout = MainLayout.class)
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

    // SplitLayouts
    SplitLayout menuSplitLayout;
    SplitLayout searchDateSplitLayout;

    // Forms
    AuthorizeForm authorizeForm;

    // Booleans
    boolean isDateSelected = false;

    // Grids
    Grid<AuthorizeModel> authorizeGrid = new Grid<>(AuthorizeModel.class);

    // Arrays
    List<AuthorizeModel> authorizeModelList = new ArrayList<>();

    // ComboBox
    ComboBox<String> placementPlaceFilter = new ComboBox<>();

    private final AuthorizeService authorizeService;

    // Dialogs
    Dialog approveAllDialog = new Dialog();

    // Strings
    String date;

    @Autowired
    public AuthorizeView(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;

        addClassName(AUTHORIZE_PAYMENTS_VIEW);
        setSizeFull();

        tabs = new Tabs(authorize, reconcile, history);

        configureDialogs();
        checkScreenSize();
        configureAuthorizeForm();
        configureAuthorizeGrid();

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
        authorizeForm.setVisible(false);

        removeClassName(VIEWING_AUTHORIZE);
    }

    private Component getToolbar() {

        placementPlaceFilter.setPlaceholder("Filter by place");
        placementPlaceFilter.setClearButtonVisible(true);

        placementPlaceFilter.addValueChangeListener(placeValueChangeEvent -> {

            searchAgent.clear();

            if (placeValueChangeEvent.getValue() == null) {
                updateAgentsPaymentsList();
            }
            else {
                authorizeGrid.setItems(
                        authorizeService.filterRemunerationHistoryByPlace(
                                date,
                                placeValueChangeEvent.getValue()));
            }

        });

        // Placement places
        placementPlaceFilter.setItems(getPlaces());
        placementPlaceFilter.setItemLabelGenerator(String::toString);

        DateTimeFormatter paymentDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm  dd MMMM yyyy");

        StreamResource resource = new StreamResource(
                "Payment "+ LocalDateTime.now().format(paymentDateTimeFormatter)+".csv",
                () -> new ByteArrayInputStream(getParticipantsFile()));

        Anchor link = new Anchor(resource, "Download");

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        MenuItem options = createIconItem(menuBar, VaadinIcon.OPTIONS, "Options", null);
        SubMenu subMenu = options.getSubMenu();
        subMenu.addItem(link);
        subMenu.add(new Hr());
        subMenu.addItem("Approve Menu").addClickListener(click -> {
            approveAllDialog.open();
        });

        menuBar.setEnabled(false);

        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchAgent.addValueChangeListener(searchAgentValueChanged -> authorizeGrid.setItems(
                authorizeService.searchAuthorize(searchAgent.getValue(), date)
        ));

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            isDateSelected = true;

            searchAgent.clear();

            menuBar.setEnabled(true);
            configureAuthorizeGrid();

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

            if (placementPlaceFilter.getValue() != null) {
                authorizeGrid.asSingleSelect().clear();

                authorizeModelList = authorizeService
                        .filterRemunerationHistoryByPlace(
                                date,
                                placementPlaceFilter.getValue());
                authorizeGrid.setItems(authorizeModelList);

            }
            else {
                updateAgentsPaymentsList();
            }

        });

        MenuBar filterMenuBar = new MenuBar();
        filterMenuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY, MenuBarVariant.LUMO_CONTRAST);

        MenuItem filter = createIconItem(filterMenuBar, VaadinIcon.FILTER, "Filter", null);
        SubMenu filterSubMenu = filter.getSubMenu();
        MenuItem villageMenuItem = filterSubMenu.addItem("Place");
        SubMenu villageSubMenu = villageMenuItem.getSubMenu();
        villageSubMenu.addItem(placementPlaceFilter);

        HorizontalLayout searchDateMenuLayout = new HorizontalLayout(
                filterMenuBar,
                searchAgent);
        searchDateMenuLayout.setAlignItems(Alignment.BASELINE);
        searchDateMenuLayout.addClassName(SEARCH_DATE_MENU_LAYOUT);
        searchDateMenuLayout.setWidthFull();
        searchDateMenuLayout.getStyle().set("overflow", "hidden");

        Div div = new Div();
        searchDateSplitLayout = new SplitLayout(searchDateMenuLayout, div);
        searchDateSplitLayout.addClassName(MENU_SLIT_LAYOUT);

        HorizontalLayout horizontalLayout = new HorizontalLayout(datePicker, menuBar);
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.getStyle().set("overflow", "hidden");

        Div div1 = new Div();
        menuSplitLayout = new SplitLayout(horizontalLayout, div1);
        menuSplitLayout.addClassName(MENU_SLIT_LAYOUT);

        VerticalLayout searchDateTimeLayout = new VerticalLayout(
                menuSplitLayout,
                searchDateSplitLayout
                );
        searchDateTimeLayout.addClassName(SEARCH_DATE_TIME_LAYOUT);
        searchDateTimeLayout.setMargin(false);
        searchDateTimeLayout.setPadding(false);

        return new HorizontalLayout(searchDateTimeLayout);

    }

    private byte[] getParticipantsFile() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);

        try {

            CSVWriter csvWriter = new CSVWriter(streamWriter);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Name", "Identity Number", "Amount", "Total Net", "Status", "Claimed"});

            for (int i = 0; i < authorizeModelList.size(); i++) {
                data.add(new String[] {
                        authorizeModelList.get(i).getParticipant(),
                        authorizeModelList.get(i).getIdentityNumber(),
                        String.valueOf(authorizeModelList.get(i).getAmount()),
                        String.valueOf(authorizeModelList.get(i).getTotalNet()),
                        authorizeModelList.get(i).getStatus(),
                        "no"
                });
            }

            streamWriter.flush();
            csvWriter.writeAll(data);

            csvWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream.toByteArray();
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,
                                    String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName,
                                    String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);

        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }

        MenuItem item = menu.addItem(icon, e -> {
        });

        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }

        if (label != null) {
            item.add(new Text(label));
        }

        return item;
    }

    private void configureDialogs() {

        approveAllDialog = new Dialog();

        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setLabel("Select status to approve");
        checkboxGroup.setItems(HOLD, DENIED, PENDING);
        checkboxGroup.select(HOLD, PENDING);
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        approveAllDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            approveAllDialog.close();
        });

        approveAllDialog.add(checkboxGroup);

        Button approveBtn = new Button(APPROVE);
        approveBtn.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);

        approveBtn.setDisableOnClick(true);

        approveBtn.addClickListener(click -> {

            List<String> statusToApprove = new ArrayList<>();

            if (checkboxGroup.isSelected(HOLD)) {
                statusToApprove.add(HOLD);
            }

            if (checkboxGroup.isSelected(DENIED)) {
                statusToApprove.add(DENIED);
            }

            if (checkboxGroup.isSelected(PENDING)) {
                statusToApprove.add(PENDING);
            }

            authorizeService.approveByStatus(date, statusToApprove, authorizeModelList);

            approveAllDialog.close();

            updateAgentsPaymentsList();

            approveBtn.setEnabled(true);

        });

        approveAllDialog.getFooter().add(approveBtn);

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                approveAllDialog.setWidth("50%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                approveAllDialog.setWidth("50%");
            }

        });

    }

    private void configureAuthorizeGrid() {

        if (isDateSelected) {
            authorizeGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            authorizeGrid.setSizeFull();

            authorizeGrid.setColumns(PARTICIPANT);

            authorizeGrid.addComponentColumn(
                    amount -> createLabel(amount.getAmount())).setHeader(CAPS_AMOUNT);

            authorizeGrid.addComponentColumn(
                    totalNet -> createLabel(totalNet.getTotalNet())).setHeader(TOTAL_NET);

            authorizeGrid.addComponentColumn(
                    status -> createBadge(status.getStatus())).setHeader(STATUS);

            authorizeGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        }
        else {
            authorizeGrid.addClassName(PAYMENTS_AUTHORIZE_GRID);
            authorizeGrid.setSizeFull();

            authorizeGrid.setColumns(PARTICIPANT);

            authorizeGrid.addComponentColumn(
                    amount -> createBadge("-")).setHeader(CAPS_AMOUNT);

            authorizeGrid.addComponentColumn(
                    totalNet -> createBadge("-")).setHeader(TOTAL_NET);

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

    private Component createLabel(double value) {

        Label label = new Label();
        DecimalFormat decimalFormat = new DecimalFormat("P#.00");

        label.setText(decimalFormat.format(value));

        return label;
    }

    private Component createBadge(String approval) {

        Span label;

        if (approval != null) {
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
        }
        else {
            label = new Span("-");
            label.getElement().getThemeList().add(BADGE_CONTRAST);
        }

        return label;
    }

    private void updateAgentsPaymentsList() {

        authorizeGrid.asSingleSelect().clear();

        authorizeModelList = authorizeService.getAllRemunerationHistory(date);

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

            updateAgentsPaymentsList();

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

            if (authorizeModel.getStatus() != null) {

                addClassName(VIEWING_AUTHORIZE);

                if (authorizeModel.getStatus().equalsIgnoreCase(DENIED)) {
                    authorizeForm.hideStatusReason(true);
                }
                else {
                    authorizeForm.hideStatusReason(false);
                }

                authorizeForm.setAuthorize(authorizeModel);
                authorizeForm.setVisible(true);
            }
            else {
                authorizeForm.setVisible(false);
            }

        }

    }

}
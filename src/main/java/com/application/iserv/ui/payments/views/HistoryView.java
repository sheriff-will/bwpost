package com.application.iserv.ui.payments.views;

import com.application.iserv.backend.services.HistoryService;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.payments.forms.HistoryForm;
import com.application.iserv.ui.payments.models.HistoryModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.application.iserv.ui.utils.Constants.*;

@PageTitle("iServ | Payments")
@RolesAllowed({"ROOT", "ADMIN"})
@Route(value = HISTORY_LOWER_CASE, layout = MainLayout.class)
    public class HistoryView extends VerticalLayout {

    // TODO Save payment method to history

    // TextFields
    TextField searchAgent = new TextField();

    // DatePickers
    DatePicker datePicker = new DatePicker();

    // Forms
    HistoryForm historyForm;

    // Booleans
    boolean isDateSelected = false;

    // Grid
    Grid<HistoryModel> historyGrid = new Grid<>(HistoryModel.class);

    // ComboBox
    ComboBox<String> placementPlaceFilter = new ComboBox<>();

    // Services
    private final HistoryService historyService;

    // SplitLayouts
    SplitLayout menuSplitLayout;
    SplitLayout searchDateSplitLayout;

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

        placementPlaceFilter.setPlaceholder("Filter by place");
        placementPlaceFilter.setClearButtonVisible(true);

        placementPlaceFilter.addValueChangeListener(placeValueChangeEvent -> {

            searchAgent.clear();

            if (placeValueChangeEvent.getValue() == null) {
                updateAgentsPaymentsList();
            }
            else {
                historyGrid.setItems(
                        historyService.filterHistoryByPlace(
                                date,
                                placeValueChangeEvent.getValue()));
            }

        });

        // Placement places
        placementPlaceFilter.setItems(getPlaces());
        placementPlaceFilter.setItemLabelGenerator(String::toString);

        searchAgent.setPlaceholder(SEARCH_EMPLOYEE_HINT);
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

        HorizontalLayout horizontalLayout = new HorizontalLayout(datePicker);
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

    private void configureHistoryGrid() {

        historyGrid.addClassName(PAYMENTS_HISTORY_GRID);
        historyGrid.setSizeFull();

        if (isDateSelected) {
            historyGrid.setColumns(EMPLOYEE, AMOUNT);

            historyGrid.addComponentColumn(
                    claimed -> createBadge(claimed.getClaimed())).setHeader(CLAIMED);

            historyGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        } else {
            historyGrid.setColumns(EMPLOYEE);

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

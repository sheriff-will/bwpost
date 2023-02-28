package com.application.iserv.ui.participants.views;

import com.application.iserv.backend.services.ParametersService;
import com.application.iserv.backend.services.ParticipantsServices;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.tests.components.navigation.drawer.NaviDrawer;
import com.application.iserv.ui.parameters.models.ParametersModel;
import com.application.iserv.ui.participants.forms.ParticipantsForm;
import com.application.iserv.ui.participants.models.EmployeesModel;
import com.application.iserv.ui.utils.ApplicationUserDataModel;
import com.application.iserv.ui.utils.SessionManager;
import com.opencsv.CSVWriter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PermitAll
@PageTitle("BotswanaPost | Employees")
@Route(value = EMPLOYEES_LOWER_CASE, layout = MainLayout.class)
public class ParticipantsView extends VerticalLayout {

    // Grid
    Grid<EmployeesModel> agentsGrid = new Grid<>(EmployeesModel.class);

    // booleans
    boolean isCsvUploaded = false;
    boolean isAttendanceOpen = false;
    boolean isAttendanceButtonClicked = false;

    // TextFields
    TextField searchAgent = new TextField();

    // ComboBox
    ComboBox<String> placementPlaceFilter = new ComboBox<>();

    // Forms
    ParticipantsForm participantsForm;

    // Drawers
    NaviDrawer naviDrawer;

    ComboBox<String> status = new ComboBox<>();


    // DatePicker
    DatePicker datePicker = new DatePicker();

    // Buttons
    Button uploadCSV = new Button(UPLOAD);
    Button backButton = new Button(BACK);
    Button attendanceButton = new Button(ATTENDANCE);

    // Layouts
    HorizontalLayout agentsHorizontalLayout;
    HorizontalLayout dateBackHorizontalLayout;

    // SplitLayouts
    SplitLayout splitLayout1;
    SplitLayout menuSplitLayout;

    // Services
    private final ParametersService parametersService;
    private final ParticipantsServices participantsServices;

    // Longs
    Long statusValue = 0L;

    // ArrayList
    List<ParametersModel> parametersModelList = new ArrayList<>();
    List<EmployeesModel> employeesModelList = new ArrayList<>();

    // Strings
    String date = "";

    // MenuItem
    MenuItem addAgentButton;

    // Dialogs
    Dialog uploadFileDialog = new Dialog();

    // Upload
    Upload upload = new Upload();

    @Autowired
    public ParticipantsView(ParametersService parametersService,
                            ParticipantsServices participantsServices) {

        this.parametersService = parametersService;
        this.participantsServices = participantsServices;

        setSizeFull();
        addClassName(AGENTS_LIST_VIEW);

        updateLists();
        checkScreenSize();
        configureAgentsForm();
        configureNaviDrawer();

        configureAgentsGrid(AGENT_POSITION);

        add(
                getToolbar(),
                getAgentsContent()
        );

        updateAgents();
        closeComponents();

        //EventBus.getDefault().register(new StringModel());

        agentsGrid.asSingleSelect().addValueChangeListener(e -> editAgent(e.getValue()));

    }

    private void updateLists() {
        // Placement places
        placementPlaceFilter.setItems(getPlaces());
        placementPlaceFilter.setItemLabelGenerator(String::toString);

        // Parameters list
        parametersModelList = parametersService.getParameters();

    }

    private void closeComponents() {

        if (!isAttendanceButtonClicked) {
            datePicker.setVisible(false);
        }

        participantsForm.setVisible(false);

        removeClassNames(
                EDITING_AGENTS,
                ADDING_AGENT,
                SLIT_LAYOUT_1,
                MENU_SLIT_LAYOUT,
                PARTICIPANT_OPEN);
    }

    private Component getAgentsContent() {
        agentsHorizontalLayout = new HorizontalLayout(agentsGrid, participantsForm);
        agentsHorizontalLayout.setFlexGrow(2, agentsGrid);
        agentsHorizontalLayout.setFlexGrow(1, participantsForm);

        agentsHorizontalLayout.setSizeFull();
        agentsHorizontalLayout.addClassName(AGENTS_CONTENT_LAYOUT);

        return agentsHorizontalLayout;
    }

    private void configureAgentsForm() {
        participantsForm = new ParticipantsForm(participantsServices);
        participantsForm.setWidth("50%");

        participantsForm.addListener(ParticipantsForm.CloseEvent.class, e -> {
            agentsGrid.asSingleSelect().clear();
            closeComponents();
        });
        participantsForm.addListener(ParticipantsForm.AgentUpdatedEvent.class, e -> {
            updateAgents();

            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();
        });

        participantsForm.addListener(ParticipantsForm.AgentTerminatedEvent.class, e -> {
            updateAgents();

            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_TERMINATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();
        });

        participantsForm.addListener(ParticipantsForm.AgentAddedEvent.class, e -> {
            updateAgents();

            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_ADDED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            closeComponents();

        });

        participantsForm.addListener(ParticipantsForm.CloseAttendanceFormEvent.class, e -> {
            participantsForm.setVisible(false);

            agentsGrid.asSingleSelect().clear();

            removeClassNames(
                    ADDING_AGENT,
                    EDITING_AGENTS,
                    PARTICIPANT_OPEN
            );

        });

        participantsForm.addListener(ParticipantsForm.AgentDaysWorkedUpdatedEvent.class, e -> {
            updateAttendance();

            Notification notification = new Notification(UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

        });

        participantsForm.addListener(ParticipantsForm.CloseAndUpdateEvent.class, e -> {
            agentsGrid.setItems(participantsServices.getAllExpiredAgents());
            agentsGrid.asSingleSelect().clear();
            closeComponents();
        });

        participantsForm.addListener(NaviDrawer.VillageEvent.class, e -> {
            Notification notification = new Notification("Got here");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

        });

    }

    private void configureNaviDrawer() {
        naviDrawer = new NaviDrawer(parametersService, participantsServices);

        naviDrawer.addListener(NaviDrawer.VillageEvent.class, e -> {
            Notification notification = new Notification("Got here");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

        });

    }

    private InputStream getStream(File file) {
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stream;
    }

    private Component getToolbar() {

        // Search Agent
        searchAgent.setPlaceholder(SEARCH_EMPLOYEE_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.addValueChangeListener(searchAgentValueChanged -> {
            if (status.getValue() != null) {
                if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                    statusValue = 0L;
                    employeesModelList = new ArrayList<>();

                    if (isAttendanceOpen && datePicker.getValue() != null) {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                        employeesModelList = participantsServices.searchAttendance(
                                datePicker.getValue().format(dateTimeFormatter),
                                searchAgent.getValue(),
                                statusValue
                        );

                    }
                    else {
                        employeesModelList = participantsServices
                                .searchAgents(
                                        searchAgent.getValue(),
                                        statusValue,
                                        false
                                );
                    }

                    agentsGrid.setItems(employeesModelList);

                }
                else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                    statusValue = 0L;
                    employeesModelList = new ArrayList<>();
                    employeesModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    true
                            );
                    agentsGrid.setItems(employeesModelList);
                }
                else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                    statusValue = 1L;
                    employeesModelList = new ArrayList<>();
                    employeesModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    false
                            );
                    agentsGrid.setItems(employeesModelList);
                }
            }
        });

        // DatePicker And Formatter
        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {

            searchAgent.clear();

            configureAgentsGrid(AGENT_ATTENDANCE);

            addAttendanceToAgents();

            isAttendanceOpen = true;

            status.setValue(ACTIVE);
            status.setReadOnly(true);

            if (participantsForm.isVisible()) {
                participantsForm.setVisible(false);
            }

            participantsForm.hideAllComponents(true);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            if (datePicker.getValue() != null) {
                date = datePicker.getValue().format(dateFormatter);
            }

        });

        dateBackHorizontalLayout = new HorizontalLayout(datePicker, backButton);

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

        HorizontalLayout menuLayout = new HorizontalLayout(menuBar);
        menuLayout.setMargin(false);
        menuLayout.setPadding(false);
        menuLayout.getStyle().set("overflow", "hidden");

        MenuBar filterMenuBar = new MenuBar();
        filterMenuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY, MenuBarVariant.LUMO_CONTRAST);

        MenuItem filter = createIconItem(filterMenuBar, VaadinIcon.FILTER, "Filter", null);
        SubMenu filterSubMenu = filter.getSubMenu();
        MenuItem villageMenuItem = filterSubMenu.addItem("Place");
        SubMenu villageSubMenu = villageMenuItem.getSubMenu();
        villageSubMenu.addItem(placementPlaceFilter);

        Div div = new Div();
        menuSplitLayout = new SplitLayout(menuLayout, div);
        menuSplitLayout.addClassName(MENU_SLIT_LAYOUT);

        MenuItem addParticipant = createIconItem(
                menuBar,
                VaadinIcon.FILE_ADD,
                "Add Employee",
                null);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm  dd MMMM yyyy");

        StreamResource resource = new StreamResource(
                "Employees "+LocalDateTime.now().format(dateFormatter)+".csv",
                () -> new ByteArrayInputStream(getParticipantsFile()));

        Anchor link = new Anchor(resource, "Export Employees");

        SubMenu addParticipantSubMenu = addParticipant.getSubMenu();
        addParticipantSubMenu.addItem("Add Manually").addClickListener(click -> {
            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

            addClassName(PARTICIPANT_OPEN);

            openAddAgentForm();

        });
        addParticipantSubMenu.addItem("Import CSV File").addClickListener(click -> {
            configureDialogs();
            uploadFileDialog.open();
        });
        addParticipantSubMenu.addItem(link);

        // Attendance
        attendanceButton.addClassName(ATTENDANCE_BUTTON);
        attendanceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        attendanceButton.setDisableOnClick(true);
        attendanceButton.addClickListener(click -> {
            datePicker.setVisible(true);
            backButton.setVisible(true);
            isAttendanceButtonClicked = true;
            dateBackHorizontalLayout.setVisible(true);
            addClassName(ATTENDANCE_BUTTON_CLICKED);

        /*    try {
                //   Path path1 = Paths.get(ClassLoader.getSystemResource("csv/participants.csv").toURI());

                String path = "participants.csv";

                File file = new File(path);

                StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));

                FileWriter fileWriter = new FileWriter(file);

                CSVWriter csvWriter = new CSVWriter(fileWriter);

                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"Name", "Mark", "Pass/Fail"});
                data.add(new String[]{"Stacy Hart", "80", "Pass"});
                data.add(new String[]{"John Doe", "40", "Fail"});

                csvWriter.writeAll(data);

                csvWriter.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
*/
        });

        backButton.setVisible(false);
        backButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        backButton.addClickListener(click -> {

            removeClassNames(
                    ADDING_AGENT,
                    EDITING_AGENTS,
                    SEARCH_AGENT,
                    ATTENDANCE_BUTTON,
                    STATUS_ATTENDANCE_LAYOUT,
                    ATTENDANCE_BUTTON_CLICKED,
                    SLIT_LAYOUT_1,
                    MENU_SLIT_LAYOUT,
                    PARTICIPANT_OPEN
            );

            menuBar.setVisible(true);

            configureBackButton();

        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() <= 500) {
                agentsGrid.setHeight("90%");
            }
            else {
                agentsGrid.setSizeFull();
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() <= 500) {
                agentsGrid.setHeight("90%");
            }
            else {
                agentsGrid.setSizeFull();
            }
        });

        placementPlaceFilter.setPlaceholder("Filter by place");
        placementPlaceFilter.setClearButtonVisible(true);

        placementPlaceFilter.addValueChangeListener(placeValueChangeEvent -> {

            searchAgent.clear();

            if (placeValueChangeEvent.getValue() == null) {
                if (isAttendanceOpen) {
                    updateAttendance();
                }
                else {
                    if (status.getValue() != null) {
                        if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                            agentsGrid.setItems(participantsServices.getAllAgents());
                        }
                        else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                            agentsGrid.setItems(participantsServices.getAllExpiredAgents());
                        }
                        else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                            agentsGrid.setItems(participantsServices.getAllTerminatedAgents());
                        }
                    }
                }
            }
            else {
                if (isAttendanceOpen) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                    agentsGrid.setItems(
                            participantsServices.getAttendanceByPlace(
                                    datePicker.getValue().format(dateTimeFormatter),
                                    placeValueChangeEvent.getValue()
                            ));
                }
                else {
                    if (status.getValue() != null) {
                        if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                            agentsGrid.setItems(
                                    participantsServices.filterAgentsByPlace(placementPlaceFilter.getValue()));
                        }
                        else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                            agentsGrid.setItems(participantsServices
                                    .filterExpiredAgentsByPlace(placementPlaceFilter.getValue()));
                        }
                        else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                            agentsGrid.setItems(participantsServices
                                    .filterTerminatedAgentsByPlace(placementPlaceFilter.getValue()));

                        }
                    }
                }
            }

        });

        status.setItems(ACTIVE, EXPIRED, TERMINATED);
        status.setValue(ACTIVE);
        status.addValueChangeListener(statusValueChangeEvent -> {
            searchAgent.clear();

            if (!isAttendanceOpen) {

                agentsGrid.asSingleSelect().clear();
                closeComponents();

                if (statusValueChangeEvent.getValue().equalsIgnoreCase(ACTIVE)) {

                    if (placementPlaceFilter.getValue() != null) {
                        agentsGrid.setItems(
                                participantsServices.filterAgentsByPlace(placementPlaceFilter.getValue()));
                    }
                    else {
                        agentsGrid.setItems(participantsServices.getAllAgents());
                    }

                    participantsForm.disableButtons(true, false);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(TERMINATED)) {

                    if (placementPlaceFilter.getValue() != null) {
                        agentsGrid.setItems(participantsServices
                                .filterTerminatedAgentsByPlace(placementPlaceFilter.getValue()));
                    }
                    else {
                        agentsGrid.setItems(participantsServices.getAllTerminatedAgents());
                    }

                    participantsForm.disableButtons(false, true);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(EXPIRED)) {

                    if (placementPlaceFilter.getValue() != null) {
                        agentsGrid.setItems(participantsServices
                                .filterExpiredAgentsByPlace(placementPlaceFilter.getValue()));
                    }
                    else {
                        agentsGrid.setItems(participantsServices.getAllExpiredAgents());
                    }

                    participantsForm.disableButtons(false, false);
                }
            }
        });

        MenuItem contractStatusMenuItem = filterSubMenu.addItem("Contract Status");
        SubMenu contractStatusSubMenu = contractStatusMenuItem.getSubMenu();
        contractStatusSubMenu.addItem(status);

        HorizontalLayout horizontalLayout = new HorizontalLayout(
                filterMenuBar,
                searchAgent
        );
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(false);
        horizontalLayout.getStyle().set("overflow", "hidden");

        Div div1 = new Div();
        splitLayout1 = new SplitLayout(horizontalLayout, div1);
        splitLayout1.addClassName(SLIT_LAYOUT_1);

        MenuItem attendanceMenu = createIconItem(menuBar, VaadinIcon.RECORDS, "Attendance",
                null);
        attendanceMenu.addClickListener(menuItemClickEvent -> {
            datePicker.setVisible(true);
            backButton.setVisible(true);
            isAttendanceButtonClicked = true;
            dateBackHorizontalLayout.setVisible(true);
            menuBar.setVisible(false);
            addClassName(ATTENDANCE_BUTTON_CLICKED);
            menuSplitLayout.setVisible(false);
            splitLayout1.setVisible(false);


        });

        VerticalLayout verticalLayout = new VerticalLayout(
                dateBackHorizontalLayout,
                menuSplitLayout,
                splitLayout1);
        verticalLayout.addClassName(TOOLBAR_VERTICAL_LAYOUT);
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);

        Button terminateButton = new Button(TERMINATE);
        terminateButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return verticalLayout;
    }

    private byte[] getParticipantsFile() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream);

        try {

            updateAgents();

            CSVWriter csvWriter = new CSVWriter(streamWriter);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Name", "Gender", "Mobile Number", "Education"});

            for (int i = 0; i < employeesModelList.size(); i++) {
                data.add(new String[] {
                        employeesModelList.get(i).getEmployee(),
                        employeesModelList.get(i).getGender(),
                        employeesModelList.get(i).getMobileNumber(),
                        employeesModelList.get(i).getEducation()
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

    private void exportParticipants() {

    }

    private void addAttendanceToAgents() {

        if (datePicker.getValue() != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

            agentsGrid.setItems(
                    participantsServices.getAttendance(
                            datePicker.getValue().format(dateTimeFormatter)
                    ));

            agentsGrid.asSingleSelect().clear();

        }

    }

    private void configureBackButton() {

        removeClassNames(
                EDITING_AGENTS,
                PARTICIPANT_OPEN
        );

        datePicker.clear();

        splitLayout1.setVisible(true);
        menuSplitLayout.setVisible(true);

        status.setValue(ACTIVE);
        status.setReadOnly(false);

        dateBackHorizontalLayout.setVisible(false);

        isAttendanceOpen = false;
        isAttendanceButtonClicked = false;

        configureAgentsGrid(AGENT_POSITION);

        participantsForm.changeLayout(false);
        participantsForm.showTab(IDENTIFICATION);

        agentsGrid.asSingleSelect().clear();

        participantsForm.resetTabs();

        updateAgents();

    }

    private void configureAgentsGrid(String columns) {

        agentsGrid.addClassName(AGENTS_GRID);

        if (columns.equalsIgnoreCase(AGENT_POSITION)) {
            agentsGrid.setColumns(EMPLOYEE, POSITION);
        }
        else if (columns.equalsIgnoreCase(AGENT_ATTENDANCE)) {
            agentsGrid.setColumns(EMPLOYEE, DAYS_WORKED_CAMEL_CASE);
        }

        agentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void updateAgents() {
        employeesModelList = participantsServices.getAllAgents();
        agentsGrid.setItems(employeesModelList);

    }

    private void updateAttendance() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

        agentsGrid.setItems(participantsServices.getAttendance(
                datePicker.getValue().format(dateTimeFormatter))
        );

    }

    private void editAgent(EmployeesModel employeesModel) {
        addClassNames(
                EDITING_AGENTS,
                PARTICIPANT_OPEN
        );

        if (employeesModel == null) {
            closeComponents();
        }
        else {
            if (isAttendanceOpen) {
                participantsForm.setAgent(employeesModel, date, parametersModelList);
                participantsForm.changeLayout(true);
                participantsForm.setVisible(true);
            }
            else {
                participantsForm.setButtonText(false);
                participantsForm.setAgent(employeesModel, date, parametersModelList);
                participantsForm.setVisible(true);
            }
        }
    }

    private void openAddAgentForm() {
        agentsGrid.asSingleSelect().clear();
        editAgent(new EmployeesModel());
        participantsForm.setButtonText(true);
    }

    private void configureDialogs() {

        // SessionManager
        SessionManager sessionManager = new SessionManager();

        // ApplicationUser
        ApplicationUserDataModel applicationUserDataModel = sessionManager.getApplicationUserData();

        uploadFileDialog = new Dialog();

        // TODO Upload not finished
        MemoryBuffer memoryBuffer = new MemoryBuffer();

        upload = new Upload(memoryBuffer);
        upload.setDropAllowed(true);
        upload.setSizeFull();
        upload.setAcceptedFileTypes(CSV_FORMAT);

        upload.addFileRejectedListener(fileRejectedEvent -> {

            Notification notification = Notification.show(
                    CSV_ERROR_MESSAGE,
                    5000,
                    Notification.Position.MIDDLE
            );

            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        });

        upload.setMaxFiles(1);

        final List<EmployeesModel>[] participantsModelList = new List[]{new ArrayList<>()};

        upload.addSucceededListener(event -> {
            isCsvUploaded = true;
        });

        uploadFileDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> {
                    uploadFileDialog.close();
                })
        );

        uploadFileDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            uploadFileDialog.close();
        });

        uploadCSV.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        uploadCSV.addClickListener(click -> {

            if (isCsvUploaded) {
                isCsvUploaded = false;

                participantsModelList[0] = new ArrayList<>();

                InputStream fileData = memoryBuffer.getInputStream();

                String line;
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(fileData, StandardCharsets.UTF_8))) {

                    while ((line = bufferedReader.readLine()) != null) {

                        String[] fields = line.split(",");
                        if (fields.length != 20) {

                            Notification notification = new Notification("Invalid csv format");
                            notification.setPosition(Notification.Position.BOTTOM_CENTER);
                            notification.addThemeVariants(
                                    NotificationVariant.LUMO_PRIMARY,
                                    NotificationVariant.LUMO_ERROR);
                            notification.setDuration(5000);
                            notification.open();

                            throw new RuntimeException("Invalid csv line: "+line);
                        }

                        if (!fields[0].equalsIgnoreCase("firstname") && !fields[0].isEmpty()) {
                            String [] getDateOfBirth = fields[3].split("-");
                            LocalDate dateOfBirth = LocalDate.of(
                                    Integer.parseInt(getDateOfBirth[0]),
                                    Integer.parseInt(getDateOfBirth[1]),
                                    Integer.parseInt(getDateOfBirth[2])
                            );

                            String [] getPlacementDate = fields[14].split("-");
                            LocalDate placementDate = LocalDate.of(
                                    Integer.parseInt(getPlacementDate[0]),
                                    Integer.parseInt(getPlacementDate[1]),
                                    Integer.parseInt(getPlacementDate[2])
                            );

                            String [] getCompletionDate = fields[15].split("-");
                            LocalDate completionDate = LocalDate.of(
                                    Integer.parseInt(getCompletionDate[0]),
                                    Integer.parseInt(getCompletionDate[1]),
                                    Integer.parseInt(getCompletionDate[2])
                            );

                            long monthsDifference = ChronoUnit.MONTHS.between(placementDate, completionDate);

                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                            List<String> contractDates = new ArrayList<>();
                            for (int k = 1; k < Integer.parseInt(String.valueOf(monthsDifference)) + 1; k++) {
                                contractDates.add(LocalDate.now().plusMonths(k).format(dateFormatter));
                            }

                            EmployeesModel employeesModel = new EmployeesModel(
                                    LocalDateTime.now(),
                                    dateOfBirth,
                                    placementDate,
                                    completionDate,
                                    fields[0],
                                    fields[1],
                                    fields[2],
                                    fields[4],
                                    fields[5],
                                    fields[6],
                                    fields[7],
                                    fields[8],
                                    fields[9],
                                    fields[10],
                                    fields[11],
                                    fields[12],
                                    fields[13],
                                    fields[16],
                                    fields[17],
                                    fields[18],
                                    fields[19],
                                    applicationUserDataModel.getDistrict(),
                                    applicationUserDataModel.getVillage(),
                                    applicationUserDataModel.getService(),
                                    String.valueOf(monthsDifference)
                            );

                            participantsModelList[0].add(employeesModel);

                            String response = participantsServices.addAgent(employeesModel, contractDates);
                            System.err.println("response: "+response);
                        }

                    }

                    uploadFileDialog.close();

                    Notification notification = new Notification("Participants Successfully Added");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();

                    updateAgents();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            else {
                Notification notification = new Notification("Add Participants CSV File");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(5000);
                notification.open();
            }

        });

        uploadFileDialog.setHeaderTitle("Upload Participants CSV File");
        uploadFileDialog.add(upload);
        uploadFileDialog.getFooter().add(uploadCSV);

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                upload.setWidth("70%");
                uploadFileDialog.setWidth("70%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                upload.setWidth("70%");
                uploadFileDialog.setWidth("70%");
            }

        });

    }

    public void filterParticipantsByPlace(String village) {

        searchAgent.clear();

        if (village == null) {
            if (isAttendanceOpen) {
                updateAttendance();
                System.err.println("attendance updated");
            }
            else {
                if (status.getValue() != null) {
                    if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                     //   agentsGrid.setItems(participantsServices.getAllAgents());

                        agentsGrid.setItems(participantsServices.getAllTerminatedAgents());

                        employeesModelList = participantsServices.getAllTerminatedAgents();
                        agentsGrid.setItems(employeesModelList);
                        searchAgent.clear();

                        System.err.println(participantsServices.getAllTerminatedAgents());
                        System.err.println("get all agents");
                    }
                    else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                        agentsGrid.setItems(participantsServices.getAllExpiredAgents());
                        System.err.println("Get all expired agents");
                    }
                    else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                        agentsGrid.setItems(participantsServices.getAllTerminatedAgents());
                        System.err.println("get all terminated agents");
                    }
                }
            }
        }
        else {
            if (isAttendanceOpen) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                agentsGrid.setItems(
                        participantsServices.getAttendanceByPlace(
                                datePicker.getValue().format(dateTimeFormatter),
                                village
                        ));

                System.err.println("get attendance by places");
            }
            else {
                if (status.getValue() != null) {
                    if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                        agentsGrid.setItems(
                                participantsServices.filterAgentsByPlace(village));

                        System.err.println("filter agents");
                    }
                    else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                        agentsGrid.setItems(participantsServices
                                .filterExpiredAgentsByPlace(village));
                        System.err.println("filter expired agents");
                    }
                    else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                        agentsGrid.setItems(participantsServices
                                .filterTerminatedAgentsByPlace(village));

                        System.err.println("Filter terminated agents");
                    }
                }
            }
        }

        Notification.show("here");
    }

}

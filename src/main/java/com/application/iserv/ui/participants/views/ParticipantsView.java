package com.application.iserv.ui.participants.views;

import com.application.iserv.StudentModel;
import com.application.iserv.backend.services.ParticipantsServices;
import com.application.iserv.tests.MainLayout;
import com.application.iserv.ui.participants.forms.ParticipantsForm;
import com.application.iserv.ui.participants.models.ParticipantsModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@PermitAll
@PageTitle("iServ | Participants")
@Route(value = PARTICIPANTS_LOWER_CASE, layout = MainLayout.class)
public class ParticipantsView extends VerticalLayout {

    // Grid
    Grid<ParticipantsModel> agentsGrid = new Grid<>(ParticipantsModel.class);

    // booleans
    boolean isAttendanceOpen = false;
    boolean isAttendanceButtonClicked = false;

    // TextFields
    TextField searchAgent = new TextField();

    // Forms
    ParticipantsForm participantsForm;

    Select<String> status = new Select<>();

    // DatePicker
    DatePicker datePicker = new DatePicker();

    // Buttons
    Button uploadCSV = new Button(UPLOAD);
    Button backButton = new Button(BACK);
    Button attendanceButton = new Button(ATTENDANCE);

    // Layouts
    HorizontalLayout agentsHorizontalLayout;
    HorizontalLayout dateBackHorizontalLayout;

    // Services
    private final ParticipantsServices participantsServices;

    // Longs
    Long statusValue = 0L;

    // ArrayList
    List<ParticipantsModel> participantsModelList = new ArrayList<>();

    // Strings
    String date = "";

    // MenuItem
    MenuItem addAgentButton;

    // Dialogs
    Dialog uploadFileDialog = new Dialog();

    // Upload
    Upload upload = new Upload();

    @Autowired
    public ParticipantsView(ParticipantsServices participantsServices) {
        this.participantsServices = participantsServices;

        setSizeFull();
        addClassName(AGENTS_LIST_VIEW);

        checkScreenSize();
        configureAgentsForm();
        configureAgentsGrid(AGENT_POSITION);

        add(
                getToolbar(),
                getAgentsContent()
        );

        updateAgents();
        closeComponents();

        agentsGrid.asSingleSelect().addValueChangeListener(e -> editAgent(e.getValue()));

    }

    private void closeComponents() {

        if (!isAttendanceButtonClicked) {
            datePicker.setVisible(false);
        }

        participantsForm.setVisible(false);

        removeClassName(ADDING_AGENT);
        removeClassName(EDITING_AGENTS);
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
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        participantsForm.addListener(ParticipantsForm.AgentTerminatedEvent.class, e -> {
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_TERMINATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

        });

        participantsForm.addListener(ParticipantsForm.AgentAddedEvent.class, e -> {
            Notification notification = new Notification(PARTICIPANT_SUCCESSFULLY_ADDED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAgents();

            closeComponents();

        });

        participantsForm.addListener(ParticipantsForm.CloseAttendanceFormEvent.class, e -> {
            participantsForm.setVisible(false);

            agentsGrid.asSingleSelect().clear();

            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

        });

        participantsForm.addListener(ParticipantsForm.AgentDaysWorkedUpdatedEvent.class, e -> {
            Notification notification = new Notification(UPDATED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            updateAttendance();

        });

    }

    private Component getToolbar() {
        searchAgent.setPlaceholder(SEARCH_AGENT_HINT);
        searchAgent.setClearButtonVisible(true);
        searchAgent.setValueChangeMode(ValueChangeMode.LAZY);
        searchAgent.addClassName(SEARCH_AGENT);
        searchAgent.addValueChangeListener(searchAgentValueChanged -> {
            if (status.getValue() != null) {
                if (status.getValue().equalsIgnoreCase(ACTIVE)) {
                    statusValue = 0L;
                    participantsModelList = new ArrayList<>();

                    if (isAttendanceOpen && datePicker.getValue() != null) {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                        participantsModelList = participantsServices.searchAttendance(
                                datePicker.getValue().format(dateTimeFormatter),
                                searchAgent.getValue(),
                                statusValue
                        );

                    }
                    else {
                        participantsModelList = participantsServices
                                .searchAgents(
                                        searchAgent.getValue(),
                                        statusValue,
                                        false
                                );
                    }

                    agentsGrid.setItems(participantsModelList);

                }
                else if (status.getValue().equalsIgnoreCase(EXPIRED)) {
                    statusValue = 0L;
                    participantsModelList = new ArrayList<>();
                    participantsModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    true
                            );
                    agentsGrid.setItems(participantsModelList);
                }
                else if (status.getValue().equalsIgnoreCase(TERMINATED)) {
                    statusValue = 1L;
                    participantsModelList = new ArrayList<>();
                    participantsModelList = participantsServices.
                            searchAgents(
                                    searchAgent.getValue(),
                                    statusValue,
                                    false
                            );
                    agentsGrid.setItems(participantsModelList);
                }
            }
        });

        status.setLabel(CONTRACT_STATUS);
        status.setItems(ACTIVE, EXPIRED, TERMINATED);
        status.setValue(ACTIVE);
        status.addComponents(ACTIVE, new Hr());
        status.addComponents(EXPIRED, new Hr());

        status.addValueChangeListener(statusValueChangeEvent -> {
            if (!isAttendanceOpen) {
                if (statusValueChangeEvent.getValue().equalsIgnoreCase(ACTIVE)) {
                    agentsGrid.setItems(participantsServices.getAllAgents());
                    participantsForm.disableButtons(true);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(TERMINATED)) {
                    agentsGrid.setItems(participantsServices.getAllTerminatedAgents());
                    participantsForm.disableButtons(false);
                }
                else if (statusValueChangeEvent.getValue().equalsIgnoreCase(EXPIRED)) {
                    agentsGrid.setItems(participantsServices.getAllExpiredAgents());
                    participantsForm.disableButtons(false);
                }
            }
        });

        attendanceButton.addClassName(ATTENDANCE_BUTTON);
        attendanceButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        attendanceButton.setDisableOnClick(true);
        attendanceButton.addClickListener(click -> {
            datePicker.setVisible(true);
            backButton.setVisible(true);
            isAttendanceButtonClicked = true;
            dateBackHorizontalLayout.setVisible(true);
            addClassName(ATTENDANCE_BUTTON_CLICKED);
        });

        backButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        backButton.setVisible(false);

        backButton.addClickListener(click -> {

            removeClassNames(
                    ADDING_AGENT,
                    EDITING_AGENTS,
                    SEARCH_AGENT,
                    ATTENDANCE_BUTTON,
                    STATUS_ATTENDANCE_LAYOUT,
                    ATTENDANCE_BUTTON_CLICKED
            );

            configureBackButton();

        });

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);
        menuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);

        addAgentButton = menuBar.addItem("Add Participant");
        SubMenu subMenu = addAgentButton.getSubMenu();
        subMenu.addItem("Manually").addClickListener(click -> {

            removeClassName(ADDING_AGENT);
            removeClassName(EDITING_AGENTS);

            openAddAgentForm();

        });
        subMenu.add(new Hr());
        subMenu.addItem("Import CSV File").addClickListener(click -> {
            configureDialogs();
            uploadFileDialog.open();
        });

        HorizontalLayout statusAttendanceLayout = new HorizontalLayout(status, attendanceButton);
        statusAttendanceLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        statusAttendanceLayout.addClassName(STATUS_ATTENDANCE_LAYOUT);

        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_MONTH_DATE_FORMAT);

        datePicker.setI18n(dateFormat);
        datePicker.setPlaceholder(DATE);
        datePicker.addValueChangeListener(datePickerValueChangeEvent -> {
            configureAgentsGrid(AGENT_ATTENDANCE);

            addAttendanceToAgents();

            isAttendanceOpen = true;

            addAgentButton.setEnabled(false);

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


        dateBackHorizontalLayout = new HorizontalLayout(datePicker, backButton);

        HorizontalLayout toolbar = new HorizontalLayout(searchAgent, menuBar, dateBackHorizontalLayout);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.addClassName(TOOLBAR);

        VerticalLayout verticalToolbar = new VerticalLayout(statusAttendanceLayout, toolbar);
        verticalToolbar.addClassName(VERTICAL_TOOLBAR);

        Button terminateButton = new Button(TERMINATE);
        terminateButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout horizontalToolbar = new HorizontalLayout(verticalToolbar, terminateButton, upload);
        horizontalToolbar.addClassNames(HORIZONTAL_TOOLBAR);
        horizontalToolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return verticalToolbar;
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

        removeClassName(EDITING_AGENTS);

        datePicker.clear();

        addAgentButton.setEnabled(true);
        attendanceButton.setEnabled(true);

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
            agentsGrid.setColumns(PARTICIPANT, POSITION);
        }
        else if (columns.equalsIgnoreCase(AGENT_ATTENDANCE)) {
            agentsGrid.setColumns(PARTICIPANT, DAYS_WORKED_CAMEL_CASE);
        }

        agentsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void updateAgents() {
        participantsModelList = participantsServices.getAllAgents();
        agentsGrid.setItems(participantsModelList);
    }

    private void updateAttendance() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

        agentsGrid.setItems(participantsServices.getAttendance(
                datePicker.getValue().format(dateTimeFormatter))
        );

    }

    private void editAgent(ParticipantsModel participantsModel) {
        addClassName(EDITING_AGENTS);

        if (participantsModel == null) {
            closeComponents();
        }
        else {
            if (isAttendanceOpen) {
                participantsForm.setAgent(participantsModel, date);
                participantsForm.changeLayout(true);
                participantsForm.setVisible(true);
            }
            else {
                participantsForm.setButtonText(false);
                participantsForm.setAgent(participantsModel, date);
                participantsForm.setVisible(true);
            }
        }
    }

    private void openAddAgentForm() {
        agentsGrid.asSingleSelect().clear();
        editAgent(new ParticipantsModel());
        participantsForm.setButtonText(true);
    }

    private void configureDialogs() {

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

        final List<StudentModel>[] studentModelList = new List[]{new ArrayList<>()};

        upload.addSucceededListener(event -> {

            studentModelList[0] = new ArrayList<>();

            InputStream fileData = memoryBuffer.getInputStream();

            String line;
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fileData, StandardCharsets.UTF_8))) {

                while ((line = bufferedReader.readLine()) != null) {

                    String[] row = line.split(",");

                    StudentModel studentModel = new StudentModel();
                    studentModel.setStudent(row[0]);
                    studentModel.setGrade(row[1]);
                    studentModel.setPass_fail(row[2]);

                    studentModelList[0].add(studentModel);

                }

                // Remove Header
                studentModelList[0].remove(0);
                System.err.println("student: "+ studentModelList[0]);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            uploadFileDialog.close();
        });

        uploadFileDialog.setHeaderTitle("Upload Participants CSV File");
        uploadFileDialog.add(upload);
        uploadFileDialog.getFooter().add(uploadCSV);

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                uploadFileDialog.setWidth("70%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                uploadFileDialog.setWidth("70%");
            }

        });

    }

}

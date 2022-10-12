package com.application.iserv.ui.participants.forms;


import com.application.iserv.backend.services.ParticipantsServices;
import com.application.iserv.ui.participants.models.ParticipantsModel;
import com.application.iserv.ui.participants.models.NomineesModel;
import com.application.iserv.ui.participants.models.ReferenceModel;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

public class ParticipantsForm extends VerticalLayout {

    // TODO Make duration always positive for expired agents

    // Grids
    Grid<NomineesModel> nomineesGrid = new Grid<>(NomineesModel.class);
    Grid<ReferenceModel> referenceGrid = new Grid<>(ReferenceModel.class);

    // IntegerField
    IntegerField daysWorked = new IntegerField();


    // ComboBox
    ComboBox<String> gender = new ComboBox<>(GENDER);
    ComboBox<String> maritalStatus = new ComboBox<>(MARITAL_STATUS);
    ComboBox<String> education = new ComboBox<>(EDUCATION);
    ComboBox<String> placementPlace = new ComboBox<>(PLACEMENT_PLACE);
    ComboBox<String> position = new ComboBox<>(POSITION_UPPER_CASE);
    ComboBox<String> relationship = new ComboBox<>(RELATIONSHIP);
    ComboBox<String> paymentMethod = new ComboBox<>(PAYMENT_METHOD);
    ComboBox<String> duration = new ComboBox<>(CONTRACT_DURATION);


    // TextFields
    TextField firstname = new TextField(FIRSTNAME);
    TextField lastname = new TextField(LASTNAME);
    TextField identityNumber = new TextField(ID_NUMBER);
    TextField mobileNumber = new TextField(PRIMARY_MOBILE);
    TextField alternateMobileNumber = new TextField(ALTERNATE_MOBILE);
    TextField postalAddress = new TextField(POSTAL_ADDRESS);
    TextField residentialAddress = new TextField(RESIDENTIAL_ADDRESS);
    TextField placementOfficer = new TextField(PLACEMENT_OFFICER);
    TextField referenceFirstname = new TextField(FIRSTNAME);
    TextField referenceLastname = new TextField(LASTNAME);
    TextField referenceIdentityNumber = new TextField(ID_NUMBER);
    TextField referenceMobileNumber = new TextField(PRIMARY_MOBILE);
    TextField referencePostalAddress = new TextField(POSTAL_ADDRESS);
    TextField nomineeFirstname = new TextField(FIRSTNAME);
    TextField nomineeLastname = new TextField(LASTNAME);
    TextField nomineeIdentityNumber = new TextField(ID_NUMBER);
    TextField nomineeMobileNumber = new TextField(PRIMARY_MOBILE);
    TextField nomineePostalAddress = new TextField(POSTAL_ADDRESS);
    TextField mobileWalletProvider = new TextField(PRIMARY_MOBILE);
    TextField bankName = new TextField(BANK_NAME);
    TextField branch = new TextField(BRANCH);
    TextField accountNumber = new TextField(ACCOUNT_NUMBER);


    // Tabs
    Tabs tabs;
    Tab identification = new Tab(IDENTIFICATION);
    Tab educationTab = new Tab(EDUCATION);
    Tab service = new Tab(SERVICE);
    Tab banking = new Tab(BANKING);
    Tab nominees = new Tab(NOMINEES);
    Tab references = new Tab(REFERENCES);

    // DatePicker
    DatePicker dateOfBirth = new DatePicker();
    DatePicker placementDate = new DatePicker();
    DatePicker completionDate = new DatePicker();

    // Attendance
    //ArrayList
    List<NomineesModel> allNomineesList = new ArrayList<>();
    List<NomineesModel> agentNomineesList = new ArrayList<>();
    List<ReferenceModel> allReferencesList = new ArrayList<>();
    List<ReferenceModel> agentReferencesList = new ArrayList<>();
    List<String> contractDates = new ArrayList<>();

    // Buttons
    Button updateAddAgent = new Button();
    Button backButton = new Button(BACK);
    Button terminateButton = new Button(TERMINATE);
    Button updateButton = new Button(UPDATE);
    Button attendanceBackButton = new Button(BACK);
    Button addNomineeButton = new Button(ADD);
    Button addReferenceButton = new Button(ADD);
    Button saveNominee = new Button();
    Button removeNominee = new Button(REMOVE);
    Button saveReference = new Button();
    Button removeReference = new Button(REMOVE);


    // Binder
    Binder<ParticipantsModel> agentsModelBinder = new Binder<>(ParticipantsModel.class);

    // Layouts
    HorizontalLayout buttonsLayout;
    HorizontalLayout addAgentButtonsLayout;
    HorizontalLayout namesLayout;
    VerticalLayout addButtonGridNomineeLayout;
    VerticalLayout addButtonGridReferenceLayout;

    // Dialogs
    Dialog nomineesDialog = new Dialog();
    Dialog referenceDialog = new Dialog();

    // Models
    private ParticipantsModel participantsModel;
    private final ParticipantsServices participantsServices;

    // Longs
    Long nomineeId;
    Long referenceId;
    Long participantId;

    // Strings
    String date;
    String selectedTabLabel = "";

    private NomineesModel nomineesModel;
    private ReferenceModel referenceModel;

    // Booleans
    boolean isUpdateNominee = false;
    boolean isUpdateReference = false;

    @Autowired
    public ParticipantsForm(ParticipantsServices participantsServices) {
        this.participantsServices = participantsServices;

        configureDate();
        configureTabs();
        configureLists();
        checkScreenSize();
        configureBinder();
        configureButtons();
        configureDialogs();
        configureGrids();
        hideComponents();
        updateGridInfo();
        configureHorizontalLayouts();
        addClassName(AGENTS_FORM);

        daysWorked.setLabel("Working Days");
        daysWorked.setHasControls(true);
        daysWorked.setMin(0);
        daysWorked.setMax(20);
        daysWorked.setVisible(false);

        FormLayout formLayout = new FormLayout(
                daysWorked,
                buttonsLayout,
                firstname,
                lastname,
                identityNumber,
                dateOfBirth,
                gender,
                maritalStatus,
                mobileNumber,
                alternateMobileNumber,
                postalAddress,
                residentialAddress,
                education,
                placementOfficer,
                placementPlace,
                position,
                duration,
                placementDate,
                completionDate,
                paymentMethod,
                mobileWalletProvider,
                bankName,
                branch,
                accountNumber
        );

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("300px", 2)
        );

        formLayout.setColspan(education, 2);
        formLayout.setColspan(paymentMethod, 2);
        formLayout.setColspan(mobileWalletProvider, 2);
        formLayout.setColspan(bankName, 2);
        formLayout.setColspan(branch, 2);
        formLayout.setColspan(accountNumber, 2);
        formLayout.setColspan(daysWorked, 2);

        VerticalLayout buttonsVerticalLayout = new VerticalLayout(addAgentButtonsLayout);
        buttonsVerticalLayout.setPadding(false);
        buttonsVerticalLayout.setMargin(false);

        addButtonGridNomineeLayout = new VerticalLayout(addNomineeButton, nomineesGrid);
        addButtonGridNomineeLayout.setMargin(false);
        addButtonGridNomineeLayout.setPadding(false);

        addButtonGridReferenceLayout = new VerticalLayout(addReferenceButton, referenceGrid);
        addButtonGridReferenceLayout.setMargin(false);
        addButtonGridReferenceLayout.setPadding(false);

        add(
                tabs,
                formLayout,
                nomineesDialog,
                addButtonGridNomineeLayout,
                addButtonGridReferenceLayout,
                buttonsVerticalLayout
        );

        tabs.addSelectedChangeListener(selectedChangeEvent
                -> showTab(selectedChangeEvent.getSelectedTab().getLabel()));
        showTab(tabs.getSelectedTab().getLabel());

        // Attendance
        paymentMethod.addValueChangeListener(paymentMethodValueChangeEvent -> {

            String payment = paymentMethodValueChangeEvent.getValue();

            if (payment != null) {
                if (payment.equalsIgnoreCase(CASH)) {
                    mobileWalletProvider.setVisible(false);
                    bankName.setVisible(false);
                    branch.setVisible(false);
                    accountNumber.setVisible(false);

                    bankName.clear();
                    branch.clear();
                    accountNumber.clear();
                    mobileWalletProvider.clear();
                }
                else if (payment.equalsIgnoreCase(MOBILE_WALLET)) {
                    mobileWalletProvider.setVisible(true);

                    bankName.setVisible(false);
                    branch.setVisible(false);
                    accountNumber.setVisible(false);

                    bankName.clear();
                    branch.clear();
                    accountNumber.clear();
                }
                else if (payment.equalsIgnoreCase(BANK_EFT)) {
                    mobileWalletProvider.setVisible(false);

                    bankName.setVisible(true);
                    branch.setVisible(true);
                    accountNumber.setVisible(true);

                    mobileWalletProvider.clear();
                }

            }

        });

        nomineesGrid.asSingleSelect().addValueChangeListener(e -> {
            removeNominee.setVisible(true);
            isUpdateNominee = true;
            saveNominee.setText(UPDATE);
            setNominees(e.getValue());
        });

        referenceGrid.asSingleSelect().addValueChangeListener(e -> {
            removeReference.setVisible(true);
            isUpdateReference = true;
            saveReference.setText(UPDATE);
            setReferences(e.getValue());
        });

        placementDate.addValueChangeListener(placementDateValueChange -> {
            if (placementDate.getValue() != null
                    && !placementDate.isEmpty() && duration.getValue() != null) {

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

                String placementDate_str = placementDate.getValue().format(dateTimeFormatter);
                String[] getPlacementDate = placementDate_str.split("-");

                LocalDate placementDateLocalDate = LocalDate.of(
                        Integer.parseInt(getPlacementDate[2]),
                        Integer.parseInt(getPlacementDate[1]),
                        Integer.parseInt(getPlacementDate[0])
                );

                String[] strings = duration.getValue().split(" ");
                long duration_lng = Long.parseLong(strings[0]);

                String duration_str = String.valueOf(duration_lng);
                int duration_int = Integer.parseInt(duration_str);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                contractDates = new ArrayList<>();
                for (int i = 1; i < duration_int + 1; i++) {
                    contractDates.add(placementDateLocalDate.plusMonths(i).format(dateFormatter));
                }

                completionDate.setValue(placementDateLocalDate.plusMonths(duration_lng));

            }
        });

        duration.addValueChangeListener(durationValueChange -> {
            if (placementDate.getValue() != null
                    && !placementDate.isEmpty() && duration.getValue() != null) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

                String placementDate_str = placementDate.getValue().format(dateTimeFormatter);
                String[] getPlacementDate = placementDate_str.split("-");

                LocalDate placementDateLocalDate = LocalDate.of(
                        Integer.parseInt(getPlacementDate[2]),
                        Integer.parseInt(getPlacementDate[1]),
                        Integer.parseInt(getPlacementDate[0])
                );

                String[] strings = duration.getValue().split(" ");
                long duration_lng = Long.parseLong(strings[0]);

                String duration_str = String.valueOf(duration_lng);
                int duration_int = Integer.parseInt(duration_str);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT);

                contractDates = new ArrayList<>();
                for (int i = 1; i < duration_int + 1; i++) {
                    contractDates.add(placementDateLocalDate.plusMonths(i).format(dateFormatter));
                }

                completionDate.setValue(placementDateLocalDate.plusMonths(duration_lng));

            }
        });

        daysWorked.addValueChangeListener(workingDaysValueChange -> {
            if (daysWorked.getValue() != null && !daysWorked.isEmpty()) {
                int workingDaysValue = daysWorked.getValue();

                // TODO Add regex as if statement
                if (workingDaysValue > 20) {
                    daysWorked.setInvalid(true);
                    daysWorked.setErrorMessage("There are only 20 working days a month");
                }
                else if (workingDaysValue < 0) {
                    daysWorked.setInvalid(true);
                    daysWorked.setErrorMessage("Enter valid working days");
                } else {
                    daysWorked.setInvalid(false);
                }
            }

        });

    }

    private void configureNullValues(ParticipantsModel participantsModel) {
        if (alternateMobileNumber != null
                && alternateMobileNumber.getValue().equalsIgnoreCase("null")) {
            alternateMobileNumber.clear();
        }

        if (participantsModel.getMobileWalletProvider() != null) {
            if (!participantsModel.getMobileWalletProvider().equalsIgnoreCase("null")) {
                paymentMethod.setValue(MOBILE_WALLET);
                bankName.clear();
                branch.clear();
                accountNumber.clear();

            }
            else if (!participantsModel.getBankName().equalsIgnoreCase("null")) {
                paymentMethod.setValue(BANK_EFT);
                mobileWalletProvider.clear();
            }
            else if (participantsModel.getBankName().equalsIgnoreCase("null")
                    && participantsModel.getMobileWalletProvider().equalsIgnoreCase("null")) {
                paymentMethod.setValue(CASH);
                bankName.clear();
                branch.clear();
                accountNumber.clear();
                mobileWalletProvider.clear();
            }

            if (!selectedTabLabel.equalsIgnoreCase(BANKING)) {
                paymentMethod.setVisible(false);
                mobileWalletProvider.setVisible(false);
                bankName.setVisible(false);
                branch.setVisible(false);
                accountNumber.setVisible(false);

            }
        }

    }

    private void hideComponents() {
        buttonsLayout.setVisible(false);

        paymentMethod.setVisible(false);
        mobileWalletProvider.setVisible(false);
        bankName.setVisible(false);
        branch.setVisible(false);
        accountNumber.setVisible(false);

        completionDate.setReadOnly(true);

    }

    private void updateGridInfo() {

        // Nominees list
        allNomineesList = participantsServices.getAllNominees();

        // Nominees
        nomineesGrid.setItems(agentNomineesList);

        // References list
        allReferencesList = participantsServices.getAllReferences();

        // References
        referenceGrid.setItems(agentReferencesList);

    }

    private void updateNomineesGrid() {

        // Nominees list
        allNomineesList = participantsServices.getAllNominees();

        configureNominees();

        // Nominees
        nomineesGrid.setItems(agentNomineesList);

    }

    private void updateReferenceGrid() {

        // References list
        allReferencesList = participantsServices.getAllReferences();

        configureReferences();

        // References
        referenceGrid.setItems(agentReferencesList);

    }

    private void configureDialogs() {

        // Nominees
        FormLayout addNomineeForm = new FormLayout(
                nomineeFirstname,
                nomineeLastname,
                nomineeIdentityNumber,
                relationship,
                nomineeMobileNumber,
                nomineePostalAddress
        );

        nomineesDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> {
                    nomineesGrid.asSingleSelect().clear();
                    nomineesDialog.close();
                })
        );

        nomineesDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            nomineesGrid.asSingleSelect().clear();
            nomineesDialog.close();
        });

        saveNominee.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveNominee.addClickListener(click -> {
            saveNominee.setDisableOnClick(true);
            configureNomineesForm();
        });

        removeNominee.addThemeVariants(
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY
        );

        removeNominee.getStyle().set("margin-right", "auto");

        removeNominee.addClickListener(click -> {
            removeNominee.setDisableOnClick(true);
            participantsServices.removeNominee(nomineeId);

            updateNomineesGrid();

            Notification notification = new Notification(NOMINEE_SUCCESSFULLY_REMOVED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            nomineesGrid.asSingleSelect().clear();
            nomineesDialog.close();
            removeNominee.setEnabled(true);

        });

        nomineesDialog.setHeaderTitle("Add Nominee");
        nomineesDialog.add(addNomineeForm);
        nomineesDialog.getFooter().add(removeNominee);
        nomineesDialog.getFooter().add(saveNominee);

        // References
        FormLayout addReferencesForm = new FormLayout(
                referenceFirstname,
                referenceLastname,
                referenceIdentityNumber,
                referenceMobileNumber,
                referencePostalAddress

        );

        addReferencesForm.setColspan(referenceIdentityNumber, 2);

        referenceDialog.getHeader().add(
                new Button(new Icon("lumo", "cross"), (e) -> {
                    referenceGrid.asSingleSelect().clear();
                    referenceDialog.close();
                })
        );

        referenceDialog.addDialogCloseActionListener(dialogCloseActionEvent -> {
            referenceGrid.asSingleSelect().clear();
            referenceDialog.close();
        });

        saveReference.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveReference.addClickListener(click -> {
            saveReference.setDisableOnClick(true);
            configureReferencesForm();
        });

        removeReference.addThemeVariants(
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY
        );

        removeReference.getStyle().set("margin-right", "auto");

        removeReference.addClickListener(click -> {
            removeReference.setDisableOnClick(true);
            participantsServices.removeReference(referenceId);

            updateReferenceGrid();

            Notification notification = new Notification(REFERENCE_SUCCESSFULLY_REMOVED);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
            notification.open();

            referenceGrid.asSingleSelect().clear();
            referenceDialog.close();
            removeReference.setEnabled(true);

        });

        referenceDialog.setHeaderTitle("Add Reference");
        referenceDialog.add(addReferencesForm);
        referenceDialog.getFooter().add(removeReference);
        referenceDialog.getFooter().add(saveReference);

    }

    private void configureGrids() {

        nomineesGrid.setColumns("nominee", "identityNumber",
                "relationship", "primaryMobile", "postalAddress");

        nomineesGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        referenceGrid.setColumns("reference", "identityNumber", "primaryMobile", "postalAddress");

        referenceGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void checkScreenSize() {

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            if (browserWindowResizeEvent.getWidth() > 500) {
                nomineesDialog.setWidth("70%");
            }

        });

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            if (extendedClientDetails.getScreenWidth() > 500) {
                nomineesDialog.setWidth("70%");
            }

        });

    }

    private void configureHorizontalLayouts() {
        // Names
        namesLayout = new HorizontalLayout(firstname, lastname);
        //namesLayout.setSizeFull();
        namesLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        namesLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        namesLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private void configureDate() {
        DatePicker.DatePickerI18n dateFormat = new DatePicker.DatePickerI18n();
        dateFormat.setDateFormat(SIMPLE_DATE_FORMAT);

        dateOfBirth.setI18n(dateFormat);
        dateOfBirth.setLabel(DATE_OF_BIRTH);

        placementDate.setI18n(dateFormat);
        placementDate.setLabel(PLACEMENT_DATE);

        completionDate.setI18n(dateFormat);
        completionDate.setLabel(COMPLETION_DATE);

    }

    private void configureBinder() {

        // Agents
        /*binder.forMemberField(firstname).asRequired();
        binder.forMemberField(lastname).asRequired();
        binder.forMemberField(identityNumber).asRequired();
        binder.forMemberField(gender).asRequired();
        binder.forMemberField(primaryMobile).asRequired();
        binder.forMemberField(alternateMobile).asRequired();
        binder.forMemberField(postalAddress).asRequired();
        binder.forMemberField(residentialAddress).asRequired();
        */

        agentsModelBinder.bindInstanceFields(this);

    }

    private void configureLists() {

        // TODO Configure duration and remove static value "Kgatleng"
        // Contract duration
        duration.setItems(participantsServices.getContractDuration("Kgatleng"));
        duration.setItemLabelGenerator(String::toString);

        // Marital statuses
        maritalStatus.setItems(getMaritalStatuses());
        maritalStatus.setItemLabelGenerator(String::toString);

        // Genders
        gender.setItems(getGenders());
        gender.setItemLabelGenerator(String::toString);

        // Education
        education.setItems(getEducationLevels());
        education.setItemLabelGenerator(String::toString);

        // Placement places
        placementPlace.setItems(getPlaces());
        placementPlace.setItemLabelGenerator(String::toString);

        // Positions
        position.setItems(getPositions());
        position.setItemLabelGenerator(String::toString);

        // Relationships
        relationship.setItems(getRelationships());
        relationship.setItemLabelGenerator(String::toString);

        // Payment method
        paymentMethod.setItems(getPaymentMethods());
        paymentMethod.setItemLabelGenerator(String::toString);

    }

    public void setAgent(ParticipantsModel participantsModel, String monthDate) {
        this.participantsModel = participantsModel;
        agentsModelBinder.readBean(participantsModel);

        if (participantsModel.getPlacementDate() != null && participantsModel.getCompletionDate() != null) {
            long monthsDifference = ChronoUnit.MONTHS.between(
                    participantsModel.getPlacementDate(), participantsModel.getCompletionDate());

            duration.setValue(monthsDifference+" Months");
        }
        else {
            duration.clear();
        }

        configureNullValues(participantsModel);

        participantId = participantsModel.getParticipantId();

        configureNominees();
        configureReferences();
        updateGridInfo();

        date = monthDate;

    }

    public void setNominees(NomineesModel nomineesModel) {
        this.nomineesModel = nomineesModel;
        if (nomineesModel != null) {
            readNomineeModel(nomineesModel);
            nomineeId = nomineesModel.getNomineeId();
            nomineesDialog.open();
        }
    }

    public void setReferences(ReferenceModel referenceModel) {
        this.referenceModel = referenceModel;
        if (referenceModel != null) {
            readReferencesModel(referenceModel);
            referenceId = referenceModel.getReferenceId();
            referenceDialog.open();
        }
    }

    public void disableButtons(boolean isEnabled) {
        if (isEnabled) {
            updateAddAgent.setEnabled(true);
            terminateButton.setEnabled(true);
            addNomineeButton.setEnabled(true);
            addReferenceButton.setEnabled(true);
            saveNominee.setEnabled(true);
            saveReference.setEnabled(true);
            removeReference.setEnabled(true);
            removeNominee.setEnabled(true);
        }
        else {
            updateAddAgent.setEnabled(false);
            terminateButton.setEnabled(false);
            addNomineeButton.setEnabled(false);
            addReferenceButton.setEnabled(false);
            saveNominee.setEnabled(false);
            saveReference.setEnabled(false);
            removeNominee.setEnabled(false);
            removeReference.setEnabled(false);
        }
    }

    public void hideAllComponents(boolean isHideAll) {
        if (isHideAll) {
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            education.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

        }
    }

    public void setButtonText(boolean isAddAgent) {

        if (isAddAgent) {
            updateAddAgent.setText(ADD);
            terminateButton.setVisible(false);
            nominees.setEnabled(false);
            references.setEnabled(false);

            tabs.setSelectedTab(identification);

            paymentMethod.clear();
            mobileWalletProvider.clear();
            bankName.clear();
            branch.clear();
            accountNumber.clear();
        }
        else {
            updateAddAgent.setText(UPDATE);
            terminateButton.setVisible(true);
            nominees.setEnabled(true);
            references.setEnabled(true);
        }

    }

    private void readNomineeModel(NomineesModel nomineesModel) {
        nomineeFirstname.setValue(nomineesModel.getFirstname());
        nomineeLastname.setValue(nomineesModel.getLastname());
        nomineeIdentityNumber.setValue(nomineesModel.getIdentityNumber());
        relationship.setValue(nomineesModel.getRelationship());
        nomineeMobileNumber.setValue(nomineesModel.getPrimaryMobile());
        nomineePostalAddress.setValue(nomineesModel.getPostalAddress());
    }

    private void readReferencesModel(ReferenceModel referenceModel) {
        referenceFirstname.setValue(referenceModel.getFirstname());
        referenceLastname.setValue(referenceModel.getLastname());
        referenceIdentityNumber.setValue(referenceModel.getIdentityNumber());
        referenceMobileNumber.setValue(referenceModel.getPrimaryMobile());
        referencePostalAddress.setValue(referenceModel.getPostalAddress());
    }

    private void configureNominees() {

        agentNomineesList = new ArrayList<>();

        for (int i = 0; i < allNomineesList.size(); i++) {
            if (allNomineesList.get(i).getParticipantId() == participantId) {
                agentNomineesList.add(
                        new NomineesModel(
                                allNomineesList.get(i).getNomineeId(),
                                allNomineesList.get(i).getParticipantId(),
                                allNomineesList.get(i).getFirstname(),
                                allNomineesList.get(i).getLastname(),
                                allNomineesList.get(i).getIdentityNumber(),
                                allNomineesList.get(i).getRelationship(),
                                allNomineesList.get(i).getPrimaryMobile(),
                                allNomineesList.get(i).getPostalAddress(),
                                allNomineesList.get(i).getFirstname()
                                        +" "+allNomineesList.get(i).getLastname()
                        )
                );
            }
        }

    }

    private void configureReferences() {

        agentReferencesList = new ArrayList<>();

        for (int i = 0; i < allReferencesList.size(); i++) {
            if (allReferencesList.get(i).getParticipantId() == participantId) {
                agentReferencesList.add(
                        new ReferenceModel(
                                allReferencesList.get(i).getReferenceId(),
                                allReferencesList.get(i).getParticipantId(),
                                allReferencesList.get(i).getFirstname(),
                                allReferencesList.get(i).getLastname(),
                                allReferencesList.get(i).getIdentityNumber(),
                                allReferencesList.get(i).getPrimaryMobile(),
                                allReferencesList.get(i).getPostalAddress(),
                                allReferencesList.get(i).getFirstname()
                                        +" "+allReferencesList.get(i).getLastname()
                        )
                );
            }
        }
    }

    private void validateFields() {

        if (firstname.isEmpty()) {
            firstname.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (lastname.isEmpty()) {
            lastname.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (identityNumber.isEmpty()) {
            identityNumber.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (dateOfBirth.isEmpty()) {
            dateOfBirth.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (gender.getValue() == null) {
            gender.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (maritalStatus.getValue() == null) {
            maritalStatus.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (mobileNumber.isEmpty()) {
            mobileNumber.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (postalAddress.isEmpty()) {
            postalAddress.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (residentialAddress.isEmpty()) {
            residentialAddress.setInvalid(true);
            tabs.setSelectedTab(identification);
            updateAddAgent.setEnabled(true);
        }
        else if (education.getValue() == null) {
            education.setInvalid(true);
            tabs.setSelectedTab(educationTab);
            updateAddAgent.setEnabled(true);
        }
        else if (placementOfficer.isEmpty()) {
            placementOfficer.setInvalid(true);
            tabs.setSelectedTab(service);
            updateAddAgent.setEnabled(true);
        }
        else if (placementPlace.getValue() == null) {
            placementPlace.setInvalid(true);
            tabs.setSelectedTab(service);
            updateAddAgent.setEnabled(true);
        }
        else if (position.getValue() == null) {
            position.setInvalid(true);
            tabs.setSelectedTab(service);
            updateAddAgent.setEnabled(true);
        }
        else if (placementDate.isEmpty()) {
            placementDate.setInvalid(true);
            tabs.setSelectedTab(service);
            updateAddAgent.setEnabled(true);
        }
        else if (completionDate.isEmpty()) {
            completionDate.setInvalid(true);
            tabs.setSelectedTab(service);
            updateAddAgent.setEnabled(true);
        }
        else if (paymentMethod.getValue() == null) {
            paymentMethod.setInvalid(true);
            tabs.setSelectedTab(banking);
            updateAddAgent.setEnabled(true);
        }
        else if (paymentMethod.getValue().equalsIgnoreCase(MOBILE_WALLET)
                && mobileWalletProvider.isEmpty()) {
            mobileWalletProvider.setInvalid(true);
            tabs.setSelectedTab(banking);
            updateAddAgent.setEnabled(true);
        }
        else if (paymentMethod.getValue().equalsIgnoreCase(BANK_EFT)
                && bankName.isEmpty()) {
            bankName.setInvalid(true);
            tabs.setSelectedTab(banking);
            updateAddAgent.setEnabled(true);
        }
        else if (paymentMethod.getValue().equalsIgnoreCase(BANK_EFT)
                && branch.isEmpty()) {
            branch.setInvalid(true);
            tabs.setSelectedTab(banking);
            updateAddAgent.setEnabled(true);
        }
        else if (paymentMethod.getValue().equalsIgnoreCase(BANK_EFT)
                && accountNumber.isEmpty()) {
            accountNumber.setInvalid(true);
            tabs.setSelectedTab(banking);
            updateAddAgent.setEnabled(true);
        }
        else {
            if (updateAddAgent.getText().equalsIgnoreCase(ADD)) {
                addAgent();
            }
            else if (updateAddAgent.getText().equalsIgnoreCase(UPDATE)) {
                updateAgentDetails();
            }
        }

    }

    public void showTab(String label) {

        selectedTabLabel = label;

        if (label.equalsIgnoreCase(IDENTIFICATION)) {

            // Showing
            firstname.setVisible(true);
            lastname.setVisible(true);
            identityNumber.setVisible(true);
            dateOfBirth.setVisible(true);
            gender.setVisible(true);
            maritalStatus.setVisible(true);
            mobileNumber.setVisible(true);
            alternateMobileNumber.setVisible(true);
            postalAddress.setVisible(true);
            residentialAddress.setVisible(true);

            // Hiding
            education.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

        }
        else if (label.equalsIgnoreCase(EDUCATION)) {

            // Showing
            education.setVisible(true);

            // Hiding
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

        }
        else if (label.equalsIgnoreCase(SERVICE)) {

            // Showing
            placementOfficer.setVisible(true);
            placementPlace.setVisible(true);
            position.setVisible(true);
            duration.setVisible(true);
            placementDate.setVisible(true);
            completionDate.setVisible(true);

            // Hiding
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            education.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

        }
        else if (label.equalsIgnoreCase(BANKING)) {

            // Showing
            paymentMethod.setVisible(true);


            // Hiding
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            education.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

            if (paymentMethod.getValue() != null
                    && !paymentMethod.getValue().isEmpty() && paymentMethod != null) {
                String payment = paymentMethod.getValue();

                if (payment.equalsIgnoreCase(CASH)) {
                    mobileWalletProvider.setVisible(false);
                    bankName.setVisible(false);
                    branch.setVisible(false);
                    accountNumber.setVisible(false);
                }
                else if (payment.equalsIgnoreCase(MOBILE_WALLET)) {
                    mobileWalletProvider.setVisible(true);

                    bankName.setVisible(false);
                    branch.setVisible(false);
                    accountNumber.setVisible(false);
                }
                else if (payment.equalsIgnoreCase(BANK_EFT)) {
                    mobileWalletProvider.setVisible(false);

                    bankName.setVisible(true);
                    branch.setVisible(true);
                    accountNumber.setVisible(true);
                }
            }

        }
        else if (label.equalsIgnoreCase(NOMINEES)) {

            // Showing
            addButtonGridNomineeLayout.setVisible(true);

            // Hiding
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            education.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridReferenceLayout.setVisible(false);

        }
        else if (label.equalsIgnoreCase(REFERENCES)) {

            // Showing
            addButtonGridReferenceLayout.setVisible(true);

            // Hiding
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            dateOfBirth.setVisible(false);
            gender.setVisible(false);
            maritalStatus.setVisible(false);
            mobileNumber.setVisible(false);
            alternateMobileNumber.setVisible(false);
            postalAddress.setVisible(false);
            residentialAddress.setVisible(false);

            education.setVisible(false);

            placementOfficer.setVisible(false);
            placementPlace.setVisible(false);
            position.setVisible(false);
            duration.setVisible(false);
            placementDate.setVisible(false);
            completionDate.setVisible(false);

            paymentMethod.setVisible(false);
            mobileWalletProvider.setVisible(false);
            bankName.setVisible(false);
            branch.setVisible(false);
            accountNumber.setVisible(false);

            addButtonGridNomineeLayout.setVisible(false);

        }
    }

    private void configureTabs() {
        tabs = new Tabs(identification, educationTab, service, banking, nominees, references);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        tabs.setWidthFull();

    }

    private void configureButtons() {

        addReferenceButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SMALL
        );

        addReferenceButton.addClickListener(e -> {
            referenceDialog.open();
            referenceFirstname.clear();
            referenceLastname.clear();
            referenceIdentityNumber.clear();
            referenceMobileNumber.clear();
            referencePostalAddress.clear();

            isUpdateReference = false;

            saveReference.setText(ADD);

            removeReference.setVisible(false);

        });

        addNomineeButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SMALL
        );

        addNomineeButton.addClickListener(e -> {
            nomineesDialog.open();
            nomineeFirstname.clear();
            nomineeLastname.clear();
            nomineeIdentityNumber.clear();
            relationship.clear();
            nomineeMobileNumber.clear();
            nomineePostalAddress.clear();

            relationship.setItems(getRelationships());

            isUpdateNominee = false;

            saveNominee.setText(ADD);

            removeNominee.setVisible(false);

        });

        updateButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        updateButton.addClickListener(click -> {
            if (daysWorked.getValue() != null && !daysWorked.isInvalid()) {
                participantsServices.updateAttendance(
                        daysWorked.getValue(),
                        participantsModel.getParticipantId(),
                        date
                );

                fireEvent(new AgentDaysWorkedUpdatedEvent(this));

            }
        });

        attendanceBackButton.addThemeVariants(
                ButtonVariant.LUMO_CONTRAST
        );

        attendanceBackButton.addClickListener(click
                -> fireEvent(new CloseAttendanceFormEvent(this)));

        updateAddAgent.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        updateAddAgent.addClickListener(click -> validateFields());

        backButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST
        );

        backButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        terminateButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR
        );

        terminateButton.addClickListener(click -> {
            participantsServices.terminateAgent(participantId);

            fireEvent(new AgentTerminatedEvent(this));
        });

        buttonsLayout = new HorizontalLayout(updateButton, attendanceBackButton);
        addAgentButtonsLayout = new HorizontalLayout(updateAddAgent, backButton, terminateButton);
        addAgentButtonsLayout.getStyle()
                .set("margin-bottom", "var(--lumo-space-xl");

    }

    private void configureNomineesForm() {

        if (relationship.getValue() != null) {
            relationship.setInvalid(false);
        }

        if (nomineeFirstname.getValue().isEmpty()) {
            nomineeFirstname.setInvalid(true);
            Notification notification = new Notification("Enter firstname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);

        }
        else if (nomineeLastname.getValue().isEmpty()) {
            nomineeLastname.setInvalid(true);
            Notification notification = new Notification("Enter lastname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);
        }
        else if (nomineeIdentityNumber.getValue().isEmpty()) {
            nomineeIdentityNumber.setInvalid(true);
            Notification notification = new Notification("Enter identity number");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);
        }
        else if (relationship.getValue() == null) {
            relationship.setInvalid(true);
            Notification notification = new Notification("Enter relationship");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);
        }
        else if (nomineeMobileNumber.getValue().isEmpty()) {
            nomineeMobileNumber.setInvalid(true);
            Notification notification = new Notification("Enter mobile number");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);
        }
        else if (nomineePostalAddress.getValue().isEmpty()) {
            nomineePostalAddress.setInvalid(true);
            Notification notification = new Notification("Enter postal address");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveNominee.setEnabled(true);
        }
        else {

            if (isUpdateNominee) {
                String response = participantsServices.updateNominee(new NomineesModel(
                        nomineeId,
                        participantId,
                        nomineeFirstname.getValue(),
                        nomineeLastname.getValue(),
                        nomineeIdentityNumber.getValue(),
                        relationship.getValue(),
                        nomineeMobileNumber.getValue(),
                        nomineePostalAddress.getValue()
                ));

                if (response.equalsIgnoreCase(NOMINEE_ALREADY_EXIST)) {
                    Notification notification = new Notification(NOMINEE_ALREADY_EXIST);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();

                }
                else {
                    updateNomineesGrid();

                    Notification notification = new Notification(NOMINEE_SUCCESSFULLY_UPDATED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();

                }

            }
            else {
                String response = participantsServices.addNominee(new NomineesModel(
                        nomineeId,
                        participantId,
                        nomineeFirstname.getValue(),
                        nomineeLastname.getValue(),
                        nomineeIdentityNumber.getValue(),
                        relationship.getValue(),
                        nomineeMobileNumber.getValue(),
                        nomineePostalAddress.getValue()
                ));

                if (response.equalsIgnoreCase(NOMINEE_ALREADY_EXIST)) {
                    Notification notification = new Notification(NOMINEE_ALREADY_EXIST);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();

                }
                else {
                    updateNomineesGrid();

                    Notification notification = new Notification(NOMINEE_SUCCESSFULLY_ADDED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();

                }
            }

            nomineesGrid.asSingleSelect().clear();
            nomineesDialog.close();
            saveNominee.setEnabled(true);

        }

    }

    private void configureReferencesForm() {

        if (referenceFirstname.getValue().isEmpty()) {
            referenceFirstname.setInvalid(true);
            Notification notification = new Notification("Enter firstname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveReference.setEnabled(true);

        }
        else if (referenceLastname.getValue().isEmpty()) {
            referenceLastname.setInvalid(true);
            Notification notification = new Notification("Enter lastname");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveReference.setEnabled(true);
        }
        else if (referenceIdentityNumber.getValue().isEmpty()) {
            referenceIdentityNumber.setInvalid(true);
            Notification notification = new Notification("Enter identity number");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveReference.setEnabled(true);
        }
        else if (referenceMobileNumber.getValue().isEmpty()) {
            referenceMobileNumber.setInvalid(true);
            Notification notification = new Notification("Enter mobile number");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveReference.setEnabled(true);
        }
        else if (referencePostalAddress.getValue().isEmpty()) {
            referencePostalAddress.setInvalid(true);
            Notification notification = new Notification("Enter postal address");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

            saveReference.setEnabled(true);
        }
        else {

            if (isUpdateReference) {
                String response = participantsServices.updateReference(new ReferenceModel(
                        referenceId,
                        participantId,
                        referenceFirstname.getValue(),
                        referenceLastname.getValue(),
                        referenceIdentityNumber.getValue(),
                        referenceMobileNumber.getValue(),
                        referencePostalAddress.getValue()
                ));

                if (response.equalsIgnoreCase(REFERENCE_ALREADY_EXIST)) {
                    Notification notification = new Notification(REFERENCE_ALREADY_EXIST);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();

                }
                else {
                    updateReferenceGrid();

                    Notification notification = new Notification(REFERENCE_SUCCESSFULLY_UPDATED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();

                }

            }
            else {
                String response = participantsServices.addReference(new ReferenceModel(
                        referenceId,
                        participantId,
                        referenceFirstname.getValue(),
                        referenceLastname.getValue(),
                        referenceIdentityNumber.getValue(),
                        referenceMobileNumber.getValue(),
                        referencePostalAddress.getValue()
                ));

                if (response.equalsIgnoreCase(REFERENCE_ALREADY_EXIST)) {
                    Notification notification = new Notification(REFERENCE_ALREADY_EXIST);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5000);
                    notification.open();

                }
                else {
                    updateReferenceGrid();

                    Notification notification = new Notification(REFERENCE_SUCCESSFULLY_ADDED);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setDuration(5000);
                    notification.open();

                }
            }

            referenceGrid.asSingleSelect().clear();
            referenceDialog.close();
            saveReference.setEnabled(true);

        }

    }

    private void addAgent() {

        paymentMethod.setInvalid(false);
        updateAddAgent.setDisableOnClick(true);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        String dateOfBirth_str = dateOfBirth.getValue().format(dateTimeFormatter);
        String[] getDateOfBirth = dateOfBirth_str.split("-");

        LocalDate dateOfBirthLocalDate = LocalDate.of(
                Integer.parseInt(getDateOfBirth[2]),
                Integer.parseInt(getDateOfBirth[1]),
                Integer.parseInt(getDateOfBirth[0])
        );

        String placementDate_str = placementDate.getValue().format(dateTimeFormatter);
        String[] getPlacementDate = placementDate_str.split("-");

        LocalDate placementDateLocalDate = LocalDate.of(
                Integer.parseInt(getPlacementDate[2]),
                Integer.parseInt(getPlacementDate[1]),
                Integer.parseInt(getPlacementDate[0])
        );

        String completionDate_str = completionDate.getValue().format(dateTimeFormatter);
        String[] getCompletionDate = completionDate_str.split("-");

        LocalDate completionDateLocalDate = LocalDate.of(
                Integer.parseInt(getCompletionDate[2]),
                Integer.parseInt(getCompletionDate[1]),
                Integer.parseInt(getCompletionDate[0])
        );

        String alternateMobileNumberValue;
        if (alternateMobileNumber.isEmpty()) {
            alternateMobileNumberValue = "null";
        }
        else {
            alternateMobileNumberValue = alternateMobileNumber.getValue();
        }

        String mobileWalletProviderValue;
        if (mobileWalletProvider.isEmpty()) {
            mobileWalletProviderValue = "null";
        }
        else {
            mobileWalletProviderValue = mobileWalletProvider.getValue();
        }

        String bankNameValue;
        if (bankName.isEmpty()) {
            bankNameValue = "null";
        }
        else {
            bankNameValue = bankName.getValue();
        }

        String branchValue;
        if (branch.isEmpty()) {
            branchValue = "null";
        }
        else {
            branchValue = branch.getValue();
        }

        String accountNumberValue;
        if (accountNumber.isEmpty()) {
            accountNumberValue = "null";
        }
        else {
            accountNumberValue = accountNumber.getValue();
        }


        String response = participantsServices.addAgent(new ParticipantsModel(
                LocalDateTime.now(),
                dateOfBirthLocalDate,
                placementDateLocalDate,
                completionDateLocalDate,
                firstname.getValue(),
                lastname.getValue(),
                identityNumber.getValue(),
                gender.getValue(),
                maritalStatus.getValue(),
                mobileNumber.getValue(),
                alternateMobileNumberValue,
                postalAddress.getValue(),
                residentialAddress.getValue(),
                education.getValue(),
                placementOfficer.getValue(),
                placementPlace.getValue(),
                position.getValue(),
                mobileWalletProviderValue,
                bankNameValue,
                branchValue,
                accountNumberValue,

                // TODO Configure district and village and service
                "Kgatleng",
                "Oodi",
                IPELEGENG

        ),
                contractDates
        );


        updateAddAgent.setEnabled(true);

        if (response.equalsIgnoreCase(PARTICIPANT_ALREADY_EXIST)) {
            Notification notification = new Notification(PARTICIPANT_ALREADY_EXIST);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (response.equalsIgnoreCase(SUCCESSFUL)) {
            fireEvent(new AgentAddedEvent(this));
        }
    }

    private void updateAgentDetails() {
        updateAddAgent.setDisableOnClick(true);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        String dateOfBirth_str = dateOfBirth.getValue().format(dateTimeFormatter);
        String[] getDateOfBirth = dateOfBirth_str.split("-");

        LocalDate dateOfBirthLocalDate = LocalDate.of(
                Integer.parseInt(getDateOfBirth[2]),
                Integer.parseInt(getDateOfBirth[1]),
                Integer.parseInt(getDateOfBirth[0])
        );

        String placementDate_str = placementDate.getValue().format(dateTimeFormatter);
        String[] getPlacementDate = placementDate_str.split("-");

        LocalDate placementDateLocalDate = LocalDate.of(
                Integer.parseInt(getPlacementDate[2]),
                Integer.parseInt(getPlacementDate[1]),
                Integer.parseInt(getPlacementDate[0])
        );

        String completionDate_str = completionDate.getValue().format(dateTimeFormatter);
        String[] getCompletionDate = completionDate_str.split("-");

        LocalDate completionDateLocalDate = LocalDate.of(
                Integer.parseInt(getCompletionDate[2]),
                Integer.parseInt(getCompletionDate[1]),
                Integer.parseInt(getCompletionDate[0])
        );

        String alternateMobileNumberValue;
        if (alternateMobileNumber.isEmpty()) {
            alternateMobileNumberValue = "null";
        }
        else {
            alternateMobileNumberValue = alternateMobileNumber.getValue();
        }

        String mobileWalletProviderValue;
        if (mobileWalletProvider.isEmpty()) {
            mobileWalletProviderValue = "null";
        }
        else {
            mobileWalletProviderValue = mobileWalletProvider.getValue();
        }

        String bankNameValue;
        if (bankName.isEmpty()) {
            bankNameValue = "null";
        }
        else {
            bankNameValue = bankName.getValue();
        }

        String branchValue;
        if (branch.isEmpty()) {
            branchValue = "null";
        }
        else {
            branchValue = branch.getValue();
        }

        String accountNumberValue;
        if (accountNumber.isEmpty()) {
            accountNumberValue = "null";
        }
        else {
            accountNumberValue = accountNumber.getValue();
        }


        String response = participantsServices.updateAgent(new ParticipantsModel(
                participantId,
                LocalDateTime.now(),
                dateOfBirthLocalDate,
                placementDateLocalDate,
                completionDateLocalDate,
                firstname.getValue(),
                lastname.getValue(),
                identityNumber.getValue(),
                gender.getValue(),
                maritalStatus.getValue(),
                mobileNumber.getValue(),
                alternateMobileNumberValue,
                postalAddress.getValue(),
                residentialAddress.getValue(),
                education.getValue(),
                placementOfficer.getValue(),
                placementPlace.getValue(),
                position.getValue(),
                mobileWalletProviderValue,
                bankNameValue,
                branchValue,
                accountNumberValue,

                // TODO Configure district and village and service
                "Kgatleng",
                "Oodi",
                IPELEGENG

        ));


        updateAddAgent.setEnabled(true);

        if (response.equalsIgnoreCase(PARTICIPANT_ALREADY_EXIST)) {
            Notification notification = new Notification(PARTICIPANT_ALREADY_EXIST);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();

        }
        else if (response.equalsIgnoreCase(SUCCESSFUL)) {
            fireEvent(new AgentUpdatedEvent(this));
        }
    }

    public void changeLayout(boolean isAttendanceShowing) {

        if (isAttendanceShowing) {
            daysWorked.setVisible(true);
            buttonsLayout.setVisible(true);

            tabs.setVisible(false);
            firstname.setVisible(false);
            lastname.setVisible(false);
            identityNumber.setVisible(false);
            gender.setVisible(false);
            postalAddress.setVisible(false);
            alternateMobileNumber.setVisible(false);
            mobileNumber.setVisible(false);
            addAgentButtonsLayout.setVisible(false);
        }
        else {
            daysWorked.setVisible(false);
            buttonsLayout.setVisible(false);

            tabs.setVisible(true);
            firstname.setVisible(true);
            lastname.setVisible(true);
            identityNumber.setVisible(true);
            gender.setVisible(true);
            postalAddress.setVisible(true);
            alternateMobileNumber.setVisible(true);
            mobileNumber.setVisible(true);
            addAgentButtonsLayout.setVisible(true);
        }
    }

    public void resetTabs() {
        tabs.setSelectedTab(identification);
    }

    // Events
    public static abstract class AddAgentFormEvent extends ComponentEvent<ParticipantsForm> {

        private ParticipantsModel participantsModel;

        protected AddAgentFormEvent(ParticipantsForm source, ParticipantsModel participantsModel) {
            super(source, false);
            this.participantsModel = participantsModel;
        }

        public ParticipantsModel getAgents() {
            return participantsModel;
        }

    }

    public static class AgentUpdatedEvent extends AddAgentFormEvent {
        AgentUpdatedEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public static class AgentDaysWorkedUpdatedEvent extends AddAgentFormEvent {
        AgentDaysWorkedUpdatedEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public static class AgentTerminatedEvent extends AddAgentFormEvent {
        AgentTerminatedEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public static class CloseEvent extends AddAgentFormEvent {
        CloseEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public static class CloseAttendanceFormEvent extends AddAgentFormEvent {
        CloseAttendanceFormEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public static class AgentAddedEvent extends AddAgentFormEvent {
        AgentAddedEvent(ParticipantsForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

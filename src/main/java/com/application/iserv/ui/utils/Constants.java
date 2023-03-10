package com.application.iserv.ui.utils;

import com.application.iserv.ui.participants.models.TestAgentsModel;
import com.application.iserv.ui.parameters.models.ParametersModel;
import com.application.iserv.ui.payments.models.AgentPaymentsModel;
import com.application.iserv.ui.payments.models.HistoryStatementModel;

import java.util.List;

public class Constants {

    // Strings
    public static final String GENDER = "Gender";
    public static final String MARITAL_STATUS = "Marital Status";
    public static final String FIRSTNAME = "Firstname";
    public static final String LASTNAME = "Lastname";
    public static final String ID_NUMBER = "ID Number";
    public static final String PRIMARY_MOBILE = "Primary Mobile";
    public static final String ALTERNATE_MOBILE = "Alternate Mobile";
    public static final String POSTAL_ADDRESS = "Postal Address";
    public static final String RESIDENTIAL_ADDRESS = "Residential Address";
    public static final String PLACEMENT_OFFICER = "Placement Officer";
    public static final String IDENTIFICATION = "Identification";
    public static final String EDUCATION = "Education";
    public static final String PLACEMENT_PLACE = "Placement Place";
    public static final String SERVICE_UPPER_CASE = "Service";
    public static final String BANKING = "Banking";
    public static final String NOMINEES = "Nominees";
    public static final String REFERENCES = "References";
    public static final String EMPLOYEES = "Employees";
    public static final String PAYMENTS = "Payments";
    public static final String PARAMETERS = "Parameters";
    public static final String REPORTS = "Reports";
    public static final String ADD_EMPLOYEE = "Add employee";
    public static final String SAVE = "Save";
    public static final String BACK = "Back";
    public static final String UPDATE = "Update";
    public static final String UPLOAD = "Upload";
    public static final String UPDATED = "Updated";
    public static final String ADD = "Add";
    public static final String ADD_PARAMETER = "Add Parameter";
    public static final String REMOVE = "Remove";

    public static final String TERMINATE = "Terminate";
    public static final String PLACEMENT_DATE = "Placement Date";
    public static final String COMPLETION_DATE = "Completion Date";
    public static final String FEMALE = "Female";
    public static final String MALE = "Male";
    public static final String MARRIED = "Married";
    public static final String DIVORCED = "Divorced";
    public static final String BACHELOR = "Bachelor";
    public static final String BANK_NAME = "Bank Name";
    public static final String BRANCH = "Branch";
    public static final String ACCOUNT_NUMBER = "Account Number";

    public static final String NON_BINARY = "Non - Binary";
    public static final String SEARCH_EMPLOYEE_HINT = "Search employee...";
    public static final String ACTIVE = "Active";
    public static final String TERMINATED = "Terminated";
    public static final String EXPIRED = "Expired";
    public static final String STATUS = "Status";
    public static final String TOTAL_NET_CAMEL_CASE = "totalNet";
    public static final String TOTAL_NET = "Total Net";
    public static final String CONTRACT_STATUS = "Contract Status";
    public static final String AGENT_POSITION = "agent_position";
    public static final String SIMPLE_DATE_FORMAT = "dd MMMM yyyy";
    public static final String SIMPLE_MONTH_DATE_FORMAT = "MMMM yyyy";
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String MONTH_DATE_FORMAT = "yyyy-MM";
    public static final String DATE = "Date";
    public static final String DATE_OF_BIRTH = "Date Of Birth";
    public static final String AGENT_ATTENDANCE = "agent_attendance";
    public static final String EMPLOYEE = "employee";
    public static final String POSITION = "position";
    public static final String RELATIONSHIP = "Relationship";
    public static final String CONTRACT_DURATION = "Contract Duration";
    public static final String PAYMENT_METHOD = "Payment Method";
    public static final String POSITION_UPPER_CASE = "Position";
    public static final String DAILY_RATE_PER_DAY_CAMEL_CASE = "Daily Rate / Day";
    public static final String DURATION = "Duration";
    public static final String ATTENDANCE_LOWER_CASE = "attendance";
    public static final String ATTENDANCE = "Attendance";
    public static final String START_TIME = "Start Time";
    public static final String END_TIME = "End Time";
    public static final String HALF_DAY = "Half Day";
    public static final String PRESENT = "Present";
    public static final String ABSENT = "Absent";
    public static final String EXPORT_STATEMENTS = "Export Statements";
    public static final String RATE = "Rate/day";
    public static final String RATE_PER_DAY = "Daily Rate Per Day";
    public static final String DAYS_WORKED_CAMEL_CASE= "daysWorked";
    public static final String DAYS_WORKED = "Days Worked";
    public static final String BASE_SALARY = "Base Salary";
    public static final String BONUS = "Bonus";
    public static final String DEDUCTION = "Deduction";
    public static final String TOTAL_SALARY = "Total Salary";
    public static final String APPROVAL = "Approval";
    public static final String CLAIMED = "Claimed";
    public static final String PAYMENT_MODE = "Payment Mode";
    public static final String PROVIDER = "Provider";
    public static final String AUTHORIZE = "Authorize";
    public static final String RECONCILE = "Reconcile";
    public static final String HISTORY = "History";
    public static final String AUTHORIZE_LOWER_CASE = "authorize";
    public static final String RECONCILE_LOWER_CASE = "reconcile";
    public static final String HISTORY_LOWER_CASE = "history";
    public static final String EMPLOYEES_LOWER_CASE = "employees";
    public static final String CSV_ERROR_MESSAGE = "Upload a csv file.";
    public static final String CSV_FORMAT = ".csv";
    public static final String DATE_TO_RECONCILE = "Select date to reconcile";
    public static final String AMOUNT = "amount";
    public static final String CAPS_AMOUNT = "Amount";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String APPROVED = "Approved";
    public static final String DENIED = "Denied";
    public static final String PENDING = "Pending";
    public static final String ON_HOLD = "On hold";
    public static final String REASON = "Reason";
    public static final String REASON_FOR_BONUS = "Reason for bonus";
    public static final String REASON_FOR_DEDUCTION = "Reason for deduction";
    public static final String APPROVE = "Approve";
    public static final String DENY = "Deny";
    public static final String HOLD = "Hold";
    public static final String PEND = "Pend";
    public static final String SELECT_STATUS = "Select Status";
    public static final String IPELEGENG = "Ipelegeng";
    public static final String TIRELO_SECHABA = "Tirelo Sechaba";
    public static final String PRIMARY = "Primary";
    public static final String SECONDARY = "Secondary";
    public static final String CERTIFICATE = "Certificate";
    public static final String DIPLOMA = "Diploma";
    public static final String DEGREE = "Degree";
    public static final String OTHER = "Other";
    public static final String SUPERVISOR = "Supervisor";
    public static final String FIELD_AGENT = "Field Agent";
    public static final String CHILD = "Child";
    public static final String FATHER = "Father";
    public static final String MOTHER = "Mother";
    public static final String COUSIN = "Cousin";
    public static final String FRIEND = "Friend";
    public static final String SPOUSE = "Spouse";
    public static final String GRANDMOTHER = "Grandmother";
    public static final String SISTER = "Sister";
    public static final String CASH = "Cash";
    public static final String MOBILE_WALLET = "Mobile Wallet";
    public static final String BANK_EFT = "Bank / EFT";
    public static final String SUCCESSFUL = "Successful";
    public static final String NOMINEE_ALREADY_EXIST = "Nominee already exist";
    public static final String PARAMETER_ALREADY_EXIST = "Parameter already exist";
    public static final String PARAMETER_SUCCESSFULLY_ADDED = "Parameter successfully added";
    public static final String PARAMETER_SUCCESSFULLY_UPDATED = "Parameter successfully updated";
    public static final String PARTICIPANT_ALREADY_EXIST = "Participant already exist";
    public static final String PARTICIPANT_SUCCESSFULLY_UPDATED = "Participant successfully updated";
    public static final String SUCCESSFULLY_APPROVED_ALL = "Successfully approved all";
    public static final String PARTICIPANT_SUCCESSFULLY_TERMINATED = "Participant successfully terminated";
    public static final String PARTICIPANT_SUCCESSFULLY_ADDED = "Participant successfully added";
    public static final String REFERENCE_ALREADY_EXIST = "Reference already exist";
    public static final String NOMINEE_SUCCESSFULLY_UPDATED = "Nominee Successfully Updated";
    public static final String REFERENCE_SUCCESSFULLY_UPDATED = "Reference Successfully Updated";
    public static final String NOMINEE_SUCCESSFULLY_ADDED = "Nominee Successfully Added";
    public static final String REFERENCE_SUCCESSFULLY_ADDED = "Reference Successfully Added";
    public static final String NOMINEE_SUCCESSFULLY_REMOVED = "Nominee Successfully Removed";
    public static final String PARAMETER_SUCCESSFULLY_REMOVED = "Parameter Successfully Removed";
    public static final String REFERENCE_SUCCESSFULLY_REMOVED = "Reference Successfully Removed";
    public static final String APPLICATION_USER = "application_user";
    public static final String USER_NAME = "user name";
    public static final String DISTRICT = "district";
    public static final String VILLAGE = "village";
    public static final String SERVICE = "service";



    // Class names
    public static final String ADDING_AGENT = "adding-agent";
    public static final String EDITING_AGENTS = "editing-agents";
    public static final String EDITING_ATTENDANCE = "editing-attendance";
    public static final String AGENTS_LIST_VIEW = "agents-list-view";
    public static final String AGENTS_FORM = "agents-form";
    public static final String AGENTS_GRID = "agents-grid";
    public static final String ATTENDANCE_BUTTON_CLICKED = "attendance-button-clicked";
    public static final String ATTENDANCE_BUTTON = "attendance-button";
    public static final String AGENTS_CONTENT_LAYOUT = "agents-content-layout";
    public static final String SEARCH_AGENT = "search-agent";
    public static final String STATUS_ATTENDANCE_LAYOUT = "status-attendance-layout";
    public static final String TOOLBAR = "toolbar";
    public static final String VERTICAL_TOOLBAR = "vertical-toolbar";
    public static final String HORIZONTAL_TOOLBAR = "horizontal-toolbar";
    public static final String VIEWING_HISTORY = "viewing-history";
    public static final String HISTORY_PAYMENTS_VIEW = "history-payments-view";
    public static final String PAYMENTS_HISTORY_GRID = "payments-history-grid";
    public static final String SEARCH_DATE_TIME_LAYOUT = "search-date-time-layout";
    public static final String AUTHORIZE_PAYMENTS_VIEW = "authorize-payments-view";
    public static final String PAYMENTS_AUTHORIZE_GRID = "payments-authorize-grid";
    public static final String VIEWING_AUTHORIZE = "viewing-authorize";
    public static final String SEARCH_DATE_MENU_LAYOUT = "search-date-menu-layout";
    public static final String SLIT_LAYOUT_1 = "slit-layout1";
    public static final String MENU_SLIT_LAYOUT = "menu-slit-layout";
    public static final String PARTICIPANT_OPEN = "participant-open";
    public static final String TOOLBAR_VERTICAL_LAYOUT = "toolbar-vertical-layout";


    // Badges
    public static final String BADGE_SUCCESSFUL = "badge success";
    public static final String BADGE_ERROR = "badge error";
    public static final String BADGE_CONTRAST = "badge contrast";
    public static final String BADGE = "badge";


    // Dimensions
    public static final String EM_30 = "30em";


    // Lists
    public static List<String> getGenders() {
        return List.of(
                FEMALE,
                MALE,
                NON_BINARY
        );
    }

    public static List<String> getEducationLevels() {
        return List.of(
                PRIMARY,
                SECONDARY,
                CERTIFICATE,
                DIPLOMA,
                DEGREE,
                OTHER
        );
    }

    public static List<String> getPlaces() {
        return List.of(
                "Oodi",
                "Modipane",
                "Mochudi",
                "Lobatse",
                "Bokaa",
                "Pilane"
        );
    }

    public static List<String> getPositions() {
        return List.of(
                SUPERVISOR,
                FIELD_AGENT
        );
    }

    public static List<String> getRelationships() {
        return List.of(
                CHILD,
                FATHER,
                MOTHER,
                COUSIN,
                FRIEND,
                SPOUSE,
                GRANDMOTHER,
                SISTER
        );
    }

    public static List<String> getPaymentMethods() {
        return List.of(
                CASH,
                MOBILE_WALLET,
                BANK_EFT
        );
    }

    public static List<String> getMaritalStatuses() {
        return List.of(
                BACHELOR,
                DIVORCED,
                MARRIED
        );
    }

    public static List<String> getStatuses() {
        return List.of(
                APPROVE,
                DENY,
                HOLD,
                PEND
        );
    }

   /* public static List<ReferenceModel> getReferences() {

        ReferenceModel reference1 = new ReferenceModel(
                "James Hart",
                "261613515",
                "26775147354",
                "PO Box 4321 Gaborone"
        );

        ReferenceModel reference2 = new ReferenceModel(
                "Mike Smith",
                "254716255",
                "26771475694",
                "PO Box 8854 Gaborone"
        );

        ReferenceModel reference3 = new ReferenceModel(
                "Jane Smith",
                "9746294731",
                "26718691354",
                "PO Box 1527 Gaborone"
        );

        return List.of(
                reference1,
                reference2,
                reference3
        );
    }

*/

    // Testing Lists
    public static List<TestAgentsModel> getTestAgents() {

        TestAgentsModel user1 = new TestAgentsModel(
                "Joe Doe",
                "Supervisor",
                "Present",
                "Joe",
                "Doe",
                "777738289",
                "Male",
                "72652562",
                "74503837",
                "PO Box 0000 Gaborone",
                "Mowana 123 st"
        );

        TestAgentsModel user2 = new TestAgentsModel(
                "Karabo Leseding",
                "Field Agent",
                "Absent",
                "Karabo",
                "Leseding",
                "874627338",
                "Male",
                "76251667",
                "",
                "PO Box 1234 Gaborone",
                "Palm 223 st"
        );

        TestAgentsModel user3 = new TestAgentsModel(
                "Katlego Metsi",
                "Field Agent",
                "Half Day",
                "Katlego",
                "Metsi",
                "93287658",
                "Female",
                "78132651",
                "71763883",
                "PO Box 9876 Gaborone",
                "Oodi 123 st"
        );

        TestAgentsModel user4 = new TestAgentsModel(
                "That Guy",
                "Field Agent",
                "Half Day",
                "Katlego",
                "Metsi",
                "93287658",
                "Female",
                "78132651",
                "71763883",
                "PO Box 9876 Gaborone",
                "Oodi 123 st"
        );

        return List.of(
                user1,
                user2,
                user3,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user2,
                user1,
                user4
        );

    }

    public static List<AgentPaymentsModel> getTestPaymentAgents() {

        AgentPaymentsModel user1 = new AgentPaymentsModel(
                "Randy Jon",
                "250.00",
                "Yes",
                "15",
                "8",
                "100.00",
                "",
                "40.00",
                "Approved",
                "Cash",
                "Orange Money",
                "400.00"
        );

        AgentPaymentsModel user2 = new AgentPaymentsModel(
                "Joe Doe",
                "150.00",
                "No",
                "14",
                "9",
                "80.00",
                "50.00",
                "",
                "Denied",
                "Cash",
                "Botswana Post",
                "350.00"
        );

        AgentPaymentsModel user3 = new AgentPaymentsModel(
                "Obakeng Motsumi",
                "500.00",
                "No",
                "10",
                "15",
                "80.00",
                "18.00",
                "",
                "",
                "Cash",
                "Botswana Post",
                "410.00"
        );

        AgentPaymentsModel user4 = new AgentPaymentsModel(
                "Brendon Khumalo",
                "200.00",
                "No",
                "16",
                "5",
                "70.00",
                "25.00",
                "",
                "Pending",
                "Cash",
                "Smega",
                "420.00"
        );

        AgentPaymentsModel user5 = new AgentPaymentsModel(
                "Thabang Metsi",
                "300.00",
                "No",
                "19",
                "14",
                "250.00",
                "90.00",
                "",
                "Approved",
                "Cash",
                "MyZaka",
                "370.00"
        );

        return List.of(
                user1,
                user2,
                user3,
                user4,
                user5
        );

    }

    public static List<AgentPaymentsModel> getEmptyTestPaymentAgents() {

        AgentPaymentsModel user1 = new AgentPaymentsModel(
                "Randy Jon",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        AgentPaymentsModel user2 = new AgentPaymentsModel(
                "Joe Doe",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        AgentPaymentsModel user3 = new AgentPaymentsModel(
                "Thabang Metsi",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        AgentPaymentsModel user4 = new AgentPaymentsModel(
                "Brendon Khumalo",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        return List.of(
                user1,
                user2,
                user3,
                user4
        );

    }

    public static List<ParametersModel> getTestParameters() {

        ParametersModel parameters1 = new ParametersModel(
                39.98,
                "Supervisor",
                "6 Months"
        );

        ParametersModel parameters2 = new ParametersModel(
                28.00,
                "Field Agent",
                "6 Months"
        );

        return List.of(
                parameters1,
                parameters2
        );

    }

    public static List<HistoryStatementModel> getStatementData() {

        HistoryStatementModel h1 = new HistoryStatementModel(
               29.95,
                "August 2022",
                "300.0",
                "Amogelang Mosane",
                "743820931",
                8,
                9
                ,9
        );

        HistoryStatementModel h2 = new HistoryStatementModel(
                39.95,
                "September 2022",
                "560.0",
                "Thabang Dithupa",
                "910013265",
                8,
                9
                ,9
        );

        return List.of(
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2,
                h1,
                h2
                );

    }

}

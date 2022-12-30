import com.application.iserv.ui.utils.ApplicationUserDataModel;
import com.application.iserv.ui.utils.SessionManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSomeStuff {

    String serviceSid;

    public static void main(String[] args) {

        // Extract month Long from string
        String[] strings = "3 Month".split(" ");
        long a = Long.parseLong(strings[0]);

        String b_str = String.valueOf(a);
        int b = Integer.parseInt(b_str);

        LocalDate localDate1 = LocalDate.now();

        for (int i = 1; i < b + 1; i++) {
            System.err.println(i+" months added: "+localDate1.plusMonths(i));
        }

        System.err.println(b);

        // Add Months to date
        LocalDate localDate = LocalDate.now();
        System.err.println("added 2 months: "+localDate.plusMonths(a));

        // Get dates difference durations in months
        System.err.println(ChronoUnit.MONTHS.between(LocalDate.now(), localDate.plusMonths(a)));

        LocalDate completionLocalDate2 = LocalDate.of(2022, 10, 26);

        String[] completionLocalDate2_str = completionLocalDate2.toString().split("-");
        String year = completionLocalDate2_str[0];
        String month = completionLocalDate2_str[1];

        String completionDate_str = year+month;

        System.err.println("date: "+completionDate_str);

        LocalDate todayLocalDate = LocalDate.now();
        String[] todayLocalDate2_str = todayLocalDate.toString().split("-");
        String year1 = todayLocalDate2_str[0];
        String month1 = todayLocalDate2_str[1];

        String todayDate2_str = year1+month1;
        System.err.println("today date: "+todayDate2_str);

        int diff = Integer.parseInt(todayDate2_str) - Integer.parseInt(completionDate_str);

        if (diff < 0) {
            System.err.println("Date valid");
        }
        else {
            System.err.println("Date invalid");
        }


    }

    @Test
    public void testTimeFormats() {

        /*String[] a = LocalDate.now().toString().split("-");
        System.err.println(a[0]+"-"+a[1]);
*/
       /* DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        System.err.println(LocalDate.now().plusMonths(6));*/

        double a = 787.765;

        int b = (int) a;

       // System.err.println(b);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm - dd MMMM yyyy");
        System.err.println(LocalDateTime.now().format(dateFormatter));

    }

    @Test
    public void readCSVFile() {

        String path = "";
        BufferedReader bufferedReader = null;
        String line = "";

        File file;

        try {
            file = ResourceUtils.getFile("classpath:students.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            while ((line = bufferedReader.readLine()) != null) {
                String[] row = line.split(",");

                for (String index : row) {
                    System.out.printf("%-10s", index);
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Test
    public void readCSVFile1() {

        Path path = Path.of("src", "main", "resources", "CSV Test Files/students.csv");

        try {

            List<StudentModel> studentModelList = Files.lines(path)
                    .skip(1)
                    .map(TestSomeStuff::getStudents).toList();

            System.err.println(studentModelList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static StudentModel getStudents(String line) {
        String[] fields = line.split(",");
        if (fields.length != 3) {
            throw new RuntimeException("Invalid csv line: "+line);
        }
        return new StudentModel(fields[0], fields[1], fields[2]);
    }

    @Test
    public void readParticipantsCSVFile() {

        Path path = Path.of("src", "main", "resources", "CSV Test Files/sample_participants.csv");

        try {

            List<ParticipantsModel> participantsModelList = Files.lines(path)
                    .skip(1)
                    .map(TestSomeStuff::getParticipants).toList();

            System.err.println(participantsModelList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static ParticipantsModel getParticipants(String line) {

        // SessionManager
        SessionManager sessionManager = new SessionManager();

        // ApplicationUser
        ApplicationUserDataModel applicationUserDataModel = sessionManager.getApplicationUserData();

        String[] fields = line.split(",");
        if (fields.length != 20) {
            throw new RuntimeException("Invalid csv line: "+line);
        }

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

        List<String> contractDates = new ArrayList<>();
        for (int i = 1; i < Integer.parseInt(String.valueOf(monthsDifference)) + 1; i++) {
            contractDates.add(i+" Months");
        }

        System.err.println(contractDates);

        return new ParticipantsModel(
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
    }


    @Test
    public void readCSVFileParticipants() {

        Path path = Path.of("src", "main", "resources", "CSV Test Files/participants.csv");

        try {

            List<ParticipantsModel> participantsModelList = Files.lines(path)
                    .skip(1)
                    .map(TestSomeStuff::getParticipants).toList();

            System.err.println(participantsModelList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static ParticipantsModel getParticipants1(String line) {
        String[] fields = line.split(",");
        if (fields.length != 23) {
            throw new RuntimeException("Invalid csv line: "+line);
        }

        String dateOfBirth_str = fields[3];
        String[] getDateOfBirth = dateOfBirth_str.split("-");

        LocalDate dateOfBirth = LocalDate.of(
                Integer.parseInt(getDateOfBirth[0]),
                Integer.parseInt(getDateOfBirth[1]),
                Integer.parseInt(getDateOfBirth[2])
        );

        String placementDate_str = fields[13];
        String[] getPlacementDate = placementDate_str.split("-");

        LocalDate placementDate = LocalDate.of(
                Integer.parseInt(getPlacementDate[0]),
                Integer.parseInt(getPlacementDate[1]),
                Integer.parseInt(getPlacementDate[2])
        );

        String completionDate_str = fields[14];
        String[] getCompletionDate = completionDate_str.split("-");

        LocalDate completionDate = LocalDate.of(
                Integer.parseInt(getCompletionDate[0]),
                Integer.parseInt(getCompletionDate[1]),
                Integer.parseInt(getCompletionDate[2])
        );

        return new ParticipantsModel(
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
                fields[19],
                fields[15],
                fields[16],
                fields[17],
                fields[18],
                fields[20],
                fields[21],
                fields[22],
                fields[23]

        );
    }

    @Test
    public void testTwilioVerify() {

        // Secret: G7RCBfgG7PX0IxS4I8eBM6c5DBDyj86l sid: SK4f440c6cdb12fe7302024ca4c83763da
        // Find your Account SID and Auth Token at twilio.com/console
        // and set the environment variables. See http://twil.io/secure
        String ACCOUNT_SID = System.getenv("ACb8c52dd2e863f42d010fc88c4274d133");
        String AUTH_TOKEN = System.getenv("0e43dc9d1dec4b5c62fbf221d556c574");

        // Create verification service
        Twilio.init("ACb8c52dd2e863f42d010fc88c4274d133", "0e43dc9d1dec4b5c62fbf221d556c574");
        Service service = Service.creator("My First Verify Service").create();

        serviceSid = service.getSid();

        System.err.println("Service Sid: "+serviceSid);

        // Send verification token
        Twilio.init("ACb8c52dd2e863f42d010fc88c4274d133", "0e43dc9d1dec4b5c62fbf221d556c574");
        Verification verification = Verification.creator(
                        service.getSid(),
                        "+26776448866",
                        "sms")
                .create();

        System.err.println("Verification Status: "+verification.getStatus());

    }

    @Test
    public void checkVerificationToken() {

        // Check Verification token
        Twilio.init("ACb8c52dd2e863f42d010fc88c4274d133", "0e43dc9d1dec4b5c62fbf221d556c574");
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        "VA16063a96c376a4a1f1aea7ee28eb9465")
                .setTo("+26776448866")
                .setCode("878986")
                .create();

        System.err.println("Verification Status: "+verificationCheck.getStatus());

    }

    @Test
    public void testRegex() {
        Pattern pattern = Pattern.compile("^[0-9]{1,45}$");
        Matcher matcher = pattern.matcher("1");

        if (matcher.find()) {
            System.err.println("Matches");
        }
        else {
            System.err.println("Does not Matches");
        }
    }

    @Test
    public void compareDates() {

        LocalDate placementDate = LocalDate.of(
                2023,
                11,
                23
        );

        LocalDate completionDate = LocalDate.of(
                2023,
                8,
                23
        );

        long monthsDifference = ChronoUnit.MONTHS.between(placementDate, completionDate);

        int compare = placementDate.compareTo(completionDate);

        System.err.println(compare);

        System.err.println(Math.abs(-monthsDifference)+ " Months");

    }

    @Test
    public void getNameForNumber() {

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        Phonenumber.PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse("+26772291792",
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }

        PhoneNumberToCarrierMapper phoneNumberToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

        System.err.println("number: "+phoneNumberToCarrierMapper.getNameForNumber(phoneNumber, Locale.ENGLISH));

    }

    @Test
    public void validatePhoneNumber() {

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        Phonenumber.PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse("+26772291792",
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }

        PhoneNumberToCarrierMapper phoneNumberToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

        System.err.println("number: "+phoneNumberToCarrierMapper.getNameForNumber(phoneNumber, Locale.ENGLISH));

    }

    @Test
    public void encodeImage() {
        // /home/sheriff-will/IdeaProjects/iserv/src/test/java/images
        String imgPath = "src/test/java/images/car.jpg";

        try {
            // Read image from file
            FileInputStream fileInputStream = new FileInputStream(imgPath);

            // Get byte array from image stream
            int bufLength = 2048;
            byte[] buffer = new byte[2048];
            byte[] data;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int readLength;

            while ((readLength = fileInputStream.read(buffer, 0, bufLength)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }

            data = outputStream.toByteArray();
            String imgString = Base64.getEncoder().withoutPadding().encodeToString(data);

            outputStream.close();
            fileInputStream.close();

            System.err.println(imgString);

            // Decode Img
            byte[] decodedImg = Base64.getDecoder().decode(imgString);

            System.err.println("decoded img: "+decodedImg);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testOpenCSVRead() throws Exception {

       // Path filePath = Path.of("src", "main", "resources", "CSV Test Files/students.csv");
        Path filePath = Paths.get(ClassLoader.getSystemResource("csv/students.csv").toURI());

        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                List<String[]> s = csvReader.readAll();
                for (int i = 0; i < s.size(); i++) {
                    System.err.println(s.get(i));
                }
            }
        }

    }

    @Test
    public void testWriteCsv() {

        String path = "participantsd.csv";

        try {
         //   Path path1 = Paths.get(ClassLoader.getSystemResource("csv/participants.csv").toURI());

            File file = new File(path);

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

    }

    @Test
    public void testStrings() {
        String s = "no";

        if (s.contains("No")) {
            System.err.println("true");
        }
    }

}

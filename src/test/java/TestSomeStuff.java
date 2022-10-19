import com.application.iserv.ui.participants.models.ParticipantsModel;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

        String[] a = LocalDate.now().toString().split("-");
        System.err.println(a[0]+"-"+a[1]);

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

    private static ParticipantsModel getParticipants(String line) {
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
                fields[22]

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

        LocalDate completionDate = LocalDate.of(
                2022,
                10,
                18
        );

        int compare = completionDate.compareTo(LocalDate.now());

        System.err.println(compare);

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

}

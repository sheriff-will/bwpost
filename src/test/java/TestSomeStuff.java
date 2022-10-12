import com.application.iserv.ui.participants.models.ParticipantsModel;
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

public class TestSomeStuff {

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

}

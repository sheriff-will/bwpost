import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

}

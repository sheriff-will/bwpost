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

    }

}

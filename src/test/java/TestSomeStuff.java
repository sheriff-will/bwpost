import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TestSomeStuff {

    public static void main(String[] args) {

        // Extract month Long from string
        String[] strings = "6 Month".split(" ");
        long a = Long.parseLong(strings[0]);

        // Add Months to date
        LocalDate localDate = LocalDate.now();
        System.err.println("added 2 months: "+localDate.plusMonths(a));

        // Get dates difference durations in months
        System.err.println(ChronoUnit.MONTHS.between(LocalDate.now(), localDate.plusMonths(a)));

    }

}

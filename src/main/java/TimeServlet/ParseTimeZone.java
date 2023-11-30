package TimeServlet;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ParseTimeZone {

    public static final String UTC = "UTC";
    public static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss z";

    public static String getCurrentTimeInTimeZone(String timeZone) {
        ZoneId zone = ZoneId.of(UTC);
        if (timeZone != null) {
            zone = ZoneId.of(toTimeZone(timeZone));
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);
        return zonedDateTime.format(DateTimeFormatter.ofPattern(FORMAT_TIME));
    }

    public static String toTimeZone(String timeZone) {
        return timeZone.replace(" ", "+");
    }
}
package ccb.pgames.helpers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FormatHelper {
    public static String formatDate(long creation_date) {
        var localDateTime = LocalDateTime.ofEpochSecond(creation_date, 0, ZoneOffset.UTC);
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}

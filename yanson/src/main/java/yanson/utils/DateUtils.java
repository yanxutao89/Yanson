package yanson.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    };

    public static Date stringToDate(String dateStr) {
        if (dateStr != null) {
            for (String format : DATE_FORMATS) {
                Date date = stringToDate(dateStr, format);
                if (date != null) {
                    return date;
                }
            }
        }
        return null;
    }

    private static Date stringToDate(String dateStr, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date date) {
        if (date != null) {
            for (String format : DATE_FORMATS) {
                String dateStr = dateToString(date, format);
                if (dateStr != null) {
                    return dateStr;
                }
            }
        }
        return null;
    }

    private static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

}

package eugene.petsshelter.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eugene on 3/5/2018.
 */

public class DateUtils {

    public static String getFormattedCurrentDate() {

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return dateFormat.format(currentDate);
    }

}

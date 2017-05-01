package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by KhoaBeo on 4/23/2017.
 */

public class Util {

    public static String getFullDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
//    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = format.format(Calendar.getInstance().getTime());
        return date;
    }
}

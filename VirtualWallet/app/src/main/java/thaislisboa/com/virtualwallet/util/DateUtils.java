package thaislisboa.com.virtualwallet.util;

import java.text.DateFormatSymbols;

public class DateUtils {

    //public String getMonth(int month, Locale locale) { return DateFormatSymbols.getInstance(locale).getMonths()[month-1]; }

    public static String getMonth(int month)
    {
        return new DateFormatSymbols().getMonths()[month];
    }
}


package thaislisboa.com.virtualwallet.util;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyUtils {

    public static String getCurrency(Context context, double value) {

        Locale current = context.getResources().getConfiguration().locale;
        NumberFormat format = NumberFormat.getCurrencyInstance(current);
        String currency = format.format(value);

        return currency;
    }

    public static double getDouble(Context context, String value) {

        Locale current = context.getResources().getConfiguration().locale;

        try {
            NumberFormat cf = NumberFormat.getCurrencyInstance(current);
            Number number = cf.parse(value);

            return number.doubleValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}

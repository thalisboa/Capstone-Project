package thaislisboa.com.virtualwallet.widget;

import android.content.Context;
import android.content.SharedPreferences;

public class WidgetDAO {

    private static final String WIDGET_SP_NAME = "widgets.info";
    private static final String WIDGET_SP_BALANCE = "balance";
    private static final String WIDGET_SP_MONTH_YEAR = "month_year";

    public static void updateBalance(Context context, String value) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

        sharedEditor.putString(WIDGET_SP_BALANCE, value);

        sharedEditor.apply();

    }

    public static void updateMonthYear(Context context, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putString(WIDGET_SP_MONTH_YEAR, value);

        sharedEditor.apply();


    }

    public static String getBalance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(WIDGET_SP_BALANCE, "");
    }

    public static String getMonthYear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(WIDGET_SP_MONTH_YEAR, "");
    }
}

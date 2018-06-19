package thaislisboa.com.virtualwallet.util;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import thaislisboa.com.virtualwallet.model.Transaction;

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

    public static String calculateBalance(Context context, List<Transaction> transactionList) {
        double balance = 0;

        for (Transaction transaction : transactionList) {
            if (transaction.isDeposit()) {
                balance += transaction.getValue();
            } else {
                balance -= transaction.getValue();
            }
        }

        String balanceStr = CurrencyUtils.getCurrency(context, balance);

        return balanceStr;
    }
}

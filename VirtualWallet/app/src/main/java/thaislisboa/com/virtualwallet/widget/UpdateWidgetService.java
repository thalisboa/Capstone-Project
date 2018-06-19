package thaislisboa.com.virtualwallet.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import thaislisboa.com.virtualwallet.callback.CallbackMonths;
import thaislisboa.com.virtualwallet.callback.CallbackTransaction;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Transaction;
import thaislisboa.com.virtualwallet.util.CurrencyUtils;


public class UpdateWidgetService extends IntentService implements CallbackMonths, CallbackTransaction {

    public static final String ACTION_UPDATE_WIDGET = ".action.update_widget";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();

            if (ACTION_UPDATE_WIDGET.equals(action)) {
                //handleActionUpdateLastResult();
                FirebaseDB.getMonths(this);
            }
        }
    }

    public static void startUpdateLastResult(Context context) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    public void onReturnMonths(List<String> monthsList) {

        String monthYear = monthsList.get(0);

        WidgetDAO.updateMonthYear(this, monthYear);

        String array[] = monthYear.split("/");
        String selectedMonth = array[0];
        String selectedYear = array[1];

        Date date = null;
        try {
            Locale current = getResources().getConfiguration().locale;
            date = new SimpleDateFormat("MMM", current).parse(selectedMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        //String.format("%02d", month + 1)
        FirebaseDB.loadTransactions(this, (month + 1), Integer.parseInt(selectedYear), 50);


    }

    @Override
    public void onReturn(List<Transaction> transactionList) {
        String balanceStr = CurrencyUtils.calculateBalance(this, transactionList);
        WidgetDAO.updateBalance(this, balanceStr);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WalletWidget.class));

        WalletWidget.updateBalanceWidgets(this, appWidgetManager, appWidgetIds);
    }
}

package thaislisboa.com.virtualwallet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.activities.HomeWalletActivity;

public class WalletWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wallet_widget);
        String balance = WidgetDAO.getBalance(context);
        String monthYear = WidgetDAO.getMonthYear(context);
        views.setTextViewText(R.id.appwidget_text_balance_price, balance);
        views.setTextViewText(R.id.appwidget_text_month, monthYear);

        //Click button
        Intent intent = new Intent(context, HomeWalletActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_add_transaction, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UpdateWidgetService.startUpdateLastResult(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateBalanceWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


}

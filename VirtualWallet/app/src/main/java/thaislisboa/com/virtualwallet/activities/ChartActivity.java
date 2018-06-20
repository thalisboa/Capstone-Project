package thaislisboa.com.virtualwallet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.callback.CallbackMonths;
import thaislisboa.com.virtualwallet.callback.CallbackTransaction;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Transaction;

public class ChartActivity extends AppCompatActivity implements CallbackMonths, CallbackTransaction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDB.getMonths(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReturnMonths(List<String> monthsList) {

        Spinner spinner = (Spinner) findViewById(R.id.spinner_month_chart);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String array[] = parent.getItemAtPosition(position).toString().split("/");
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
                FirebaseDB.loadTransactions(ChartActivity.this, (month + 1), Integer.parseInt(selectedYear), 50);

                Log.d("thais-log", "MONTH: " + String.format("%02d", month + 1) + " " + selectedYear);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onReturn(List<Transaction> transactionList) {

        List<String> categoryNames = new ArrayList<>();
        List<Double> sumByCategories = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            if (!categoryNames.contains(transactionList.get(i).getCategory())) {
                categoryNames.add(transactionList.get(i).getCategory());
                Log.d("thais-log", "Category: " + transactionList.get(i).getCategory());
                sumByCategories.add(transactionList.get(i).getValue());
            } else {

                int index = -1;
                for (int j = 0; j < categoryNames.size(); j++) {
                    if (categoryNames.get(j).equals(transactionList.get(i).getCategory())) {
                        index = j;
                        Log.d("thais-log", "Category: index" + index);
                        break;
                    }
                }

                double sum = sumByCategories.get(index) + transactionList.get(index).getValue();
                Log.d("thais-log", "Category: " + transactionList.get(index).getCategory() + " " + sum);

            }
        }

        Pie pie = AnyChart.pie();

        pie.setTitle(getString(R.string.spending_by_category));
        pie.getLabels().setPosition(getString(R.string.outside));
        pie.getLegend().getTitle().setEnabled(true);
        pie.getLegend().getTitle()
                .setText(getString(R.string.percentage_title))
                .setPadding(0d, 0d, 10d, 0d);
        pie.getLegend()
                .setPosition(getString(R.string.center_bottom))
                .setItemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .setAlign(EnumsAlign.CENTER);

        List<DataEntry> data = new ArrayList<>();

        for (int i = 0; i < categoryNames.size(); i++) {
            data.add(new ValueDataEntry(categoryNames.get(i), sumByCategories.get(i)));
        }

        pie.setData(data);
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);

    }
}

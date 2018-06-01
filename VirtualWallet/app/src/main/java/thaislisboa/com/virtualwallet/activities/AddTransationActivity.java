package thaislisboa.com.virtualwallet.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.fragment.MyDatePickerFragment;
import thaislisboa.com.virtualwallet.model.Transaction;

public class AddTransationActivity extends AppCompatActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et = (EditText) findViewById(R.id.transaction_date);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Transaction t = new Transaction();

                t.setName("Creme de Cabelo");
                t.setDate(new Date());
                t.setCategory("Beleza");
                t.setValue(200.00);
                t.setDeposit(true);

                //FirebaseDB.save(t, AddTransationActivity.this);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.transaction_category);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Beer");
        categories.add("Transport");
        categories.add("Health");
        categories.add("Food");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment(dateSetListener);
        newFragment.show(getSupportFragmentManager(), "date picker");
    }


    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Toast.makeText(AddTransationActivity.this, "selected date is " + view.getYear() +
                            " / " + (view.getMonth() + 1) +
                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();

                    et.setText(view.getYear() + "/" + view.getMonth() + "/" + view.getDayOfMonth());
                }
            };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

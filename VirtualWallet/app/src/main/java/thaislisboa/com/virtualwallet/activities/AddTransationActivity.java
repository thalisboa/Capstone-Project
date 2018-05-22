package thaislisboa.com.virtualwallet.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
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

        et = (EditText) findViewById(R.id.et_calendar);

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

                FirebaseDB.save(t, AddTransationActivity.this);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


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

                    et.setText(view.getYear() + "/" + view.getMonth()+"/"+ view.getDayOfMonth());
                }
            };
}

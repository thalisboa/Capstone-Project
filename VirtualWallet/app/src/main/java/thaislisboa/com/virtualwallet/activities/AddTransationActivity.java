package thaislisboa.com.virtualwallet.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.callback.CallbackCategory;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Category;
import thaislisboa.com.virtualwallet.model.Transaction;

public class AddTransationActivity extends AppCompatActivity implements CallbackCategory {

    private TextView mTvName;
    private TextView mTvDate;
    private Spinner mSpinnerCategories;
    private CurrencyEditText mTvValue;
    private RadioGroup mRadioGroupType;
    private RadioButton mRadioButtonSelected;
    private String mSelectedDate;


    private TextInputLayout mTILName;
    private TextInputLayout mTILDate;
    private TextInputLayout mTILValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTvName = findViewById(R.id.transaction_name);
        mTvDate = (EditText) findViewById(R.id.transaction_date);
        mSpinnerCategories = (Spinner) findViewById(R.id.transaction_category);
        mTvValue = findViewById(R.id.transaction_value);
        mRadioGroupType = findViewById(R.id.transaction_radio_group);

        mTILName = findViewById(R.id.til_name);
        mTILDate = findViewById(R.id.til_date);
        mTILValue = findViewById(R.id.til_value);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mTvName.getText())) {
                    mTILName.setError(getString(R.string.please_fill));
                    return;
                }

                if (TextUtils.isEmpty(mTvDate.getText())) {
                    mTILDate.setError(getString(R.string.please_fill));
                    return;
                }

                if (parseCurrency(mTvValue.formatCurrency(mTvValue.getRawValue())) == 0) {
                    mTILValue.setError(getString(R.string.please_fill));
                    return;
                }


                Transaction t = new Transaction();

                t.setName(mTvName.getText().toString());
                t.setDateTransaction(mSelectedDate);
                t.setCategory(mSpinnerCategories.getSelectedItem().toString());
                t.setValue(parseCurrency(mTvValue.formatCurrency(mTvValue.getRawValue())));

                int selectedId = mRadioGroupType.getCheckedRadioButtonId();
                mRadioButtonSelected = findViewById(selectedId);

                if (mRadioButtonSelected.getText().toString().equals(getString(R.string.expense))) {
                    t.setDeposit(false);
                } else {
                    t.setDeposit(true);
                }

                FirebaseDB.saveTransaction(t, AddTransationActivity.this);

                finish();
            }
        });


        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);


    }

    public double parseCurrency(String price) {

        Locale current = getResources().getConfiguration().locale;

        try {
            NumberFormat cf = NumberFormat.getCurrencyInstance(current);
            Number number = cf.parse(price);

            return number.doubleValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void showDatePicker(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mTvDate.setText(day + "/" + (month + 1) + "/" + year);
                        mSelectedDate = year + "-" + (month + 1) + "-" + String.format("%02d", day);
                    }
                }, year, month, day);

        datePickerDialog.show();
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
    protected void onResume() {
        super.onResume();
        FirebaseDB.loadCategories(this);
    }

    @Override
    public void onReturn(List<Category> categoryList) {
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategories.setAdapter(dataAdapter);
    }

    public void addCaregory(View view) {

        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);

    }
}

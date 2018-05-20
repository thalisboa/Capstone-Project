package thaislisboa.com.virtualwallet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thaislisboa.com.virtualwallet.ListAdapter;
import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.model.Transaction;

public class HomeWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_month);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        List<Transaction> list = new ArrayList<>();

        Transaction t = new Transaction();

        t.setName("Creme de Cabelo");
        t.setDate(new Date());
        t.setCategory("Beleza");
        t.setValue(200.00);
        t.setDeposit(true);

        list.add(t);

        Transaction t2 = new Transaction();

        t2.setName("Energetico");
        t2.setDate(new Date());
        t2.setCategory("Bebida");
        t2.setValue(100.00);
        t2.setDeposit(false);

        list.add(t2);

        Transaction t3 = new Transaction();

        t3.setName("Viagem");
        t3.setDate(new Date());
        t3.setCategory("Lazer");
        t3.setValue(2000.00);
        t3.setDeposit(false);

        list.add(t3);


        RecyclerView mRecyclerView = findViewById(R.id.rv_insert);
        ListAdapter mListAdapter = new ListAdapter(list, this);


        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeWalletActivity.this));


    }

}

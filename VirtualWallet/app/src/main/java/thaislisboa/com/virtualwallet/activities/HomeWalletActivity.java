package thaislisboa.com.virtualwallet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thaislisboa.com.virtualwallet.adapter.ListAdapter;
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
        categories.add("Janeiro");
        categories.add("Fevereiro");
        categories.add("Março");
        categories.add("Abril");

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


                Intent intent = new Intent(HomeWalletActivity.this,AddTransationActivity.class);
                startActivity(intent);

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

        Transaction t4 = new Transaction();
        t4.setName("Udacity");
        t4.setDate(new Date());
        t4.setCategory("extensão :[");
        t4.setValue(400.00);
        t4.setDeposit(true);

        list.add(t4);

        Transaction t5 = new Transaction();
        t5.setName("MBA");
        t5.setDate(new Date());
        t5.setCategory("Educação");
        t5.setValue(700.00);
        t5.setDeposit(true);
        list.add(t5);


        RecyclerView mRecyclerView = findViewById(R.id.rv_insert);
        ListAdapter mListAdapter = new ListAdapter(list, this);


        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeWalletActivity.this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_signout) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                    switch (which) {

                        case DialogInterface.BUTTON_POSITIVE:

                            AuthUI.getInstance()
                                    .signOut(HomeWalletActivity.this)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(HomeWalletActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }

                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();


            return true;

        }

        if (id == R.id.menu_add_category) {

            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.menu_chart) {

            Intent intent = new Intent(this, ChartActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

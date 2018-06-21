package thaislisboa.com.virtualwallet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.adapter.ListAdapter;
import thaislisboa.com.virtualwallet.callback.CallbackMonths;
import thaislisboa.com.virtualwallet.callback.CallbackTransaction;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Transaction;
import thaislisboa.com.virtualwallet.util.AlertDialogHelper;
import thaislisboa.com.virtualwallet.util.CurrencyUtils;
import thaislisboa.com.virtualwallet.util.RecyclerItemClickListener;
import thaislisboa.com.virtualwallet.widget.UpdateWidgetService;

public class HomeWalletActivity extends AppCompatActivity implements CallbackTransaction, AlertDialogHelper.AlertDialogListener, CallbackMonths {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CircleImageView mProfileImg;
    private TextView mProfileName;
    private TextView mPriceBalance;
    private Menu mContextMenu;
    boolean isMultiSelect = false;
    private ActionMode mActionMode;
    private AlertDialogHelper alertDialogHelper;
    private ListAdapter mListAdapter;
    private ProgressBar mProgressBar;
    private AdView mAdView;

    private ArrayList<Transaction> user_list = new ArrayList<>();
    private ArrayList<Transaction> multiselect_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alertDialogHelper = new AlertDialogHelper(this);

        mProfileName = findViewById(R.id.tv_user_name);
        mProfileImg = findViewById(R.id.profile_image);
        mPriceBalance = findViewById(R.id.tv_price_balace);
        mProgressBar = findViewById(R.id.pg_home_wallet);

        mProfileName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(mProfileImg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeWalletActivity.this, AddTransationActivity.class);
                startActivity(intent);

            }
        });

        MobileAds.initialize(this, getString(R.string.admob_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //ToolBar Actions
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
                                            Intent intent = new Intent(HomeWalletActivity.this, MainActivity.class);
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
            builder.setMessage(getString(R.string.are_you_sure)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener).show();

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


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDB.getMonths(this);
    }

    @Override
    public void onReturn(List<Transaction> transactionList) {

        user_list = new ArrayList<>(transactionList);


        RecyclerView mRecyclerView = findViewById(R.id.rv_insert);
        mListAdapter = new ListAdapter(transactionList, this, multiselect_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeWalletActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mListAdapter);

        mProgressBar.setVisibility(View.GONE);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Transaction>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));

        calculateBalance(transactionList);

    }

    private void calculateBalance(List<Transaction> transactionList) {
        double balance = 0;

        for (Transaction transaction : transactionList) {
            if (transaction.isDeposit()) {
                balance += transaction.getValue();
            } else {
                balance -= transaction.getValue();
            }
        }

        mPriceBalance.setText(CurrencyUtils.getCurrency(this, balance));

        if (balance < 0) {
            mPriceBalance.setTextColor(getResources().getColor(R.color.red));
        } else {
            mPriceBalance.setTextColor(getResources().getColor(R.color.green));
        }
    }


    public void multi_select(int position) {
        if (mActionMode != null && position >= 0) {
            if (multiselect_list.contains(user_list.get(position)))
                multiselect_list.remove(user_list.get(position));
            else {
                multiselect_list.add(user_list.get(position));

            }

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        mListAdapter.selected_usersList = multiselect_list;
        mListAdapter.transactions = user_list;
        mListAdapter.notifyDataSetChanged();
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete_transaction, menu);
            mContextMenu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("", getString(R.string.delete_transaction), getString(R.string.delete), getString(R.string.cancel), 1, false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Transaction>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multiselect_list.size() > 0) {
                for (int i = 0; i < multiselect_list.size(); i++) {
                    user_list.remove(multiselect_list.get(i));
                    FirebaseDB.deleteTransaction(multiselect_list.get(i), this);
                }

                mListAdapter.notifyDataSetChanged();
                UpdateWidgetService.startUpdateLastResult(this);


                if (mActionMode != null) {
                    mActionMode.finish();
                }

                calculateBalance(user_list);
            }
        } else if (from == 2) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }

    @Override
    public void onReturnMonths(List<String> monthsList) {

        Spinner spinner = (Spinner) findViewById(R.id.spinner_month);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthsList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
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
                FirebaseDB.loadTransactions(HomeWalletActivity.this, (month + 1), Integer.parseInt(selectedYear), 50);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}

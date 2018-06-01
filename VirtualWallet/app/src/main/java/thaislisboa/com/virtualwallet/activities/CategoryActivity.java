package thaislisboa.com.virtualwallet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.adapter.CategoryAdapter;
import thaislisboa.com.virtualwallet.callback.CallbackCategory;
import thaislisboa.com.virtualwallet.firebase.FirebaseDB;
import thaislisboa.com.virtualwallet.model.Category;

public class CategoryActivity extends AppCompatActivity implements CallbackCategory {

    private Button mBtAdd;
    private EditText mEdNewCategory;
    private Category mCategory;

    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private final List<Category> mCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCategory = new Category();
        mRecyclerView = findViewById(R.id.rv_category);
        mBtAdd = findViewById(R.id.btn_add_categoty);
        mEdNewCategory = findViewById(R.id.et_new_category);

        FirebaseDB.loadCategories(this);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Button
        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCategory.setName(mEdNewCategory.getText().toString());

                if (!TextUtils.isEmpty(mEdNewCategory.getText().toString())) {

                    Category category = FirebaseDB.saveCategory(mCategory, CategoryActivity.this);

                    if (mCategoryList != null) {
                        mAdapter.addCategory(category);
                    }

                    mEdNewCategory.setText("");

                } else {
                    mEdNewCategory.setError(getString(R.string.text_empty));
                }

            }
        });

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
    public void onReturn(List<Category> categoryList) {
        Log.d("thais-log", "onReturn: ");
        mRecyclerView.setAdapter(null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.line));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new CategoryAdapter(this, categoryList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}





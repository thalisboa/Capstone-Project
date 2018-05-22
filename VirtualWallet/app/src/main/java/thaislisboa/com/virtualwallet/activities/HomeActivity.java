package thaislisboa.com.virtualwallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import thaislisboa.com.virtualwallet.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn = findViewById(R.id.btn_signout);
        Button btn_2 = findViewById(R.id.btn_insert);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthUI.getInstance()
                        .signOut(HomeActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...

                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AddTransationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
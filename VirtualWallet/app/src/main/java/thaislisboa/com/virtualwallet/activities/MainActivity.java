package thaislisboa.com.virtualwallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.firebase.Authenticator;

public class MainActivity extends AppCompatActivity {


    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Authenticator.isUserAuthenticated()) {

            Intent intent = new Intent(this, HomeWalletActivity.class);
            startActivity(intent);
            finish();
        }

        signInButton = findViewById(R.id.login_with_google);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and launch sign-in intent
                Authenticator.startAuthenticationUI(MainActivity.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Authenticator.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(this, HomeWalletActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}

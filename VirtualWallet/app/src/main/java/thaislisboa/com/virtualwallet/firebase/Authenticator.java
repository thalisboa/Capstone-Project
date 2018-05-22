package thaislisboa.com.virtualwallet.firebase;

import android.app.Activity;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import thaislisboa.com.virtualwallet.activities.HomeWalletActivity;

public class Authenticator {

    public static final int RC_SIGN_IN = 123;
    private static List<AuthUI.IdpConfig> providers;


    public static void startAuthenticationUI(Activity context) {

        // Choose authentication providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            Intent intent = new Intent(context, HomeWalletActivity.class);
            context.startActivity(intent);

        }

        context.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    public static boolean isUserAuthenticated() {

        return FirebaseAuth.getInstance().getCurrentUser() != null;

    }

    public static String getUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}

package net.seismos.android.seismos.ui.global;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.onboarding.OnboardingActivity;

import java.util.Arrays;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            createSignInIntent();
        } else {
            startActivity(new Intent(this, DashActivity.class));
            finish();
        }
    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firebase_sign_in)
                .setGoogleButtonId(R.id.googleButton)
                .setEmailButtonId(R.id.emailButton)
                .build();


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setTheme(R.style.SignInTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(this, OnboardingActivity.class));
                finish();
            } else {
                // sign in failed
            }
        }


    }
}

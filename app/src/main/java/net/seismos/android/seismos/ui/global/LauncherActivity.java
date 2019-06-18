package net.seismos.android.seismos.ui.global;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.User;
import net.seismos.android.seismos.ui.onboarding.OnboardingActivity;

import java.util.Arrays;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";

    private int RC_SIGN_IN = 123;

    FirebaseFirestore fb;
    CollectionReference users;


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

         fb = FirebaseFirestore.getInstance();

         users = fb.collection("users");


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firebase_sign_in)
                .setGoogleButtonId(R.id.googleButton)
                .setEmailButtonId(R.id.editProfileButton)
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
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                User user = new User();
                user.setName(firebaseUser.getDisplayName());
                user.setEmail(firebaseUser.getEmail());
                user.setId(firebaseUser.getUid());
                user.setPhoto(firebaseUser.getPhotoUrl() ==
                        null ? null : firebaseUser.getPhotoUrl().toString());
                user.setPhone(firebaseUser.getPhoneNumber());
                user.setProvider(firebaseUser.getProviderId());

                users.document(firebaseUser.getUid()).set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "user document successfully written");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error writing document: " + e.toString());
                        }
                    });
                startActivity(new Intent(this, OnboardingActivity.class));
                finish();
            } else {
                // sign in failed
            }
        }


    }
}

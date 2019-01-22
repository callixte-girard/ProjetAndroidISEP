package fr.isep.c.projetandroidisep;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FirebaseUIActivity extends AppCompatActivity {

    // TO-DO :
    // - [DONE] make google sign in work
    // - make fb sign in work

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth.AuthStateListener listener_auth = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser current_user = firebaseAuth.getCurrentUser();

            if (current_user != null) {
                Log.d("auth_state_changed", firebaseAuth.getCurrentUser().getEmail());

                // checks if user path exists in nosql db + creates it if not
                String auth_uid = firebaseAuth.getInstance().getUid();

                try {
                    // to enable offline temp storage if network access gets lost or shitty
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                    DatabaseReference current_user_ref = FirebaseDatabase.getInstance()
                            .getReference().child(auth_uid);

                    Log.d("current_user_ref", current_user_ref.toString());

                } catch (Exception e) {
                    Log.d("current_user_ref", e.getMessage());
                }


                transferToMainActivity(current_user);
                finish();

            } else {
                Log.d("auth_state_changed", "no user");

                createSignInIntent();
                //themeAndLogo();
                //privacyAndTerms();

            }

        }
    };

    ///////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        FirebaseAuth.getInstance().addAuthStateListener(listener_auth);
    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseAuth.getInstance().removeAuthStateListener(listener_auth);
    }


    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                Log.d("login_success", user.getEmail());


                transferToMainActivity(user);
                finish();

            } else {
                Log.d("login_fail", "sorry bro :(");
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]


    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                //        new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
                //        new AuthUI.IdpConfig.FacebookBuilder().build(),
                //        new AuthUI.IdpConfig.TwitterBuilder().build())
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }


    protected void transferToMainActivity(FirebaseUser user)
    {
         ///////// perform transfer to main activity
        Intent intent_to_main_activity = new Intent(getApplicationContext(), MainActivity.class);
        intent_to_main_activity.putExtra("user_mail", user.getEmail());
        intent_to_main_activity.putExtra("user_name", user.getDisplayName());
        intent_to_main_activity.putExtra("user_uid", user.getUid());

        startActivity(intent_to_main_activity);
        finish();
    }




    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    public void themeAndLogo() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_squidge_logo)      // Set logo drawable
                        .setTheme(R.style.AppTheme)      // Set theme
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_theme_logo]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();
        // [START auth_fui_pp_tos]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_pp_tos]
    }

}

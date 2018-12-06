package fr.isep.c.projetandroidisep.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import butterknife.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.R;


public class LoginActivity extends AppCompatActivity
{

    protected final static String INVALID_NAME = "Your name or pseudo cannot be empty";
    protected final static String INVALID_EMAIL = "Invalid email, please check";
    protected final static String INVALID_PASSWORD = "Password must be longer than 3 characters";

    protected final static String DATAPATH_USERS = "users/";

    // ## Keeps the same fragments
    private FragmentManager frag_manager = getSupportFragmentManager();
    private static Fragment frag_login = new FragLogin();
    private static Fragment frag_register = new FragRegister();
    private static Fragment frag_forgot_password = new FragForgotPassword();


    /* LoginActivity #TO-DO :
    - Instantier une seule Fragment et l'appeler directement
    - [FragForgotPswd] rajouter envoi mail réel (attemptUserPasswordRecovery)
    - aotu-login after registration done
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebaseDBTest();

        SharedPreferences sp = getSharedPreferences("pipou", Context.MODE_PRIVATE);
        boolean logged = sp.getBoolean("logged", false);
        Log.d("logged", String.valueOf(logged));

        if (logged) {
            this.transferToMainActivity();
        } else {
            this.toFragLogin();
        }


    }

/*
    @Override
    public void onBackPressed()
    {
        // handle action here
        Fragment current_frag = frag_manager.getFragments().get(0);
        Log.d("current_frag", String.valueOf(current_frag.getClass()));

        if (current_frag.getClass() == FragLogin.class) {

        } else if (current_frag.getClass() == FragRegister.class) {
            toFragLogin();

        } else if (current_frag.getClass() == FragForgotPassword.class) {
            toFragLogin();

        }
    }
*/
    private void firebaseDBTest()
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DATAPATH_USERS + "user0");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("value_event", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("value_event", "Failed to read value...", error.toException());
            }
        });

        //myRef.setValue("Hello, World!");

    }


    protected void transferToMainActivity()
    {
        // save logged in status in sharedprefs
        SharedPreferences sp = getSharedPreferences("pipou", Context.MODE_PRIVATE);
        sp.edit().remove("logged").putBoolean("logged", true).apply();

        ///////// perform transfer to main activity
        Intent intent_to_main_activity = new Intent(this, MainActivity.class);
        startActivity(intent_to_main_activity);

        // kill process for it not to come back after login completed
        finish();
    }


    protected void toFragLogin() {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_login)
                .commit();
    }

    protected void toFragRegister() {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_register)
                .commit();
    }

    protected void toFragForgotPassword() {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_forgot_password)
                .commit();
    }


    protected static boolean isNameValid(String name) {
        boolean b = name.length() > 0 ;
        Log.d("name_valid", String.valueOf(b));
        return b ;
    }

    protected static boolean isEmailValid(String email) {
        boolean b = email.contains("@") && email.contains(".") ;
        Log.d("email_valid", String.valueOf(b));
        return b ;
    }

    protected static boolean isPasswordValid(String password) {
        boolean b = password.length() > 3 ;
        Log.d("password_valid", String.valueOf(b));
        return b ;
    }


}

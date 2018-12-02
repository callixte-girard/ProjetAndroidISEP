package fr.isep.c.projetandroidisep.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import butterknife.*;

import fr.isep.c.projetandroidisep.*;


public class LoginActivity extends AppCompatActivity
{
    private FragmentManager frag_manager = getSupportFragmentManager();

    protected final static String INVALID_NAME = "Your name or pseudo cannot be empty";
    protected final static String INVALID_EMAIL = "Invalid email, please check";
    protected final static String INVALID_PASSWORD = "Password must be longer than 3 characters";

    // ## Keeps the same fragments
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

        this.toFragLogin();
    }

    /*
    @Override
    public void onBackPressed() {
        // handle action here
    }
    */

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

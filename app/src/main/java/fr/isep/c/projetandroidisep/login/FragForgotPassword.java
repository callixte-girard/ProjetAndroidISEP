package fr.isep.c.projetandroidisep.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.isep.c.projetandroidisep.*;

public class FragForgotPassword extends Fragment implements View.OnClickListener
{
    Button button_recover_password, button_back_to_login ;
    EditText input_email ;

    private final static String SUBMIT_SUCCESS = "If the email entered is in our database, " +
            "you will receive a mail containing password recovery instructions.";

    private UsersDBDataSource datasource ;

    private View view ;
    private FragmentManager frag_manager ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // initiate mother view and frag manager
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        frag_manager = getActivity().getSupportFragmentManager();

        // initiate child views
        input_email = view.findViewById(R.id.input_email);
        button_recover_password = view.findViewById(R.id.button_recover_password);
        button_back_to_login = view.findViewById(R.id.button_back_to_login);

        // set click listeners
        button_recover_password.setOnClickListener(this);
        button_back_to_login.setOnClickListener(this);

        return view ;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_recover_password:
                attemptPasswordRecovery();
                break;

            case R.id.button_back_to_login:
                LoginActivity mother_activity = (LoginActivity) getActivity();
                mother_activity.toFragLogin();
                break;
        }
    }


    private void attemptPasswordRecovery()
    {
        boolean success ;

        String email = input_email.getText().toString();

        if (!LoginActivity.isEmailValid(email)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_EMAIL, Toast.LENGTH_SHORT).show();
        }
        else
        {
            datasource = new UsersDBDataSource(getContext());
            datasource.open();

            // performs login task
            success = datasource.attemptUserPasswordRecovery(email);

            datasource.close();

            Toast.makeText(getContext(), SUBMIT_SUCCESS, Toast.LENGTH_SHORT).show();
            Log.d("forgot_password_success", "really sent only if email is in database");

        }
    }

}

package fr.isep.c.projetandroidisep.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.isep.c.projetandroidisep.*;


public class FragLogin extends Fragment implements View.OnClickListener
{
    Button button_login ;
    EditText input_email, input_password ;
    TextView to_register, to_forgot_password ;

    private final static String AUTHENTICATION_FAIL = "Could not authenticate : email or password incorrect";
    private final static String AUTHENTICATION_SUCCESS = "You have been authentified. Welcome back :)";


    private UsersDBDataSource datasource ;

    private View view ;
    private FragmentManager frag_manager ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // initiate mother view and frag manager
        view = inflater.inflate(R.layout.fragment_login, container, false);
        frag_manager = getActivity().getSupportFragmentManager();

        // initiate child views
        input_email = view.findViewById(R.id.input_email);
        input_password = view.findViewById(R.id.input_password);
        button_login = view.findViewById(R.id.button_login);
        to_register = view.findViewById(R.id.to_register);
        to_forgot_password = view.findViewById(R.id.to_forgot_password);

        // set click listeners
        button_login.setOnClickListener(this);
        to_register.setOnClickListener(this);
        to_forgot_password.setOnClickListener(this);

        return view ;
    }

    @Override
    public void onClick(View v)
    {
        LoginActivity mother_activity = (LoginActivity) getActivity();

        switch (v.getId())
        {
            case R.id.button_login:
                User matching_user = attemptLogin();

                if (matching_user != null)  // si le login a été effectué avec succès
                {
                    ///////// perform transfer to main activity
                    //Intent intent_to_main_activity = new Intent(getActivity(), MainActivity.class);
                    Intent intent_to_main_activity = new Intent(getActivity(), MainActivity.class);

                    intent_to_main_activity.putExtra("name", matching_user.getName());
                    //intent_to_main_activity.putExtra("email", matching_user.getEmail());
                    startActivity(intent_to_main_activity);
                }
                break;

            case R.id.to_register:
                mother_activity.toFragRegister();
                break;

            case R.id.to_forgot_password:
                mother_activity.toFragForgotPassword();
                break;
        }
    }


    private User attemptLogin()
    {
        User matching_user ;

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (!LoginActivity.isEmailValid(email)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_EMAIL, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else if (!LoginActivity.isPasswordValid(password)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_PASSWORD, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else
        {
            datasource = new UsersDBDataSource(getContext());
            datasource.open();

            // performs login task
            matching_user = datasource.attemptUserAuth(email, password);

            datasource.close();

            if (matching_user != null)
            {
                //Toast.makeText(getContext(), AUTHENTICATION_SUCCESS, Toast.LENGTH_SHORT).show();
                Log.d("login_success", matching_user.getName() + " | " + matching_user.getEmail());

                return matching_user ;
            }
            else {
                Toast.makeText(getContext(), AUTHENTICATION_FAIL, Toast.LENGTH_SHORT).show();
                Log.d("login_fail", "");

                return null ;
            }
        }
    }

}

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
import android.widget.TextView;
import android.widget.Toast;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.customTypes.User;


public class FragRegister extends Fragment implements View.OnClickListener
{
    Button button_register ;
    EditText input_name, input_email, input_password, input_confirm_password ;
    TextView to_login ;

    private final static String PASSWORDS_DONT_MATCH = "The two passwords you entered do not match.";
    private final static String REGISTRATION_FAIL = "Could not register, are you sure you're not already a member ?";
    private final static String REGISTRATION_SUCCESS = " Registration done, congratulations ! Now please login with your brand news credentials :)";

    private UsersDBDataSource datasource ;

    private View view ;
    private FragmentManager frag_manager ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // initiate mother view and frag manager
        view = inflater.inflate(R.layout.fragment_register, container, false);
        frag_manager = getActivity().getSupportFragmentManager();

        // initiate child views
        input_name = view.findViewById(R.id.input_name);
        input_email = view.findViewById(R.id.input_email);
        input_password = view.findViewById(R.id.input_password);
        input_confirm_password = view.findViewById(R.id.input_confirm_password);
        button_register = view.findViewById(R.id.button_register);
        to_login = view.findViewById(R.id.to_login);

        // set click listeners
        button_register.setOnClickListener(this);
        to_login.setOnClickListener(this);

        return view ;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_register:
                User validated_user = attemptRegister();

                if (validated_user != null) {
                    ///////// perform transfer to main activity, or login fragment
                    LoginActivity mother_activity = (LoginActivity) getActivity();
                    mother_activity.toFragLogin();

                }
                break;

            case R.id.to_login:
                LoginActivity mother_activity = (LoginActivity) getActivity();
                mother_activity.toFragLogin();

                break;
                
        }
    }


    private User attemptRegister()
    {
        User validated_user ;

        String name = input_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();
        String confirm_password = input_confirm_password.getText().toString();

        if (!LoginActivity.isNameValid(name)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_NAME, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else if (!LoginActivity.isEmailValid(email)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_EMAIL, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else if (!LoginActivity.isPasswordValid(password)) {
            Toast.makeText(getContext(), LoginActivity.INVALID_PASSWORD, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else if (!password.equals(confirm_password)) {
            Toast.makeText(getContext(), PASSWORDS_DONT_MATCH, Toast.LENGTH_SHORT).show();
            return null ;
        }
        else
        {
            datasource = new UsersDBDataSource(getContext());
            datasource.open();

            // performs login task
            validated_user = datasource.attemptUserRegistration(name, email, password);

            datasource.close();

            if (validated_user != null)
            {
                Toast.makeText(getContext(), REGISTRATION_SUCCESS, Toast.LENGTH_LONG).show();
                Log.d("register_success", validated_user.getName() + " | " + validated_user.getEmail());

                return validated_user ;
            }
            else {
                Toast.makeText(getContext(), REGISTRATION_FAIL, Toast.LENGTH_LONG).show();
                Log.d("register_fail", name + " could not be registered");

                return null ;
            }
        }
    }

}

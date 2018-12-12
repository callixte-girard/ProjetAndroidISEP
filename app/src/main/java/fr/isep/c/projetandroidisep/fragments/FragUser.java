package fr.isep.c.projetandroidisep.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.login.LoginActivity;


public class FragUser extends Fragment
{
    Button button_logout ;
    TextView user_name ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        /*
        MainActivity main_activity = (MainActivity) getActivity();
        Intent intent = main_activity.getIntent();
        boolean sign_out = intent.getBooleanExtra("sign_out", false);
        */
        String user_mail = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;

        user_name = view.findViewById(R.id.user_name);
        user_name.setText(user_mail);

        button_logout = view.findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try {
                    //Log.d("logout", user.getEmail());

                    MainActivity mother_activity = (MainActivity) getActivity();
                    /*
                    mother_activity.transferToFirebaseAuthActivity(true);
                    mother_activity.finish();
                    */
                    mother_activity.signOut();

                } catch (Exception e) {
                    Log.d("log_out_button", e.getMessage());
                }

            }
        });

        return view ;
    }


}

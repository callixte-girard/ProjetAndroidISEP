package fr.isep.c.projetandroidisep.frag_User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import fr.isep.c.projetandroidisep.*;


public class Frag_User extends Fragment
{
    Button button_sign_out ;
    TextView user_name ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        user_name = view.findViewById(R.id.user_name);
        String user_mail = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        user_name.setText(user_mail);

        button_sign_out = view.findViewById(R.id.button_sign_out);
        button_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try {
                    MainActivity mother_activity = (MainActivity) getActivity();

                    mother_activity.signOut();

                } catch (Exception e) {
                    Log.d("log_out_button", e.getMessage());
                }

            }
        });

        return view ;
    }


}

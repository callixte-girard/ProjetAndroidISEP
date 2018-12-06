package fr.isep.c.projetandroidisep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.login.LoginActivity;


public class FragUser extends Fragment implements View.OnClickListener
{
    Button button_logout ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        button_logout = view.findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MainActivity act = (MainActivity) getActivity();
                act.transferToLoginActivity();
            }
        });

        return view ;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_logout:
                SharedPreferences sp = getContext().getSharedPreferences("pipou", Context.MODE_PRIVATE);
                sp.edit().remove("logged").putBoolean("logged", false).commit();
        }
    }
}

package fr.isep.c.projetandroidisep;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.isep.c.projetandroidisep.*;


public class FragMyRecipes extends Fragment implements View.OnClickListener
{


    View view ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_my_recipes, container, false);


        return view ;
    }

    @Override
    public void onClick(View v) {

    }
}

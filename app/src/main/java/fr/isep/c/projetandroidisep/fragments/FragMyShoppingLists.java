package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.*;


public class FragMyShoppingLists extends Fragment
{
    SearchView filter_shopping_lists ;
    TextView number_shopping_lists ;
    RecyclerView my_shopping_lists ;
    FloatingActionButton create_shopping_list ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_shopping_lists, container, false);

        filter_shopping_lists = view.findViewById(R.id.filter_shopping_lists);

        number_shopping_lists = view.findViewById(R.id.number_shopping_lists);
        number_shopping_lists.setText("? shopping lists");

        my_shopping_lists = view.findViewById(R.id.my_shopping_lists);

        create_shopping_list = view.findViewById(R.id.create_shopping_list);
        create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Log.d("number_favorites", String.valueOf(
                        MainActivity.getFavoriteRecipes().size()));

                MainActivity.displayFrag_createShoppingList(getFragmentManager());
            }
        });


        return view ;
    }


}

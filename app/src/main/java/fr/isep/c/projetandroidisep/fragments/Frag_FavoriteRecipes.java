package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.adapters.Adapter_FavoriteRecipes;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Frag_FavoriteRecipes extends Fragment
{
    private MainActivity main_act ;

    private RecyclerView my_favorite_recipes ;
    //private SearchView filter_favorite_recipes ;
    private TextView number_favorite_recipes ;

    private static final String DEFAULT_COUNTER = "Fetching your favorite recipes...";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        main_act = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        number_favorite_recipes = view.findViewById(R.id.number_favorite_recipes);
        number_favorite_recipes.setText(DEFAULT_COUNTER);

        my_favorite_recipes = view.findViewById(R.id.my_favorite_recipes);

        initFavoritesList(); // to set decoration and layout
        updateFavoritesList(); // to fill it

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private void initFavoritesList()
    {
        my_favorite_recipes.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        my_favorite_recipes.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        my_favorite_recipes.addItemDecoration(itemDecor);


    }

    public void updateFavoritesList()
    {
        // favorites count
        int count = main_act.getFavoriteRecipes().size();
        number_favorite_recipes.setText(String.valueOf(count) + " favorite recipes");

        // custom adapter
        Adapter_FavoriteRecipes adapter = new Adapter_FavoriteRecipes(getContext());
        my_favorite_recipes.setAdapter(adapter);
    }






}

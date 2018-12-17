package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_MyRecipes;
import fr.isep.c.projetandroidisep.adapters.Adapter_SelectRecipes;


public class FragCreateShoppingList extends Fragment
{
    private RecyclerView select_favorite_recipes ;
    //private SearchView filter_favorite_recipes ;
    private Button button_create_shopping_list , button_back ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_shopping_list, container, false);

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        select_favorite_recipes = view.findViewById(R.id.select_favorite_recipes);

        button_create_shopping_list = view.findViewById(R.id.button_create_shopping_list);
        button_create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        select_favorite_recipes.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        select_favorite_recipes.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        select_favorite_recipes.addItemDecoration(itemDecor);


    }

    public void updateFavoritesList()
    {
        // custom adapter
        Adapter_SelectRecipes adapter = new Adapter_SelectRecipes
                (getContext(), MainActivity.getFavoriteRecipes());
        select_favorite_recipes.setAdapter(adapter);
    }






}

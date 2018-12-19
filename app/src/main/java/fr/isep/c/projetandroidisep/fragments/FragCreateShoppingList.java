package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_FavoriteRecipes;
import fr.isep.c.projetandroidisep.adapters.Adapter_SelectRecipes;
import fr.isep.c.projetandroidisep.myCustomTypes.Aliment;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class FragCreateShoppingList extends Fragment
{
    private RecyclerView select_favorite_recipes ;
    //private SearchView filter_favorite_recipes ;
    private Button button_create_shopping_list , button_back ;

    private static final String NOTHING_SELECTED = "You must choose at least one recipe." ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_shopping_list, container, false);

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        select_favorite_recipes = view.findViewById(R.id.select_favorite_recipes);

        button_create_shopping_list = view.findViewById(R.id.button_create_shopping_list);
        button_create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ArrayList<Recipe> lc_recipes = new ArrayList<>();

                // fills it
                for (Recipe rec : MainActivity.getFavoriteRecipes())
                {
                    if (rec.getSelected()) {
                        lc_recipes.add(rec);
                    }
                }

                // checks validity
                if (!lc_recipes.isEmpty()) {
                    ListeCourses lc = new ListeCourses(lc_recipes);

                    MainActivity.saveListeCourses(lc);

                    MainActivity.hideFrag_createShoppingList(getFragmentManager());
                } else {
                    Snackbar.make(v, NOTHING_SELECTED, Snackbar.LENGTH_LONG).show();
                }

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

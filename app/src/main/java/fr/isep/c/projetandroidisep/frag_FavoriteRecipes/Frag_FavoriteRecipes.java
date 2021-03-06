package fr.isep.c.projetandroidisep.frag_FavoriteRecipes;

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
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.frag_SearchRecipe.Listener_SearchRecipe_SelectIngredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.frag_SearchRecipe.Listener_SearchRecipe_AddRemove;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Frag_FavoriteRecipes extends Fragment
        implements Listener_FavoriteRecipes_AddRemove, Listener_FavoriteRecipes_SelectIngredient

{
    private View view ;

    private RecyclerView my_favorite_recipes ;
    private SearchView filter_favorite_recipes ;
    private TextView number_favorite_recipes ;

    private static final String DEFAULT_COUNTER = "Fetching your favorite recipes...";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

        filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);
        filter_favorite_recipes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("test_filterOn", s);
                return false;
            }
        });
        filter_favorite_recipes.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("test_filterOff", "");
                // restore filtered state to normal
                return false;
            }
        });

        number_favorite_recipes = view.findViewById(R.id.label);
        number_favorite_recipes.setText(DEFAULT_COUNTER);

        initFavoritesList(); // to set decoration, layout and adapter
        updateFavoritesList(((MainActivity) getActivity()).getFavoriteRecipes()); // to fill adapter

        return view ;
    }


    public void checkedListener_selectIngredient
            (View view, final int index_rec, final int index_ingr, boolean isChecked)
    {
        final Recipe rec = ((MainActivity) getActivity()).getFavoriteRecipes().get(index_rec);
        final Ingredient ingr = rec.getIngredients().get(index_ingr);

        ingr.setSelected(isChecked);
        Log.d("checkedListener_ingr", rec.getName() + " | " + ingr.getName() + " | " + isChecked);

        updateFavoritesList(((MainActivity) getActivity()).getFavoriteRecipes());

        // saves recipe in db
        ((MainActivity) getActivity()).saveRecipeInFavorites(rec);
    }



    public void checkedListener_myRecipes(View view, final int position, boolean isChecked)
    {
        final Recipe rec = ((MainActivity) getActivity()).getFavoriteRecipes().get(position);
        Log.d("checkedListener_favo", position + " | " + isChecked + " | " + rec.getName());

        if (isChecked)
        {

        }
        else {
            ((MainActivity) getActivity()).removeRecipeFromFavorites(rec);

            //backup if error
            Snackbar.make(view,
                        /* rec.getName() + */ MainActivity.REMOVED_SUCCESSFULLY,
                        Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelRecipeDeletion(rec);
                        }
                    }).show();
        }

        //updateFavoritesList(((MainActivity) getActivity()).getFavoriteRecipes());
    }


    private void cancelRecipeDeletion(Recipe rec) {
        Log.d("cancelRecipeDeletion", rec.getName());
        ((MainActivity) getActivity()).saveRecipeInFavorites(rec);
    }


    private ArrayList<Recipe> filterRecipes(ArrayList<Recipe> to_filter, String filter)
    {
        ArrayList<Recipe> filtered = new ArrayList<>();

        for (Recipe rec : to_filter) {
            if (rec.getName().toLowerCase().contains(filter)) {
                filtered.add(rec);
            }
        }

        return filtered ;
    }


    private void initFavoritesList()
    {
        my_favorite_recipes = view.findViewById(R.id.my_favorite_recipes);

        my_favorite_recipes.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...
        my_favorite_recipes.setNestedScrollingEnabled(false);

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        my_favorite_recipes.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        my_favorite_recipes.addItemDecoration(itemDecor);

        // custom adapter
        Adapter_FavoriteRecipes adapter = new Adapter_FavoriteRecipes
                (getContext(), this, this);
        my_favorite_recipes.setAdapter(adapter);
    }

    public void updateFavoritesList(ArrayList<Recipe> new_list)
    {
        // updates adapter
        ((Adapter_FavoriteRecipes) my_favorite_recipes.getAdapter()).updateFavoritesList(new_list);

        // and label
        updateFavoritesNumber();
    }


    private void updateFavoritesNumber()
    {
        // updates counter
        int count = ((MainActivity) getActivity()).getFavoriteRecipes().size();
        number_favorite_recipes.setText(String.valueOf(count) + " favorite recipes");
    }



}

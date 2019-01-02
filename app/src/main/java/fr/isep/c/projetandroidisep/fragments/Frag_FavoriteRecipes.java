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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchIngredients;
import fr.isep.c.projetandroidisep.interfaces.Response_FetchIngredients;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.recyclerViewAdapters.Adapter_FavoriteRecipes;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Frag_FavoriteRecipes extends Fragment
        implements Listener_AddRemoveRecipe
{
    private MainActivity main_act ;
    private View view ;

    private RecyclerView my_favorite_recipes ;
    private SearchView filter_favorite_recipes ;
    private TextView number_favorite_recipes ;

    private static final String DEFAULT_COUNTER = "Fetching your favorite recipes...";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        main_act = (MainActivity) getActivity();

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

        number_favorite_recipes = view.findViewById(R.id.number_favorite_recipes);
        number_favorite_recipes.setText(DEFAULT_COUNTER);

        initFavoritesList(); // to set decoration, layout and adapter
        updateFavoritesList(main_act.getFavoriteRecipes()); // to fill adapter

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void checkedListener_myRecipes(View view, final int position, boolean isChecked)
    {
        final Recipe rec = main_act.getFavoriteRecipes().get(position);

        Log.d("checkedListener_favo", position + " | " + isChecked + " | " + rec.getName());

        if (isChecked) {
            if (!rec.alreadyExists(main_act.getFavoriteRecipes())) main_act.saveRecipeInFavorites(rec);
        } else {
            main_act.removeRecipeFromFavorites(rec);

            //backup if error
            Snackbar.make(view,
                        rec.getName() + MainActivity.REMOVED_SUCCESSFULLY,
                        Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelRecipeDeletion(rec);
                        }
                    }).show();
        }
    }


    private void cancelRecipeDeletion(Recipe rec) {
        Log.d("cancelRecipeDeletion", rec.getName());
        main_act.saveRecipeInFavorites(rec);
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

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        my_favorite_recipes.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        my_favorite_recipes.addItemDecoration(itemDecor);

        // custom adapter
        Adapter_FavoriteRecipes adapter = new Adapter_FavoriteRecipes(getContext(), this);
        my_favorite_recipes.setAdapter(adapter);
    }

    public void updateFavoritesList(ArrayList<Recipe> new_list)
    {
        // updates adapter
        ((Adapter_FavoriteRecipes) my_favorite_recipes.getAdapter()).updateFavoritesList(new_list);

        // updates counter
        int count = main_act.getFavoriteRecipes().size();
        number_favorite_recipes.setText(String.valueOf(count) + " favorite recipes");

    }






}

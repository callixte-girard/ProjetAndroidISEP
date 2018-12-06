package fr.isep.c.projetandroidisep.myRecipes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.objects.Recette;
import fr.isep.c.projetandroidisep.searchRecipe.RecyclerViewAdapter_SearchRecipe;


public class FragMyRecipes extends Fragment implements View.OnClickListener
{
    private RecyclerView favorites_list ;


    private static ArrayList<Recette> favorite_recipes = new ArrayList<>();
    private static ArrayList<Recette> deleted_recipes_history = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        favorites_list = view.findViewById(R.id.favorites_list);
        initFavoritesList();

        return view ;
    }

    @Override
    public void onClick(View v) {

    }


    private void initFavoritesList()
    {
        favorites_list.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        favorites_list.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        favorites_list.addItemDecoration(itemDecor);

        // custom adapter
        RecyclerViewAdapter_SearchRecipe adapter = new RecyclerViewAdapter_SearchRecipe
                (getContext(), favorite_recipes);
        favorites_list.setAdapter(adapter);
    }


    public static void performAdd(Recette rec_to_add)
    {
        boolean success = addToFavorites(rec_to_add);

        if (success) {
            Log.d("favorites_add", rec_to_add.getNom() + " : " + "success");
        } else  {
            Log.d("favorites_add", rec_to_add.getNom() + " : " + "fail");
        }
    }

    public static void performDelete(Recette rec_to_delete)
    {
        // first records it into history
        //deleted_recipes_history.add(rec_to_delete);

        // remove from my favorite recipes
        boolean success = removeFromFavorites(rec_to_delete);

        if (success) {
            Log.d("favorites_remove", rec_to_delete.getNom() + " : " + "success");
        } else  {
            Log.d("favorites_remove", rec_to_delete.getNom() + " : " + "fail");
        }
    }


    private static boolean addToFavorites(Recette rec)
    {
        // returns result state.
        if (!rec.alreadyExists(favorite_recipes)) {
            favorite_recipes.add(rec);
            return true;
        } else {
            return false;
        }
    }

    private static boolean removeFromFavorites(Recette rec)
    {
        // returns result state.
        if (rec.alreadyExists(favorite_recipes)) {
            favorite_recipes.remove(rec);
            return true ;
        } else {
            return false ;
        }

    }


    public static ArrayList<Recette> getFavoriteRecipes() {
        return favorite_recipes ;
    }


}

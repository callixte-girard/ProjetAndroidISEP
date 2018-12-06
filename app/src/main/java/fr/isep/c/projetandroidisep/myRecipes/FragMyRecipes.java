package fr.isep.c.projetandroidisep.myRecipes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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



    public static boolean addToFavorites(Recette rec)
    {
        // returns result state.
        if (!rec.alreadyExists(favorite_recipes)) {
            favorite_recipes.add(rec);
            Log.d("favorites_add", rec.getNom() + " : " + "success");
            return true;
        } else {
            Log.d("favorites_add", rec.getNom() + " : " + "fail");
            return false;
        }
    }

    public static boolean removeFromFavorites(Recette rec)
    {
        // returns result state.
        if (rec.alreadyExists(favorite_recipes)) {
            favorite_recipes.remove(rec);
            Log.d("favorites_remove", rec.getNom() + " : " + "success");
            return true ;
        } else {
            Log.d("favorites_remove", rec.getNom() + " : " + "fail");
            return false ;
        }
    }


}

package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.objects.Recette;
import fr.isep.c.projetandroidisep.adapters.Adapter_SearchRecipe;


public class FragMyRecipes extends Fragment implements View.OnClickListener
{
    private RecyclerView favorites_list ;
    private SearchView favorites_filter ;
    private TextView favorites_number ;

    private DatabaseReference ref = MainActivity.current_user_ref.child("favorite_recipes");

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try
            {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                while (it.hasNext())
                {
                    Recette rec = it.next().getValue(Recette.class);
                    Log.d("favorite_recipes", rec.getUrl());
                }
            }
            catch (NullPointerException npe) {
                //////
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("onCancelled", databaseError.getMessage());
        }
    };

    private static ArrayList<Recette> favorite_recipes = new ArrayList<>();
    //private static ArrayList<Recette> deleted_recipes_history = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        favorites_list = view.findViewById(R.id.favorites_list);
        //favorites_filter = view.findViewById(R.id.favorites_filter);
        favorites_number = view.findViewById(R.id.favorites_number);

        // initialises the arraylist with favorite recipes
        fetchFavoriteRecipes();
        // creates recyclerview
        initFavoritesList();

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

        ref.removeEventListener(listener);
    }

    @Override
    public void onClick(View v) {

    }


    private void fetchFavoriteRecipes()
            // #### fetches from firebase and populates al
    {
        // init listener for favorites
        ref.addValueEventListener(listener);


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
        Adapter_SearchRecipe adapter = new Adapter_SearchRecipe
                (getContext(), favorite_recipes);
        favorites_list.setAdapter(adapter);
    }

/*
    public static void performAdd(Recette rec_to_add)
    {
        // returns result state.
        if (!rec_to_add.alreadyExists(favorite_recipes)) {
            favorite_recipes.add(rec_to_add);
            Log.d("favorites_add", rec_to_add.getNom() + " : " + "success");

        } else {
            Log.d("favorites_add", rec_to_add.getNom() + " : " + "fail");

        }
    }

    public static void performDelete(Recette rec_to_delete)
    {
        // first records it into history
        //deleted_recipes_history.add(rec_to_delete);

        // remove from my favorite recipes
        if (rec_to_delete.alreadyExists(favorite_recipes)) {
            favorite_recipes.remove(rec_to_delete);
            Log.d("favorites_remove", rec_to_delete.getNom() + " : " + "success");

        } else {
            Log.d("favorites_remove", rec_to_delete.getNom() + " : " + "fail");

        }
    }
*/


    public static ArrayList<Recette> getFavoriteRecipes() {
        return favorite_recipes ;
    }


}

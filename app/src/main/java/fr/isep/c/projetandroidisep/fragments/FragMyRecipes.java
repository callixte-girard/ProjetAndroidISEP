package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
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

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Iterator;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.adapters.Adapter_MyRecipes;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncResponse_FetchIngredients;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncTask_FetchIngredients;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncTask_SearchRecipe;
import fr.isep.c.projetandroidisep.customTypes.Ingredient;
import fr.isep.c.projetandroidisep.customTypes.Recette;
import fr.isep.c.projetandroidisep.myClasses.EncodingCorrecter;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;


public class FragMyRecipes extends Fragment
        implements AsyncResponse_FetchIngredients
{
    private RecyclerView favorites_list ;
    private SearchView favorites_filter ;
    private TextView favorites_number ;


    private static ArrayList<Recette> favorite_recipes = new ArrayList<>();
    //private static ArrayList<Recette> deleted_recipes_history = new ArrayList<>();

    private FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference favorite_recipes_ref = FirebaseDatabase.getInstance().getReference()
            .child(current_user.getUid()).child("favorite_recipes");


    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try
            {
                // 1) GET SAVED DATA
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                favorite_recipes.clear();

                while (it.hasNext())
                {
                    Recette rec = it.next().getValue(Recette.class);
                    Log.d("favorite_recipes", rec.getUrl());

                    favorite_recipes.add(rec);
                }

                // number of favorites
                int nb = favorite_recipes.size();
                favorites_number.setText(String.valueOf(nb) + " favorite recipes");

                // creates recyclerview
                initFavoritesList();

                // launches the method to parse ingredients of all favorite recipes
                performIngredientParseFavoriteRecipes();
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


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        favorites_list = view.findViewById(R.id.favorites_list);
        //favorites_filter = view.findViewById(R.id.favorites_filter);
        favorites_number = view.findViewById(R.id.favorites_number);

        // waiting label
        favorites_number.setText("Fetching your favorite recipes...");
        // initialises the arraylist with favorite recipes
        // ### INCLUDES CREATING RECYCLERVIEW
        favorite_recipes_ref.addValueEventListener(listener);

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

        favorite_recipes_ref.removeEventListener(listener);
    }


    @Override
    public void processFinish(Document doc)
    {
        ArrayList<Ingredient> ingr_list = new ArrayList<>();

        String[] html = ParseHtml.splitStringIntoLinesArray(doc.html());

        // --> extracts only needed lines
        ArrayList<String> extracted = ParseHtml.extractOnlyNeededLines
                (html
                        , "Mrtn.recipesData ="
                        , "Mrtn = Mrtn || {};"
                );
        //for (String s : extracted) { System.out.println(s); }

        // --> cleans the line with just what we need
        String subl = ParseHtml.extractOnlyNeededSubline(
                extracted.get(0),
                '"' + "ingredients" + '"' + ":[",
                "]}]};"
        ) ;

        // --> petite correction sur les pbs d'encodage
        subl = EncodingCorrecter.convertFromU00(subl);

        // --> splits the line into requests.
        String[] requests = subl.split("\\{");

        // --> ...finally, fetches ingredients
        for (String s : requests)
        {
            String[] attr_list = s.split(",");

            String name = "" ;
            String forme = "" ;
            double qty = 0 ;
            String unit = "" ;

            for (String s2 : attr_list)
            {
                s2 = ParseHtml.removeSpecifiedCharFromString(s2, '"');
                //System.out.println(s2);

                try
                {
                    String[] spl = s2.split(":");


                    if (spl[0].equals("name"))
                    {
                        name = spl[1] ;
                    }
                    else if (spl[0].equals("qty"))
                    {
                        qty = Double.parseDouble(spl[1]) ;
                    }
                    else if (spl[0].equals("unit"))
                    {
                        unit = spl[1].replaceAll("}", "") ;
                    }

                    // quand créer l'ingredient ?
                    if (s2.contains("}"))
                    {
                        String name_and_forme = Ingredient.splitsNomIntoNomAndForme(name);
                        String[] split_name = name_and_forme.split(",");
                        name = split_name[0].trim();
                        forme = split_name[1].trim();

                        // # MODE 1 : méthode publique, constructeur privé
                        Ingredient ingr = Ingredient.createNewOrMatchExistingAlim
                                (name, forme, qty, unit);
                        ingr_list.add(ingr);
                    }
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    Log.d("ingr_parse_error", ex.getMessage());
                }
            }
        }

        // finds corresponding
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
        Adapter_MyRecipes adapter = new Adapter_MyRecipes
                (getContext(), favorite_recipes);
        favorites_list.setAdapter(adapter);
    }


    protected void performIngredientParseFavoriteRecipes()
    {
        for (Recette rec : favorite_recipes)
        {
            Log.d("ingr_parsing", rec.getUrl());

            AsyncTask_FetchIngredients task_fetchIngredients = new AsyncTask_FetchIngredients();
            task_fetchIngredients.setDelegate(this);
            task_fetchIngredients.execute(rec.getUrl());

        }
    }


    public static ArrayList<Recette> getFavoriteRecipes() {
        return favorite_recipes ;
    }


}

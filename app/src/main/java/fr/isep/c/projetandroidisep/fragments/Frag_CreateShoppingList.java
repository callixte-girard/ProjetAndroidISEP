package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_CreateShoppingList;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Frag_CreateShoppingList extends Fragment
{
    private MainActivity main_act ;

    private TextView label ;
    private RecyclerView select_favorite_recipes ;
    //private SearchView filter_favorite_recipes ;
    private Button button_create_shopping_list , button_cancel ;

    private static final String NOTHING_SELECTED = "You must choose at least one recipe." ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        main_act = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_create_shopping_list, container, false);

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        label = view.findViewById(R.id.label);
        label.setText("Choose your recipes for this shopping list :");


        select_favorite_recipes = view.findViewById(R.id.select_favorite_recipes);

        button_create_shopping_list = view.findViewById(R.id.button_right);
        button_create_shopping_list.setText("Create Shopping List");
        button_create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ArrayList<Recipe> lc_recipes = new ArrayList<>();

                // fills it
                for (Recipe rec : main_act.getFavoriteRecipes())
                {
                    if (rec.getSelected()) {
                        lc_recipes.add(rec);
                    }
                }

                // checks validity
                if (!lc_recipes.isEmpty())
                {
                    // saves shopping list on the db
                    ListeCourses lc = new ListeCourses(lc_recipes);
                    main_act.saveShoppingList(lc);

                    // removes actual fragment and restores my shopping lists one
                    MainActivity act = (MainActivity) getActivity();
                    act.destroyFrag_createShoppingList();
                    act.displayFrag_myShoppingLists();
                }
                else {
                    Snackbar.make(v, NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        initChoicesList(); // to set decoration and layout
        updateChoicesFromFavorites(); // to fill it

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private void initChoicesList()
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

    public void updateChoicesFromFavorites()
    {
        // custom adapter
        Adapter_CreateShoppingList adapter = new Adapter_CreateShoppingList(
                getContext(),
                main_act.getFavoriteRecipes());
        select_favorite_recipes.setAdapter(adapter);
    }






}

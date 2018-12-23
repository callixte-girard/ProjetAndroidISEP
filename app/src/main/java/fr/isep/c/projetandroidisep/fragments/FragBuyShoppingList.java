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

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_BuyShoppingList;
import fr.isep.c.projetandroidisep.adapters.Adapter_CreateShoppingList;


public class FragBuyShoppingList extends Fragment
{
    private RecyclerView select_bought_aliments ;
    //private SearchView filter_favorite_recipes ;
    private Button button_done ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_buy_shopping_list, container, false);

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        select_bought_aliments = view.findViewById(R.id.select_bought_aliments);

        /*
        button_create_shopping_list = view.findViewById(R.id.butto);
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
                if (!lc_recipes.isEmpty())
                {
                    // saves shopping list on the db
                    ListeCourses lc = new ListeCourses(lc_recipes);
                    MainActivity.saveShoppingList(lc);

                    // removes actual fragment and restores my shopping lists one
                    MainActivity.destroyFrag_createShoppingList(getFragmentManager());
                    MainActivity.displayFrag_myShoppingLists(getFragmentManager());
                }
                else {
                    Snackbar.make(v, NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
                }

            }
        });
*/
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
        select_bought_aliments.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        select_bought_aliments.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        select_bought_aliments.addItemDecoration(itemDecor);


    }

    public void updateChoicesFromFavorites()
    {
        // custom adapter
        Adapter_BuyShoppingList adapter = new Adapter_BuyShoppingList(getContext());
        select_bought_aliments.setAdapter(adapter);
    }






}

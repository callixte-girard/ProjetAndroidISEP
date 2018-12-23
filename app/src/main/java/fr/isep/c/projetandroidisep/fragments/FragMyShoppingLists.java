package fr.isep.c.projetandroidisep.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.adapters.Adapter_FavoriteRecipes;
import fr.isep.c.projetandroidisep.adapters.Adapter_MyShoppingLists;


public class FragMyShoppingLists extends Fragment
{
    SearchView filter_shopping_lists ;
    TextView number_shopping_lists ;
    RecyclerView my_shopping_lists ;
    FloatingActionButton create_shopping_list ;

    private static final String DEFAULT_COUNTER = "Fetching your shopping lists...";
    private static final String FAVORITES_EMPTY = "You must add recipes in your favorites first ;)" ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_shopping_lists, container, false);

        filter_shopping_lists = view.findViewById(R.id.filter_shopping_lists);

        number_shopping_lists = view.findViewById(R.id.number_shopping_lists);
        number_shopping_lists.setText(DEFAULT_COUNTER);

        my_shopping_lists = view.findViewById(R.id.my_shopping_lists);

        create_shopping_list = view.findViewById(R.id.create_shopping_list);
        create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("number_favorites", String.valueOf(
                        MainActivity.getFavoriteRecipes().size()));

                if (MainActivity.getFavoriteRecipes().isEmpty())
                {
                    Snackbar.make(view, FAVORITES_EMPTY, Snackbar.LENGTH_SHORT).show();
                }
                else {
                    MainActivity act = (MainActivity) getActivity();
                    act.displayFrag_createShoppingList();
                }
            }
        });

        initShoppingLists();
        updateShoppingLists();

        return view ;
    }


    private void initShoppingLists()
    {
        my_shopping_lists.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        my_shopping_lists.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        my_shopping_lists.addItemDecoration(itemDecor);

    }


    public void updateShoppingLists()
    {
        // favorites count
        int count = MainActivity.getMyShoppingLists().size();
        number_shopping_lists.setText(String.valueOf(count) + " shopping lists");

        // custom adapter
        Adapter_MyShoppingLists adapter = new Adapter_MyShoppingLists(getContext());
        my_shopping_lists.setAdapter(adapter);
    }

}

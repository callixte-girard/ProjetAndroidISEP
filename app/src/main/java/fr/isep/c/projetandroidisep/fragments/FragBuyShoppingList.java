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
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_BuyShoppingList;
import fr.isep.c.projetandroidisep.adapters.Adapter_CreateShoppingList;


public class FragBuyShoppingList extends Fragment
{
    private MainActivity main_act = (MainActivity) getActivity();

    private TextView label ;
    private RecyclerView select_bought_aliments ;
    //private SearchView filter_favorite_recipes ;
    private Button button_done ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_buy_shopping_list, container, false);

        label = view.findViewById(R.id.label);
        label.setText("Check the aliments you bought :");

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        select_bought_aliments = view.findViewById(R.id.select_bought_aliments);

        button_done = view.findViewById(R.id.button_right);
        button_done.setText("OK");
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removes actual fragment and restores my shopping lists one
                MainActivity act = (MainActivity) getActivity();
                act.destroyFrag_buyShoppingList();
                act.displayFrag_myShoppingLists();
            }
        });

        initBuyShoppingList();

        return view ;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private void initBuyShoppingList()
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
/*
    private void updateBuyShoppingList()
    {
        // custom adapter
        Adapter_BuyShoppingList adapter = new Adapter_BuyShoppingList(getContext(), );
        select_bought_aliments.setAdapter(adapter);
    }
*/

}

package fr.isep.c.projetandroidisep.frag_BuyShoppingList;

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
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;


public class Frag_BuyShoppingList extends Fragment
    implements Listener_BuyIngredient
{
    private ListeCourses lc_buy ;

    private TextView label ;
    private RecyclerView select_bought_aliments ;
    //private SearchView filter_favorite_recipes ;
    private Button button_finish, button_back ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_buy_shopping_list, container, false);

        label = view.findViewById(R.id.label);
        label.setText("Check the aliments you bought :");

        //filter_favorite_recipes = view.findViewById(R.id.filter_favorite_recipes);

        select_bought_aliments = view.findViewById(R.id.select_bought_aliments);

        /*
        button_finish = view.findViewById(R.id.button_right);
        button_finish.setText("Finish");
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
            }
        });
        */

        button_back = view.findViewById(R.id.button_center);
        button_back.setText("Back to shopping lists");
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removes actual fragment and restores my shopping lists one
                ((MainActivity) getActivity()).destroyFrag_buyShoppingList();
                ((MainActivity) getActivity()).displayFrag_myShoppingLists();
            }
        });

        initBuyShoppingList();

        return view ;
    }

    public ListeCourses getShoppingListToBuy() {
        return this.lc_buy ;
    }

    public void setShoppingListToBuy(ListeCourses lc_buy) {
        this.lc_buy = lc_buy ;
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

        // custom adapter
        Adapter_BuyShoppingList adapter = new Adapter_BuyShoppingList
                (getContext(), lc_buy, this);
        select_bought_aliments.setAdapter(adapter);
    }

    public void checkedListener_buyIngredient(View view, int position, boolean isChecked)
    {
        final Ingredient ingr = lc_buy.getIngredients().get(position);

        ingr.setSelected(isChecked); // here it is more isBought than isSelected but it works

        ((MainActivity) getActivity()).saveShoppingList(lc_buy);
    }

}

package fr.isep.c.projetandroidisep.frag_MyShoppingLists;

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
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.*;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;


public class Frag_MyShoppingLists extends Fragment
    implements Listener_AddRemoveShoppingList, Listener_BuyShoppingList
{
    private SearchView filter_shopping_lists ;
    private TextView number_shopping_lists ;
    private RecyclerView my_shopping_lists ;
    private FloatingActionButton create_shopping_list ;

    private static final String DEFAULT_COUNTER = "Fetching your shopping lists...";
    private static final String FAVORITES_EMPTY = "You must add recipes in your favorites first ;)" ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_shopping_lists, container, false);

        filter_shopping_lists = view.findViewById(R.id.filter_shopping_lists);

        number_shopping_lists = view.findViewById(R.id.label);
        number_shopping_lists.setText(DEFAULT_COUNTER);

        my_shopping_lists = view.findViewById(R.id.my_shopping_lists);

        create_shopping_list = view.findViewById(R.id.create_shopping_list);
        create_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("number_favorites", String.valueOf(
                        ((MainActivity) getActivity()).getFavoriteRecipes().size()));

                if (((MainActivity) getActivity()).getFavoriteRecipes().isEmpty())
                {
                    Snackbar.make(view, FAVORITES_EMPTY, Snackbar.LENGTH_SHORT).show();
                }
                else {
                    ((MainActivity) getActivity()).displayFrag_createShoppingList();
                }
            }
        });

        initShoppingLists();
        updateShoppingLists(((MainActivity) getActivity()).getMyShoppingLists());

        return view ;
    }


    public void clickListener_myShoppingLists(View v, final int position)
    {
        final ListeCourses lc = ((MainActivity) getActivity()).getMyShoppingLists().get(position);

        ((MainActivity) getActivity()).displayFrag_buyShoppingList(lc);
    }


    public void checkedListener_myShoppingLists(View view, final int position, boolean isChecked)
    {
        final ListeCourses lc = ((MainActivity) getActivity()).getMyShoppingLists().get(position);

        Log.d("checkedListener_favo", position + " | " + isChecked + " | " + lc.getDateCreation());

        if (isChecked) {
            ((MainActivity) getActivity()).saveShoppingList(lc);
        } else {
            ((MainActivity) getActivity()).removeShoppingList(lc);

            //backup if error
            Snackbar.make(view,
                    "Shopping list created on " + lc.getDateCreation() + MainActivity.REMOVED_SUCCESSFULLY,
                    Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelShoppingListDeletion(lc);
                        }
                    }).show();
        }
    }


    private void cancelShoppingListDeletion(ListeCourses lc) {
        Log.d("cancelShoppingListDel", lc.getDateCreation());
        ((MainActivity) getActivity()).saveShoppingList(lc);
    }


    private void initShoppingLists()
    {
        my_shopping_lists.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...
        my_shopping_lists.setNestedScrollingEnabled(false);

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        my_shopping_lists.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        my_shopping_lists.addItemDecoration(itemDecor);

        // custom adapter
        Adapter_MyShoppingLists adapter = new Adapter_MyShoppingLists
                (getContext(), this, this);
        my_shopping_lists.setAdapter(adapter);
    }


    public void updateShoppingLists(ArrayList<ListeCourses> new_list)
    {
        // updates adapter
        ((Adapter_MyShoppingLists) my_shopping_lists.getAdapter()).updateShoppingLists(new_list);

        // updates counter
        int count = ((MainActivity) getActivity()).getMyShoppingLists().size();
        number_shopping_lists.setText(String.valueOf(count) + " shopping lists");
    }

}

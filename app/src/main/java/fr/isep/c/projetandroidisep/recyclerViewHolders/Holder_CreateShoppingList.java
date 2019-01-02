package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;

public class Holder_CreateShoppingList extends RecyclerView.ViewHolder
        implements View.OnClickListener {


    public TextView recipe_name, recipe_duration ;
    public CheckBox checkbox_select_recipe ;


    public Holder_CreateShoppingList(View view)
    {
        super(view);

        recipe_name = view.findViewById(R.id.title);
        recipe_duration = view.findViewById(R.id.subtitle);
        checkbox_select_recipe = view.findViewById(R.id.checkbox);
    }

    @Override
    public void onClick(View view) {

    }
}


package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;

public class Holder_BuyShoppingList extends RecyclerView.ViewHolder
        implements View.OnClickListener {


    public TextView ingr_name_form, ingr_qty_unit ;
    public CheckBox checkbox_bought ;

    public Holder_BuyShoppingList(View view)
    {
        super(view);

        ingr_name_form = view.findViewById(R.id.ingr_name_form);
        ingr_qty_unit = view.findViewById(R.id.ingr_qty_unit);
        checkbox_bought = view.findViewById(R.id.checkbox);
    }

    @Override
    public void onClick(View view) {

    }
}
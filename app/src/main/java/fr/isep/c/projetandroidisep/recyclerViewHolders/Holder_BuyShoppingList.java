package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;

public class Holder_BuyShoppingList extends RecyclerView.ViewHolder
        implements View.OnClickListener {


    private TextView ingr_name, ingr_qty ;
    private CheckBox checkbox_bought ;

    public Holder_BuyShoppingList(View view)
    {
        super(view);

        ingr_name = view.findViewById(R.id.title);
        ingr_qty = view.findViewById(R.id.subtitle);
        checkbox_bought = view.findViewById(R.id.checkbox);
    }

    @Override
    public void onClick(View view) {

    }
}
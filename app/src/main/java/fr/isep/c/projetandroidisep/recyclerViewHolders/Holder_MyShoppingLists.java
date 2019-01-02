package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;

public class Holder_MyShoppingLists extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public LinearLayout lc_header ;
    public TextView lc_name, lc_date_creation ;
    public CheckBox checkbox_delete_shopping_list ;


    public Holder_MyShoppingLists(View view)
    {
        super(view);

        lc_header = view.findViewById(R.id.header_vertical);
        lc_name = view.findViewById(R.id.title);
        lc_date_creation = view.findViewById(R.id.subtitle);
        checkbox_delete_shopping_list = view.findViewById(R.id.checkbox_add_remove);
    }

    @Override
    public void onClick(View view) {

    }
}
package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_BuyIngredient;
import fr.isep.c.projetandroidisep.interfaces.Listener_SelectIngredient;

public class Holder_IngredientGrid
        extends RecyclerView.ViewHolder
        implements View.OnClickListener
{
    private MainActivity main_act ;

    private Listener_SelectIngredient listener_selectIngredient ;

    public TextView ingr_name_form, ingr_qty, ingr_unit ;
    public CheckBox chkbx_ingr_select ;


    public Holder_IngredientGrid(Context context, View view
            , Listener_SelectIngredient listener_selectIngredient
    ) {
        super(view);

        this.main_act = (MainActivity) context ;
        this.listener_selectIngredient = listener_selectIngredient;

        ingr_name_form = view.findViewById(R.id.ingr_name_form);
        ingr_qty = view.findViewById(R.id.ingr_qty);
        ingr_unit = view.findViewById(R.id.ingr_unit);
        chkbx_ingr_select = view.findViewById(R.id.checkbox);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
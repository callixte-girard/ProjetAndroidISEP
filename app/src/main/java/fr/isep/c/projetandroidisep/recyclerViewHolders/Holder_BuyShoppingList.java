package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Listener_BuyIngredient;

public class Holder_BuyShoppingList
        extends RecyclerView.ViewHolder
        implements View.OnClickListener
{
    private MainActivity main_act ;
    private Listener_BuyIngredient listener_buyIngredient ;

    public TextView ingr_name_form, ingr_qty_unit ;
    public CheckBox checkbox_bought ;


    public Holder_BuyShoppingList(Context context, View view
            , Listener_BuyIngredient listener_buyIngredient
    ) {
        super(view);

        this.main_act = (MainActivity) context ;
        this.listener_buyIngredient = listener_buyIngredient ;

        ingr_name_form = view.findViewById(R.id.ingr_name_form);
        ingr_qty_unit = view.findViewById(R.id.ingr_qty_unit);
        checkbox_bought = view.findViewById(R.id.checkbox);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
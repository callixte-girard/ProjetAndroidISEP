package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Aliment;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_BuyShoppingList;


public class Adapter_BuyShoppingList
        extends RecyclerView.Adapter<Holder_BuyShoppingList>
{
    private MainActivity main_act ;
    private ArrayList<Ingredient> al = new ArrayList<>();


    public Adapter_BuyShoppingList(Context context, ListeCourses lc) {
        this.main_act = (MainActivity) context ;
        this.al.addAll(lc.getIngredients());
    }


    @Override
    public Holder_BuyShoppingList onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2cells, viewGroup, false);

        return new Holder_BuyShoppingList(v);
    }

    @Override
    public void onBindViewHolder(final Holder_BuyShoppingList holder, final int i)
    {
        final Ingredient ingr = al.get(i);

        holder.ingr_name_form.setText(ingr.returnNameAndForm());

        holder.ingr_qty_unit.setText(ingr.getQty() + " " + ingr.getUnit());

        holder.checkbox_bought.setChecked(ingr.isSelected());
    }

    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }

}

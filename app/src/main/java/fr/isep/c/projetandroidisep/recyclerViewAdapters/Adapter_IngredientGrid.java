package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_BuyIngredient;
import fr.isep.c.projetandroidisep.interfaces.Listener_SelectIngredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_IngredientGrid;


public class Adapter_IngredientGrid
        extends RecyclerView.Adapter<Holder_IngredientGrid>
        //    implements Listener_SelectIngredient
{
    private MainActivity main_act ;
    //private Recipe rec ;
    private int index_rec ;
    private ArrayList<Ingredient> al = new ArrayList<>();

    private Listener_SelectIngredient listener_selectIngredient;


    public Adapter_IngredientGrid(Context context
         //   , Recipe rec
           , int index_rec
        , Listener_SelectIngredient listener_selectIngredient
    ) {
        this.main_act = (MainActivity) context ;
        //this.rec = rec ;
        this.index_rec = index_rec ;
        this.al.addAll(this.main_act.getFavoriteRecipes().get(index_rec).getIngredients());
        this.listener_selectIngredient = listener_selectIngredient ;
    }




    @Override
    public Holder_IngredientGrid onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_grid_chkbx, viewGroup, false);

        return new Holder_IngredientGrid(main_act, v, listener_selectIngredient);
    }

    @Override
    public void onBindViewHolder(final Holder_IngredientGrid holder, final int i)
    {
        final Ingredient ingr = al.get(i);

        holder.ingr_name_form.setText(ingr.returnNameAndForm());

        if (ingr.getQty() != 0) {
            holder.ingr_qty.setText(String.valueOf(ingr.getQty()));
            holder.ingr_unit.setText(ingr.getUnit());
        }

        holder.ingr_qty.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        holder.ingr_unit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        holder.chkbx_ingr_select.setChecked(ingr.isSelected());
        holder.chkbx_ingr_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_selectIngredient.checkedListener_selectIngredient
                        (buttonView, index_rec, holder.getAdapterPosition(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }

}

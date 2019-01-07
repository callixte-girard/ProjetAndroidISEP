package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Listener_SelectIngredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_FavoriteRecipes;


public class Adapter_FavoriteRecipes
        extends RecyclerView.Adapter<Holder_FavoriteRecipes>
{
    private MainActivity main_act ;
    private ArrayList<Recipe> al = new ArrayList<>();
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private Listener_SelectIngredient listener_selectIngredient ;


    public Adapter_FavoriteRecipes(Context context
            , Listener_AddRemoveRecipe listener_addRemoveRecipe
            , Listener_SelectIngredient listener_selectIngredient
    ) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        this.listener_selectIngredient = listener_selectIngredient ;
        updateFavoritesList(main_act.getFavoriteRecipes());
    }


    @Override
    public Holder_FavoriteRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_FavoriteRecipes(v, listener_addRemoveRecipe, listener_selectIngredient);
    }


    @Override
    public void onBindViewHolder(final Holder_FavoriteRecipes holder, final int i)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // checkboxes
        //holder.checkbox_show_expandable.setChecked(rec.show_expandable);
        holder.checkbox_show_expandable.setChecked(false);
        holder.checkbox_show_expandable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("show_expandable", rec.getName() + " | " + isChecked);

                // hides or show panel
                //rec.show_expandable = isChecked ;

                holder.hideShowExpandableList(isChecked);
            }
        });

        holder.checkbox_delete_from_favorites.setChecked(true);
        holder.checkbox_delete_from_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_myRecipes
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

        initIngredientGrid(holder.getAdapterPosition(), holder);
    }


    private void initIngredientGrid(int index_rec, Holder_FavoriteRecipes holder)
    {
        holder.recipe_ingr_expandable.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main_act);
        holder.recipe_ingr_expandable.setLayoutManager(linearLayoutManager);
/*
        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (main_act, linearLayoutManager.getOrientation());
        holder.recipe_ingr_expandable.addItemDecoration(itemDecor);
*/
        // custom adapter
        Adapter_IngredientGrid adapter = new Adapter_IngredientGrid
                (main_act, index_rec, this.listener_selectIngredient);
        holder.recipe_ingr_expandable.setAdapter(adapter);

        // default : hidden
        holder.hideShowExpandableList(false);

    }


    public void updateFavoritesList(ArrayList<Recipe> al) {
        this.al.clear();
        this.al.addAll(al);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }

}

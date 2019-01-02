package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
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
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_FavoriteRecipes;


public class Adapter_FavoriteRecipes
        extends RecyclerView.Adapter<Holder_FavoriteRecipes>
{
    private MainActivity main_act ;
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private ArrayList<Recipe> al = new ArrayList<>();


    public Adapter_FavoriteRecipes(Context context, Listener_AddRemoveRecipe listener_addRemoveRecipe) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        updateFavoritesList(main_act.getFavoriteRecipes());
    }


    @Override
    public Holder_FavoriteRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_FavoriteRecipes(v, listener_addRemoveRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_FavoriteRecipes holder, final int i)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_labels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.show_expandable = !holder.show_expandable ;
                Log.d("show_expandable", rec.getName() + " | " + holder.show_expandable);

                if (holder.show_expandable) {
                    holder.recipe_ingr_expandable.setVisibility(View.VISIBLE);
                } else {
                    holder.recipe_ingr_expandable.setVisibility(View.GONE);
                }
            }
        });

        // checkboxes
        holder.checkbox_delete_from_favorites.setChecked(true);
        holder.checkbox_delete_from_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_myRecipes
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

        LinearLayout expandable_ingr_list = holder.recipe_ingr_expandable ;
        expandable_ingr_list = buildIngredientsExpandableList(rec, expandable_ingr_list);
        // default : hidden
        expandable_ingr_list.setVisibility(View.GONE);
    }


    private LinearLayout buildIngredientsExpandableList(Recipe rec, LinearLayout expandable_ingr_list)
    {
        for (Ingredient ingr : rec.getIngredients())
        {
            TextView tv_ingr = new TextView(main_act);
            tv_ingr.setText(" - " + ingr.returnNameAndForm());
            //tv_ingr.setTextColor(tv_ingr.getResources().getColor(R.color.black));
            expandable_ingr_list.addView(tv_ingr);
        }
        return expandable_ingr_list ;
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

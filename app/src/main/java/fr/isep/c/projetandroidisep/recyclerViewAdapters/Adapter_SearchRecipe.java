package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_SearchRecipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Holder_SearchRecipe>
{
    private MainActivity main_act;
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private ArrayList<Recipe> al ;

    private boolean show_expandable = false ;


    public Adapter_SearchRecipe(Context context, Listener_AddRemoveRecipe listener_addRemoveRecipe) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
    }


    @Override
    public Holder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_SearchRecipe(v, this.listener_addRemoveRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, final int position)
    {
        //Recipe rec = main_act.getSearchResults().get(holder.getAdapterPosition());
        Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check that expandable is filled

                show_expandable = !show_expandable ;

                if (show_expandable) {
                    holder.recipe_ingr_expandable.setVisibility(View.VISIBLE);
                } else {
                    holder.recipe_ingr_expandable.setVisibility(View.GONE);
                }
            }
        });

        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_myRecipes(buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

        LinearLayout expandable_ingr_list = holder.recipe_ingr_expandable ;
        expandable_ingr_list.setVisibility(View.GONE); // default : hidden

        // check ingredients list
        if (rec.getIngredients().isEmpty()) {
            // launch asynctask
            main_act.performFetchRecipeIngredients(rec);

        } else { // !!!!!!!!!!!! THIS PART IS WRONGLY DONE !!!!!!!!!!!!!!!!!!
            // dynamically adds ingredient views to child LinearLayout
            for (Ingredient ingr : rec.getIngredients())
            {
                TextView tv_ingr = new TextView(main_act);
                tv_ingr.setText(" - " + ingr.returnNameAndForm());
                //tv_ingr.setTextColor(tv_ingr.getResources().getColor(R.color.black));
                expandable_ingr_list.addView(tv_ingr);

                CheckBox cb_ingr = new CheckBox(main_act);
                cb_ingr.setChecked(ingr.getSelected());
                // suite
            }
        }

    }

    public void updateResultsList(ArrayList<Recipe> al) {
        this.al = al ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return (null != al ? al.size() : 0);
    }
}

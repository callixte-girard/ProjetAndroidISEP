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

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchIngredients;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Response_FetchIngredients;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_SearchRecipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Holder_SearchRecipe>
{
    private MainActivity main_act;
    private ArrayList<Recipe> al ;

    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;


    public Adapter_SearchRecipe(Context context
            , Listener_AddRemoveRecipe listener_addRemoveRecipe
    ) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
    }


    @Override
    public Holder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_SearchRecipe(main_act, v, this.listener_addRemoveRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, final int position)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.show_expandable = !holder.show_expandable ;
                Log.d("show_expandable", rec.getName() + " | " + holder.show_expandable);

                Recipe rec_corresponding = Recipe.getByUrl(main_act.getFavoriteRecipes(), rec.getUrl());

                if (rec_corresponding != null) {
                    // updates actual recipe with corresponding favorite object's ingr list
                    rec.setIngredients(rec_corresponding.getIngredients());
                    // now updates UI
                    holder.buildIngredientsExpandableList(rec, holder.recipe_ingr_expandable);
                }
                else {
                    // launch asynctask
                    holder.performFetchRecipeIngredients(rec);
                }

                // displays it if available (peut etre à déplacer dans un if ou autre)
                if (!rec.getIngredients().isEmpty())
                {
                    if (holder.show_expandable) {
                        holder.recipe_ingr_expandable.setVisibility(View.VISIBLE);
                    } else {
                        holder.recipe_ingr_expandable.setVisibility(View.GONE);
                    }
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

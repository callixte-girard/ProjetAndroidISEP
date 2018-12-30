package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recycleViewHolders.Holder_SearchRecipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Holder_SearchRecipe>
{
    private MainActivity main_act;
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private ArrayList<Recipe> al ;


    public Adapter_SearchRecipe(Context context, Listener_AddRemoveRecipe listener_addRemoveRecipe) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
    }


    @Override
    public Holder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2_lines, viewGroup, false);

        return new Holder_SearchRecipe(v, this.listener_addRemoveRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, final int position)
    {
        Recipe rec = main_act.getSearchResults().get(holder.getAdapterPosition());

        //set values of data here
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_myRecipes(buttonView, holder.getAdapterPosition(), isChecked);
            }
        });
    }

    public void updateResultsList(ArrayList<Recipe> al) {
        this.al = al ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return (null != main_act.getSearchResults()
                ? main_act.getSearchResults().size() : 0);
    }
}

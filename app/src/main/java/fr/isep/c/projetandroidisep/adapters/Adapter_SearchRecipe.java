package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.database.snapshot.Index;

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
    private ArrayList<Recipe> list_results = new ArrayList<>();


    public Adapter_SearchRecipe(Context context, Listener_AddRemoveRecipe listener_addRemoveRecipe) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
    }

    public void updateResults(ArrayList<Recipe> new_results) {
        this.list_results.clear();
        this.list_results.addAll(new_results);
        notifyDataSetChanged();
    }

    public void clearResults() {
        this.list_results.clear();
        notifyDataSetChanged();
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
        //Recipe rec = main_act.getSearchResults().get(position);
        Recipe rec = list_results.get(position);

        //set values of data here
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_searchRecipe(buttonView, position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != list_results
                ? list_results.size() : 0);
    }
}

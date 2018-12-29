package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_SearchRecipe;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recycleViewHolders.Holder_SearchRecipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Holder_SearchRecipe>
{
    private MainActivity main_act;

    private Listener_SearchRecipe listener_searchRecipe ;
    private ArrayList<Recipe> list_results = new ArrayList<>();


    public Adapter_SearchRecipe(Context context, Listener_SearchRecipe listener_searchRecipe) {
        this.main_act = (MainActivity) context ;
        this.listener_searchRecipe = listener_searchRecipe ;
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

        return new Holder_SearchRecipe(v, this.listener_searchRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, int position)
    {
        //Recipe rec = main_act.getSearchResults().get(position);
        Recipe rec = list_results.get(position);

        //set values of data here
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());
        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
    }

    @Override
    public int getItemCount()
    {
        return (null != list_results
                ? list_results.size() : 0);
    }
}

package fr.isep.c.projetandroidisep.searchRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myRecipes.FragMyRecipes;
import fr.isep.c.projetandroidisep.objects.Recette;


public class Adapter_SearchRecipe extends RecyclerView.Adapter
            <Adapter_SearchRecipe
                .RecyclerViewHolder_SearchRecipe>
{
    static class RecyclerViewHolder_SearchRecipe extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {

        private TextView recipe_name ;
        private CheckBox checkbox_add_to_favorites ;

        RecyclerViewHolder_SearchRecipe(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.recipe_name);
            checkbox_add_to_favorites = view.findViewById(R.id.checkbox_add_to_favorites);
        }

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.checkbox_add_to_favorites:
                    Log.d("test", "search_recipe");
            }
        }
    }

    private Context context ;
    private ArrayList<Recette> al ;
    private SparseBooleanArray bool_arr;


    public Adapter_SearchRecipe(Context context, ArrayList<Recette> al)
    {
        this.al = al;
        this.context = context;
        bool_arr = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row_search_layout, viewGroup, false);

        // pre-checks recipes that are already present in favorites
        //bool_arr = preCheckAlreadyFavoriteRecipes(bool_arr);

        return new RecyclerViewHolder_SearchRecipe(v);
    }

    private SparseBooleanArray preCheckAlreadyFavoriteRecipes(SparseBooleanArray bool_arr)
    {
        for (int i=0 ; i < getItemCount() ; i++)
        {
            Recette rec = getRecetteAtPosition(i);
            bool_arr.put(i, rec.alreadyExists(FragMyRecipes.getFavoriteRecipes()));
        }
        return bool_arr ;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SearchRecipe holder, final int i)
    {
        holder.recipe_name.setText(al.get(i).getNom());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////// action that happens when the NAME is pressed (not the checkbox)
            }
        });

        Recette rec = FragSearchRecipe.getSearchResults().get(i);
        boolean already_favorite = rec.alreadyExists(FragMyRecipes.getFavoriteRecipes()) ;
        Log.d(rec.getUrl(), String.valueOf(already_favorite));

        holder.checkbox_add_to_favorites.setChecked(already_favorite);
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Recette rec = getRecetteAtPosition(i);

                Log.d("is_checked",
                        rec.getNom() + " | " + String.valueOf(isChecked));

                if (isChecked) {
                    // add to favorite recipes
                    FragMyRecipes.performAdd(rec);
                } else {
                    // remove from my favorite recipes
                    FragMyRecipes.performDelete(rec);
                }

                //notifyDataSetChanged();
            }
        });
    }

    public Recette getRecetteAtPosition(int position) {
        return al.get(position);
    }

    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }

    /**
     * Check the Checkbox if not checked
     **/
    private void addOrRemoveFromFavoriteRecipes(int position, boolean already_favorite)
    {
        Recette rec_to_add_or_remove = getRecetteAtPosition(position);

        if (already_favorite) {
            bool_arr.put(position, true);
            // add recipe to favorites

        } else {
            bool_arr.delete(position);
            // remove recipe from favorites
            ////
        }

        //Log.d("add_to_recipes", String.valueOf(bool_arr.get(position)));
        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return bool_arr;
    }
}

package fr.isep.c.projetandroidisep.adapters;

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

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.fragments.FragSearchRecipe;


public class Adapter_SearchRecipe extends RecyclerView.Adapter
            <Adapter_SearchRecipe.RecyclerViewHolder_SearchRecipe>
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
            checkbox_add_to_favorites = view.findViewById(R.id.checkbox_favorite);
        }

        @Override
        public void onClick(View view)
        {

        }
    }

    private Context context ;
    private ArrayList<Recipe> al ;
    private SparseBooleanArray bool_arr;


    public Adapter_SearchRecipe(Context context, ArrayList<Recipe> al)
    {
        this.al = al;
        this.context = context;
        bool_arr = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row_recipes_layout, viewGroup, false);

        return new RecyclerViewHolder_SearchRecipe(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SearchRecipe holder, final int i)
    {
        holder.recipe_name.setText(al.get(i).getName());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////// action that happens when the NAME is pressed (not the checkbox)
            }
        });

        Recipe rec = FragSearchRecipe.getSearchResults().get(i);
        boolean already_favorite = rec.alreadyExists(MainActivity.getFavoriteRecipes()) ;
        //Log.d(rec.getUrl(), String.valueOf(already_favorite));

        holder.checkbox_add_to_favorites.setChecked(already_favorite);
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Recipe rec = getRecipeAtPosition(i);

                Log.d("is_checked",
                        rec.getName() + " | " + String.valueOf(isChecked));

                if (isChecked) {
                    MainActivity.saveRecipeInFavorites(rec);

                } else {
                    MainActivity.removeRecipeFromFavorites(rec);
                }

                //notifyDataSetChanged();
            }
        });
    }

    public Recipe getRecipeAtPosition(int position) {
        return al.get(position);
    }

    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }


    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return bool_arr;
    }


}

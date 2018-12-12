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

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.fragments.FragMyRecipes;
import fr.isep.c.projetandroidisep.objects.Recette;
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
            checkbox_add_to_favorites = view.findViewById(R.id.checkbox_add_to_favorites);
        }

        @Override
        public void onClick(View view)
        {

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
                    saveRecipeInFavorites(rec);

                } else {
                    removeRecipeFromFavorites(rec);
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

    protected void saveRecipeInFavorites(Recette rec) {

        // quickly splits url to get only the short
        String[] spl_slash = rec.getUrl().split("/");
        int index_dot = spl_slash[4].indexOf(".");
        String short_url = spl_slash[4].substring(0, index_dot);

        //Log.d("short_url", spl_slash[4]);
        Log.d("short_url", short_url);

        MainActivity.current_user_ref.child("favorite_recipes")
                .child(short_url)
                .setValue(rec);
    }

    protected void removeRecipeFromFavorites(Recette rec) {
        //MainActivity.current_user_ref.child("favorite_recipes");
    }


    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return bool_arr;
    }


}

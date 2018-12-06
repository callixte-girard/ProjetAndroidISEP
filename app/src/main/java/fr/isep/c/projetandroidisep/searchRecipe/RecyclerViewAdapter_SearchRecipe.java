package fr.isep.c.projetandroidisep.searchRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.objects.Recette;


public class RecyclerViewAdapter_SearchRecipe
        extends RecyclerView.Adapter
            <RecyclerViewAdapter_SearchRecipe
                .RecyclerViewHolder_SearchRecipe>
{
    static class RecyclerViewHolder_SearchRecipe extends RecyclerView.ViewHolder {

        private TextView recipe_name ;
        private CheckBox add_to_favorites ;

        RecyclerViewHolder_SearchRecipe(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.recipe_name);
            add_to_favorites = view.findViewById(R.id.add_to_favorites);
        }
    }

    private Context context ;
    private ArrayList<Recette> al ;
    private SparseBooleanArray mSelectedItemsIds;


    public RecyclerViewAdapter_SearchRecipe(Context context, ArrayList<Recette> al)
    {
        this.al = al;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row_checkbox_layout, viewGroup, false);

        return new RecyclerViewHolder_SearchRecipe(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SearchRecipe holder, final int i)
    {
        holder.recipe_name.setText(al.get(i).getNom());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveFromFavoriteRecipes(i, !mSelectedItemsIds.get(i));
            }
        });

        holder.add_to_favorites.setChecked(mSelectedItemsIds.get(i));
        holder.add_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveFromFavoriteRecipes(i, !mSelectedItemsIds.get(i));
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
    public void addOrRemoveFromFavoriteRecipes(int position, boolean already_favorite)
    {
        Recette rec_to_add_or_remove = getRecetteAtPosition(position);

        if (already_favorite) {
            mSelectedItemsIds.put(position, true);
            // add recipe to favorites
            ////
        } else {
            mSelectedItemsIds.delete(position);
            // remove recipe from favorites
            ////
        }

        //Log.d("add_to_recipes", String.valueOf(mSelectedItemsIds.get(position)));
        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}

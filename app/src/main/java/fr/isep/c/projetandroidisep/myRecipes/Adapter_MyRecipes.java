package fr.isep.c.projetandroidisep.myRecipes;

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
import fr.isep.c.projetandroidisep.objects.Recette;


public class Adapter_MyRecipes
        extends RecyclerView.Adapter
            <Adapter_MyRecipes
                .RecyclerViewHolder_SearchRecipe>
{

    static class RecyclerViewHolder_SearchRecipe extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView recipe_name ;
        private CheckBox checkbox_delete_from_favorites ;


        RecyclerViewHolder_SearchRecipe(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.recipe_name);
            checkbox_delete_from_favorites = view.findViewById(R.id.checkbox_delete_from_favorites);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.checkbox_delete_from_favorites:
                    Log.d("test", "my_recipes");
            }
        }
    }

    private Context context ;
    private ArrayList<Recette> al ;
    private SparseBooleanArray mSelectedItemsIds;


    public Adapter_MyRecipes(Context context, ArrayList<Recette> al)
    {
        this.al = al;
        this.context = context;
        //mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row_favorites_layout, viewGroup, false);

        return new RecyclerViewHolder_SearchRecipe(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SearchRecipe holder, final int i)
    {
        holder.recipe_name.setText(al.get(i).getNom());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.checkbox_delete_from_favorites.setChecked(mSelectedItemsIds.get(i));
        holder.checkbox_delete_from_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Recette rec = getRecetteAtPosition(i);

                Log.d("is_checked",
                        rec.getNom() + " | " + String.valueOf(isChecked));

                if (isChecked) {

                    FragMyRecipes.performDelete(rec);

                    // change icon : possibility to revert

                } else {
                    // adds back recipe (revert deletion)
                    FragMyRecipes.performAdd(rec);

                    // change icon : delete

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
    public void addOrRemoveFromFavoriteRecipes(int position, boolean already_favorite)
    {
        Recette rec_to_add_or_remove = getRecetteAtPosition(position);

        if (already_favorite) {
            mSelectedItemsIds.put(position, true);
            // add recipe to favorites

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
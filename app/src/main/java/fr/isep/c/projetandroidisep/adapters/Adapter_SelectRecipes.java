package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


public class Adapter_SelectRecipes extends RecyclerView.Adapter
            <Adapter_SelectRecipes
                .RecyclerViewHolder_SelectRecipes>
{

    static class RecyclerViewHolder_SelectRecipes extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView recipe_name ;
        private CheckBox checkbox_delete_from_favorites ;


        RecyclerViewHolder_SelectRecipes(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.recipe_name);
            checkbox_delete_from_favorites = view.findViewById(R.id.checkbox_favorite);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.checkbox_favorite:
                    Log.d("test", "my_recipes");
            }
        }
    }

    private Context context ;
    private ArrayList<Recipe> al ;


    public Adapter_SelectRecipes(Context context, ArrayList<Recipe> al)
    {
        this.al = al;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder_SelectRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row_recipes_layout, viewGroup, false);

        return new RecyclerViewHolder_SelectRecipes(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SelectRecipes holder, final int i)
    {
        holder.recipe_name.setText(al.get(i).getName());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        Recipe rec = MainActivity.getFavoriteRecipes().get(i);
        boolean already_favorite = rec.alreadyExists(MainActivity.getFavoriteRecipes()) ;
        //Log.d(rec.getUrl(), String.valueOf(already_favorite));

        holder.checkbox_delete_from_favorites.setChecked(already_favorite);
        holder.checkbox_delete_from_favorites.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Recipe rec = MainActivity.getFavoriteRecipes().get(i);

                Log.d("is_selected",
                        rec.getName() + " | " + String.valueOf(isChecked));

                rec.setSelected(isChecked);

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

}

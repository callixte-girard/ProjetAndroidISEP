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


public class Adapter_MyShoppingLists extends RecyclerView.Adapter
            <Adapter_MyShoppingLists
                .RecyclerViewHolder_MyRecipes>
{

    static class RecyclerViewHolder_MyRecipes extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView lc_date_creation ;
        private CheckBox checkbox_delete_shopping_list ;


        RecyclerViewHolder_MyRecipes(View view)
        {
            super(view);

            lc_date_creation = view.findViewById(R.id.title);
            checkbox_delete_shopping_list = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.checkbox:
                    Log.d("test", "my_recipes");
            }
        }
    }

    private Context context ;
    private ArrayList<Recipe> al ;


    public Adapter_MyShoppingLists(Context context, ArrayList<Recipe> al)
    {
        this.al = al;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder_MyRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_simple_checklist, viewGroup, false);

        return new RecyclerViewHolder_MyRecipes(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_MyRecipes holder, final int i)
    {
        holder.lc_date_creation.setText(al.get(i).getName());
        holder.lc_date_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        Recipe rec = MainActivity.getFavoriteRecipes().get(i);
        boolean already_favorite = rec.alreadyExists(MainActivity.getFavoriteRecipes()) ;
        //Log.d(rec.getUrl(), String.valueOf(already_favorite));

        holder.checkbox_delete_shopping_list.setChecked(already_favorite);
        holder.checkbox_delete_shopping_list.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Recipe rec = MainActivity.getFavoriteRecipes().get(i);

                Log.d("is_checked",
                        rec.getName() + " | " + String.valueOf(isChecked));

                if (isChecked) {
                    //MainActivity.saveRecipeInFavorites(rec);

                } else {
                    // adds back recipe (revert deletion)
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

}

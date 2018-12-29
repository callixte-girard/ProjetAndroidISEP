package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Adapter_FavoriteRecipes extends RecyclerView.Adapter
            <Adapter_FavoriteRecipes.RecyclerViewHolder_FavoriteRecipes>
{

    static class RecyclerViewHolder_FavoriteRecipes extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private TextView recipe_name, recipe_duration, recipe_rating ;
        private ImageView recipe_img ;
        private CheckBox checkbox_delete_from_favorites ;


        RecyclerViewHolder_FavoriteRecipes(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.title);
            //recipe_img = view.findViewById(R.id.recipe_img);
            recipe_duration = view.findViewById(R.id.sub_title);
            //recipe_rating = view.findViewById(R.id.recipe_rating);
            checkbox_delete_from_favorites = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private Context context ;
    private MainActivity main_act ;

    public Adapter_FavoriteRecipes(Context context) {
        this.context = context ;
        this.main_act = (MainActivity) this.context ;
    }


    @Override
    public RecyclerViewHolder_FavoriteRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2_lines, viewGroup, false);
        
        return new RecyclerViewHolder_FavoriteRecipes(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_FavoriteRecipes holder, final int i)
    {

    }


    @Override
    public int getItemCount() {
        return (null != main_act.getFavoriteRecipes()
                ? main_act.getFavoriteRecipes().size() : 0);
    }

}

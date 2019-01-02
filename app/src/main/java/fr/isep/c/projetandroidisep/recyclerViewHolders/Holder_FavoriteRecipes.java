package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;

public class Holder_FavoriteRecipes
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public LinearLayout recipe_header, recipe_ingr_expandable ;
    public ImageView recipe_img ;
    public TextView recipe_name, recipe_duration, recipe_rating ;
    public CheckBox checkbox_delete_from_favorites ;

    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    public boolean show_expandable = false ;


    public Holder_FavoriteRecipes(View view, Listener_AddRemoveRecipe listener_addRemoveRecipe)
    {
        super(view);
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;

        recipe_header = view.findViewById(R.id.header_vertical); // the linearlayout clickable
        recipe_name = view.findViewById(R.id.title);
        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_duration = view.findViewById(R.id.subtitle);
        //recipe_rating = view.findViewById(R.id.recipe_rating);
        checkbox_delete_from_favorites = view.findViewById(R.id.checkbox);
        recipe_ingr_expandable = view.findViewById(R.id.expandable_list);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

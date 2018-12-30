package fr.isep.c.projetandroidisep.recycleViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;

public class Holder_FavoriteRecipes
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public TextView recipe_name, recipe_duration, recipe_rating ;
    public ImageView recipe_img ;
    public CheckBox checkbox_delete_from_favorites ;

    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;


    public Holder_FavoriteRecipes(View view, Listener_AddRemoveRecipe listener_addRemoveRecipe)
    {
        super(view);

        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;

        recipe_name = view.findViewById(R.id.title);
        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_duration = view.findViewById(R.id.sub_title);
        //recipe_rating = view.findViewById(R.id.recipe_rating);
        checkbox_delete_from_favorites = view.findViewById(R.id.checkbox);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

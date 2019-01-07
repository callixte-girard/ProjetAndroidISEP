package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Listener_SelectIngredient;

public class Holder_FavoriteRecipes
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public LinearLayout recipe_header ;
    public RecyclerView recipe_ingr_expandable ;
    public ImageView recipe_img ;
    public TextView recipe_name, recipe_duration, recipe_rating ;
    public CheckBox checkbox_delete_from_favorites, checkbox_show_expandable ;

    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private Listener_SelectIngredient listener_selectIngredient ;


    public Holder_FavoriteRecipes(View view
            , Listener_AddRemoveRecipe listener_addRemoveRecipe
            , Listener_SelectIngredient listener_selectIngredient
    ) {
        super(view);
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        this.listener_selectIngredient = listener_selectIngredient ;

        recipe_header = view.findViewById(R.id.header_vertical); // the linearlayout clickable
        recipe_name = view.findViewById(R.id.title);
        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_duration = view.findViewById(R.id.subtitle);
        //recipe_rating = view.findViewById(R.id.recipe_rating);
        checkbox_delete_from_favorites = view.findViewById(R.id.checkbox_add_remove);
        checkbox_show_expandable = view.findViewById(R.id.checkbox_show_expandable);
        recipe_ingr_expandable = view.findViewById(R.id.ingr_grid_expandable);

        view.setOnClickListener(this);
    }


    public void hideShowExpandableList(boolean isChecked)
    {
        //if (this.show_expandable) {
        if (isChecked) {
            this.recipe_ingr_expandable.setVisibility(View.VISIBLE);
        } else {
            this.recipe_ingr_expandable.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {

    }
}

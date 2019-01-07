package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchIngredients;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Response_FetchIngredients;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;

public class Holder_SearchRecipe
        extends RecyclerView.ViewHolder
        implements View.OnClickListener //, Response_FetchIngredients
{
    public LinearLayout recipe_header ;
    public RecyclerView recipe_ingr_expandable ;
    public ImageView recipe_img ;
    public TextView recipe_name, recipe_duration, recipe_rating ;
    public CheckBox checkbox_add_to_favorites, checkbox_show_expandable ;

    private MainActivity main_act ;
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;

    public boolean show_expandable = false ;


    public Holder_SearchRecipe(Context context, View view
            , Listener_AddRemoveRecipe listener_addRemoveRecipe)
    {
        super(view);

        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        this.main_act = (MainActivity) context ;

        recipe_header = view.findViewById(R.id.header_vertical);
        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_name = view.findViewById(R.id.title);
        recipe_duration = view.findViewById(R.id.subtitle);
        //recipe_rating = view.findViewById(R.id.recipe_rating);
        checkbox_add_to_favorites = view.findViewById(R.id.checkbox_add_remove);
        checkbox_show_expandable = view.findViewById(R.id.checkbox_show_expandable);
        recipe_ingr_expandable = view.findViewById(R.id.ingr_grid_expandable);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

    }



    public void hideShowExpandableList()
    {
        if (this.show_expandable) {
        //if (isChecked) {
            this.recipe_ingr_expandable.setVisibility(View.VISIBLE);
        } else {
            this.recipe_ingr_expandable.setVisibility(View.GONE);
        }
    }


    public void buildIngredientsExpandableList(Recipe rec)
    {


    }

}


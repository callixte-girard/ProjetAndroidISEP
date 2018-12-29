package fr.isep.c.projetandroidisep.recycleViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_SearchRecipe;

public class Holder_SearchRecipe
        extends RecyclerView.ViewHolder
        implements View.OnClickListener
{
    public ImageView recipe_img ;
    public TextView recipe_name, recipe_duration ;
    public CheckBox checkbox_add_to_favorites ;

    private Listener_SearchRecipe listener_searchRecipe ;

    public Holder_SearchRecipe(View view, Listener_SearchRecipe listener_searchRecipe)
    {
        super(view);

        this.listener_searchRecipe = listener_searchRecipe ;

        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_name = view.findViewById(R.id.title);
        recipe_duration = view.findViewById(R.id.sub_title);
        checkbox_add_to_favorites = view.findViewById(R.id.checkbox);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        listener_searchRecipe.onChecked(view, getAdapterPosition());
    }
}


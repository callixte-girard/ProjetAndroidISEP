package fr.isep.c.projetandroidisep.recyclerViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        implements View.OnClickListener, Response_FetchIngredients
{
    public LinearLayout recipe_header, recipe_ingr_expandable ;
    public ImageView recipe_img ;
    public TextView recipe_name, recipe_duration, recipe_rating ;
    public CheckBox checkbox_add_to_favorites ;

    private MainActivity main_act ;
    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    public boolean show_expandable = false ;

    public Holder_SearchRecipe(Context context, View view, Listener_AddRemoveRecipe listener_addRemoveRecipe)
    {
        super(view);

        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        this.main_act = (MainActivity) context ;

        recipe_header = view.findViewById(R.id.labels);
        //recipe_img = view.findViewById(R.id.recipe_img);
        recipe_name = view.findViewById(R.id.title);
        recipe_duration = view.findViewById(R.id.subtitle);
        //recipe_rating = view.findViewById(R.id.recipe_rating);
        checkbox_add_to_favorites = view.findViewById(R.id.checkbox_add_remove);
        recipe_ingr_expandable = view.findViewById(R.id.expandable_list);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

    }

    public void performFetchRecipeIngredients(Recipe rec)
    {
        Task_FetchIngredients task_fetchIngredients = new Task_FetchIngredients();
        task_fetchIngredients.setDelegate(this);
        task_fetchIngredients.setUrl(rec.getUrl());
        task_fetchIngredients.execute(task_fetchIngredients.getUrl());
    }

    @Override
    public void processFinish_fetchIngredients(Document doc, String url)
    {
        try {
            ArrayList<Ingredient> ingr_list = Ingredient.fetchAllFromDoc(doc);

            // --> finally adds to appropriate recipe
            Recipe rec_to_update = Recipe.getByUrl(main_act.getSearchResults(), url);
            rec_to_update.setIngredients(ingr_list);

            // update SearchRecipe UI
            buildIngredientsExpandableList(rec_to_update, this.recipe_ingr_expandable);

            // toggles corresponding action
            if (show_expandable) {
                recipe_ingr_expandable.setVisibility(View.VISIBLE);
            } else {
                recipe_ingr_expandable.setVisibility(View.GONE);
            }

            Log.d("task_results_fetchIngr", url);

        } catch (Exception e) {}

    }


    public void buildIngredientsExpandableList(Recipe rec, LinearLayout expandable_ingr_list)
    {
        for (Ingredient ingr : rec.getIngredients())
        {
            TextView tv_ingr = new TextView(main_act);
            tv_ingr.setText(" - " + ingr.returnNameAndForm());
            //tv_ingr.setTextColor(tv_ingr.getResources().getColor(R.color.black));
            expandable_ingr_list.addView(tv_ingr);
        }
    }
}


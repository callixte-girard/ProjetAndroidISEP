package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchIngredients;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Listener_SelectIngredient;
import fr.isep.c.projetandroidisep.interfaces.Response_FetchIngredients;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_FavoriteRecipes;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_SearchRecipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Holder_SearchRecipe>
            implements Response_FetchIngredients
{
    private MainActivity main_act;
    private ArrayList<Recipe> al = new ArrayList<>();

    private Listener_AddRemoveRecipe listener_addRemoveRecipe ;
    private Listener_SelectIngredient listener_selectIngredient ;


    public Adapter_SearchRecipe(Context context
            , Listener_AddRemoveRecipe listener_addRemoveRecipe
            , Listener_SelectIngredient listener_selectIngredient

    ) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveRecipe = listener_addRemoveRecipe ;
        this.listener_selectIngredient = listener_selectIngredient ;
    }


    @Override
    public Holder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_SearchRecipe(main_act, v, this.listener_addRemoveRecipe);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, final int position)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // checkboxes
        holder.checkbox_show_expandable.setChecked(holder.checkbox_show_expandable.isChecked());
        holder.checkbox_show_expandable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("show_expandable", rec.getName() + " | " + isChecked);

                // hides or show panel
                holder.show_expandable = isChecked ;
                holder.hideShowExpandableList();

                if (rec.getIngredients().isEmpty())
                {
                    Recipe rec_corresponding ;

                    try
                    {
                        rec_corresponding = Recipe.getByUrl
                                (main_act.getFavoriteRecipes(), rec.getUrl());
                    }
                    catch (NullPointerException npe)
                    {
                        rec_corresponding = null ;
                    }

                    // else
                    if (rec_corresponding != null) {
                        rec.setIngredients(rec_corresponding.getIngredients());
                    } else {
                        performFetchRecipeIngredients(rec);
                    }
                }
            }
        });

        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveRecipe.checkedListener_myRecipes(buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

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
            Log.d("task_results_holder", url);

            ArrayList<Ingredient> ingr_list = Ingredient.fetchAllFromDoc(doc);

            // --> finally adds to appropriate recipe
            //Recipe rec_to_update = Recipe.getByUrl(main_act.getSearchResults(), url);
            Recipe rec_to_update = Recipe.getByUrl(al, url);
            rec_to_update.setIngredients(ingr_list);
            Log.d("test", rec_to_update.getName() + " | " + rec_to_update.getIngredients().size());

            // update SearchRecipe UI
            updateResultsList(main_act.getSearchResults());
            //updateResultsList(al);


        } catch (Exception e) {}

    }



    public void putFetchingIngredientsLabel(Holder_SearchRecipe holder_searchRecipe)
    {
        if (holder_searchRecipe.recipe_ingr_expandable.getChildCount() == 0)
        {
            TextView fetching_label = new TextView(main_act);
            fetching_label.setText(MainActivity.FETCHING_INGREDIENTS);
            holder_searchRecipe.recipe_ingr_expandable.addView(fetching_label);
        }
        Log.d("test", ""+holder_searchRecipe.recipe_ingr_expandable.getChildCount());
    }


    private void initIngredientGrid(int index_rec, Holder_SearchRecipe holder)
    {
        holder.recipe_ingr_expandable.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main_act);
        holder.recipe_ingr_expandable.setLayoutManager(linearLayoutManager);
/*
        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (main_act, linearLayoutManager.getOrientation());
        holder.recipe_ingr_expandable.addItemDecoration(itemDecor);
*/
        // custom adapter
        Adapter_IngredientGrid adapter = new Adapter_IngredientGrid
                (main_act, index_rec, this.listener_selectIngredient);
        holder.recipe_ingr_expandable.setAdapter(adapter);

        // default : hidden
        holder.hideShowExpandableList();

    }


    public void updateResultsList(ArrayList<Recipe> al) {
        this.al.clear();
        this.al.addAll(al);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return (null != al ? al.size() : 0);
    }
}

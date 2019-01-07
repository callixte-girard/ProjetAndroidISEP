package fr.isep.c.projetandroidisep.frag_SearchRecipe;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Etape;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;



public class Frag_SearchRecipe extends Fragment
        implements Response_SearchRecipe, Response_FetchIngredients
        ,Listener_SearchRecipe_AddRemove, Listener_SearchRecipe_SelectIngredient
{
    private SearchView search_bar ;
    private RecyclerView results_list ;
    private TextView results_number ;
    //private FloatingActionButton add_recipe ;

    private final int deepness = 2 ; // creuse 2 fois, càd cherche 3 x 15 résultats maximum.
    private int current_deepness = 0 ;

    private View view ;

    private ArrayList<AsyncTask> async_tasks_list  = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        search_bar = view.findViewById(R.id.search_bar);
        results_list = view.findViewById(R.id.results_list);
        results_number = view.findViewById(R.id.label);

        initSearchBar();
        initResultsList();
        updateResultsList(((MainActivity) getActivity()).getSearchResults());

        return view ;
    }


    private void initSearchBar()
    {
        // make it fully visible
        search_bar.setIconifiedByDefault(false);

        // change text color
        TextView query_field = getSearchSrcTextView(search_bar);
        query_field.setTextColor(Color.WHITE);

        // set method when search pressed
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d("onQueryTextSubmit", query);

                // reset all results list
                resetResultsList();
                results_number.setText("Searching...");

                //// PERFORM SEARCH HERE
                performSearchFromKeywordsAndDeepness(query);

                // then resets deepness counter
                current_deepness = 0 ;

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                String optional = "";

                if (newText.isEmpty()) {
                    optional = "_clear";
                    //Misc is cleared, do your thing

                    resetResultsList();
                }

                Log.d("onQueryTextChange" + optional, newText);

                return true;
            }
        });
    }


    private void initResultsList()
    {
        results_list.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...
        results_list.setNestedScrollingEnabled(false);

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        results_list.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        results_list.addItemDecoration(itemDecor);

        // custom adapter
        Adapter_SearchRecipe adapter = new Adapter_SearchRecipe
                (getContext(), this, this);
        results_list.setAdapter(adapter);
    }


    private void resetResultsList()
    {
        // cancels eventual new callings
        current_deepness = deepness ;

        for (AsyncTask task : async_tasks_list) {
            task.cancel(true);
            Log.d(task.toString(), "isCancelled=" + task.isCancelled());
        }

        // clear results
        ((MainActivity) getActivity()).getSearchResults().clear();
        updateResultsNumber();
    }


    public void updateResultsList(ArrayList<Recipe> new_list)
    {
        // updates adapter
        ((Adapter_SearchRecipe) results_list.getAdapter()).updateResultsList(new_list);

        // and label
        updateResultsNumber();
    }


    private void updateResultsNumber()
    {
        // updates counter label
        int count = ((MainActivity) getActivity()).getSearchResults().size();
        results_number.setText(count + " results");
    }


    public void checkedListener_selectIngredient
            (View view, final int index_rec, final int index_ingr, boolean isChecked)
    {
        final Recipe rec = ((MainActivity) getActivity()).getSearchResults().get(index_rec);
        final Ingredient ingr = rec.getIngredients().get(index_ingr);

        ingr.setSelected(isChecked);
        Log.d("checkedListener_ingr", rec.getName() + " | " + ingr.getName() + " | " + isChecked);

        updateResultsList(((MainActivity) getActivity()).getSearchResults());
    }



    public void checkedListener_myRecipes(View view, int position, boolean isChecked)
    {
        Recipe rec = ((MainActivity) getActivity()).getSearchResults().get(position);
        Log.d("checkedListener_search", position + " | " + isChecked + " | " + rec.getName());

        if (isChecked)
        {
            if (!rec.alreadyExists(((MainActivity) getActivity()).getFavoriteRecipes()))
            {
                ((MainActivity) getActivity()).saveRecipeInFavorites(rec);
/*
                if (rec.getIngredients().isEmpty())
                {
                    ((Adapter_SearchRecipe) results_list.getAdapter())
                            .performFetchRecipeIngredients(rec);
                }*/
            }
        }
        else {
            ((MainActivity) getActivity()).removeRecipeFromFavorites(rec);
        }
    }



    @Override
    public void processFinish_searchRecipe(Document doc)
    {
        // parse et ajoute les 15 recipes du doc aux résultats
        try
        {
            ArrayList<Recipe> search_results = Recipe.fetchPageResultsFromDoc(doc);

            for (Recipe rec : search_results)
            {
                // first checks if it's already present in the favorites
                Recipe rec_corresp = Recipe.getByUrl(
                        ((MainActivity) getActivity()).getFavoriteRecipes(), rec.getUrl()
                );

                //if (rec_corresp.getIngredients().isEmpty())
                if (rec_corresp == null) {
                    performFetchRecipeIngredients(rec);
                } else {
                    rec.setIngredients(rec_corresp.getIngredients());
                }

                //performFetchRecipeImages(rec);
            }

            ((MainActivity) getActivity()).getSearchResults().addAll(search_results);
        }
        catch (Exception ex) {}

        updateResultsList(((MainActivity) getActivity()).getSearchResults());

        // refait une nouvelle task
        if (current_deepness < deepness)
        {
            try
            {
                current_deepness ++ ;

                // fetch next page's url
                Element next_page_tag = doc
                        .getElementsByClass("af-pagination").first()
                        .getElementsByClass("next-page").first();

                String next_page_link = next_page_tag
                        .getElementsByTag("a").first()
                        .attr("href");

                String next_page_url = Recipe.URL_BASE + next_page_link ;
                Log.d("next_page_url", next_page_url);

                // lance la nouvelle task pour le deepness suivant
                Task_SearchRecipe task_search_recipe =
                        new Task_SearchRecipe();
                task_search_recipe.setDelegate(this);
                task_search_recipe
                        .execute(next_page_url, String.valueOf(current_deepness));

                Log.d("current_deepness",
                        String.valueOf(current_deepness) + " / " + String.valueOf(deepness));

            } catch (NullPointerException npe) {
                Log.d("npe", "No results ||| Please check your query.");
            }
            catch (Exception unknown_ex) {
                Log.d("unknown_ex", unknown_ex.getMessage());
            }
        }
    }


    @Override
    public void processFinish_fetchIngredients(Document doc, String url)
    {
        try {
            ArrayList<Ingredient> ingr_list = Ingredient.fetchAllFromDoc(doc);
            ArrayList<String> etape_list = Etape.fetchInstructions(doc) ;

            // --> finally adds to appropriate recipe
            Recipe rec_to_update = Recipe.getByUrl(
                    ((MainActivity) getActivity()).getSearchResults(), url
            );
            rec_to_update.setIngredients(ingr_list);
            rec_to_update.setInstructions(etape_list);

            // update SearchRecipe UI
            updateResultsList(((MainActivity) getActivity()).getSearchResults());

            Log.d("task_results_frag", url);

        } catch (Exception e) {}

    }


    protected void performSearchFromKeywordsAndDeepness(String search)
    {
        search = search.replaceAll(" ", "-");

        String first_url = Recipe.URL_BASE + Recipe.URL_SEARCH + search ;
        Log.d("first_page_url", first_url);

        Task_SearchRecipe task_searchRecipe = new Task_SearchRecipe();
        task_searchRecipe.setDelegate(this);
        task_searchRecipe.execute(first_url, String.valueOf(current_deepness));

        async_tasks_list.add(task_searchRecipe);
    }


    protected void performFetchRecipeIngredients(Recipe rec)
    {
        Task_FetchIngredients task_fetchIngredients = new Task_FetchIngredients();
        task_fetchIngredients.setDelegate(this);
        task_fetchIngredients.setUrl(rec.getUrl());
        task_fetchIngredients.execute(task_fetchIngredients.getUrl());

        async_tasks_list.add(task_fetchIngredients);
    }


    private TextView getSearchSrcTextView(SearchView search_bar)
    {
        int id = search_bar.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        return search_bar.findViewById(id);
    }





}

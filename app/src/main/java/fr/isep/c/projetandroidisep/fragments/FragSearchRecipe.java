package fr.isep.c.projetandroidisep.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_SearchRecipe;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncResponse_FetchIngredients;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncResponse_SearchRecipe;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncTask_FetchIngredients;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncTask_SearchRecipe;
import fr.isep.c.projetandroidisep.customTypes.Ingredient;
import fr.isep.c.projetandroidisep.customTypes.Recipe;



public class FragSearchRecipe extends Fragment
        implements AsyncResponse_SearchRecipe, AsyncResponse_FetchIngredients
{
    private SearchView search_bar ;
    private RecyclerView results_list ;
    private TextView results_number ;
    //private FloatingActionButton add_recipe ;

    private final int deepness = 2 ; // creuse 2 fois, càd cherche 3 x 15 résultats maximum.
    private int current_deepness = 0 ;

    private static ArrayList<Recipe> search_results = new ArrayList<>();

    private ArrayList<AsyncTask> async_tasks_list  = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        search_bar = view.findViewById(R.id.search_bar);
        results_list = view.findViewById(R.id.results_list);
        results_number = view.findViewById(R.id.results_number);

        initSearchBar();
        initResultsList();
        if (search_results.size() > 0) updateResultsCount(search_results.size());


        return view ;
    }



    @Override
    public void processFinish_fetchIngredients(Document doc, String url)
    {
        try
        {
            Log.d("task_results_id", url);

            ArrayList<Ingredient> ingr_list = Ingredient.fetchAllFromDoc(doc);

            // --> finally adds to appropriate recipe
            Recipe rec_to_update = Recipe.getByUrl(search_results, url);
            rec_to_update.setIngredients(ingr_list);

        } catch (Exception e) {}
    }


    @Override
    public void processFinish_searchRecipe(Document doc)
    {
        // parse et ajoute les recipes aux résultats
        try
        {
            search_results.addAll(Recipe.fetchPageResultsFromDoc(doc));
            Log.d("results", String.valueOf(search_results.size()));

            // to get the 15-recipe block's ingredients one by one
            performFetchRecipeIngredients(search_results);
        }
        catch (Exception ex) {}

        // update textview that counts results
        updateResultsCount(search_results.size());

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
                AsyncTask_SearchRecipe task_search_recipe =
                        new AsyncTask_SearchRecipe();
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


    private void initSearchBar()
    {
        // make it fully visible
        search_bar.setIconifiedByDefault(false);

        // change text color to white
        int id = search_bar.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView query_field = search_bar.findViewById(id);
        query_field.setTextColor(Color.WHITE);


        // set method when search pressed
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d("onQueryTextSubmit", query);


                // reset all results list
                results_number.setText("Searching...");

                resetResultsList();

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
                    //Text is cleared, do your thing
                    resetResultsList();
                    results_number.setText("");
                }

                Log.d("onQueryTextChange" + optional, newText);


                return true;
            }
        });
    }


    private void resetResultsList()
    {
        // cancels eventual new callings
        current_deepness = deepness ;

        for (AsyncTask task : async_tasks_list) {
            task.cancel(true);
        }

        search_results.clear();
        results_list.getAdapter().notifyDataSetChanged();
    }


    private void initResultsList()
    {
        results_list.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        results_list.setLayoutManager(linearLayoutManager);

        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (getContext(), linearLayoutManager.getOrientation());
        results_list.addItemDecoration(itemDecor);

        // custom adapter
        Adapter_SearchRecipe adapter = new Adapter_SearchRecipe
                (getContext(), search_results);
        results_list.setAdapter(adapter);
    }



    protected void updateResultsCount(int number)
    {
        String nb_res = String.valueOf(number) + " results";
        results_number.setText(nb_res);
    }


    protected void performSearchFromKeywordsAndDeepness(String search)
    {
        search = search.replaceAll(" ", "-");

        String first_url = Recipe.URL_BASE + Recipe.URL_SEARCH + search ;
        Log.d("first_page_url", first_url);

        AsyncTask_SearchRecipe task_searchRecipe = new AsyncTask_SearchRecipe();
        task_searchRecipe.setDelegate(this);
        task_searchRecipe.execute(first_url, String.valueOf(current_deepness));

        async_tasks_list.add(task_searchRecipe);
    }


    protected void performFetchRecipeIngredients(ArrayList<Recipe> al)
    {
        for (Recipe rec : al)
        {
            if (rec.getIngredients().isEmpty())
            {
                AsyncTask_FetchIngredients task_fetchIngredients = new AsyncTask_FetchIngredients();
                task_fetchIngredients.setDelegate(this);
                task_fetchIngredients.setUrl(rec.getUrl());
                task_fetchIngredients.execute(task_fetchIngredients.getUrl());

                async_tasks_list.add(task_fetchIngredients);
            }
        }
    }


    public static ArrayList<Recipe> getSearchResults() {
        return search_results ;
    }

}

package fr.isep.c.projetandroidisep.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.recyclerViewAdapters.Adapter_SearchRecipe;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveRecipe;
import fr.isep.c.projetandroidisep.interfaces.Response_FetchImages;
import fr.isep.c.projetandroidisep.interfaces.Response_SearchRecipe;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchImages;
import fr.isep.c.projetandroidisep.asyncTasks.Task_SearchRecipe;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;



public class Frag_SearchRecipe extends Fragment
        implements Response_SearchRecipe, Response_FetchImages , Listener_AddRemoveRecipe
{
    private MainActivity main_act ;

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
        main_act = (MainActivity) getActivity();

        view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        search_bar = view.findViewById(R.id.search_bar);
        results_list = view.findViewById(R.id.results_list);
        results_number = view.findViewById(R.id.results_number);

        initSearchBar();
        initResultsList();
        updateResultsList(main_act.getSearchResults());

        return view ;
    }


    public void checkedListener_myRecipes(View view, int position, boolean isChecked)
    {
        Recipe rec = main_act.getSearchResults().get(position);

        Log.d("checkedListener_search", position + " | " + isChecked + " | " + rec.getName());

        if (isChecked) {
            if (!rec.alreadyExists(main_act.getFavoriteRecipes())) main_act.saveRecipeInFavorites(rec);
        } else {
            main_act.removeRecipeFromFavorites(rec);
        }
    }


    @Override
    public void processFinish_searchRecipe(Document doc)
    {
        // parse et ajoute les 15 recipes du doc aux résultats
        try
        {
            main_act.getSearchResults().addAll(Recipe.fetchPageResultsFromDoc(doc));
            //performFetchRecipeImages(search_results);
        }
        catch (Exception ex) {}

        updateResultsList(main_act.getSearchResults());

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
    public void processFinish_fetchImages(Bitmap bitmap, String url)
    {
        // récupère l'image et la refourgue à la recette en question

        Recipe rec = Recipe.getByUrl(main_act.getSearchResults(), url);
        rec.setImgBitmap(bitmap);
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
                }

                Log.d("onQueryTextChange" + optional, newText);


                return true;
            }
        });

        search_bar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("onClose", "");
                return false;
            }
        });
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
        Adapter_SearchRecipe adapter = new Adapter_SearchRecipe(getContext(), this);
        results_list.setAdapter(adapter);
    }


    private void resetResultsList()
    {
        // cancels eventual new callings
        current_deepness = deepness ;

        for (AsyncTask task : async_tasks_list) {
            task.cancel(true);
        }

        // clear results
        main_act.getSearchResults().clear();
    }


    public void updateResultsList(ArrayList<Recipe> new_list)
    {
        // updates adapter
        ((Adapter_SearchRecipe) results_list.getAdapter()).updateResultsList(new_list);

        // updates counter
        int count = main_act.getSearchResults().size();
        results_number.setText(String.valueOf(count) + " results");

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


    protected void performFetchRecipeImages(ArrayList<Recipe> al)
    {
        for (Recipe rec : al)
        {
            Task_FetchImages task_fetchImages = new Task_FetchImages();
            task_fetchImages.setDelegate(this);
            task_fetchImages.setUrl(rec.getUrl());
            task_fetchImages.execute(task_fetchImages.getUrl());

            async_tasks_list.add(task_fetchImages);
        }
    }




}

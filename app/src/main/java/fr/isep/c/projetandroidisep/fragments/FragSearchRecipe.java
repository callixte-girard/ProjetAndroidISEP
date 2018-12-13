package fr.isep.c.projetandroidisep.fragments;

import android.graphics.Color;
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

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.adapters.Adapter_SearchRecipe;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncResponse_SearchRecipe;
import fr.isep.c.projetandroidisep.asyncTasks.AsyncTask_SearchRecipe;
import fr.isep.c.projetandroidisep.customTypes.Recette;



public class FragSearchRecipe extends Fragment implements AsyncResponse_SearchRecipe
{
    protected final static String URL_BASE = "https://www.marmiton.org" ;
    protected final static String URL_SEARCH = "/recettes/recherche.aspx?aqt=" ;

    private SearchView search_bar ;
    private RecyclerView results_list ;
    private TextView results_number ;
    //private FloatingActionButton add_recipe ;

    private final int deepness = 2 ; // creuse 2 fois, càd cherche 3 x 15 résultats maximum.
    private int current_deepness = 0 ;

    private static ArrayList<Recette> all_results = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        search_bar = view.findViewById(R.id.search_bar);
        results_list = view.findViewById(R.id.results_list);
        results_number = view.findViewById(R.id.results_number);

        initSearchBar();
        initResultsList();
        if (all_results.size() > 0) updateResultsCount(all_results.size());


        return view ;
    }


    @Override
    public void processFinish(Document doc)
    {
        // parse et ajoute les recettes aux résultats
        try
        {
            all_results.addAll(fetchRecetteFromURLAsDocument(doc));
            Log.d("results", String.valueOf(all_results.size()));
        }
        catch (Exception ex) {
            ex.getMessage();
        }

        // update textview that counts results
        updateResultsCount(all_results.size());

        // refait une nouvelle task
        if (current_deepness < deepness)  // ne le fait qu'une fois si deepness = 1
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

                String next_page_url = FragSearchRecipe.URL_BASE + next_page_link ;
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

        search_bar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // reset all results list
                return false;
            }
        });

        // set method when search pressed
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("query_submit", query);

                // reset all results list
                all_results.clear();
                results_number.setText("Searching...");
                results_list.getAdapter().notifyDataSetChanged();

                //// PERFORM SEARCH HERE
                performSearchFromKeywordsAndDeepness(query);

                // then resets deepness counter
                current_deepness = 0 ;

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("query_change", newText);

                if (TextUtils.isEmpty(newText)) {
                    //Text is cleared, do your thing
                    all_results.clear();
                    results_number.setText("");
                    results_list.getAdapter().notifyDataSetChanged();
                }

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
        Adapter_SearchRecipe adapter = new Adapter_SearchRecipe
                (getContext(), all_results);
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

        String first_url = URL_BASE + URL_SEARCH + search ;
        Log.d("first_page_url", first_url);

        AsyncTask_SearchRecipe task_searchRecipe = new AsyncTask_SearchRecipe();
        task_searchRecipe.setDelegate(this);
        task_searchRecipe.execute(first_url, String.valueOf(current_deepness));
    }


    private static ArrayList<Recette> fetchRecetteFromURLAsDocument(Document doc)
    {
        ArrayList<Recette> result = new ArrayList<>();

        Elements search_results = doc
                .getElementsByClass("recipe-search__resuts").first()
                .getElementsByClass("recipe-card");

        // ## parse les recettes contenues dans la page
        for (Element el : search_results)
        {
            String nom ;
            String url ;
            double rating ;
            String description ;
            String duration ;
            String img_url ;
            String type ;

            nom = el.getElementsByClass("recipe-card__title").first()
                    .html().trim();
            url = el.getElementsByClass("recipe-card-link").first()
                    .attr("href");
            rating = Double.parseDouble(el.getElementsByClass("recipe-card__rating__value").first()
                    .html());
            // description = el.getElementsByClass("recipe-card__description").first()
            // 		.html(); // incomplete
            duration = el.getElementsByClass("recipe-card__duration__value").first()
                    .html().trim();
            img_url = el.getElementsByClass("recipe-card__picture").first()
                    .getElementsByTag("img").attr("src");
            type = el.getElementsByClass("recipe-card__tags").first()
                    .getElementsByTag("li").first().html();

            url = URL_BASE + url; // pour rendre l'url complète et pas partielle comme dans le html

            Recette rec = new Recette(nom, type, url);
            rec.setRating(rating);
            rec.setDuration(duration);
            //rec.setType(type);

            result.add(rec);
        }

        return result;
    }

    public static ArrayList<Recette> getSearchResults() {
        return all_results ;
    }

}

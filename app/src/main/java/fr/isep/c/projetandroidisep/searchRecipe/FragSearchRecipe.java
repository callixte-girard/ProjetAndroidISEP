package fr.isep.c.projetandroidisep.searchRecipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;
import fr.isep.c.projetandroidisep.parseAlim.*;



public class FragSearchRecipe extends Fragment implements AsyncResponse_SearchRecipe
{
    protected final static String URL_BASE = "https://www.marmiton.org" ;
    protected final static String URL_SEARCH = "/recettes/recherche.aspx?aqt=" ;

    private SearchView search_bar ;
    private ListView results_list ;
    private FloatingActionButton add_recipe ;

    private final int deepness = 0 ; // creuse 2 fois, càd cherche 3 fois.
    private int current_deepness = 0 ;
    private static ArrayList<Recette> all_results = new ArrayList<>();

    View view ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        results_list = view.findViewById(R.id.results);
        search_bar = view.findViewById(R.id.search);

        initSearchBar();

        return view ;
    }


    @Override
    public void processFinish(Document doc)
    {
        // parse et ajoute les recettes aux résultats
        all_results.addAll(fetchRecetteFromURLAsDocument(doc));
        Log.d("results", String.valueOf(all_results.size()));

        // update views
        updateList();

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
        search_bar.setIconifiedByDefault(false);

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("query_submit", query);

                all_results.clear();

                //// PERFORM SEARCH HERE
                performSearchFromKeywordsAndDeepness(query);

                // then resets deepness counter
                current_deepness = 0 ;

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("query_change", newText);
                return false;
            }
        });
    }


    protected void updateList()
    {

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

            Recette rec = new Recette(nom, url);
            rec.setRating(rating);
            rec.setDuration(duration);
            rec.setType(type);

            result.add(rec);
        }

        return result;
    }

}

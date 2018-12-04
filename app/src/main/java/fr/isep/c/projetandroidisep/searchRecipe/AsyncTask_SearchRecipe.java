package fr.isep.c.projetandroidisep.searchRecipe;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.myClasses.ParseHtml;

public class AsyncTask_SearchRecipe extends AsyncTask<String, Void, Document>
{
    private AsyncResponse_SearchRecipe delegate_search_recipe = null ;

    public AsyncTask_SearchRecipe setDelegate(AsyncResponse_SearchRecipe delegate) {
        this.delegate_search_recipe = delegate;
        return this ;
    }


    @Override
    public Document doInBackground(String... param)
    {

        String requested_url = param[0];
        //Log.d("requested_url", requested_url);
        /*
        int requested_deepness = Integer.parseInt(param[1]);
        int current_deepness = 0 ;
        Document doc ;
        String next_page_url = requested_url ; // initialisation

        do {
            doc = ParseHtml.fetchHtmlAsDocumentFromUrl(next_page_url);

            // fetch next page's url
            Element next_page_tag = doc
                    .getElementsByClass("af-pagination").first()
                    .getElementsByClass("next-page").first();

            String next_page_link = next_page_tag
                    .getElementsByTag("a").first()
                    .attr("href");

            next_page_url = FragSearchRecipe.URL_BASE + next_page_link ;
            Log.d("next_page_url", next_page_url);

            current_deepness ++ ;

        } while (current_deepness < requested_deepness);
        */

        return ParseHtml.fetchHtmlAsDocumentFromUrl(requested_url);

    }

    @Override
    public void onPostExecute(Document doc)
    {
        // traitement local pour parser les données


        // and then return
        this.delegate_search_recipe.processFinish(doc);
    }
}

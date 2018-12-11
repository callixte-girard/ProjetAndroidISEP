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

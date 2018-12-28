package fr.isep.c.projetandroidisep.asyncTasks;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import fr.isep.c.projetandroidisep.interfaces.Response_SearchRecipe;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;

public class Task_SearchRecipe extends AsyncTask<String, Void, Document>
{
    private Response_SearchRecipe delegate_search_recipe = null ;

    public Task_SearchRecipe setDelegate(Response_SearchRecipe delegate) {
        this.delegate_search_recipe = delegate;
        return this ;
    }


    @Override
    public Document doInBackground(String... param)
    {
        if (!this.isCancelled()) {
            String requested_url = param[0];
            return ParseHtml.fetchHtmlAsDocumentFromUrl(requested_url);
        } else {
            return null ;
        }

    }

    @Override
    public void onPostExecute(Document doc)
    {
        // traitement local pour parser les données


        // and then return
        this.delegate_search_recipe.processFinish_searchRecipe(doc);
    }
}

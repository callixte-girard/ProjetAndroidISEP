package fr.isep.c.projetandroidisep.asyncTasks;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import fr.isep.c.projetandroidisep.myClasses.ParseHtml;

public class AsyncTask_FetchIngredients extends AsyncTask<String, Void, Document>
{
    private AsyncResponse_FetchIngredients delegate_fetch_ingredients = null ;

    public AsyncTask_FetchIngredients setDelegate(AsyncResponse_FetchIngredients delegate) {
        this.delegate_fetch_ingredients = delegate;
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
        this.delegate_fetch_ingredients.processFinish(doc);
    }
}

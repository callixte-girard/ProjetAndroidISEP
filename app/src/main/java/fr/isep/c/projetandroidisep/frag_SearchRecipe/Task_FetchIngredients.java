package fr.isep.c.projetandroidisep.frag_SearchRecipe;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import fr.isep.c.projetandroidisep.myClasses.utils.ParseHtml;

public class Task_FetchIngredients extends AsyncTask<String, Void, Document>
{
    private Response_FetchIngredients delegate_fetch_ingredients = null ;
    private String url ;

    public Task_FetchIngredients setDelegate(Response_FetchIngredients delegate) {
        this.delegate_fetch_ingredients = delegate;

        return this ;
    }

    public String getUrl() { return this.url ; }
    public void setUrl(String url) {
        this.url = url ;
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
        if (!this.isCancelled()) {
            // and then return
            this.delegate_fetch_ingredients.processFinish_fetchIngredients(doc, this.url);
        }
    }
}

package fr.isep.c.projetandroidisep;

import fr.isep.c.projetandroidisep.myClasses.*;


import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Document;


public class JsoupRequestTask extends AsyncTask<String, Void, String> {



    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate ;

    public JsoupRequestTask(AsyncResponse delegate){
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... urls) {

        String url = urls[0];
        Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(url);
        String html = doc.html();

        return html ;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);

        //Log.d("YOUPI",result);
    }
}
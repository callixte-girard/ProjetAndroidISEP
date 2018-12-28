package fr.isep.c.projetandroidisep.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

import fr.isep.c.projetandroidisep.interfaces.Response_FetchImages;

public class Task_FetchImages extends AsyncTask<String, Void, Bitmap>
{
    private Response_FetchImages delegate_fetch_ingredients = null ;
    private String url ;

    public Task_FetchImages setDelegate(Response_FetchImages delegate) {
        this.delegate_fetch_ingredients = delegate;

        return this ;
    }

    public String getUrl() { return this.url ; }
    public void setUrl(String url) {
        this.url = url ;
    }


    @Override
    public Bitmap doInBackground(String... param)
    {
        if (!this.isCancelled()) {
            String requested_url = param[0];
            return getBitmap(requested_url);
        } else {
            return null ;
        }
    }

    @Override
    public void onPostExecute(Bitmap img)
    {
        // and then return
        this.delegate_fetch_ingredients.processFinish_fetchImages(img, this.url);
    }

    private static Bitmap getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}

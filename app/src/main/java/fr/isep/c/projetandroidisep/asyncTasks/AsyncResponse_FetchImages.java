package fr.isep.c.projetandroidisep.asyncTasks;

import org.jsoup.nodes.Document;

public interface AsyncResponse_FetchImages
{
    void processFinish_fetchImages(Document doc, String url);
}

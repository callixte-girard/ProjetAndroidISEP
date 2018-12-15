package fr.isep.c.projetandroidisep.asyncTasks;

import org.jsoup.nodes.Document;

public interface AsyncResponse_FetchIngredients
{
    void processFinish(Document doc, String url);
}

package fr.isep.c.projetandroidisep.asyncTasks;

import org.jsoup.nodes.Document;

public interface AsyncResponse_FetchIngredients
{
    void processFinish_fetchIngredients(Document doc, String url);
}

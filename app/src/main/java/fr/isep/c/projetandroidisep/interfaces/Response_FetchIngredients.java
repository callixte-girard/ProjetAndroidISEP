package fr.isep.c.projetandroidisep.interfaces;

import org.jsoup.nodes.Document;

public interface Response_FetchIngredients
{
    void processFinish_fetchIngredients(Document doc, String url);
}
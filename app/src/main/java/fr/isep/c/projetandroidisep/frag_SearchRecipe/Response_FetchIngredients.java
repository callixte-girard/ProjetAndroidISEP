package fr.isep.c.projetandroidisep.frag_SearchRecipe;

import org.jsoup.nodes.Document;

public interface Response_FetchIngredients
{
    void processFinish_fetchIngredients(Document doc, String url);
}

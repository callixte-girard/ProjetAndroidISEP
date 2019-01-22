package fr.isep.c.projetandroidisep.myCustomTypes;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Instructions
{
    private int numero ;
    private String description ;
    private Recipe rec ;


    public Instructions() {}


    public Instructions(int numero, String description)
    {
        //this.rec = rec ;
        this.numero = numero ;
        this.description = description ;
    }


    public static ArrayList<String> fetchInstructions(Document doc)
    {
        //ArrayList<Instructions> etapes = new ArrayList<>();
        ArrayList<String> etapes = new ArrayList<>();

        Elements instructions = doc
                .getElementsByClass("recipe-preparation__list__item");

        for (Element el : instructions)
        {
            String description ;

            // numero etape (remove etape number)
            el.getElementsByTag("h3").remove();

            // desc (includes other children)
            description = el.text();
//            Log.d("test", description);

            etapes.add(description);
        }

        return etapes ;
    }



    public int getNumero() {
        return this.numero ;
    }

    public void setNumero(int numero) {
        this.numero = numero ;
    }

    public String getDescription() {
        return this.description ;
    }

    public void setDescription(String description) {
        this.description = description ;
    }
}

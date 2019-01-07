package fr.isep.c.projetandroidisep.myCustomTypes;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Etape
{
    private int numero ;
    private String description ;
    private Recipe rec ;


    public Etape() {}


    public Etape(int numero, String description)
    {
        //this.rec = rec ;
        this.numero = numero ;
        this.description = description ;
    }


    public static ArrayList<String> fetchInstructions(Document doc)
    {
        //ArrayList<Etape> etapes = new ArrayList<>();
        ArrayList<String> etapes = new ArrayList<>();

        Elements instructions = doc
                .getElementsByClass("recipe-preparation__list__item");

        for (Element el : instructions)
        {
            int numero ;
            String description ;

            String[] numero_spl = el.getElementsByClass("__secondary").first()
                    .html().split(" ");
            numero = Integer.parseInt(numero_spl[1]);
            description = el.ownText();

            Log.d("test", numero + "- " + description);

            // MODE 1 : Etape object (lol)
            //etapes.add(new Etape(numero, description));
            // MODE 2 : Strings simples, on crée le num à la volée
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

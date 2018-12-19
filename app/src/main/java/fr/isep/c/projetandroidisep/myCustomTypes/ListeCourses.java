package fr.isep.c.projetandroidisep.myCustomTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import java.util.Calendar;

import fr.isep.c.projetandroidisep.myClasses.ParseText;

public class ListeCourses
{
    public static final String DATE_PATTERN = "yyyy-MM-dd_HH:mm:ss" ;


    public static ArrayList<ListeCourses> al = new ArrayList<>();

    public static int counter = 0 ;

    private int id ;
    private String dateCreation ;
    //private String creator ;
    //private LocalDate duree ; // a travailler
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Aliment> aliments = new ArrayList<>();


    public ListeCourses() {}

    public ListeCourses(
      //      String creator,
            ArrayList<Recipe> recipes)
    {
        counter ++ ;
        this.id = counter ;

        this.dateCreation = ParseText.formatDate(new Date(System.currentTimeMillis()), DATE_PATTERN);
        //this.creator = creator ;
        this.recipes = recipes ;

        // ce qui nous intéresse c'est surtout la liste d'aliments en fait.
        this.aliments = createListAlimentsFromRecipes(this.recipes) ;

        this.sortAlimAlphabetically();
    }

/*
    public void dispAttr(boolean sort)
    {
        if (sort)
        {
            this.sortAlimAlphabetically();
        }

        System.out.println("# Liste de courses N°" + this.id
                + " | créée le : " + ParseText.formatLocalDateTime(this.date_creation)
                + " | par : " + this.creator
        );
        System.out.println(Disp.line);

        for (Aliment alim : this.aliments)
        {
            Ingredient ingr = (Ingredient) alim ;
            ingr.dispQty();
        }

        System.out.println(Disp.star);
    }
*/

    private static ArrayList<Aliment> createListAlimentsFromRecipes(ArrayList<Recipe> recipes)
    {
        ArrayList<Aliment> temp_ingr = new ArrayList<>();

        for (Recipe rec : recipes)
        {
            for (Ingredient ingr : rec.getIngredients())
            {
                // regarde s'il est déjà présent. Si non, l'ajoute.
                if (!Ingredient.existsName(ingr.getName(), ingr.getForm(), temp_ingr))
                {
                    Ingredient new_ingr = new Ingredient(
                            ingr.getName(), ingr.getForm(), ingr.getQty(), ingr.getUnit()
                    );

                    temp_ingr.add(new_ingr);
                }
                else // sinon, augmente la qtité de l'alim
                {
                    Ingredient ingr_to_update = (Ingredient) Ingredient
                            .getByNameAndForm(ingr.getName(), ingr.getForm(), temp_ingr);

                    ingr_to_update.updateQty(ingr);
                }

            }
        }
        return temp_ingr ;
    }


    private void sortAlimAlphabetically()
    {
        Collections.sort(this.aliments, new Comparator<Aliment>()
        {
            @Override
            public int compare(Aliment al1, Aliment al2)
            {
                return  al1.getName().compareTo(al2.getName());
            }
        });
    }


    public ArrayList<Recipe> getRecipes()
    {
        return this.recipes ;
    }
    public void setRecipes(ArrayList<Recipe> recipes)
    {
        this.recipes = recipes ;
    }

    public ArrayList<Aliment> getAliments()
    {
        return this.aliments ;
    }
    public void setAliments(ArrayList<Aliment> aliments)
    {
        this.aliments = aliments ;
    }

    public String getDateCreation() { return this.dateCreation ; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation ; }

    //public String getCreator() { return this.creator ; }
    //public void setCreator(String creator) { this.creator = creator ; }

    public int getId()
    {
        return this.id ;
    }
    public void setId(int id)
    {
        this.id = id ;
    }
}

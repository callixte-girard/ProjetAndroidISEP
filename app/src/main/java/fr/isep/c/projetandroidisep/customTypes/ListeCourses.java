package fr.isep.c.projetandroidisep.customTypes;

import fr.isep.c.projetandroidisep.myClasses.*;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import java.util.Calendar;

public class ListeCourses
{
    public static ArrayList<ListeCourses> al = new ArrayList<>();

    public static int counter = 0 ;

    private int id ;
    //private LocalDateTime date_creation ;
    Date date_creation ;
    private String creator ;
    //private LocalDate duree ; // a travailler
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Aliment> aliments = new ArrayList<>();


    public ListeCourses() {}

    protected ListeCourses(String creator, ArrayList<Recipe> recipes)
    {
        counter ++ ;
        this.id = counter ;

        //this.date_creation = LocalDateTime.now();
        this.date_creation = Calendar.getInstance().getTime();

        this.creator = creator ;
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

    public Date getDateCreation()
    {
        return this.date_creation ;
    }
    public void setDateCreation(Date date_creation) {
        //this.date_creation = date_creation ;
        this.date_creation = date_creation ;
    }

    public String getCreator()
    {
        return this.creator ;
    }
    public void setCreator(String creator)
    {
        this.creator = creator ;
    }

    public int getId()
    {
        return this.id ;
    }
    public void setId(int id)
    {
        this.id = id ;
    }
}

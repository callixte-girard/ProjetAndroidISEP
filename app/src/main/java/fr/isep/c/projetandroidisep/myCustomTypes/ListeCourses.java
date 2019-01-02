package fr.isep.c.projetandroidisep.myCustomTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import fr.isep.c.projetandroidisep.myClasses.utils.Misc;

public class ListeCourses
{
    public static final String DATE_PATTERN = "yyyy-MM-dd_HH:mm:ss" ;


    private String dateCreation ;
    //private String dateAchat ;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private String boughtAt ;


    public ListeCourses() {}

    public ListeCourses(
      //      String creator,
            ArrayList<Recipe> recipes)
    {
        this.dateCreation = Misc.formatDate(new Date(System.currentTimeMillis()), DATE_PATTERN);
        //this.creator = creator ;
        this.recipes = recipes ;

        // ce qui nous intéresse c'est surtout la liste d'aliments en fait.
        this.ingredients = createListIngredientsFromRecipes(this.recipes) ;

        this.sortAlimAlphabetically();
    }


    private static ArrayList<Ingredient> createListIngredientsFromRecipes(ArrayList<Recipe> recipes)
    {
        ArrayList<Ingredient> temp_ingr = new ArrayList<>();

        for (Recipe rec : recipes)
        {
            for (Ingredient ingr : rec.getIngredients())
            {
                // gestion des ingrédients sélectionnés ou non :
                if (ingr.isSelected()) {
                    // regarde s'il est déjà présent. Si non, l'ajoute.
                    if (!Ingredient.existsName(ingr.getName(), ingr.getForm(), temp_ingr))
                    {
                        Ingredient new_ingr = new Ingredient(
                                ingr.getName(), ingr.getForm(), ingr.getQty(), ingr.getUnit()
                        );
                        // put it to false for shopping list
                        new_ingr.setSelected(false);

                        temp_ingr.add(new_ingr);
                    }
                    else // sinon, augmente la qtité de l'alim
                    {
                        Ingredient ingr_to_update = (Ingredient) Ingredient
                                .getByNameAndForm(ingr.getName(), ingr.getForm(), temp_ingr);

                        ingr_to_update.updateQty(ingr);
                    }
                } else {
                    /// ne l'ajoute pas.
                }
            }
        }
        return temp_ingr ;
    }


    private void sortAlimAlphabetically()
    {
        Collections.sort(this.ingredients, new Comparator<Aliment>()
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

    public ArrayList<Ingredient> getIngredients()
    {
        return this.ingredients ;
    }
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients ;
    }

    public String getDateCreation() { return this.dateCreation ; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation ; }

    public String getBoughtAt() { return this.boughtAt ; }
    public void setBoughtAt(String boughtAt) { this.boughtAt = boughtAt ; }

    public boolean isFinished()
    {
        try {
            return !this.getBoughtAt().isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }
}

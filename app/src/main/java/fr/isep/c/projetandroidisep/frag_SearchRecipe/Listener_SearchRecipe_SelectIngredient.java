package fr.isep.c.projetandroidisep.frag_SearchRecipe;

import android.view.View;

import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;

public interface Listener_SearchRecipe_SelectIngredient
{
    void checkedListener_selectIngredient(View view, int index_rec, int index_ingr, boolean isChecked);
}

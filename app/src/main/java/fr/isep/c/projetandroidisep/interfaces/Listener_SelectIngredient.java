package fr.isep.c.projetandroidisep.interfaces;

import android.view.View;

import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;

public interface Listener_SelectIngredient
{
    void checkedListener_selectIngredient(View view, int index_rec, int index_ingr, boolean isChecked);
}

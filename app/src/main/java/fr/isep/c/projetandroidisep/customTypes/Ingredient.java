package fr.isep.c.projetandroidisep.customTypes;


import android.util.Log;

import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;


import fr.isep.c.projetandroidisep.asyncTasks.AsyncResponse_FetchIngredients;
import fr.isep.c.projetandroidisep.myClasses.*;
//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;

public class Ingredient extends Aliment implements Serializable
{
	public static ArrayList<Ingredient> all_ingr = new ArrayList<Ingredient>();


	private boolean select = true ; // can be deactivated to remove an aliment from the parsed recipe
	private double qty ;
	private String unit ;


	private Ingredient(Aliment alim, double qty, String unit)
	{
		super(alim.getNom(), alim.getCat(), alim.getUrl()); // sets cat & url from alim
		// NB : alim.getNom() is already lowercased

		// association des attributs de l'ingr
		this.qty = qty ;
		this.unit = unit ;

		// ### special alim ;)
		this.setNutr(alim.getNutr()) ;

		// ajout à la bdd des ingr SEULEMENT SI n'y est pas encore. no doublon
		Ingredient ingr = Ingredient.getCorresponding
				(this.getNom(), this.getForme(), this.getUnit(), all_ingr);

		if (ingr == null)
		{
			all_ingr.add(this);
		}
	}


	private static boolean splitException(String nom)
	{
		// list manually all exceptions here
		String[] splitExceptions = {
				"noix",
				"vinaigre",
				"huile"
		} ;

		for (String exc : splitExceptions)
		{
			if (nom.contains(exc))
			{
				return true ;
			}
		}
		return false ;
	}


	public static String splitsNomIntoNomAndForme(String nom)
	{
		String forme = "" ;

		// tout d'abord, couper le nom s'il contient " de ". ex : feuilles de coriandre
		if (!splitException(nom) &&
				(nom.contains(" de " ) || nom.contains(" d'" ))
			)
		{
			String patched ;

			if (nom.contains(" de " ))
			{
				patched = ParseText.cutAndPatchUpString(
						nom, " de ", "=");
			}
			else // if (nom.contains(" d'"))
			{
				patched = ParseText.cutAndPatchUpString(
						nom, " d'", "=");
			}

			String[] nom_spl = patched.split("=");

			nom = nom_spl[1];
			forme = nom_spl[0];
		}
		else if (nom.contains(" en " )) // ATTENTION ! inversé par rapport à de.
		{
			String patched = ParseText.cutAndPatchUpString(
					nom, " en ", "=");

			String[] nom_spl = patched.split("=");

			nom = nom_spl[0];
			forme = nom_spl[1];
		}
		else if (nom.contains(" à " )) // pareil.
		{
			String patched = ParseText.cutAndPatchUpString(
					nom, " à ", "=");

			String[] nom_spl = patched.split("=");

			nom = nom_spl[0];
			forme = nom_spl[1];
		}

		return nom + ", " + forme ;
	}

	
	public void dispQty()
	{
		System.out.print("- [" + this.getNom());

		if (!this.getForme().isEmpty())
		{
			System.out.print(", " + this.getForme());
		}
		System.out.print("]");
		
		if (this.qty != 0)
		{
			System.out.print(" : " + this.qty + " " + this.unit);
		}
		else
		{
			System.out.print(" : " + "some");
		}
		
		System.out.println();
	}





	public static Ingredient getCorresponding(String nom, String forme, String unit, ArrayList<Ingredient> al) // if returns null, means Ingredient.exists() == false.
	{
		for (Ingredient ingr : Ingredient.all_ingr)
		{
			if (ingr.getNom().equals(nom) && ingr.getForme().equals(forme) && ingr.getUnit().equals(unit))
			{
				return ingr ;
			}
		}

		return null ;
	}


	public Aliment returnCorrespAlimEquals(ArrayList<Aliment> al)
	{
		for (Aliment alim : al)
		{			
			if (alim.getNom().equals(this.getNom()))
			{
				return alim ;
			}
		}
		return null ;
	}



	
	public ArrayList<Aliment> returnCorrespAlimContains(ArrayList<Aliment> al)
	{
		ArrayList<Aliment> out = new ArrayList<Aliment>();
		
		for (Aliment alim : al)
		{
			if (alim.getNom().contains(this.getNom()) || this.getNom().contains(alim.getNom()))
			{
				out.add(alim);
			}
		}
		return out ;
	}

	public static Ingredient createNewOrMatchExistingAlim(String nom, String forme, double qty, String unit)
	{
		// On passe par cette méthode publique qui utilise le constructeur privé, s'il a envie.
		// ### includes adding to ingr_al.

		// puis récupère l'aliment correspondant, s'il existe
		Ingredient ingr_out ;

		Ingredient corresp_ingr = Ingredient.getCorresponding(nom, forme, unit, Ingredient.all_ingr) ;
		Aliment corresp_alim = Aliment.getByNameAndForme(nom, forme, Aliment.al);

		if (corresp_ingr != null) // d'abord check si c'est pas un ingr déjà connu
			// NB : dans ce sens, on est sûr d'avoir le maximum de renseignements car si l'alim existe,
			// il fera hériter l'ingr et donc retournera cet ingr à chaque fois
		{
			ingr_out = new Ingredient(corresp_ingr, qty, unit);
		}
		else if (corresp_alim != null) // s'il ne l'est pas, construit depuis l'aliment.
		{
			Aliment alim = corresp_alim ;
			ingr_out = new Ingredient(corresp_alim, qty, unit);
		}
		else // si l'ingr n'existe manifestement pas dans notre bdd
		{
			Aliment unknown_alim = new Aliment(nom, "-", "-");
			Aliment.al.add(unknown_alim);

			ingr_out = new Ingredient(unknown_alim, qty, unit);
		}

		// ajoute la forme à l'ingr : ne change rien si la forme est vide
		ingr_out.setForme(forme);

		return ingr_out ;
	}

	public double getQty()
	{
		return this.qty ;
	}

	public String getUnit()
	{
		return this.unit ;
	}


	public void updateQty(Ingredient ingr_to_add)
	{
		if (this.unit.equals(ingr_to_add.unit))
		{
			this.qty += ingr_to_add.qty ;
		}
	}

}

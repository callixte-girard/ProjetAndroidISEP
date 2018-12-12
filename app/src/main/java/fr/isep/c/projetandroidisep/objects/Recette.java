package fr.isep.c.projetandroidisep.objects;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;


//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;



public class Recette implements Serializable
{
	public static ArrayList<Recette> al = new ArrayList<Recette>();

	public static int counter = 0 ;

	private int id ;
	private boolean select = true ; // can be deactivated to remove an aliment from the parsed recipe
	private String nom ;
	private String url ;
	private String instructions ;
	private String img_url ;
	//private LocalDateTime date_ajout ;
	private double rating = 0 ;
	private String duration = "?" ;
	private String type = "" ; // dépend de l'user
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>() ;


	public Recette(String nom
			, String url
			)
	{
		counter ++ ;
		this.id = counter ;

		this.nom = nom ;
		this.url = url ;
		//this.date_ajout = LocalDateTime.now() ;
	}


	public static void addToFavorites(Recette rec) {
		al.add(rec);
	}

	public static void removeFromFavorites(Recette rec) {
		al.remove(rec);
	}
	

	public void dispAttr(boolean disp_ingr)
	{
		//System.out.print("# Recette N°" + this.id);
		//System.out.print(" | " + ParseText.formatLocalDateTime(this.date_ajout));

		if (!this.type.isEmpty())
		{
			////System.out.print(" | " + this.type);
		}

		////System.out.print(" | Durée : " + this.duration);

		if (this.rating != 0)
		{
			////System.out.print(" | " + this.rating);
		}

		//System.out.print(" | " + this.nom);
		//System.out.print(" | " + this.url);

		////System.out.println();
		////System.out.println(Disp.line);

		if (disp_ingr)
		{
			for (Ingredient ing : this.ingredients)
			{
				ing.dispQty();
			}
			////System.out.println(Disp.star);
		}
	}

	public boolean alreadyExists(ArrayList<Recette> al_scan)
	{
		for (Recette rec : al_scan) {
			if (rec.getUrl().equals(this.url)) return true ;
		}
		return false ;
 	}

	
	public String getNom()
	{
		return this.nom ;
	}

	public String getUrl()
	{
		return this.url ;
	}

	public String getType()
	{
		return this.type ;
	}

	public void setRating(double rating)
	{
		this.rating = rating ;
	}

	public void setDuration(String duration)
	{
		if (!duration.isEmpty())
		{
			this.duration = duration ;
		}
	}

	public void setType(String type) {
		this.type = type ;
	}

	//public LocalDateTime getDate() {return this.date_ajout ;}

	public ArrayList<Ingredient> getIngredients()
	{
		return this.ingredients ;
	}
	
}

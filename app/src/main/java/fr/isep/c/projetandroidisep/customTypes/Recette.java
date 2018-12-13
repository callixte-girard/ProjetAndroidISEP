package fr.isep.c.projetandroidisep.customTypes;


import java.io.Serializable;
import java.util.ArrayList;


//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;



public class Recette implements Serializable
{
	public static ArrayList<Recette> al = new ArrayList<Recette>();

	public static int counter = 0 ;

	private int id ;
	private boolean select = true ; // can be deactivated to remove an aliment from the parsed recipe
	private String name ;
	private String url ;
	private String instructions ;
	private String img_url ;
	//private LocalDateTime date_ajout ;
	private double rating = 0 ;
	private String duration = "?" ;
	private String type = "" ; // dépend de l'user
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>() ;

	// it's just for google firebase
	public Recette() {}

	public Recette(String name
			, String type
			, String url
			)
	{
		counter ++ ;
		this.id = counter ;

		this.name = name ;
		this.type = type ;
		this.url = url ;
		//this.date_ajout = LocalDateTime.now() ;
	}

/*
	public static void addToFavorites(Recette rec) {
		al.add(rec);
	}

	public static void removeFromFavorites(Recette rec) {
		al.remove(rec);
	}
*/

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

		//System.out.print(" | " + this.name);
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

	// needed for creation
	public String getName() {
		return this.name ;
	}
	public void setName(String name) { this.name = name ; }
	public String getUrl()
	{
		return this.url ;
	}
	public void setUrl(String url) { this.url = url ; }
	public String getType()
	{
		return this.type ;
	}
	public void setType(String type) {
		this.type = type ;
	}

	// possible after creation
	public double getRating() { return this.rating ; }
	public void setRating(double rating)
	{
		this.rating = rating ;
	}
	public String getDuration() { return this.duration ; }
	public void setDuration(String duration) {
		if (!duration.isEmpty())
			this.duration = duration ;
	}



	//public LocalDateTime getDate() {return this.date_ajout ;}

	public ArrayList<Ingredient> getIngredients()
	{
		return this.ingredients ;
	}
	
}

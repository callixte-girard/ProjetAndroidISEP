package fr.isep.c.projetandroidisep.myCustomTypes;


import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;

import fr.isep.c.projetandroidisep.myClasses.ParseText;

import static fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses.DATE_PATTERN;


//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;



public class Recipe
{
	public final static String URL_BASE = "https://www.marmiton.org" ;
	public final static String URL_SEARCH = "/recettes/recherche.aspx?aqt=" ;

	public static int counter = 0 ;

	private int id ;
	private boolean selected = false ; // can be deactivated to remove an aliment from the parsed recipe
	private String dateAjout ;
    private String name ;
	private String url ;
	private String instructions ;
	private String imgUrl ;
	private Bitmap imgBitmap ;
	private double rating = 0 ;
	private String duration = "?" ;
	private String type = "" ; // dépend de l'user
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>() ;

	// it's just for google firebase
	public Recipe() {}

	public Recipe(String name
			, String type
			, String url
			)
	{
		counter ++ ;
		this.id = counter ;

		this.name = name ;
		this.type = type ;
		this.url = url ;

        this.dateAjout = ParseText.formatDate(new Date(System.currentTimeMillis()), DATE_PATTERN);

    }


	public boolean alreadyExists(ArrayList<Recipe> al_scan)
	{
		for (Recipe rec : al_scan) {
			if (rec.getUrl().equals(this.url)) return true ;
		}
		return false ;
 	}


 	public static Recipe getByUrl(ArrayList<Recipe> al_scan, String url)
	{
		for (Recipe rec : al_scan) {
			if (rec.getUrl().equals(url)) return rec ;
		}
		return null ;
	}

	//public static Recipe getById(ArrayList<Recipe> al_scan, int id)



	public static ArrayList<Recipe> fetchPageResultsFromDoc(Document doc)
	{
		ArrayList<Recipe> result = new ArrayList<>();

		Elements search_results = doc
				.getElementsByClass("recipe-search__resuts").first()
				.getElementsByClass("recipe-card");

		// ## parse les recipes contenues dans la page
		for (Element el : search_results)
		{
			String nom ;
			String url ;
			double rating ;
			String description ;
			String duration ;
			String img_url ;
			String type ;

			nom = el.getElementsByClass("recipe-card__title").first()
					.html().trim();
			url = el.getElementsByClass("recipe-card-link").first()
					.attr("href");
			rating = Double.parseDouble(el.getElementsByClass("recipe-card__rating__value").first()
					.html());
			// description = el.getElementsByClass("recipe-card__description").first()
			// 		.html(); // incomplete
			duration = el.getElementsByClass("recipe-card__duration__value").first()
					.html().trim();
			img_url = el.getElementsByClass("recipe-card__picture").first()
					.getElementsByTag("img").attr("src");
			type = el.getElementsByClass("recipe-card__tags").first()
					.getElementsByTag("li").first().html();

			url = URL_BASE + url; // pour rendre l'url complète et pas partielle comme dans le html

			Recipe rec = new Recipe(nom, type, url);
			rec.setRating(rating);
			rec.setDuration(duration);
			rec.setType(type);
			rec.setImgUrl(img_url);

			result.add(rec);
		}

		return result;
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
	public boolean getSelected() { return this.selected ; }
	public void setSelected(boolean selected) {this.selected = selected ; }
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
	public String getImgUrl() { return this.imgUrl ; }
	public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl ; }
	public Bitmap getImgBitmap() {return this.imgBitmap ; }
	public void setImgBitmap(Bitmap imgBitmap) { this.imgBitmap = imgBitmap ; }
	public String getDateAjout() {return this.dateAjout ;}
	public void setDateAjout(String dateAjout) { this.dateAjout = dateAjout ;}
	public ArrayList<Ingredient> getIngredients()
	{
		return this.ingredients ;
	}
	public void setIngredients(ArrayList<Ingredient> ingr_list) {
		this.ingredients = ingr_list ;
	}
}

package fr.isep.c.projetandroidisep.parseRecette;

import android.os.AsyncTask;
import android.util.Log;

import fr.isep.c.projetandroidisep.JsoupRequestTask;
import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.myClasses.*;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


////// ###### !!!!!!! IF NOT WORKING, PUT ENCODING TO UTF-8 !!!!!!!! ######################

public class ParseRecette extends AsyncTask<Void, Void, String>  {

	public String url_search ;

	public JsoupRequestTask.AsyncResponse delegate ;

	public ParseRecette(JsoupRequestTask.AsyncResponse delegate, String url_search){
		this.delegate = delegate ;
		this.url_search = url_search ;
	}

	// document à analyser
	private Document doc ;

	// holder for the async task
	JsoupRequestTask jsoupRequestTask = new JsoupRequestTask(new JsoupRequestTask.AsyncResponse()
	{
		@Override
		public void processFinish(String output){
			//Here you will receive the result fired from async class of onPostExecute(result) method.
			doc = Jsoup.parse(output);
			//Log.d("output", doc.html());
		}
	});

	
	// variables
	private static String url_base = "https://www.marmiton.org" ;
	private static String url_search_suffix = "/recettes/recherche.aspx?aqt=" ;
		// coche la case "afficher seulement les recettes".
		// pour tout afficher, il faut rajouter "type=all&" après "aspx?"


	// listes
	private static ArrayList<Recette> rec_temp = new ArrayList<Recette>() ;


	///////////////////////////////////// MAIN METHODS /////////////////////////////////////

/*
	public static ArrayList<Recette> searchRecetteByKeywordsAndDeepness
			(String search, int deepness)
	{
		// adapte les keywords cherchés au site
		search = search.replaceAll(" ", "-");

		ArrayList<Recette> result_full = new ArrayList<>();

		// crafts full URL to perform search
		String url_search_base = url_base + url_search_suffix;
		String url_search = url_search_base + search;

		// récupère le doc parsé depuis l'async task
		//Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(url_search);
		// .execute();

		result_full.addAll(fetchRecetteFromURLDocument(doc));

		// le fait autant de fois que deepness le demande
		for (int i=0 ; i < deepness ; i++)
		{
			Element next_page_tag = doc
					.getElementsByClass("af-pagination").first()
					.getElementsByClass("next-page").first();

			String next_page_link = next_page_tag
					.getElementsByTag("a").first()
					.attr("href");

			String next_page_url = url_base + next_page_link ;

			doc = ParseHtml.fetchHtmlAsDocumentFromUrl(next_page_url);

			result_full.addAll(fetchRecetteFromURLDocument(doc));
		}

		return result_full ;
	}
*/


	@Override
	protected String doInBackground(Void... voids) {

		//String url = urls[0];
		Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(this.url_search);
		String html = doc.html();

		return html ;
	}

	@Override
	protected void onPostExecute(String result) {
		delegate.processFinish(result);

		//Log.d("YOUPI",result);
	}



	private ArrayList<Recette> fetchRecetteFromURLDocument(Document doc)
	{
		ArrayList<Recette> result = new ArrayList<>();

		Elements search_results = doc
				.getElementsByClass("recipe-search__resuts").first()
				.getElementsByClass("recipe-card");

		// ## parse les recettes contenues dans la page
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

			url = url_base + url; // pour rendre l'url complète et pas partielle comme dans le html

			Recette rec = new Recette(nom, url);
			rec.setRating(rating);
			rec.setDuration(duration);
			rec.setType(type);

			result.add(rec);
		}

		return result;
	}



	private Recette parseFromUrl(String url)
	{
		System.out.println("> Fetching recette at : " + url);
		double start = System.currentTimeMillis();
		/////////// la mesure commence ici.

		String url_toFetch = /* url_main + */ url ; // pour switcher url absolue/globale

		// ### 1) FIRST OF ALL, parses title directly with Jsoup
		Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(url_toFetch);
		String nom ;

		// --> then get the inner html of <title>
		String title = doc.select("title").first().html() ;
		String[] split = title.split(":") ;
		nom = split[0].trim() ;

		// --> create Recette object from title & url
		Recette rec = new Recette(nom, url);

		///////////////

		// ### 2) THEN, fetches all the ingredients from the specific part
		String[] html = ParseHtml.splitStringIntoLinesArray(doc.html());

		// --> extracts only needed lines
		ArrayList<String> extracted = ParseHtml.extractOnlyNeededLines
				(html
						, "Mrtn.recipesData ="
						, "Mrtn = Mrtn || {};"
				);
		//for (String s : extracted) { System.out.println(s); }

		// --> cleans the line with just what we need
		String subl = ParseHtml.extractOnlyNeededSubline(
				extracted.get(0),
				'"' + "ingredients" + '"' + ":[",
				"]}]};"
		) ;

		// --> petite correction sur les pbs d'encodage
		subl = EncodingCorrecter.convertFromU00(subl);

		// --> splits the line into requests.
		String[] requests = subl.split("\\{");

		// --> ...finally, fetches ingredients
		for (String s : requests)
		{
			String[] attr_list = s.split(",");

			String name = "" ;
			String forme = "" ;
			double qty = 0 ;
			String unit = "" ;

			for (String s2 : attr_list)
			{
				s2 = ParseHtml.removeSpecifiedCharFromString(s2, '"');
				//System.out.println(s2);

				try
				{
					String[] spl = s2.split(":");


					if (spl[0].equals("name"))
					{
						name = spl[1] ;
					}
					else if (spl[0].equals("qty"))
					{
						qty = Double.parseDouble(spl[1]) ;
					}
					else if (spl[0].equals("unit"))
					{
						unit = spl[1].replaceAll("}", "") ;
					}

					// quand créer l'ingredient ?
					if (s2.contains("}"))
					{
						String name_and_forme = Ingredient.splitsNomIntoNomAndForme(name);
						String[] split_name = name_and_forme.split(",");
						name = split_name[0].trim();
						forme = split_name[1].trim();

						// # MODE 1 : méthode publique, constructeur privé
						Ingredient ingr = Ingredient.createNewOrMatchExistingAlim
								(name, forme, qty, unit);
						rec.getIngredients().add(ingr);
						// # MODE 2 : constructeur public
						// inclut l'ajout à la recette
						//new Ingredient(rec, name, qty, unit);
					}
				}
				catch (ArrayIndexOutOfBoundsException ex)
				{
					///
				}
			}
		}

		////////// la mesure finit ici.
		double end = System.currentTimeMillis();

		return rec ;
	}

}

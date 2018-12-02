package fr.isep.c.projetandroidisep.parseAlim;

import fr.isep.c.projetandroidisep.myClasses.*;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

////// ###### !!!!!!! IF NOT WORKING, PUT ENCODING TO UTF-8 !!!!!!!! ######################

public class ParseAlim3 {
	
	private static String url_main = "https://informationsnutritionnelles.fr" ;

	private static ArrayList<Aliment> alim_temp = new ArrayList<Aliment>();

	//////////////////////////////////////////////////////////:
	
	//public static void main(String[] args) throws Exception // parses everything from online DB
	public static ArrayList<Aliment> parseAlimDB() throws Exception
	{
		System.out.println("-------------------- ParseAlim3 --------------------");
		
		double start = System.currentTimeMillis();
		int nb_actuel = 0 ; // pour compter la prog
	
		
		/////// INIT : ### download the page with all Aliment
		System.out.println(">>> Fetching aliment list... May take some time. Please wait");
		Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(url_main + "/aliments");
		System.out.println(Disp.htag);
		
		ArrayList<String> url_list = getLinksToParse(doc);
		
		
		// then parse them one by one.
		for (String url : url_list)
		{
			Aliment al = parseFromShortUrl(url);
			// and adds the aliment to al.
			alim_temp.add(al);

		//	al.dispBlock(false); // true/false = with/without nutr
			al.dispCompact();
			
			// display progress
			nb_actuel += 1 ;
			Disp.displayProgress(nb_actuel, url_list.size());
			
			// ### NB : la sauvegarde a lieu plus tard,
			// uniquement si tout le téléchargement
			// s'est passé sans problème
		}
		
		double end = System.currentTimeMillis();

		// on s'en balek de la durée en vrai
		//System.out.println(":D :D :D ------------ GOOD JOB ! Total time : " + (end-start) + " ms ------------ :D :D :D");

		return alim_temp ;

	} // fin du main
	
	
	private static ArrayList<String> getLinksToParse(Document doc)
	{		
		Element tbody = doc.getElementsByTag("tbody").first();
		Elements rows = tbody.getElementsByTag("tr");
		
		ArrayList<String> url_to_parse = new ArrayList<String>();
		
		for (Element el : rows)
		{
			// ne récupère que les aliments moyens (ou qui proviennent de Ciqual 2013)
			Elements elected = el.getElementsContainingText("Aliment moyen");
			
			for (Element td : elected)
			{
				Element link = td.getElementsByAttribute("href").first();

				if (link != null)
				{
					String url = link.attr("href");
					
					url_to_parse.add(url);
				}
			}
		}
		
		return url_to_parse ;
	}
	
	
	private static Aliment parseFromShortUrl(String url)
	{				
		String url_toFetch = ParseAlim3.url_main + url ;
		
	//	System.out.println(">>> Now fetching : [" + url_toFetch + "]");
	//	System.out.println(Disp.line);
		double start = System.currentTimeMillis();
		/////////// la mesure commence ici.
		
		Document doc = ParseHtml.fetchHtmlAsDocumentFromUrl(url_toFetch);
		
		// 1) ### parse name & cats
		
		String nom = doc.getElementsByTag("h1").first().html();
		String cat = doc.getElementsByTag("b").first().getElementsByTag("a").first().html();
		
		// 2) ### parse nutr list
		
		// --> converts " into ° so that it can be designed as string in java
		String html = ParseHtml.makeUsableInJavaStrings(doc.html(), '"', '°');
		
		String[] html_split = ParseHtml.splitStringIntoLinesArray(html);
		//for (String s : html_split) {System.out.println(s);}
		
		ArrayList<String> extracted = ParseHtml.extractOnlyNeededLines(
				html_split, 
				"<table", 
			//	"<tbody>"
				"</table>"
		);
	
		extracted = ParseHtml.recollageOneRequestPerLine(extracted, "tr", "td", "§");
		//for (String s : extracted) {System.out.println(s);}
		
		ArrayList<Nutr> fetched = parseNutrLineByLine(extracted);
		
		// puis fusionne kcal et kJ pour l'énergie et beta-carotène et rétinol pour vitamine A
		fetched = Nutr.postTraitement_energie(fetched);
		fetched = Nutr.postTraitement_vitamineA(fetched);
		
		// FINALLY, creates alim and adds nutr_al to it
		Aliment alim = new Aliment(nom, cat, url);
		alim.setNutr(fetched) ;
		
		///////////
		double end = System.currentTimeMillis();
		
		//Nutr.dispAllFull(fetched);
		
		System.out.println("* Parsing for [" + url_toFetch + "] took : " + (end - start) + " ms. ***");
	//	System.out.println(Disp.star);
		
		return alim ;
	}

	
	private static ArrayList<Nutr> parseNutrLineByLine(ArrayList<String> al)
	//private static void parseNutrLineByLine(String[] al)
	{		
		ArrayList<Nutr> fetched = new ArrayList<Nutr>() ;
		
		Nutr n = null ;
		
		Nutr famille = null ;
		Nutr nutr = null ;
	
		String nom = null ;
		Double val = null ;
		String unit = "" ;
		
		for (String req : al)
		{
			req = req.trim();
			//System.out.println(req);
			
			Document doc = Jsoup.parse(req);
			
			
			// 2) AUTRE LIGNE : parse info indiquée selon condition.
			String[] splu = doc.body().text().split("§");
			// ici on parse les valeurs !!!! 
			{
				try
				{
					nom = splu[1];
					String[] spla = splu[2].split(" ");
					
					unit = spla[1];
					val = Double.parseDouble(spla[0]);
				}
				catch (NumberFormatException e1)
				{
					unit = "" ;
					val = null ;
				}
				catch (ArrayIndexOutOfBoundsException e)
				// si <td> vide
				{
					unit = "" ;
					val = null ;
				}
			}
			
			
			// 3) FINALLY c'est le moment de créer et ranger dans la lhm !!!
			if (nom != null)
			{
				n = new Nutr(nom, val, unit);
			
	
				// Inspect lines that change level
				if (req.contains("<tr class=°tr1"))
				{
					n.setParent(n) ;
					famille = n ;
				}
				else if (req.contains("<tr class=°tr2"))
				{
					if (req.contains("&nbsp; -"))
					{
						n.setParent(nutr) ;
						
						nutr.addSubNutr(n);
					}
					else
					{
						n.setParent(famille) ;
						nutr = n ;
						
						famille.addSubNutr(nutr);
					}
				}
				
				// DEBUGGING
				//n.dispParent();
				//n.dispWithSubNutr();
			
				fetched.add(n);
			}
		//	disp(star);
		//	disp(line);
			
		} // FIN BOUCLE
		
		return fetched ;
	}
	

}

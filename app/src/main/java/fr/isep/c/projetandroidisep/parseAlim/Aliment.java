package fr.isep.c.projetandroidisep.parseAlim;

import fr.isep.c.projetandroidisep.myClasses.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Aliment implements Serializable
{
	public static ArrayList<Aliment> al = new ArrayList<>() ;
	
	// SET-AT-CREATION ATTRIBUTES
	private String nom ;
	private String cat ; // categorie. Différent de type. On ne prend que le type "Aliment moyen"
	final private String url ;
	//final private String country_code ;

	// MODIFIABLE ET OPTIONNEL
	private String forme = "" ;
	
	// --------------------- NUTRIMENTS ----------------------------
	private ArrayList<Nutr> nutr = new ArrayList<Nutr>();
		
	//////////// ( à implémenter )
	/*
	public int portion = 0 ;
	public String qtite_portion = null ;	
	// for veg only :
	public boolean[] dispo = new boolean[12] ;
	public boolean[] haute_saison = new boolean[12] ;
	*/

	// #######################################################################################################

	public Aliment(String nom
			,String cat
			,String url
		//	,String country_code
		//	,boolean from_db
		//	,Nutr proteines,	Nutr lipides, Nutr glucides, Nutr kcal, Nutr vitamines, Nutr mineraux
		//	,LinkedHashMap<String, Nutr> nutr
		//	,ArrayList<Nutr> nutr
			)
	{
		/*
		// correcter 3-in-1 pour csv, jsoup et pbs d'encodage
		this.nom = EncodingCorrecter.normaliseAzertySpecialLetters(nom
				.replace(",", "")
				.replace("°", "'")) ;
		*/
		// sets to lower case to make it more likely to correspond to an ingredient
		nom = nom.toLowerCase();
		cat = cat.toLowerCase();
		
		this.nom = nom ;			 
		this.cat = cat ;
		this.url = url ;
	//	this.country_code = country_code ;
	//	this.from_db = from_db ;

	}
	
	
	public static boolean existsName(String nom, String forme, ArrayList<Aliment> l)
	{
		for (Aliment a : l)
		{
			if (nom.equals(a.nom) && forme.equals(a.forme))
			{
				return true ;
			}
		}
		return false ;
	}
	
	
	public static boolean existsCat(String nom, ArrayList<Aliment> l)
	{
		for (Aliment a : l)
		{
			if (nom.equals(a.cat))
			{
				return true ;
			}
		}
		return false ;
	}


	public static boolean isContainedNotEquals(String search, ArrayList<Aliment> l) 
	{
		for (Aliment a : l) 
		{
			if (a.nom.equals(search)) 
			{
				return false ;
			}
			else if (a.nom.contains(search)) 
			{
				return true ;
			}
			else if (search.contains(a.nom))
			{
				return true ;
			}
		}
		return false ;
	}


	public boolean isEmpty()
	{
		if (this.nutr.isEmpty())
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}

	/*
	public boolean isDispoNow() 
	{
		LocalDate ld = LocalDate.now();
		int month = ld.getMonthValue();
		
		if (this.haute_saison[month] || this.dispo[month])
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	*/
	
	
	public void dispBlock(boolean with_nutr)
	{
		System.out.println("- nom : " + this.nom);
		System.out.println("- url : " + this.url);
		System.out.println("- cat : " + this.cat);
		System.out.println(Disp.line);
		
		if (with_nutr)
		{
			Nutr.dispAllFull(this.nutr);
		}	
	}
	
	
	public void dispCompact() {
		System.out.println("### "
						+ this.cat + " | "
						+ this.nom + " | "
						+ this.url + " | "
				//		+ this.country_code + " | "
		);

		//System.out.println(Disp.line);
	}




	public static Aliment getByNameAndForme(String name, String forme, ArrayList<Aliment> l)
	{
		if (Aliment.existsName(name, forme, l))
		{
			for (Aliment a : l)
			{
				if (a.nom.equals(name) && a.forme.equals(forme))
				{
					return a ;
				}
			}
		}
		return null ;
	}


	public static ArrayList<Aliment> getAllCustom(boolean custom_alim, ArrayList<Aliment> l)
	{
		// return all the custom aliments if custom_alim == true, all the official else.

		ArrayList<Aliment> out = new ArrayList<>();

		for (Aliment a : l)
		{
			if (custom_alim)
			{
				if (a.url.equals("-"))
				{
					out.add(a);
				}
			}
			else
			{
				if (!a.url.equals("-"))
				{
					out.add(a);
				}
			}
		}

		return out ;
	}

	
	public static ArrayList<Aliment> getByCat(String search, ArrayList<Aliment> l)
	{
		ArrayList<Aliment> out = new ArrayList<Aliment>();
		
		if (Aliment.existsCat(search, l)) 
		{
			for (Aliment a : l) 
			{
				if (a.cat.equals(search)) 
				{
					out.add(a);
				}
			}
		}
		return out ;
	}


	public static ArrayList<String> listAllCat(ArrayList<Aliment> al)
	{
		ArrayList<String> all_cat = new ArrayList<String>();
		
		for (Aliment alim : al)
		{
			if (!all_cat.contains(alim.cat))
			{
				all_cat.add(alim.cat);
			}
		}
		return all_cat ;
	}
	
	
	public static ArrayList<Aliment> filterOnlyCatInList(ArrayList<String> cat_list, ArrayList<Aliment> al)
	{
		ArrayList<Aliment> out = new ArrayList<Aliment>();
		
		for (Aliment alim : al)
		{
			if (cat_list.contains(alim.cat))
			{
				out.add(alim);
			}
		}
		
		return out ;
	}

	public String getNom()
	{
		return this.nom ;
	}

	public String getCat()
	{
		return this.cat ;
	}

	public String getUrl()
	{
		return this.url ;
	}

	public ArrayList<Nutr> getNutr()
	{
		return this.nutr ;
	}

	public String getForme()
	{
		return this.forme ;
	}

	public void setForme(String forme)
	{
		this.forme = forme ;
	}

	public void setNutr(ArrayList<Nutr> nutr)
	{
		this.nutr = nutr ;
	}
}
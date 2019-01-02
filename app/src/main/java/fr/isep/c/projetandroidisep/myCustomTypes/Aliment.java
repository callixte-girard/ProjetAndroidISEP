package fr.isep.c.projetandroidisep.myCustomTypes;

import fr.isep.c.projetandroidisep.myClasses.utils.Disp;

import java.util.ArrayList;


public class Aliment
{
	public static ArrayList<Aliment> all_alim = new ArrayList<>();

	// SET-AT-CREATION ATTRIBUTES
	private String name ;
	private String cat ; // categorie. Différent de type. On ne prend que le type "Aliment moyen"
	private String url ;
	//final private String country_code ;

	// MODIFIABLE ET OPTIONNEL
	private String form = "" ;
	
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

	public Aliment() {}
	
	public Aliment(String name
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
		this.name = EncodingCorrecter.normaliseAzertySpecialLetters(name
				.replace(",", "")
				.replace("°", "'")) ;
		*/
		// sets to lower case to make it more likely to correspond to an ingredient
		name = name.toLowerCase();
		cat = cat.toLowerCase();
		
		this.name = name ;			 
		this.cat = cat ;
		this.url = url ;
	//	this.country_code = country_code ;
	//	this.from_db = from_db ;

	}
	
	
	public static boolean existsName(String name, String form, ArrayList<? extends Aliment> l)
	{
		for (Aliment a : l)
		{
			if (name.equals(a.name) && form.equals(a.form))
			{
				return true ;
			}
		}
		return false ;
	}
	
	
	public static boolean existsCat(String name, ArrayList<Aliment> l)
	{
		for (Aliment a : l)
		{
			if (name.equals(a.cat))
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
			if (a.name.equals(search)) 
			{
				return false ;
			}
			else if (a.name.contains(search)) 
			{
				return true ;
			}
			else if (search.contains(a.name))
			{
				return true ;
			}
		}
		return false ;
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
		System.out.println("- name : " + this.name);
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
						+ this.name + " | "
						+ this.url + " | "
				//		+ this.country_code + " | "
		);

		//System.out.println(Disp.line);
	}




	public static Aliment getByNameAndForm(String name, String form, ArrayList<? extends Aliment> l)
	{
		if (Aliment.existsName(name, form, l))
		{
			for (Aliment a : l)
			{
				if (a.name.equals(name) && a.form.equals(form))
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

	public String getName() {
		return this.name ;
	}
	public void setName(String name) { this.name = name ; }
	public String getCat()
	{
		return this.cat ;
	}
	public void setCat(String cat) { this.cat = cat ; }
	public String getUrl()
	{
		return this.url ;
	}
	public void setUrl(String url) { this.url = url ; }
	public String getForm()
	{
		return this.form ;
	}
	public void setForm(String form) { this.form = form ; }
	public ArrayList<Nutr> getNutr()
	{
		return this.nutr ;
	}
	public void setNutr(ArrayList<Nutr> nutr) { this.nutr = nutr ; }
	
}
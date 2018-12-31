package fr.isep.c.projetandroidisep.myCustomTypes;


import android.util.Log;

import org.jsoup.nodes.Document;

import java.util.ArrayList;


import fr.isep.c.projetandroidisep.myClasses.*;
//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;

public class Ingredient extends Aliment
{
	//private String name ;
	private boolean selected = true ; // can be deactivated to remove an aliment from the parsed recipe
	private double qty ;
	private String unit ;

	public Ingredient() {}

	private Ingredient(Aliment alim, double qty, String unit)
	{
		super(alim.getName(), alim.getCat(), alim.getUrl()); // sets cat & url from alim
		// NB : alim.getNom() is already lowercased

		// association des attributs de l'ingr
		this.qty = qty ;
		this.unit = unit ;

	}

	public Ingredient(String name, String form, double qty, String unit)
	{
		this.setName(name);
		this.setForm(form);
		this.qty =  qty ;
		this.unit = unit ;

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


	public static ArrayList<Ingredient> fetchAllFromDoc(Document doc)
	{
		ArrayList<Ingredient> ingr_list = new ArrayList<>();

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
			String form = "" ;
			double qty = 0 ;
			String unit = "" ;

			for (String s2 : attr_list)
			{
				s2 = ParseHtml.removeSpecifiedCharFromString(s2, '"');

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
						unit = spl[1].replaceAll("\\}", "") ;
					}

					// quand créer l'ingredient ?
					if (s2.contains("}"))
					{
						String name_and_forme = Ingredient.splitsNomIntoNomAndForme(name);
						String[] split_name = name_and_forme.split(",");
						name = split_name[0].trim();
						form = split_name[1].trim();

						/*
						Log.d("name + forme", name_and_forme);
						Log.d("qty", String.valueOf(qty));
						Log.d("unit", unit);
						Log.d("", Disp.line);
						*/

						// # MODE 1 : méthode publique, constructeur privé
						Ingredient ingr = new Ingredient(name, form, qty, unit);
						ingr_list.add(ingr);
					}
				}
				catch (ArrayIndexOutOfBoundsException ex)
				{
					//Log.d("ingr_parse_error", ex.getMessage());
				}
				catch (Exception e)
				{
					Log.d("ingr_parse_severe_error", e.getMessage());
				}
			}
		}

		return ingr_list ;
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


	public Aliment returnCorrespAlimEquals(ArrayList<Aliment> al)
	{
		for (Aliment alim : al)
		{			
			if (alim.getName().equals(this.getName()))
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
			if (alim.getName().contains(this.getName()) || this.getName().contains(alim.getName()))
			{
				out.add(alim);
			}
		}
		return out ;
	}

    public void updateQty(Ingredient ingr_to_add)
    {
        if (this.unit.equals(ingr_to_add.unit))
        {
            this.qty += ingr_to_add.qty ;
        }
    }

    public String getNameAndForm()
	{
		String out = this.getName();

		if (!this.getForm().isEmpty()) {
			out += " (" + this.getForm() + ")" ;
		}

		return out ;
	}

    public boolean getSelected() {
		return this.selected ;
	}
	public void setSelected(boolean selected) {
		this.selected = selected ;
	}
	public double getQty() {
		return this.qty ;
	}
	public void setQty(double qty) {
	    this.qty = qty ;
    }
	public String getUnit() {
		return this.unit ;
	}
	public void setUnit(String unit) {
	    this.unit = unit ;
    }

}

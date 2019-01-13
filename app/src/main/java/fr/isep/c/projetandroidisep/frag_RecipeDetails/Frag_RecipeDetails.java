package fr.isep.c.projetandroidisep.frag_RecipeDetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myClasses.utils.Misc;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;

public class Frag_RecipeDetails extends Fragment
{
    private TextView recipe_name, recipe_instructions ;
    private ImageView recipe_img ;
    private Button button_back ;

    private View view ;

    private Recipe rec ;


    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        Bundle bundle = this.getArguments();

        String name = bundle.getString("recipe_name");
        String img_url = bundle.getString("recipe_img_url");
        ArrayList<String> instructions = bundle.getStringArrayList("recipe_instructions");

        // name
        recipe_name = view.findViewById(R.id.recipe_name);
        recipe_name.setText(name);

        // image
        recipe_img = view.findViewById(R.id.recipe_img);
        Glide.with(this).load(img_url).into(recipe_img);
        //recipe_img.setVisibility(View.GONE);

        // instructions
        recipe_instructions = view.findViewById(R.id.recipe_instructions);
        String instructions_unified = Misc.patchStringArrayList(instructions);
        recipe_instructions.setText(instructions_unified);

        // button
        button_back = view.findViewById(R.id.button_center);
        button_back.setText("Back to favorite recipes");
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).destroyFrag_recipeDetails();
                ((MainActivity) getActivity()).displayFrag_myRecipes();
            }
        });

        return view ;
    }


    public void setRecipe(Recipe rec) {
        this.rec = rec ;
    }
    public Recipe getRecipe() {
        return this.rec ;
    }

}

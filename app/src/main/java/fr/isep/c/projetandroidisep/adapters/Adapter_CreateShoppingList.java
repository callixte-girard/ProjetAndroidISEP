package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Adapter_CreateShoppingList extends RecyclerView.Adapter
            <Adapter_CreateShoppingList
                .RecyclerViewHolder_SelectRecipes>
{

    static class RecyclerViewHolder_SelectRecipes extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView recipe_name, recipe_duration ;
        private CheckBox checkbox_select_recipe ;


        RecyclerViewHolder_SelectRecipes(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.title);
            recipe_duration = view.findViewById(R.id.sub_title);
            checkbox_select_recipe = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private Context context ;
    private MainActivity main_act ;

    private ArrayList<Recipe> recipe_list ;

    public Adapter_CreateShoppingList(Context context, ArrayList<Recipe> recipe_list) {
        this.context = context ;
        this.recipe_list = recipe_list ;
        this.main_act = (MainActivity) this.context;
    }


    @Override
    public RecyclerViewHolder_SelectRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2_lines, viewGroup, false);

        return new RecyclerViewHolder_SelectRecipes(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SelectRecipes holder, final int i)
    {
        /*
        final Recipe rec = main_act.getFavoriteRecipes().get(holder.getAdapterPosition()) ;

        //holder.recipe_name.setText(ParseText.shortifyTitle(rec.getName(), MainActivity.MAX_LABEL_LENGTH));
        holder.recipe_name.setText(rec.getName());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.recipe_duration.setText(rec.getDuration());

        holder.checkbox_select_recipe.setChecked(false);
        holder.checkbox_select_recipe.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Recipe rec = MainActivity.getFavoriteRecipes().get(i);

                Log.d("is_selected",
                        rec.getName() + " | " + String.valueOf(isChecked));

                rec.setSelected(isChecked);
            }
        }); */
    }

    @Override
    public int getItemCount() {

        MainActivity act = (MainActivity) this.context ;

        return (null != act.getFavoriteRecipes()
                ? act.getFavoriteRecipes().size() : 0);
    }

}

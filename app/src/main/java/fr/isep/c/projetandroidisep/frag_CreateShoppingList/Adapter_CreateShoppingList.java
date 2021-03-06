package fr.isep.c.projetandroidisep.frag_CreateShoppingList;

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


public class Adapter_CreateShoppingList
        extends RecyclerView.Adapter<Adapter_CreateShoppingList.Holder_CreateShoppingList>
{
    public class Holder_CreateShoppingList
            extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        public TextView recipe_name, recipe_duration ;
        public RecyclerView recipe_ingr_expandable ;
        public CheckBox checkbox_select_recipe, checkbox_show_expandable ;

        public boolean show_expandable = false ;

        public Holder_CreateShoppingList(View view)
        {
            super(view);

            recipe_name = view.findViewById(R.id.title);
            recipe_duration = view.findViewById(R.id.subtitle);
            //checkbox_show_expandable = view.findViewById(R.id.checkbox_show_expandable);
            checkbox_select_recipe = view.findViewById(R.id.checkbox_select);
            recipe_ingr_expandable = view.findViewById(R.id.ingr_grid_expandable);
        }

        @Override
        public void onClick(View view) {

        }

        public void hideShowExpandableList(boolean isChecked)
        {
            //if (this.show_expandable) {
            if (isChecked) {
                this.recipe_ingr_expandable.setVisibility(View.VISIBLE);
            } else {
                this.recipe_ingr_expandable.setVisibility(View.GONE);
            }
        }

    }



    private MainActivity main_act ;
    private ArrayList<Recipe> al = new ArrayList<>();


    public Adapter_CreateShoppingList(Context context) {
        this.main_act = (MainActivity) context ;
        this.al.addAll(main_act.getFavoriteRecipes()) ;
    }


    @Override
    public Holder_CreateShoppingList onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable_selection, viewGroup, false);

        return new Holder_CreateShoppingList(v);
    }

    @Override
    public void onBindViewHolder(final Holder_CreateShoppingList holder, final int i)
    {
        final Recipe rec = main_act.getFavoriteRecipes().get(holder.getAdapterPosition()) ;

        //holder.recipe_name.setText(Misc.shortifyTitle(rec.getName(), MainActivity.MAX_LABEL_LENGTH));
        holder.recipe_name.setText(rec.getName());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.recipe_duration.setText(rec.getDuration());

        // checkboxes
        /*
        holder.checkbox_show_expandable.setChecked(false);
        holder.checkbox_show_expandable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("show_expandable", rec.getName() + " | " + isChecked);

                // hides or show panel
                holder.show_expandable = isChecked ;
                holder.hideShowExpandableList(holder.show_expandable);
            }
        }); */


        holder.checkbox_select_recipe.setChecked(false);
        holder.checkbox_select_recipe.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Recipe rec = main_act.getFavoriteRecipes().get(i);

                Log.d("is_selected",
                        rec.getName() + " | " + String.valueOf(isChecked));

                rec.setSelected(isChecked);
            }
        });


    }

    @Override
    public int getItemCount() {

        return (null != al ? al.size() : 0);
    }


}

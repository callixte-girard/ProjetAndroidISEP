package fr.isep.c.projetandroidisep.frag_FavoriteRecipes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.frag_SearchRecipe.Listener_SearchRecipe_SelectIngredient;
import fr.isep.c.projetandroidisep.frag_SearchRecipe.Listener_SearchRecipe_AddRemove;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Adapter_FavoriteRecipes
        extends RecyclerView.Adapter<Adapter_FavoriteRecipes.Holder_FavoriteRecipes>
{
    private class Adapter_IngredientGrid
            extends RecyclerView.Adapter<Adapter_IngredientGrid.Holder_IngredientGrid>
            //    implements Listener_SearchRecipe_SelectIngredient
    {
        // HOLDER
        public class Holder_IngredientGrid
                extends RecyclerView.ViewHolder
                implements View.OnClickListener
        {
            private Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient;

            public TextView ingr_name_form, ingr_qty, ingr_unit ;
            public CheckBox chkbx_ingr_select ;


            public Holder_IngredientGrid(Context context, View view
                    , Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient
            ) {
                super(view);

                this.listener_favoriteRecipes_selectIngredient = listener_favoriteRecipes_selectIngredient;

                ingr_name_form = view.findViewById(R.id.ingr_name_form);
                ingr_qty = view.findViewById(R.id.ingr_qty);
                ingr_unit = view.findViewById(R.id.ingr_unit);
                chkbx_ingr_select = view.findViewById(R.id.checkbox);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }
        // ADAPTER

        private MainActivity main_act ;
        //private Recipe rec ;
        private int index_rec ;
        private ArrayList<Ingredient> al = new ArrayList<>();

        private Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient;


        public Adapter_IngredientGrid(Context context
                                      //   , Recipe rec
                , int index_rec
                , Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient
        ) {
            this.main_act = (MainActivity) context ;
            //this.rec = rec ;
            this.index_rec = index_rec ;
            this.al.addAll(this.main_act.getFavoriteRecipes().get(index_rec).getIngredients());
            this.listener_favoriteRecipes_selectIngredient = listener_favoriteRecipes_selectIngredient;
        }




        @Override
        public Holder_IngredientGrid onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_grid_chkbx, viewGroup, false);

            return new Holder_IngredientGrid
                    (main_act, v, listener_favoriteRecipes_selectIngredient);
        }

        @Override
        public void onBindViewHolder(final Holder_IngredientGrid holder, final int i)
        {
            final Ingredient ingr = al.get(i);

            holder.ingr_name_form.setText(" - " + ingr.returnNameAndForm());

            if (ingr.getQty() != 0) {
                holder.ingr_qty.setText(String.valueOf(ingr.getQty()));
                holder.ingr_unit.setText(ingr.getUnit());
            }

            holder.ingr_qty.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.ingr_unit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            holder.chkbx_ingr_select.setChecked(ingr.isSelected());
            holder.chkbx_ingr_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    listener_favoriteRecipes_selectIngredient.checkedListener_selectIngredient
                            (buttonView, index_rec, holder.getAdapterPosition(), isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != al ? al.size() : 0);
        }
    }

/////////////////////////////

    public class Holder_FavoriteRecipes
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public LinearLayout recipe_header ;
        public RecyclerView recipe_ingr_expandable ;
        public ImageView recipe_img ;
        public TextView recipe_name, recipe_duration, recipe_rating ;
        public CheckBox checkbox_delete_from_favorites, checkbox_show_expandable ;

        private Listener_FavoriteRecipes_AddRemove listener_favoriteRecipes_addRemove;
        private Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient;

        public boolean show_expandable = false ;


        public Holder_FavoriteRecipes(View view
                , Listener_FavoriteRecipes_AddRemove listener_favoriteRecipes_addRemove
                , Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient
        ) {
            super(view);
            this.listener_favoriteRecipes_addRemove = listener_favoriteRecipes_addRemove ;
            this.listener_favoriteRecipes_selectIngredient = listener_favoriteRecipes_selectIngredient;

            recipe_header = view.findViewById(R.id.header_vertical); // the linearlayout clickable
            recipe_name = view.findViewById(R.id.title);
            //recipe_img = view.findViewById(R.id.recipe_img);
            recipe_duration = view.findViewById(R.id.subtitle);
            //recipe_rating = view.findViewById(R.id.recipe_rating);
            checkbox_delete_from_favorites = view.findViewById(R.id.checkbox_add_remove);
            checkbox_show_expandable = view.findViewById(R.id.checkbox_show_expandable);
            recipe_ingr_expandable = view.findViewById(R.id.ingr_grid_expandable);

            view.setOnClickListener(this);
        }


        public void hideShowExpandableList()
        {
            if (this.show_expandable) {
                //if (isChecked) {
                this.recipe_ingr_expandable.setVisibility(View.VISIBLE);
            } else {
                this.recipe_ingr_expandable.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View view) {

        }
    }



    private MainActivity main_act ;
    private ArrayList<Recipe> al = new ArrayList<>();

    private Listener_FavoriteRecipes_AddRemove listener_favoriteRecipes_addRemove;
    private Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient ;


    public Adapter_FavoriteRecipes(Context context
            , Listener_FavoriteRecipes_AddRemove listener_favoriteRecipes_addRemove
            , Listener_FavoriteRecipes_SelectIngredient listener_favoriteRecipes_selectIngredient
    ) {
        this.main_act = (MainActivity) context ;
        this.listener_favoriteRecipes_addRemove = listener_favoriteRecipes_addRemove;
        this.listener_favoriteRecipes_selectIngredient = listener_favoriteRecipes_selectIngredient;
        updateFavoritesList(main_act.getFavoriteRecipes());
    }


    @Override
    public Holder_FavoriteRecipes onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_FavoriteRecipes
                (v, listener_favoriteRecipes_addRemove, listener_favoriteRecipes_selectIngredient);
    }


    @Override
    public void onBindViewHolder(final Holder_FavoriteRecipes holder, final int i)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // labels
        holder.recipe_name.setText(rec.getName());
        holder.recipe_duration.setText(rec.getDuration());

        // container for the labels
        holder.recipe_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // checkboxes
        holder.checkbox_show_expandable.setChecked(holder.show_expandable);
        holder.checkbox_show_expandable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("show_expandable", rec.getName() + " | " + isChecked);

                // hides or show panel
                holder.show_expandable = isChecked ;
                holder.hideShowExpandableList();
            }
        });

        holder.checkbox_delete_from_favorites.setChecked(true);
        holder.checkbox_delete_from_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_favoriteRecipes_addRemove.checkedListener_myRecipes
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

        // expandable list
        initIngredientGrid(holder.getAdapterPosition(), holder);
    }


    private void initIngredientGrid(int index_rec, Holder_FavoriteRecipes holder)
    {
        holder.recipe_ingr_expandable.setHasFixedSize(false); // je sais pas trop ce que ca change en vrai...

        // layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main_act);
        holder.recipe_ingr_expandable.setLayoutManager(linearLayoutManager);
/*
        // add a line to divide them more clearly
        DividerItemDecoration itemDecor = new DividerItemDecoration
                (main_act, linearLayoutManager.getOrientation());
        holder.recipe_ingr_expandable.addItemDecoration(itemDecor);
*/
        // custom adapter
        Adapter_IngredientGrid adapter = new Adapter_IngredientGrid
                (main_act, index_rec, this.listener_favoriteRecipes_selectIngredient);
        holder.recipe_ingr_expandable.setAdapter(adapter);

        // default : hidden
        holder.hideShowExpandableList();

    }


    public void updateFavoritesList(ArrayList<Recipe> al) {
        this.al.clear();
        this.al.addAll(al);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }
}

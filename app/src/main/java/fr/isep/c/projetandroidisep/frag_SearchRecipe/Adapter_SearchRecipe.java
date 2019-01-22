package fr.isep.c.projetandroidisep.frag_SearchRecipe;

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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;


public class Adapter_SearchRecipe
        extends RecyclerView.Adapter<Adapter_SearchRecipe.Holder_SearchRecipe>
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
            private Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient;

            public TextView ingr_name_form, ingr_qty, ingr_unit ;
            public CheckBox chkbx_ingr_select ;


            public Holder_IngredientGrid(Context context, View view
                    , Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient
            ) {
                super(view);

                this.listener_searchRecipe_selectIngredient = listener_searchRecipe_selectIngredient;

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

        private Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient;


        public Adapter_IngredientGrid(Context context
                                      //   , Recipe rec
                , int index_rec
                , Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient
        ) {
            this.main_act = (MainActivity) context ;
            //this.rec = rec ;
            this.index_rec = index_rec ;
            this.al.addAll(this.main_act.getSearchResults().get(index_rec).getIngredients());
            this.listener_searchRecipe_selectIngredient = listener_searchRecipe_selectIngredient;
        }


        @Override
        public Holder_IngredientGrid onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_grid_chkbx, viewGroup, false);

            return new Holder_IngredientGrid(main_act, v, listener_searchRecipe_selectIngredient);
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

                    listener_searchRecipe_selectIngredient.checkedListener_selectIngredient
                            (buttonView, index_rec, holder.getAdapterPosition(), isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != al ? al.size() : 0);
        }
    }

    ///////////////////////////////////////


    public class Holder_SearchRecipe
            extends RecyclerView.ViewHolder
            implements View.OnClickListener //, Response_FetchIngredients
    {
        public LinearLayout recipe_header ;
        public RecyclerView recipe_ingr_expandable ;
        public ImageView recipe_img ;
        public TextView recipe_name, recipe_duration, recipe_rating ;
        public CheckBox checkbox_add_to_favorites, checkbox_show_expandable ;

        private Listener_SearchRecipe_AddRemove listener_searchRecipe_addRemove;

        public boolean show_expandable = false ;


        public Holder_SearchRecipe(Context context, View view
                , Listener_SearchRecipe_AddRemove listener_searchRecipe_addRemove)
        {
            super(view);

            this.listener_searchRecipe_addRemove = listener_searchRecipe_addRemove;

            recipe_header = view.findViewById(R.id.header_vertical);
            recipe_img = view.findViewById(R.id.image);
            recipe_name = view.findViewById(R.id.title);
            recipe_duration = view.findViewById(R.id.subtitle);
            //recipe_rating = view.findViewById(R.id.recipe_rating);
            checkbox_add_to_favorites = view.findViewById(R.id.checkbox_add_remove);
            checkbox_show_expandable = view.findViewById(R.id.checkbox_show_expandable);
            recipe_ingr_expandable = view.findViewById(R.id.ingr_grid_expandable);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {

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


    private MainActivity main_act;
    private ArrayList<Recipe> al = new ArrayList<>();

    private Listener_SearchRecipe_AddRemove listener_searchRecipe_addRemove;
    private Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient;


    public Adapter_SearchRecipe(Context context
            , Listener_SearchRecipe_AddRemove listener_searchRecipe_addRemove
            , Listener_SearchRecipe_SelectIngredient listener_searchRecipe_selectIngredient

    ) {
        this.main_act = (MainActivity) context ;
        this.listener_searchRecipe_addRemove = listener_searchRecipe_addRemove;
        this.listener_searchRecipe_selectIngredient = listener_searchRecipe_selectIngredient;
    }


    @Override
    public Holder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable_add_remove, viewGroup, false);

        return new Holder_SearchRecipe(main_act, v, this.listener_searchRecipe_addRemove);
    }


    @Override
    public void onBindViewHolder(final Holder_SearchRecipe holder, final int position)
    {
        final Recipe rec = al.get(holder.getAdapterPosition());

        // image
        Glide.with(main_act).load(rec.getImgUrl()).into(holder.recipe_img);

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
                holder.hideShowExpandableList(holder.show_expandable);

                Recipe rec_corresp = Recipe.getByUrl
                        (main_act.getFavoriteRecipes(), rec.getUrl());

                if (rec_corresp != null) {
                    rec.setIngredients(rec_corresp.getIngredients());
                }
/*
                if (rec.getIngredients().isEmpty())
                {
                    Recipe rec_corresponding ;

                    try
                    {
                        rec_corresponding = Recipe.getByUrl
                                (main_act.getFavoriteRecipes(), rec.getUrl());
                    }
                    catch (NullPointerException npe)
                    {
                        rec_corresponding = null ;
                    }

                    // else
                    if (rec_corresponding != null) {
                        rec.setIngredients(rec_corresponding.getIngredients());
                    } else {
                        performFetchRecipeIngredients(rec);
                    }
                }*/
            }
        });

        holder.checkbox_add_to_favorites.setChecked(rec.alreadyExists(main_act.getFavoriteRecipes()));
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_searchRecipe_addRemove.checkedListener_myRecipes
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });

        // expandable grid
        initIngredientGrid(holder.getAdapterPosition(), holder);
    }



    private void initIngredientGrid(int index_rec, Holder_SearchRecipe holder)
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
                (main_act, index_rec, this.listener_searchRecipe_selectIngredient);
        holder.recipe_ingr_expandable.setAdapter(adapter);

        // default : hidden
        holder.hideShowExpandableList(false);

    }


    public void addRecipeToResults(Recipe rec) {
        this.al.add(rec);
//        notifyDataSetChanged();
    }

    public void updateResultsList(ArrayList<Recipe> al) {
        this.al.clear();
        this.al.addAll(al);
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getResultsList() {
        return this.al ;
    }

    @Override
    public int getItemCount()
    {
        return (null != al ? al.size() : 0);
    }




}

package fr.isep.c.projetandroidisep.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Recipe;
import fr.isep.c.projetandroidisep.fragments.FragSearchRecipe;


public class Adapter_SearchRecipe extends RecyclerView.Adapter
            <Adapter_SearchRecipe.RecyclerViewHolder_SearchRecipe>
{
    static class RecyclerViewHolder_SearchRecipe extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private ImageView recipe_img ;
        private TextView recipe_name, recipe_duration ;
        private CheckBox checkbox_add_to_favorites ;

        RecyclerViewHolder_SearchRecipe(View view)
        {
            super(view);

            //recipe_img = view.findViewById(R.id.recipe_img);
            recipe_name = view.findViewById(R.id.title);
            recipe_duration = view.findViewById(R.id.sub_title);
            checkbox_add_to_favorites = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view)
        {

        }
    }

    private Context context ;
    private MainActivity main_act ;


    public Adapter_SearchRecipe(Context context) {
        this.context = context ;
        this.main_act = (MainActivity) this.context;
    }


    @Override
    public RecyclerViewHolder_SearchRecipe onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2_lines, viewGroup, false);

        return new RecyclerViewHolder_SearchRecipe(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder_SearchRecipe holder, final int i)
    {
        final Recipe rec = this.main_act.getSearchResults().get(holder.getAdapterPosition());

        //holder.recipe_img.setImageBitmap(rec.getImgBitmap());

        //holder.recipe_name.setText(ParseText.shortifyTitle(rec.getName(), MainActivity.MAX_LABEL_LENGTH));
        holder.recipe_name.setText(rec.getName());
        holder.recipe_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////// action that happens when the NAME is pressed (not the checkbox)
            }
        });

        holder.recipe_duration.setText(rec.getDuration());

        boolean already_favorite = rec.alreadyExists(main_act.getFavoriteRecipes()) ;
        //Log.d(rec.getUrl(), String.valueOf(already_favorite));

        holder.checkbox_add_to_favorites.setChecked(already_favorite);
        holder.checkbox_add_to_favorites.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("is_checked",
                        rec.getName() + " | " + String.valueOf(isChecked));

                if (isChecked) {
                    main_act.saveRecipeInFavorites(rec);

                } else {
                    main_act.removeRecipeFromFavorites(rec);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {

        return (null != main_act.getSearchResults()
                ? main_act.getSearchResults().size() : 0);
    }
}

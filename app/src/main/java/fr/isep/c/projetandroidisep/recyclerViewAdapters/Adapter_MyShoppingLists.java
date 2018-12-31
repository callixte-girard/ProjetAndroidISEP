package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;


public class Adapter_MyShoppingLists extends RecyclerView.Adapter
            <Adapter_MyShoppingLists
                .RecyclerViewHolder_MyShoppingLists>
{
    static class RecyclerViewHolder_MyShoppingLists extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView lc_name, lc_date_creation ;
        private CheckBox checkbox_delete_shopping_list ;



        RecyclerViewHolder_MyShoppingLists(View view)
        {
            super(view);

            lc_name = view.findViewById(R.id.title);
            lc_date_creation = view.findViewById(R.id.subtitle);
            checkbox_delete_shopping_list = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private View.OnClickListener listener_shopping_list_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            MainActivity act = (MainActivity) context ;
            act.displayFrag_buyShoppingList();
        }
    };

    private Context context ;
    private MainActivity main_act ;

    public Adapter_MyShoppingLists(Context context) {
        this.context = context ;
        this.main_act = (MainActivity) this.context ;
    }


    @Override
    public RecyclerViewHolder_MyShoppingLists onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new RecyclerViewHolder_MyShoppingLists(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_MyShoppingLists holder, final int i)
    {
        final ListeCourses lc = main_act.getMyShoppingLists().get(holder.getAdapterPosition());

        String displayed_title = lc.getAliments().size() + " items" ;
        holder.lc_name.setText(displayed_title);
        holder.lc_name.setOnClickListener(listener_shopping_list_clicked);

        String displayed_date = lc.getDateCreation()
                .replace("_", " at ")
                .replace("-", "/");
        holder.lc_date_creation.setText(displayed_date);
        holder.lc_date_creation.setOnClickListener(listener_shopping_list_clicked);

        holder.checkbox_delete_shopping_list.setChecked(true);
        holder.checkbox_delete_shopping_list.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ListeCourses lc = main_act.getMyShoppingLists().get(i);

                Log.d("is_checked",
                        lc.getDateCreation() + " | " + String.valueOf(isChecked));

                if (isChecked) {
                    //main_act.saveRecipeInFavorites(rec);

                } else {
                    // adds back recipe (revert deletion)
                    main_act.removeShoppingList(lc);
                }

                //notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        return (null != main_act.getMyShoppingLists()
                ? main_act.getMyShoppingLists().size() : 0);
    }


}

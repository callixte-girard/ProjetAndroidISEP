package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Aliment;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;


public class Adapter_BuyShoppingList extends RecyclerView.Adapter
            <Adapter_BuyShoppingList
                .RecyclerViewHolder_SelectRecipes>
{

    static class RecyclerViewHolder_SelectRecipes extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        private TextView ingr_name, ingr_qty ;
        private CheckBox checkbox_bought ;


        RecyclerViewHolder_SelectRecipes(View view)
        {
            super(view);

            ingr_name = view.findViewById(R.id.title);
            ingr_qty = view.findViewById(R.id.subtitle);
            checkbox_bought = view.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private Context context ;
    private MainActivity main_act ;

    private ListeCourses lc ;

    public Adapter_BuyShoppingList(Context context, ListeCourses lc) {
        this.context = context ;
        this.main_act = (MainActivity) this.context ;
        this.lc = lc ;
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
        ArrayList<Aliment> ingr_al = this.lc.getAliments();
    }

    @Override
    public int getItemCount() {
        return (null != main_act.getFavoriteRecipes()
                ? main_act.getFavoriteRecipes().size() : 0);
    }

}

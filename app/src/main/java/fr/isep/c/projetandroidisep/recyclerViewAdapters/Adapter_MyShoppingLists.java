package fr.isep.c.projetandroidisep.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.interfaces.Listener_AddRemoveShoppingList;
import fr.isep.c.projetandroidisep.myClasses.Misc;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;
import fr.isep.c.projetandroidisep.recyclerViewHolders.Holder_MyShoppingLists;


public class Adapter_MyShoppingLists
        extends RecyclerView.Adapter<Holder_MyShoppingLists>
{
    private MainActivity main_act ;
    private ArrayList<ListeCourses> al = new ArrayList<>();
    private Listener_AddRemoveShoppingList listener_addRemoveShoppingList ;


    public Adapter_MyShoppingLists(Context context, Listener_AddRemoveShoppingList listener_addRemoveShoppingList) {
        this.main_act = (MainActivity) context ;
        this.listener_addRemoveShoppingList = listener_addRemoveShoppingList ;
        updateShoppingLists(main_act.getMyShoppingLists());
    }


    @Override
    public Holder_MyShoppingLists onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_2lines_expandable, viewGroup, false);

        return new Holder_MyShoppingLists(v);
    }

    @Override
    public void onBindViewHolder(final Holder_MyShoppingLists holder, final int i)
    {
        final ListeCourses lc = main_act.getMyShoppingLists().get(holder.getAdapterPosition());

        // labels
        String displayed_title = lc.getIngredients().size() + " items" ;
        holder.lc_name.setText(displayed_title);
        String displayed_date = "Created on " + Misc.makeFormattedDateHumanReadable(lc.getDateCreation());
        holder.lc_date_creation.setText(displayed_date);

        // container for the labels
        holder.lc_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_act.displayFrag_buyShoppingList();
            }
        });

        // checkboxes
        holder.checkbox_delete_shopping_list.setChecked(true);
        holder.checkbox_delete_shopping_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_addRemoveShoppingList.checkedListener_myShoppingLists
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });
    }


    public void updateShoppingLists(ArrayList<ListeCourses> al) {
        this.al.clear();
        this.al.addAll(al);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }


}

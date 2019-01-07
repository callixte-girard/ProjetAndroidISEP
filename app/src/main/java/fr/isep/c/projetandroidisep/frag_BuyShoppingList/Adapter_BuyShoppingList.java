package fr.isep.c.projetandroidisep.frag_BuyShoppingList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import fr.isep.c.projetandroidisep.MainActivity;
import fr.isep.c.projetandroidisep.R;
import fr.isep.c.projetandroidisep.myCustomTypes.Ingredient;
import fr.isep.c.projetandroidisep.myCustomTypes.ListeCourses;


public class Adapter_BuyShoppingList
        extends RecyclerView.Adapter<Adapter_BuyShoppingList.Holder_BuyShoppingList>
{
    private MainActivity main_act ;
    private ArrayList<Ingredient> al = new ArrayList<>();

    private Listener_BuyIngredient listener_buyIngredient ;


    public Adapter_BuyShoppingList(Context context, ListeCourses lc
        ,Listener_BuyIngredient listener_buyIngredient
    ) {
        this.main_act = (MainActivity) context ;
        this.al.addAll(lc.getIngredients());
        this.listener_buyIngredient = listener_buyIngredient ;
    }


    @Override
    public Holder_BuyShoppingList onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_grid_chkbx, viewGroup, false);

        return new Holder_BuyShoppingList(main_act, v, this.listener_buyIngredient);
    }

    @Override
    public void onBindViewHolder(final Holder_BuyShoppingList holder, final int i)
    {
        final Ingredient ingr = al.get(i);

        holder.ingr_name_form.setText(ingr.returnNameAndForm());

        if (ingr.getQty() != 0) {
            holder.ingr_qty.setText(String.valueOf(ingr.getQty()));
            holder.ingr_unit.setText(ingr.getUnit());
        }

        holder.ingr_qty.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        holder.ingr_unit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        holder.checkbox_bought.setChecked(ingr.isSelected());
        holder.checkbox_bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                listener_buyIngredient.checkedListener_buyIngredient
                        (buttonView, holder.getAdapterPosition(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != al ? al.size() : 0);
    }



    public class Holder_BuyShoppingList
            extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private MainActivity main_act ;
        private Listener_BuyIngredient listener_buyIngredient ;

        public TextView ingr_name_form, ingr_qty, ingr_unit ;
        public CheckBox checkbox_bought ;


        public Holder_BuyShoppingList(Context context, View view
                , Listener_BuyIngredient listener_buyIngredient
        ) {
            super(view);

            this.main_act = (MainActivity) context ;
            this.listener_buyIngredient = listener_buyIngredient ;

            ingr_name_form = view.findViewById(R.id.ingr_name_form);
            ingr_qty = view.findViewById(R.id.ingr_qty);
            ingr_unit = view.findViewById(R.id.ingr_unit);
            checkbox_bought = view.findViewById(R.id.checkbox);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

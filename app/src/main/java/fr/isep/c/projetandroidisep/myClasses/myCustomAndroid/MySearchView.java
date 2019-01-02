package fr.isep.c.projetandroidisep.myClasses.myCustomAndroid;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.*;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

public class MySearchView extends SearchView {

    /*
     * Created by: Jens Klingenberg (jensklingenberg.de)
     * GPLv3
     *
     *   //This SearchView gets triggered even when the query submit is empty
     *
     * */

    SearchView.SearchAutoComplete mSearchSrcTextView;
    OnQueryTextListener listener;

    public MySearchView(Context context) {
        super(context);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override public void setOnQueryTextListener(final OnQueryTextListener listener) {
        super.setOnQueryTextListener(listener);
        this.listener = listener;

        mSearchSrcTextView = this.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // make it fully visible
        this.setIconifiedByDefault(false);
/*
        // get textview
        TextView tv_src = MySearchView.getSearchSrcTextView(this);

        //tv_src.setTextColor(Color.WHITE);
        tv_src.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {
                        if (listener != null)
                            listener.onQueryTextSubmit(getQuery().toString());

                        return true;
                    }
                }
        ); */
    }


}
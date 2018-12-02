package fr.isep.c.projetandroidisep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.isep.c.projetandroidisep.login.LoginActivity;
import fr.isep.c.projetandroidisep.parseRecette.*;


public class MainActivity extends Activity
{

    private BottomNavigationView bnv ;
    private FloatingActionButton fab ;
    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFabAndBnv();

        try  // get already logged in user
        {
            Intent intent_from_login_activity = getIntent();
            Bundle e = intent_from_login_activity.getExtras();
            String name = e.getString("name");

            Toast.makeText(getApplicationContext(),
                    "Welcome back " + name + " ! :)", Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException npe) // start login activity
        {
            Intent intent_to_login_activity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent_to_login_activity);
        }


        // test
       // parseRecette.execute();
    }

    @Override
    public void onBackPressed() {
        ///
    }


    public void setFabAndBnv() {
        // sets default fab action
        fab = findViewById(R.id.fab);
        setFloatingActionButton_recipes();

        // sets fab action button
        bnv = findViewById(R.id.navigation);
        bnv.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        // stores the item id
                        int id = item.getItemId();

                        if (id == R.id.nav_search_recipe) {
                            setFloatingActionButton_recipes();
                            Log.d("bnv", "search_recipe");
                            return true ;
                        }
                        else if (id == R.id.nav_my_recipes) {
                            setFloatingActionButton_shopping();
                            Log.d("bnv", "my_recipes");
                            return true ;
                        }
                        else if (id == R.id.nav_my_shopping_lists) {
                            setFloatingActionButton_user();
                            Log.d("bnv", "my_shopping_lists");
                            return true ;
                        }
                        else if (id == R.id.nav_user) {
                            setFloatingActionButton_user();
                            Log.d("bnv", "user");
                            return true ;
                        }

                        return false;

                    }
                });
    }


    private void setFloatingActionButton_recipes() {
        //fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fab", "recipes");
            }
        });
    }

    private void setFloatingActionButton_shopping() {
        //fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("fab", "shopping");
            }
        });
    }

    private void setFloatingActionButton_user() {
        //fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fab", "user");
            }
        });
    }

}

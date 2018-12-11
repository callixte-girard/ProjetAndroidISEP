package fr.isep.c.projetandroidisep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import fr.isep.c.projetandroidisep.login.LoginActivity;
import fr.isep.c.projetandroidisep.myRecipes.FragMyRecipes;
import fr.isep.c.projetandroidisep.profile.FragUser;
import fr.isep.c.projetandroidisep.searchRecipe.*;
//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;

//import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bnv ;

    private FragmentManager frag_manager = getSupportFragmentManager();

    private Fragment frag_search_recipe = new FragSearchRecipe();
    private Fragment frag_my_recipes = new FragMyRecipes();
    private Fragment frag_my_shopping_lists = new FragMyShoppingLists();
    private Fragment frag_user = new FragUser();

    private FirebaseUser current_user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Intent intent_from_firebase_auth_activity = getIntent();
        Bundle extras = intent_from_firebase_auth_activity.getExtras();
        String mail = extras.getString("user_mail");
        String name = extras.getString("user_name");
        */

        current_user = FirebaseAuth.getInstance().getCurrentUser();

        if (current_user != null) {
            setBottomNavigationDrawer();
        } else {
            transferToFirebaseAuthActivity(false);
        }

    }




    public void transferToFirebaseAuthActivity(boolean sign_out)
    {
        ///////// perform transfer to login activity
        Intent intent_to_firebase_auth_activity = new Intent(this, FirebaseUIActivity.class);

        intent_to_firebase_auth_activity.putExtra("sign_out", sign_out);

        startActivity(intent_to_firebase_auth_activity);

        // kill process for it not to come back after login completed
        finish();
    }


    public void transferToLoginActivity()
    {
        // save logged in status in sharedprefs
        SharedPreferences sp = getSharedPreferences("pipou", Context.MODE_PRIVATE);
        sp.edit().remove("logged").putBoolean("logged", false).apply();

        ///////// perform transfer to login activity
        Intent intent_to_login_activity = new Intent(this, LoginActivity.class);
        startActivity(intent_to_login_activity);

        // kill process for it not to come back after login completed
        finish();
    }


    private void setBottomNavigationDrawer() {
        // set default bnv item action
        displayFrag_searchRecipe();

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
                            displayFrag_searchRecipe();
                            Log.d("bnv", "search_recipe");
                            return true ;
                        }
                        else if (id == R.id.nav_my_recipes) {
                            displayFrag_myRecipes();
                            Log.d("bnv", "my_recipes");
                            return true ;
                        }
                        else if (id == R.id.nav_my_shopping_lists) {
                            displayFrag_myShoppingLists();
                            Log.d("bnv", "my_shopping_lists");
                            return true ;
                        }
                        else if (id == R.id.nav_user) {
                            displayFrag_user();
                            Log.d("bnv", "user");
                            return true ;
                        }

                        return false;

                    }
                });
    }

    private void displayFrag_searchRecipe()
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_search_recipe)
                .commit();
        
        
    }


    private void displayFrag_myRecipes()
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_my_recipes)
                .commit();


    }

    private void displayFrag_myShoppingLists()
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_my_shopping_lists)
                .commit();


    }

    private void displayFrag_user()
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_user)
                .commit();

    }

}

package fr.isep.c.projetandroidisep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import fr.isep.c.projetandroidisep.fragments.FragMyShoppingLists;
import fr.isep.c.projetandroidisep.fragments.FragSearchRecipe;
import fr.isep.c.projetandroidisep.fragments.FragMyRecipes;
import fr.isep.c.projetandroidisep.fragments.FragUser;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;
import fr.isep.c.projetandroidisep.customTypes.Recette;
//import fr.isep.c.projetandroidisep.myRecipes.*;
//import fr.isep.c.projetandroidisep.myShoppingLists.*;

//import com.facebook.AccessToken;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bnv ;

    private FragmentManager frag_manager = getSupportFragmentManager();

    private Fragment frag_search_recipe = new FragSearchRecipe();
    private Fragment frag_my_recipes = new FragMyRecipes();
    private Fragment frag_my_shopping_lists = new FragMyShoppingLists();
    private Fragment frag_user = new FragUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        if (current_user != null) {
            setBottomNavigationDrawer();
        } else {
            transferToFirebaseAuthActivity();
        }

    }


    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.d("sign_out", "complete");

                        transferToFirebaseAuthActivity();
                    }
                });
        // [END auth_fui_signout]
    }



    public void transferToFirebaseAuthActivity()
    {
        ///////// perform transfer to login activity
        Intent intent_to_firebase_auth_activity = new Intent
                (this, FirebaseUIActivity.class);

        startActivity(intent_to_firebase_auth_activity);

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


    public static void saveRecipeInFavorites(Recette rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                .setValue(rec);
    }

    public static void removeRecipeFromFavorites(Recette rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                .removeValue();
    }

}

package fr.isep.c.projetandroidisep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import fr.isep.c.projetandroidisep.fragments.FragCreateShoppingList;
import fr.isep.c.projetandroidisep.fragments.FragMyShoppingLists;
import fr.isep.c.projetandroidisep.fragments.FragSearchRecipe;
import fr.isep.c.projetandroidisep.fragments.FragMyRecipes;
import fr.isep.c.projetandroidisep.fragments.FragUser;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;
import fr.isep.c.projetandroidisep.myClasses.ParseText;
import fr.isep.c.projetandroidisep.myCustomTypes.*;

//import com.facebook.AccessToken;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bnv ;

    private static FragSearchRecipe frag_search_recipe = new FragSearchRecipe();
    private static FragMyRecipes frag_my_recipes = new FragMyRecipes();
    private static FragMyShoppingLists frag_my_shopping_lists = new FragMyShoppingLists();
    private static FragCreateShoppingList frag_create_shopping_list = new FragCreateShoppingList();
    private static FragUser frag_user = new FragUser();


    private FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference favorite_recipes_ref = FirebaseDatabase.getInstance().getReference()
            .child(current_user.getUid()).child("favorite_recipes");

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try
            {
                // 1) GET SAVED DATA
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                getFavoriteRecipes().clear();

                while (it.hasNext())
                {
                    Recipe rec = it.next().getValue(Recipe.class);
                    Log.d("favorite_recipes", rec.getUrl());

                    getFavoriteRecipes().add(rec);
                }

                // update fragment's number of favorites
                frag_my_recipes.updateFavoritesList();

            }
            catch (NullPointerException npe) {
                //////
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("onCancelled", databaseError.getMessage());
        }
    };

    private static ArrayList<Recipe> favorite_recipes = new ArrayList<>();
    //private static ArrayList<Recipe> deleted_recipes_history = new ArrayList<>();

    /////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialises the arraylist with favorite recipes
        favorite_recipes_ref.addValueEventListener(listener);

        if (current_user != null) {
            setBottomNavigationDrawer();
        } else {
            transferToFirebaseAuthActivity();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        favorite_recipes_ref.removeEventListener(listener);
    }


    @Override
    public void onBackPressed() {
        // ca marche po du tout :(
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
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
        displayFrag_searchRecipe(getSupportFragmentManager());

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
                            displayFrag_searchRecipe(getSupportFragmentManager());
                            Log.d("bnv", "search_recipe");
                            return true ;
                        }
                        else if (id == R.id.nav_my_recipes) {
                            displayFrag_myRecipes(getSupportFragmentManager());
                            Log.d("bnv", "my_recipes");
                            return true ;
                        }
                        else if (id == R.id.nav_my_shopping_lists) {
                            displayFrag_myShoppingLists(getSupportFragmentManager());
                            Log.d("bnv", "my_shopping_lists");
                            return true ;
                        }
                        else if (id == R.id.nav_user) {
                            displayFrag_user(getSupportFragmentManager());
                            Log.d("bnv", "user");
                            return true ;
                        }

                        return false;

                    }
                });
    }

    public static void displayFrag_searchRecipe(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_search_recipe)
                .commit();
    }


    public static void displayFrag_myRecipes(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_my_recipes)
                .commit();
    }

    public static void displayFrag_myShoppingLists(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_my_shopping_lists)
                .commit();
    }

    public static void displayFrag_createShoppingList(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_create_shopping_list)
                .commit();
    }

    public static void hideFrag_createShoppingList(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .remove(frag_create_shopping_list)
                .commitAllowingStateLoss();
    }

    public static void displayFrag_user(FragmentManager frag_manager)
    {
        frag_manager
                .beginTransaction()
                .replace(R.id.frame_container, frag_user)
                .commit();
    }



    ////////////////////////////////////////////////////////:


    public static void saveRecipeInFavorites(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                .setValue(rec);
    }

    public static void saveListeCourses(ListeCourses lc)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("my_shopping_lists")
                .child(lc.getDateCreation())
                .setValue(lc);
    }

    public static void removeRecipeFromFavorites(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                .removeValue();
    }

    public static ArrayList<Recipe> getFavoriteRecipes() {
        return favorite_recipes ;
    }


}

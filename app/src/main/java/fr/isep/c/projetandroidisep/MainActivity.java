package fr.isep.c.projetandroidisep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import fr.isep.c.projetandroidisep.frag_BuyShoppingList.Frag_BuyShoppingList;
import fr.isep.c.projetandroidisep.frag_CreateShoppingList.Frag_CreateShoppingList;
import fr.isep.c.projetandroidisep.frag_MyShoppingLists.Frag_MyShoppingLists;
import fr.isep.c.projetandroidisep.frag_RecipeDetails.Frag_RecipeDetails;
import fr.isep.c.projetandroidisep.frag_SearchRecipe.Frag_SearchRecipe;
import fr.isep.c.projetandroidisep.frag_FavoriteRecipes.Frag_FavoriteRecipes;
import fr.isep.c.projetandroidisep.frag_User.Frag_User;
import fr.isep.c.projetandroidisep.myClasses.utils.ParseHtml;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity
    //implements Response_FetchIngredients
{
    // display
    private BottomNavigationView bnv ;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private Frag_SearchRecipe frag_search_recipe ;
    private Frag_FavoriteRecipes frag_favorite_recipes ;
    private Frag_RecipeDetails frag_recipe_details ;
    private Frag_MyShoppingLists frag_my_shopping_lists ;
    private Frag_User frag_user  ;
    private Frag_CreateShoppingList frag_create_shopping_list ;
    private Frag_BuyShoppingList frag_buy_shopping_list ;

    // app lists
    private ArrayList<Recipe> search_results = new ArrayList<>();
    private ArrayList<Recipe> favorite_recipes = new ArrayList<>();
    private ArrayList<ListeCourses> my_shopping_lists = new ArrayList<>();
    //private static ArrayList<Recipe> deleted_recipes_history = new ArrayList<>();

    // firebase
    private FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref_current_user = FirebaseDatabase.getInstance().getReference().child(current_user.getUid());
    private DatabaseReference ref_favorite_recipes = ref_current_user.child("favorite_recipes");
    private DatabaseReference ref_my_shopping_lists = ref_current_user.child("my_shopping_lists");

    private ValueEventListener listener_favorite_recipes = new ValueEventListener() {
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
                    //Log.d("favorite_recipes_" + getFavoriteRecipes().size(), rec.getName());

                    getFavoriteRecipes().add(rec);
                }

                // update fragment's number of favorites
                frag_favorite_recipes.updateFavoritesList(getFavoriteRecipes());

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
    private ValueEventListener listener_my_shopping_lists = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try
            {
                // 1) GET SAVED DATA
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                getMyShoppingLists().clear();

                while (it.hasNext())
                {
                    ListeCourses lc = it.next().getValue(ListeCourses.class);
                    Log.d("my_shopping_lists", lc.getDateCreation());

                    getMyShoppingLists().add(lc);
                }

                // update fragment's number of favorites
                frag_my_shopping_lists.updateShoppingLists(getMyShoppingLists());

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

    // other shits
    public static final int MAX_LABEL_LENGTH = 31 ;

    public static final String REMOVED_SUCCESSFULLY = " has been removed successfully. ";
    public static final String FETCHING_INGREDIENTS = "Fething ingredients, please wait...";


    /////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialises the arraylist with favorite recipes
        ref_favorite_recipes.addValueEventListener(listener_favorite_recipes);
        ref_my_shopping_lists.addValueEventListener(listener_my_shopping_lists);

        if (current_user != null)
        {
            // initiates all fragments
            frag_search_recipe = new Frag_SearchRecipe();
            frag_favorite_recipes = new Frag_FavoriteRecipes();
            frag_my_shopping_lists = new Frag_MyShoppingLists();
            frag_user = new Frag_User();

            setBottomNavigationDrawer();

        } else {
            transferToFirebaseAuthActivity();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        ref_favorite_recipes.removeEventListener(listener_favorite_recipes);
        ref_my_shopping_lists.removeEventListener(listener_my_shopping_lists);

        destroyFrag_searchRecipe();
    }


    @Override
    public void onBackPressed() {
        /*
        // ca marche po du tout :(
        int count = fragmentManager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            fragmentManager.popBackStack();
        } */
        fragmentManager.popBackStackImmediate();

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

        // sets fragments to bnv tab
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

    public void displayFrag_searchRecipe()
    {
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_search_recipe)
                .commit();
    }


    public void displayFrag_myRecipes()
    {
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_favorite_recipes)
                .commit();
    }

    public void displayFrag_myShoppingLists()
    {
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_my_shopping_lists)
                .commit();
    }

    public void displayFrag_user()
    {
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_user)
                .commit();
    }

    public void displayFrag_createShoppingList()
    {
        // creates new one first
        frag_create_shopping_list = new Frag_CreateShoppingList();

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_create_shopping_list)
                .commit();
    }

    public void displayFrag_buyShoppingList(ListeCourses lc_buy)
    {
        // creates new and adds lc_buy to it
        frag_buy_shopping_list = new Frag_BuyShoppingList();
        frag_buy_shopping_list.setShoppingListToBuy(lc_buy);

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_buy_shopping_list)
                .commit();
    }

    public void displayFrag_recipeDetails(Recipe rec)
    {
        // creates info to transfer
        frag_recipe_details = new Frag_RecipeDetails();
        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", rec.getName());
        bundle.putString("recipe_img_url", rec.getImgUrl());
        bundle.putStringArrayList("recipe_instructions", rec.getInstructions());
        frag_recipe_details.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_recipe_details)
                .commit();
    }

    public void destroyFrag_createShoppingList()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_create_shopping_list)
                .commitAllowingStateLoss();

        // destroys it now
        frag_create_shopping_list = null ;
    }

    public void destroyFrag_buyShoppingList()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_buy_shopping_list)
                .commitAllowingStateLoss();

        // destroys it now
        frag_buy_shopping_list = null ;
    }

    public void destroyFrag_searchRecipe()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_search_recipe)
                .commitAllowingStateLoss();
    }

    public void destroyFrag_recipeDetails()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_recipe_details)
                .commitAllowingStateLoss();

        // destroys it now
        frag_recipe_details = null ;
    }

    ////////////////////////////////////////////////////////:



    public void saveRecipeInFavorites(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        DatabaseReference favorite_recipes_ref = current_user_ref
                .child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()));

        favorite_recipes_ref.orderByChild("dateAjout");
        favorite_recipes_ref.setValue(rec);
    }

    public void saveShoppingList(ListeCourses lc)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("my_shopping_lists")
                .child(lc.getDateCreation())
                .setValue(lc);
    }

    public void removeRecipeFromFavorites(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                //.child(rec.getDateAjout())
                .removeValue();
    }

    public void removeShoppingList(ListeCourses lc)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("my_shopping_lists")
                .child(lc.getDateCreation())
                .removeValue();
    }

    public ArrayList<Recipe> getSearchResults() {
        return this.search_results ;
    }
    public ArrayList<Recipe> getFavoriteRecipes() {
        return this.favorite_recipes ;
    }
    public ArrayList<ListeCourses> getMyShoppingLists() {
        return this.my_shopping_lists ;
    }
}

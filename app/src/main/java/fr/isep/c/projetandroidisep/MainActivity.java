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

import fr.isep.c.projetandroidisep.asyncTasks.Response_FetchIngredients;
import fr.isep.c.projetandroidisep.asyncTasks.Task_FetchIngredients;
import fr.isep.c.projetandroidisep.fragments.FragBuyShoppingList;
import fr.isep.c.projetandroidisep.fragments.FragCreateShoppingList;
import fr.isep.c.projetandroidisep.fragments.FragMyShoppingLists;
import fr.isep.c.projetandroidisep.fragments.FragSearchRecipe;
import fr.isep.c.projetandroidisep.fragments.FragFavoriteRecipes;
import fr.isep.c.projetandroidisep.fragments.FragUser;
import fr.isep.c.projetandroidisep.myClasses.ParseHtml;
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

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity
    implements Response_FetchIngredients
{
    // display
    private BottomNavigationView bnv ;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private static FragSearchRecipe frag_search_recipe = new FragSearchRecipe();
    private static FragFavoriteRecipes frag_favorite_recipes = new FragFavoriteRecipes();
    private static FragMyShoppingLists frag_my_shopping_lists = new FragMyShoppingLists();
    private static FragUser frag_user = new FragUser();

    private static FragCreateShoppingList frag_create_shopping_list = new FragCreateShoppingList();
    private static FragBuyShoppingList frag_buy_shopping_list = new FragBuyShoppingList();

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
                    Log.d("favorite_recipes_" + getFavoriteRecipes().size(), rec.getName());

                    getFavoriteRecipes().add(rec);

                    // TEST
                    if (rec.getIngredients().isEmpty()) {
                        // parse them :
                        // - call performFetchRecipeIngredients from FragSearchRecipe
                        performFetchRecipeIngredients(rec);
                        // - transform the other perform(...) to there
                        // - transfer async_tasks_list here too !!!
                    }
                }

                // update fragment's number of favorites
                frag_favorite_recipes.updateFavoritesList();

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
                frag_my_shopping_lists.updateShoppingLists();

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

    // app lists
    private static ArrayList<Recipe> favorite_recipes = new ArrayList<>();
    //private static ArrayList<Recipe> deleted_recipes_history = new ArrayList<>();
    private static ArrayList<ListeCourses> my_shopping_lists = new ArrayList<>();

    // other shits
    public static final int MAX_LABEL_LENGTH = 31 ;

    /////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialises the arraylist with favorite recipes
        ref_favorite_recipes.addValueEventListener(listener_favorite_recipes);
        ref_my_shopping_lists.addValueEventListener(listener_my_shopping_lists);

        if (current_user != null) {
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


    @Override
    public void processFinish_fetchIngredients(Document doc, String url)
    {
        try
        {
            ArrayList<Ingredient> ingr_list = Ingredient.fetchAllFromDoc(doc);

            // --> finally adds to appropriate recipe
            Recipe rec_to_update = Recipe.getByUrl(getFavoriteRecipes(), url);
            rec_to_update.setIngredients(ingr_list);

            // saves
            saveIngredientsInRecipe(rec_to_update);

            Log.d("task_results_fetchIngr", url);

        } catch (Exception e) {}
    }


    //protected void performFetchRecipeIngredients(ArrayList<Recipe> al)
    protected void performFetchRecipeIngredients(Recipe rec)
    {
        Task_FetchIngredients task_fetchIngredients = new Task_FetchIngredients();
        task_fetchIngredients.setDelegate(this);
        task_fetchIngredients.setUrl(rec.getUrl());
        task_fetchIngredients.execute(task_fetchIngredients.getUrl());
    }


    public static Drawable loadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_create_shopping_list)
                .commit();
    }

    public void displayFrag_buyShoppingList()
    {
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, frag_buy_shopping_list)
                .commit();
    }

    public void destroyFrag_createShoppingList()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_create_shopping_list)
                .commitAllowingStateLoss();
    }

    public void destroyFrag_buyShoppingList()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_buy_shopping_list)
                .commitAllowingStateLoss();
    }

    public void destroyFrag_searchRecipe()
    {
        fragmentManager
                .beginTransaction()
                .remove(frag_search_recipe)
                .commitAllowingStateLoss();
    }

    ////////////////////////////////////////////////////////:



    public static void saveRecipeInFavorites(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                //.child(rec.getDateAjout())
                .setValue(rec);
    }

    public static void saveIngredientsInRecipe(Recipe rec)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("favorite_recipes")
                .child(ParseHtml.shortifyUrl(rec.getUrl()))
                .child("ingredients")
                .setValue(rec.getIngredients());
    }

    public static void saveShoppingList(ListeCourses lc)
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
                //.child(rec.getDateAjout())
                .removeValue();
    }

    public static void removeShoppingList(ListeCourses lc)
    {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference()
                .child(current_user.getUid());

        current_user_ref.child("my_shopping_lists")
                .child(lc.getDateCreation())
                .removeValue();
    }



    public static ArrayList<Recipe> getFavoriteRecipes() {
        return favorite_recipes ;
    }
    public static ArrayList<ListeCourses> getMyShoppingLists() {
        return my_shopping_lists ;
    }
}

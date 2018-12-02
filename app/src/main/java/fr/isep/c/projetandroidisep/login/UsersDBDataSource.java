package fr.isep.c.projetandroidisep.login;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UsersDBDataSource
{
    // Champs de la base de données
    private SQLiteDatabase database;
    private UsersDBHelper dbHelper;
    private String[] allColumns = {
            dbHelper.COLUMN_NAME,
            dbHelper.COLUMN_EMAIL,
            dbHelper.COLUMN_PASSWORD
    };

    public UsersDBDataSource(Context context) {
        dbHelper = new UsersDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    private User queryInsertNewUser(String name, String email, String password)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[0], name);
        values.put(allColumns[1], email);
        values.put(allColumns[2], password);

        database.insert(UsersDBHelper.TABLE_USERS, null, values);

        Cursor cursor = database.query(UsersDBHelper.TABLE_USERS, allColumns,
                // selection by email :
                null, null,
                null, null, null);
        cursor.moveToFirst();
        User new_user = cursorToUser(cursor);
        cursor.close();

        Log.d("user_create_success", new_user.getName() + " | " + new_user.getEmail());
        return new_user;

    }


    private boolean queryDeleteUser(User user)
    {
        String email = user.getEmail();

        int nb_rows_deleted = database.delete(UsersDBHelper.TABLE_USERS, UsersDBHelper.COLUMN_EMAIL
                + " = " + "'" + email + "'", null);

        if (nb_rows_deleted == 0) {
            Log.d("user_delete_fail", "User does not exists");
            return false ;
        }
        else if (nb_rows_deleted == 1) {
            Log.d("user_delete_success", email);
            return true ;
        }
        else {
            Log.d("user_delete_success", "!!! " + String.valueOf(nb_rows_deleted) + " entries deleted.");
            return true ;
        }
    }


    private User queryMatchingUserByEmail(String email)
    {
        List<User> users = new ArrayList<>();
        User matching_user ;

        Cursor cursor = database.query(UsersDBHelper.TABLE_USERS, allColumns,
                UsersDBHelper.COLUMN_EMAIL + " = " + "'" + email + "'"
                //+ " AND " + UsersDBHelper.COLUMN_PASSWORD + " = " + "'" + email + "'"
                , null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        try {
            matching_user = users.get(0);
        } catch (IndexOutOfBoundsException ex) {
            matching_user = null ;
        }
        return matching_user ;
    }



    public User attemptUserAuth(String email, String password)
    {
        User matching_user = queryMatchingUserByEmail(email);

        if (matching_user != null && matching_user.getPassword().equals(password)) {
            return matching_user ;
        }
        else {
            return null ;
        }
    }


    public boolean attemptUserPasswordRecovery(String email)
    {
        User matching_user = queryMatchingUserByEmail(email);

        if (matching_user != null) {
            // handle action : send mail / SMS / Anything else
        }
        else {
            // do nothing but show success
        }

        return true ;
    }


    public User attemptUserRegistration(String name, String email, String password)
    {
        // does user already exist ?
        User matching_user = queryMatchingUserByEmail(email);
        
        if (matching_user != null) {
            return null ;
        }
        else {
            User validated_user = queryInsertNewUser(name, email, password);
            return validated_user ;
        }
    }


    public boolean attemptUserDeletion(User user_to_delete)
    {
        // does user already exist ?
        User matching_user = queryMatchingUserByEmail(user_to_delete.getEmail());

        if (matching_user != null) {
            return queryDeleteUser(user_to_delete);
        }
        else {
            return false ;
        }

    }

    /*
    private List<User> getAllUserNames()
    {
        List<User> users = new ArrayList<>();

        Cursor cursor = database.query(UsersDBHelper.TABLE_USERS,
                allColumns, null, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return users;
    } */

    private void clearUserTable()
    {
        database.delete(UsersDBHelper.TABLE_USERS, null, null);
        Log.d("clear_user_table", "success");
    }

    private User cursorToUser(Cursor cursor)
    {
        User user = new User(
                cursor.getString(cursor.getColumnIndex(allColumns[0])),
                cursor.getString(cursor.getColumnIndex(allColumns[1])),
                cursor.getString(cursor.getColumnIndex(allColumns[2]))
        );

        return user;
    }
}
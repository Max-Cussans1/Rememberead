package com.example.mobile_assignment;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

//shared preferences helper class to manage saving data in local storage
public class SharedPHelp
{
    private SharedPHelp()
    {
    }

    public static final String preferences = "BooksPreferences";
    public static final String position = "position";
    public static final String query = "query";

    public static SharedPreferences getSharedPrefences(Context ctx)
    {
        return ctx.getSharedPreferences(preferences, ctx.MODE_PRIVATE);
    }


    //create values for getting and setting strings and ints
    public static String getPrefString(Context ctx, String key)
    {
        return getSharedPrefences(ctx).getString(key, "");
    }

    public static int getPrefInt(Context ctx, String key)
    {
        return getSharedPrefences(ctx).getInt(key, 0);
    }

    public static void setPrefString(Context ctx, String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPrefences(ctx).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setPrefInt(Context ctx, String key, int value)
    {
        SharedPreferences.Editor editor = getSharedPrefences(ctx).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //function to get the last few searches
    public static ArrayList<String> getSavedSearches(Context ctx)
    {
        int numberOfSearchesToSave = 5;

        ArrayList<String> SavedSearches = new ArrayList<String>();

        for(int i = 1; i <=numberOfSearchesToSave; i++)
        {
            String search = getSharedPrefences(ctx).getString(query + String.valueOf(i), "");
            if(!search.isEmpty())
            {
                search = search.replace(","," ");
                SavedSearches.add(search.trim());
            }
        }
        return SavedSearches;
    }
}

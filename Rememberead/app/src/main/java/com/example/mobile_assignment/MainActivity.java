package com.example.mobile_assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    private RecyclerView rv;
    private ProgressBar searchProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchProgress = (ProgressBar)findViewById(R.id.search_loading);
        rv = (RecyclerView)findViewById(R.id.recyclerview);

        //use layoutmanager to set the recyclerview vertically
        LinearLayoutManager booksLayout = new LinearLayoutManager(this);
        booksLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(booksLayout);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL book_URL;

        try
        {
            if(query == null)
            {
                //search for programming by default
                book_URL = ApiHelp.create_URL("Programming");
            }
            else
            {
                book_URL = new URL(query);
            }
            new BookSearch().execute(book_URL);
        }
        catch (Exception e)
        {
            Log.d("Error", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //create our search option
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        //get our saved searches through shared preferences
        ArrayList<String> savedSearches = SharedPHelp.getSavedSearches(getApplicationContext());
        int numberOfSearches = savedSearches.size();
        //add the search dropdown menu items dynamically based on the last few searches
        MenuItem menuItem;
        for(int i =0; i<numberOfSearches; i++)
        {
            menuItem = menu.add(Menu.NONE, i, Menu.NONE, savedSearches.get(i));
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        try
        {
            URL bookURL = ApiHelp.create_URL(s);
            new BookSearch().execute(bookURL);
        }
        catch(Exception e)
        {
            Log.d("Error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    //AsyncTask to avoid main thread taking a long time
    public class BookSearch extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground(URL... urls)
        {
            URL searchURL = urls[0];
            String searchResult = null;

            try
            {
                searchResult = ApiHelp.getData(searchURL);
            }
            catch(IOException e)
            {
                //Write to error logs
                Log.e("Error", e.getMessage());
            }
            return searchResult;
        }

        //onPostExecute is called after doInBackground has completed
        @Override
        protected void onPostExecute(String searchResult)
        {
            TextView Error = (TextView)findViewById(R.id.errorMessage);

            //stop showing loading
            searchProgress.setVisibility(View.INVISIBLE);

            //check if we have no result and show error message if we do (i.e airplane mode)
            if(searchResult == null)
            {
                rv.setVisibility(View.INVISIBLE);
                Error.setVisibility(View.VISIBLE);
            }
            else
            {
                rv.setVisibility(View.VISIBLE);
                Error.setVisibility(View.INVISIBLE);
                ArrayList<BookData> books = ApiHelp.parseData(searchResult);

                Adapter adapter = new Adapter(books);
                rv.setAdapter(adapter);
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //show loading
            searchProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        //switch statement to launch the advanced search activity with an intent
        switch(item.getItemId())
        {
            case R.id.advanced_search:
                Intent advancedSearchIntent = new Intent (this, AdvancedSearch.class);
                startActivity(advancedSearchIntent);
                return true;

            default:
                //get position of the item in the saved searches
                int position = item.getItemId()+1;
                //get strings from the sharedphelp class since we already removed the commas from the menu items
                String pref = SharedPHelp.query + String.valueOf(position);
                String search = SharedPHelp.getPrefString(getApplicationContext(),pref);

                //create URL from the stored data in shared preferences
                String[] prefParams = search.split("\\,");
                String[] searchParams = new String[4];

                for(int i =0; i<prefParams.length;i++)
                {
                    searchParams[i] = prefParams[i];
                }
                //check to see if some of the advanced search fields were null when creating URL
                URL url = ApiHelp.create_URL((searchParams[0]==null)?"":searchParams[0],(searchParams[1]==null)?"":searchParams[1],(searchParams[2]==null)?"":searchParams[2],(searchParams[3]==null)?"":searchParams[3]);
                new BookSearch().execute(url);

                return super.onOptionsItemSelected(item);
        }
    }
}

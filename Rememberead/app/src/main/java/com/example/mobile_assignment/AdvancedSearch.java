package com.example.mobile_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class AdvancedSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        final EditText searchTitle = (EditText)findViewById(R.id.searchTitle);
        final EditText searchAuthor = (EditText)findViewById(R.id.searchAuthor);
        final EditText searchPublisher = (EditText)findViewById(R.id.searchPublisher);
        final EditText searchNumber = (EditText)findViewById(R.id.searchNumber);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //trim to get rid of white space in searches
                String title = searchTitle.getText().toString().trim();
                String author = searchAuthor.getText().toString().trim();
                String publisher = searchPublisher.getText().toString().trim();
                String number = searchNumber.getText().toString().trim();

                //check to see if no data has been input and show message if it hasn't
                if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && number.isEmpty())
                {
                    String noData = getString(R.string.no_search_data);
                    Toast.makeText(getApplicationContext(), noData, Toast.LENGTH_LONG).show();
                }
                else
                {
                    //build URL with search data
                    URL url = ApiHelp.create_URL(title, author, publisher, number);

                    //shared preferences we want to save the last 5 searches
                    Context ctx = getApplicationContext();
                    int position = SharedPHelp.getPrefInt(ctx, SharedPHelp.position);
                    if(position == 0 || position == 5)
                    {
                        position = 1;
                    }
                    else
                    {
                        position++;
                    }
                    //get the key and value in order to set the shared prefs
                    String key = SharedPHelp.query + String.valueOf(position);
                    String value = title + "," + author + "," + publisher + "," + number;
                    SharedPHelp.setPrefString(ctx,key,value);
                    SharedPHelp.setPrefInt(ctx,SharedPHelp.position,position);

                    Intent listIntent = new Intent(getApplicationContext(), MainActivity.class);
                    listIntent.putExtra("Query", url.toString());
                    startActivity(listIntent);
                }
            }
        });
    }
}

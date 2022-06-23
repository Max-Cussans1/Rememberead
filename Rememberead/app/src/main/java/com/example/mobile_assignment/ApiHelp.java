package com.example.mobile_assignment;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiHelp
{
    private ApiHelp()
    {
    }

    public static final String api_URL = "https://www.googleapis.com/books/v1/volumes";

    //query key is q in the URL
    public static final String query_key= "q";
    public static final String key = "key";
    public static final String ApiKey = "AIzaSyAa0teTRvpjbqkS7frI6LnROnU7O7bqw2s";

    //strings required for advanced search by the API to create the URL
    public static final String urlTitle = "intitle:";
    public static final String urlAuthor = "inauthor:";
    public static final String urlPublisher = "inpublisher:";
    public static final String urlNumber = "isbn:";

    //function to retrieve URL from
    public static URL create_URL(String title)
    {
        URL url = null;

        //build url from query params and the base URL
        Uri uri = Uri.parse(api_URL).buildUpon().appendQueryParameter(query_key, title).appendQueryParameter(key, ApiKey).build();
        try
        {
            url = new URL(uri.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return url;
    }

    //used for the advanced search fields
    public static URL create_URL(String title, String authors, String publisher, String number)
    {
        URL url = null;
        StringBuilder sb = new StringBuilder();
        if(!title.isEmpty())
        {
            sb.append(urlTitle + title + "+");
        }
        if(!authors.isEmpty())
        {
            sb.append(urlAuthor + authors + "+");
        }
        if(!publisher.isEmpty())
        {
            sb.append(urlPublisher + publisher + "+");
        }
        if(!number.isEmpty())
        {
            sb.append(urlNumber + number + "+");
        }
        //remove the final "+"
        sb.setLength(sb.length()-1);

        String queryURL = sb.toString();
        //build the URL from the keys and the search query data
        Uri uri = Uri.parse(api_URL).buildUpon().appendQueryParameter(query_key, queryURL).appendQueryParameter(key, ApiKey).build();

        try
        {
            url = new URL(uri.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return url;
    }

    //specify exception because openConnection can throw IO exception
    public static String getData(URL url) throws IOException
    {
        //open a connection
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        try
        {
            InputStream input = http.getInputStream();
            Scanner scanner = new Scanner(input);
            // \\A to read everything
            scanner.useDelimiter("\\A");
            boolean data = scanner.hasNext();

            if (data == true)
            {
                return scanner.next();
            } else
                {
                return null;
            }
        }
        catch(Exception e)
        {
            //write to debug logs
            Log.d("Error", e.toString());
            return null;
        }
        //make sure we disconnect
        finally
        {
            http.disconnect();
        }
    }

    //function to parse the JSON data
    public static ArrayList<BookData> parseData(String data)
    {
        final String id = "id";
        final String title = "title";
        final String subtitle = "subtitle";
        final String authors = "authors";
        final String publisher = "publisher";
        final String date = "publishedDate";
        final String items = "items";
        final String info = "volumeInfo";
        final String description = "description";

        ArrayList<BookData> books = new ArrayList<BookData>();

        try
        {
            //create a JSONObject from the data
            JSONObject jsonData = new JSONObject(data);
            //create an array using the array name which is items in the JSON data
            JSONArray dataArray = jsonData.getJSONArray(items);
            int numberOfBooks = dataArray.length();

            for(int i = 0; i < numberOfBooks; i++)
            {
                //get the data
                JSONObject bookDataJSON = dataArray.getJSONObject(i);
                //get the volume info from the data
                JSONObject infoJSON = bookDataJSON.getJSONObject(info);

                int numberOfAuthors = infoJSON.getJSONArray(authors).length();
                String[] authorNames = new String[numberOfAuthors];
                for(int j =0; j <numberOfAuthors; j++)
                {
                    authorNames[j] = infoJSON.getJSONArray(authors).get(j).toString();
                }
                //create a new bookData object for the book with all the data - check to see if it has any null strings that would cause the loop to break early from the IO exception
                BookData book = new BookData(bookDataJSON.isNull(id)?"":bookDataJSON.getString(id), infoJSON.getString(title),infoJSON.isNull(subtitle)?"":infoJSON.getString(subtitle),authorNames, infoJSON.isNull(publisher)?"":infoJSON.getString(publisher),infoJSON.isNull(date)?"":infoJSON.getString(date), infoJSON.getString(description));
                //add the book to the arraylist
                books.add(book);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return books;
    }
}


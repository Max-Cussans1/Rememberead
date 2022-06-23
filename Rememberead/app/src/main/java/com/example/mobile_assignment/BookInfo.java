package com.example.mobile_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BookInfo extends AppCompatActivity
{
    private Button take_photo;
    private ImageView photo;
    private static final int CameraRequestCode = 144;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        BookData book = getIntent().getParcelableExtra("Book");

        TextView title = (TextView)findViewById(R.id.Title);
        TextView subtitle = (TextView)findViewById(R.id.Subtitle);
        TextView authors = (TextView)findViewById(R.id.Authors);
        TextView publisher = (TextView)findViewById(R.id.Publisher);
        TextView date = (TextView)findViewById(R.id.Date);
        TextView description = (TextView)findViewById(R.id.Description);

        take_photo = (Button)findViewById(R.id.takePhoto);
        photo = (ImageView) findViewById(R.id.photo);

        String authorsString = "";
        int i = 0;

        title.setText(book.title);
        subtitle.setText(book.subtitle);

        //need to create a string for authors since they're an array at the moment
        for(String s:book.authors)
        {
            authorsString += s;
            i++;

            if(i<book.authors.length)
            {
                //add a comma between authors if there are more
                authorsString +=", ";
            }
        }

        authors.setText(authorsString);
        publisher.setText(book.publisher);
        date.setText(book.date);
        description.setText(book.description);

        take_photo.setOnClickListener(new accessCamera());

    }


    //Display the photo taken by the camera in the imageview
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
       if(requestCode == CameraRequestCode)
       {
           if (resultCode == Activity.RESULT_OK) {
               super.onActivityResult(requestCode, resultCode, data);
               Bitmap bitmap = (Bitmap) data.getExtras().get("data");
               photo.setImageBitmap((bitmap));
           }
       }
   }

   //Press the button to access the camera
   class accessCamera implements Button.OnClickListener
   {
       @Override
       public void onClick(View view)
       {
           Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(cameraIntent,CameraRequestCode);
       }
   }
}

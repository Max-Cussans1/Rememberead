package com.example.mobile_assignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;


//adapter class for our recyclerview
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{
    ArrayList<BookData> books;
    public Adapter(ArrayList<BookData> books)
    {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context ctx = parent.getContext();
        //inflate the layout for the books that will be shown
        View item = LayoutInflater.from(ctx).inflate(R.layout.book_list, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        BookData book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount()
    {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView book_title;
        TextView book_authors;
        TextView publisher;
        TextView date;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            book_title = (TextView) itemView.findViewById(R.id.book_title);
            book_authors = (TextView) itemView.findViewById(R.id.book_authors);
            publisher = (TextView) itemView.findViewById(R.id.publisher);
            date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        //use this to get the data from the book and apply the data to the textviews
        public void bind (BookData book)
        {
            book_title.setText(book.title);
            String authors ="";
            int i = 0;
            for(String s:book.authors)
            {
                authors += s;
                i++;

                if(i<book.authors.length)
                {
                    //add a comma between authors if there are more
                    authors += ", ";
                }
            }
            book_authors.setText(authors);
            date.setText(book.date);
            publisher.setText(book.publisher);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            BookData book = books.get(adapterPosition);
            Intent bookInfo = new Intent(view.getContext(), BookInfo.class);
            //using putextra for simple string data to pass between activities
            bookInfo.putExtra("Book", book);
            view.getContext().startActivity(bookInfo);
        }
    }
}

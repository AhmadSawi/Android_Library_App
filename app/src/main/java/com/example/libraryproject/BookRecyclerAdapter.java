package com.example.libraryproject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private List<Book> listItems;
    private Context context;

    public BookRecyclerAdapter(List<Book> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = listItems.get(position);

        holder.text_title.setText(book.getTitle());
        holder.text_pages.setText(book.getPages()+" Pages");
        holder.text_author.setText("Author: " + book.getAuthor());
        holder.text_publisher.setText("Publisher: " + book.getPublisher());

        final Button btn = holder.reserve_button;
        final String book_isbn = book.getIsbn();

        SharedPrefManager sharedPrefManager;
        sharedPrefManager = SharedPrefManager.getInstance(context);
        final String loggedInUser = sharedPrefManager.readString("LoggedInUsername", "Android");

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "Library", null, 1);
        Cursor reservationByUser = dataBaseHelper.getBookReservedByUser(book_isbn, loggedInUser);
        if(reservationByUser.getCount() != 0){
            btn.setClickable(false);
            btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        //DELETE THIS WHEN YOU FINISH DEBUGGINGG**************************************************************************
        //dataBaseHelper.deleteAllResrevations();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                BookReservations newReservation = new BookReservations();
                newReservation.setIsbn(book_isbn);
                newReservation.setUsername(loggedInUser);
                newReservation.setReservationDate(date);

                System.out.println(newReservation.getReservationDate());
                System.out.println(newReservation.getIsbn());
                System.out.println(newReservation.getUsername());

                //DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "Library", null, 1);
                Cursor user = dataBaseHelper.getUser(loggedInUser);
                user.moveToFirst();
                //here we have the user who did the transaction in cursor and can check number of reservations

                if(user.getInt(6) >= 3){
                    String TOAST_TEXT = "You can't reserve more than 3 books at a time!";
                    Toast toast =Toast.makeText(context, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    dataBaseHelper.newReservation(newReservation);
                    dataBaseHelper.addReservationToUser(loggedInUser);

                    btn.setClickable(false);
                    btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_title;
        public TextView text_pages;
        public TextView text_author;
        public TextView text_publisher;

        public Button reserve_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_title = (TextView) itemView.findViewById(R.id.textView_card_title);
            text_pages = (TextView) itemView.findViewById(R.id.textView_card_pages);
            text_author = (TextView) itemView.findViewById(R.id.textView_card_author);
            text_publisher = (TextView) itemView.findViewById(R.id.textView_card_publisher);

            reserve_button = (Button) itemView.findViewById(R.id.button_card_reserve);

        }


    }

}

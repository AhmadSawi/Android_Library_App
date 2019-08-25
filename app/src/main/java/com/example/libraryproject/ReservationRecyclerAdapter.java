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

import com.example.libraryproject.Book;
import com.example.libraryproject.BookReservations;
import com.example.libraryproject.DataBaseHelper;
import com.example.libraryproject.R;
import com.example.libraryproject.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<ReservationRecyclerAdapter.ViewHolder> {

    private List<Book> listItems;
    private Context context;

    public ReservationRecyclerAdapter(List<Book> listItems, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Book book = listItems.get(position);

        holder.text_title.setText(book.getTitle());
        holder.text_pages.setText(book.getPages()+" Pages");
        holder.text_author.setText("Author: " + book.getAuthor());
        holder.text_publisher.setText("Publisher: " + book.getPublisher());

        final Button btn = holder.reserve_button;
        btn.setText("CANCEL");

        final String book_isbn = book.getIsbn();

        SharedPrefManager sharedPrefManager;
        sharedPrefManager = SharedPrefManager.getInstance(context);
        final String loggedInUser = sharedPrefManager.readString("LoggedInUsername", "Android");

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "Library", null, 1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteResrevation(book_isbn, loggedInUser);
                dataBaseHelper.removeReservationFromUser(loggedInUser);

                btn.setClickable(false);
                btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                listItems.remove(position);
                notifyItemRemoved(position);

                String TOAST_TEXT = "Reservation Cancelled!";
                Toast toast =Toast.makeText(context, TOAST_TEXT,Toast.LENGTH_SHORT);
                toast.show();
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

package com.example.libraryproject;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Book> allBooks;


    public MyReservationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reservations, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_reservations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        allBooks = new ArrayList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);

        SharedPrefManager sharedPrefManager;
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        final String loggedInUser = sharedPrefManager.readString("LoggedInUsername", "Android");

        Cursor allUserReservationsCursor = dataBaseHelper.getUserReservations(loggedInUser);
        while (allUserReservationsCursor.moveToNext()) {
            Cursor bookCursor = dataBaseHelper.getBook(allUserReservationsCursor.getString(0));
            bookCursor.moveToFirst();

            Book oneBook = new Book();
            oneBook.setIsbn(bookCursor.getString(0));
            oneBook.setTitle(bookCursor.getString(1));
            oneBook.setPages( Integer.parseInt(bookCursor.getString(4)));
            oneBook.setAuthor(bookCursor.getString(2));
            oneBook.setPublisher(bookCursor.getString(3));
            System.out.println(oneBook);

            allBooks.add(oneBook);


        }

        adapter = new ReservationRecyclerAdapter(allBooks, getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

}

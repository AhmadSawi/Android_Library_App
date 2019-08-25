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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Book> allBooks;

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_books);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        allBooks = new ArrayList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);

        Cursor allBooksCursor = dataBaseHelper.getAllBooks();
        while (allBooksCursor.moveToNext()) {
            Book oneBook = new Book();
            oneBook.setIsbn(allBooksCursor.getString(0));
            oneBook.setTitle(allBooksCursor.getString(1));
            oneBook.setPages( Integer.parseInt(allBooksCursor.getString(4)));
            oneBook.setAuthor(allBooksCursor.getString(2));
            oneBook.setPublisher(allBooksCursor.getString(3));
            System.out.println(oneBook);

            allBooks.add(oneBook);

            adapter = new BookRecyclerAdapter(allBooks, getActivity());
            recyclerView.setAdapter(adapter);

            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        }

    }

}

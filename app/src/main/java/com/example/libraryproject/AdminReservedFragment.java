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
public class AdminReservedFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<BookReservations> allReservations;

    public AdminReservedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_reserved, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_admin_reservations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        allReservations = new ArrayList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);

        Cursor allReservationsCursor = dataBaseHelper.getAllReservations();
        while (allReservationsCursor.moveToNext()) {
            BookReservations bookReservation = new BookReservations();
            bookReservation.setIsbn(allReservationsCursor.getString(0));
            bookReservation.setUsername(allReservationsCursor.getString(1));
            bookReservation.setReservationDate(allReservationsCursor.getString(2));

            allReservations.add(bookReservation);
        }

        adapter = new AdminReservationsAdapter(allReservations, getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

}

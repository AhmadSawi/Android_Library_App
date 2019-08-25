package com.example.libraryproject;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBooksFragment extends Fragment {

    public SearchBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_books, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] options = {"Number of Pages", "Publisher", "Both"};
        final Spinner searchSpinner = (Spinner) getActivity().findViewById(R.id.spinner_search_choice);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
        searchSpinner.setAdapter(objGenderArr);

        final EditText pagesMin = (EditText)getActivity().findViewById(R.id.editText_search_pages_min);
        final EditText pagesMax = (EditText)getActivity().findViewById(R.id.editText_search_pages_max);
        final EditText searchPublisher = (EditText)getActivity().findViewById(R.id.editText_search_publisher);
        Button search_btn = (Button) getActivity().findViewById(R.id.button_search);

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor bookResults = null;
                boolean dataTrueFlag = true;

                if(searchSpinner.getSelectedItemPosition() == 0){ //0 is pages
                    if(pagesMax.getText().toString().isEmpty() || pagesMin.getText().toString().isEmpty()){
                        String TOAST_TEXT = "One or more of the fields are empty!";
                        Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                        toast.show();
                        dataTrueFlag = false;
                    }else
                    bookResults = dataBaseHelper.getBooksByPages(Integer.parseInt(pagesMin.getText().toString()), Integer.parseInt(pagesMax.getText().toString()));

                }else if(searchSpinner.getSelectedItemPosition() == 1){ //1 is publisher
                    if(searchPublisher.getText().toString().isEmpty()){
                        String TOAST_TEXT = "One or more of the fields are empty!";
                        Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                        toast.show();
                        dataTrueFlag = false;
                    }else
                    bookResults = dataBaseHelper.getBooksByPublisher(searchPublisher.getText().toString());

                }else{//both
                    if(pagesMax.getText().toString().isEmpty() || pagesMin.getText().toString().isEmpty() || searchPublisher.getText().toString().isEmpty()){
                        String TOAST_TEXT = "One or more of the fields are empty!";
                        Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                        toast.show();
                        dataTrueFlag = false;
                    }else
                    bookResults = dataBaseHelper.getBooksByPagesAndPublisher(Integer.parseInt(pagesMin.getText().toString()), Integer.parseInt(pagesMax.getText().toString()), searchPublisher.getText().toString());
                }

                if(dataTrueFlag) {
                    ArrayList<Book> booksList = new ArrayList<>();
                    while (bookResults.moveToNext()) {
                        Book oneBook = new Book();
                        oneBook.setIsbn(bookResults.getString(0));
                        oneBook.setTitle(bookResults.getString(1));
                        oneBook.setPages(Integer.parseInt(bookResults.getString(4)));
                        oneBook.setAuthor(bookResults.getString(2));
                        oneBook.setPublisher(bookResults.getString(3));
                        System.out.println(oneBook);

                        booksList.add(oneBook);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("books", booksList);

                    SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                    searchResultsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLayout, searchResultsFragment).commit();
                }
            }
        });




    }

}

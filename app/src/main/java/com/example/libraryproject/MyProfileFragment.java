package com.example.libraryproject;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] options = {"Male", "Female", "Other"};
        final Spinner genderSpinner = (Spinner) getActivity().findViewById(R.id.spinner_profile_gender);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
        genderSpinner.setAdapter(objGenderArr);

        TextView username = (TextView)getActivity().findViewById(R.id.textView_profile_username);
        TextView email = (TextView)getActivity().findViewById(R.id.textView_profile_email);
        TextView reserved = (TextView)getActivity().findViewById(R.id.textView_profile_reserved);
        final EditText age = (EditText)getActivity().findViewById(R.id.editText_profile_age);

        SharedPrefManager sharedPrefManager;
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        final String loggedInUser = sharedPrefManager.readString("LoggedInUsername", "Android");

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);
        final Cursor userCursor = dataBaseHelper.getUser(loggedInUser);
        userCursor.moveToFirst();

        username.setText(userCursor.getString(1));
        email.setText(userCursor.getString(3));
        reserved.setText(userCursor.getInt(6)+"");
        age.setText(userCursor.getString(5));

        if(userCursor.getString(2).equals("Male"))
            genderSpinner.setSelection(0);
        else if(userCursor.getString(2).equals("Female"))
            genderSpinner.setSelection(1);
        else
            genderSpinner.setSelection(2);

        Button saveChanges = (Button) getActivity().findViewById(R.id.button_profile_save);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.updateUserData(userCursor.getInt(0), Integer.parseInt(age.getText().toString()), genderSpinner.getSelectedItem().toString());
                String TOAST_TEXT = "Edits Saved!";
                Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }







}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUsersFragment extends Fragment {


    public AdminUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_users, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final boolean[] blockStatus = {false};
        final int[] userID = {0};

        final EditText user = (EditText) getActivity().findViewById(R.id.editText_username_tocheck);
        Button check = (Button)getActivity().findViewById(R.id.button_check_status);
        final TextView status = (TextView)getActivity().findViewById(R.id.textView_status);
        final Button changeStatus = (Button)getActivity().findViewById(R.id.button_block_unblock);

        status.setVisibility(View.GONE);
        changeStatus.setVisibility(View.INVISIBLE);

        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "Library", null, 1);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().isEmpty()){
                    String TOAST_TEXT = "Empty Field!";
                    Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    Cursor userCursor = dataBaseHelper.getUser(user.getText().toString());
                    if(userCursor.getCount() != 0){
                        userCursor.moveToFirst();
                        blockStatus[0] = userCursor.getInt(7) > 0;
                        userID[0] = userCursor.getInt(0);

                        if(!blockStatus[0]) {
                            status.setText("Not Blocked");
                            changeStatus.setText("Block");
                        } else {
                            status.setText("Blocked");
                            changeStatus.setText("UnBlock");
                        }
                        status.setVisibility(View.VISIBLE);

                        changeStatus.setVisibility(View.VISIBLE);
                    }else{
                        String TOAST_TEXT = "User doesnt exist!";
                        Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });


        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blockStatus[0]){//blocked and button is unblock
                    dataBaseHelper.unblockUser(userID[0]);
                    status.setText("Not Blocked");
                    blockStatus[0] = false;
                    changeStatus.setText("Block");
                    String TOAST_TEXT = "User unblocked successfully!";
                    Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    dataBaseHelper.blockUser(userID[0]);
                    status.setText("Blocked");
                    blockStatus[0] = true;
                    changeStatus.setText("UnBlock");
                    String TOAST_TEXT = "User blocked successfully!";
                    Toast toast =Toast.makeText(getActivity(), TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }

}

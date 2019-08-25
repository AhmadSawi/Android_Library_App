package com.example.libraryproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    String admin_username = "sael";
    String admin_password = "advancedproject123";

    SharedPrefManager sharedPrefManager;
    EditText usernameEditText;
    EditText passwordEditText;
    CheckBox checkbox;
    ArrayList<Book> booksFromServer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText)findViewById(R.id.editTextL_username);
        passwordEditText = (EditText)findViewById(R.id.editTextL_password);
        checkbox = (CheckBox)findViewById(R.id.checkBox_rememberme);

        sharedPrefManager = SharedPrefManager.getInstance(this);

        String savedUsername = sharedPrefManager.readString("username", "");
        String savedPassword = sharedPrefManager.readString("password", "");
        if(!savedUsername.isEmpty() && !savedPassword.isEmpty()){
            usernameEditText.setText(savedUsername);
            passwordEditText.setText(savedPassword);
            checkbox.setChecked(true);
        }

        Button login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usernameString = usernameEditText.getText().toString();
                String passwordString = passwordEditText.getText().toString();

                if(usernameString.equals(admin_username) && passwordString.equals(admin_password)){
                    Intent intent=new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                }else{
                    DataBaseHelper dataBaseHelper =new DataBaseHelper(LoginActivity.this,"Library", null,1);
                    Cursor allCustomersCursor = dataBaseHelper.getAllUsers();
                    while (allCustomersCursor.moveToNext()){
                        System.out.println(allCustomersCursor.getString(1));
                    }

                    Cursor matchedUserCursor = dataBaseHelper.AuthenticateUser(usernameString, passwordString);
                    if(matchedUserCursor.getCount()!=0){
                        if(checkbox.isChecked()){
                            sharedPrefManager.writeString("username",usernameString);
                            sharedPrefManager.writeString("password",passwordString);
                        }else{
                            sharedPrefManager.writeString("username","");
                            sharedPrefManager.writeString("password","");
                        }

                        matchedUserCursor.moveToFirst();
                        boolean status = matchedUserCursor.getInt(7) > 0;
                        if(!status){
                            sharedPrefManager.writeString("LoggedInUsername", usernameString);

                            //reading from HTTP and storing in database
                            ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(LoginActivity.this);
                            connectionAsyncTask.execute("https://api.myjson.com/bins/1epyfj");


                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            String TOAST_TEXT = "User blocked! please contact admin";
                            Toast toast =Toast.makeText(LoginActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }else{
                        String TOAST_TEXT = "username or password may be wrong! try again!";
                        Toast toast =Toast.makeText(LoginActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        Button register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        usernameEditText.setText("");
        passwordEditText.setText("");

        sharedPrefManager = SharedPrefManager.getInstance(this);

        String savedUsername = sharedPrefManager.readString("username", "");
        String savedPassword = sharedPrefManager.readString("password", "");
        if(!savedUsername.isEmpty() && !savedPassword.isEmpty()){
            usernameEditText.setText(savedUsername);
            passwordEditText.setText(savedPassword);
        }
    }

    public void fillBooksFromServer(ArrayList<Book> books) {
        if (books == null) {
            Toast.makeText(this, "Error Connecting to API", Toast.LENGTH_LONG).show();
        } else {
            booksFromServer = books;
            DataBaseHelper dataBaseHelper = new DataBaseHelper(LoginActivity.this, "Library", null, 1);
            dataBaseHelper.deleteAllBooks();
            for (Book book : booksFromServer)
                dataBaseHelper.insertBook(book);

            //dataBaseHelper.getReadableDatabase().delete("BOOK",null,null);
            //Cursor cursor = dataBaseHelper.getAllBooks();
            //while (cursor.moveToNext()) {
             //   System.out.println(cursor.getString(0)+' '+cursor.getString(1)+' '+
              //          cursor.getString(2)+' '+cursor.getString(3)+' '+cursor.getString(4));
            //}

        }
    }

}

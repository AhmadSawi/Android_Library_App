package com.example.libraryproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        final TextView activityName = (TextView)findViewById(R.id.ActivityName);
        activityName.startAnimation(AnimationUtils.loadAnimation(RegisterationActivity.this,R.anim.pulsate));

        String[] options = {"Male", "Female", "Other"};
        final Spinner genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        genderSpinner.setAdapter(objGenderArr);

        final EditText usernameEditText = (EditText)findViewById(R.id.editTextR_username);
        final EditText passwordEditText = (EditText)findViewById(R.id.editTextR_password);
        final EditText ageEditText = (EditText)findViewById(R.id.editTextR_age);
        final EditText emailEditText = (EditText)findViewById(R.id.editText_email);

        Button register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameString = usernameEditText.getText().toString();
                String passwordString = passwordEditText.getText().toString();
                String emailString = emailEditText.getText().toString();
                int ageInt = 0;
                if(!ageEditText.getText().toString().isEmpty())
                    ageInt = Integer.parseInt(ageEditText.getText().toString());
                String genderString = genderSpinner.getSelectedItem().toString();

                if(passwordString.isEmpty() || !isValidPassword(passwordString)){
                    String TOAST_TEXT = "Password must be at least 8 characters and cotains at least 2 numbers!";
                    Toast toast =Toast.makeText(RegisterationActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else if(usernameString.isEmpty() || !isValidName(usernameString)){
                    String TOAST_TEXT = "username must be atleast 3 characters and unique!";
                    Toast toast =Toast.makeText(RegisterationActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else if(ageInt==0 || !isValidAge(ageInt)){
                    String TOAST_TEXT = "age must at least be 10!";
                    Toast toast =Toast.makeText(RegisterationActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else if(emailString.isEmpty() || !isValidEmail(emailString)) {
                    String TOAST_TEXT = "this is not a correct email format!";
                    Toast toast =Toast.makeText(RegisterationActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterationActivity.this, "Library", null, 1);
                    User newUser = new User();
                    newUser.setName(usernameString);
                    newUser.setPassword(passwordString);
                    newUser.setEmail(emailString);
                    newUser.setAge(ageInt);
                    newUser.setGender(genderString);
                    newUser.setNumberOfReservedBooks(0);
                    newUser.setBlocked(false);
                    dataBaseHelper.insertUser(newUser);

                    String TOAST_TEXT = "Successfully registered! Please login";
                    Toast toast =Toast.makeText(RegisterationActivity.this, TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent=new Intent(RegisterationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    boolean isValidPassword(String password){
        boolean hasAlpha=false;
        boolean hasNumeric = false;
        int numericCount = 0;
        for(int i = 0;i<password.length();i++){
            if(Character.isAlphabetic(password.charAt(i)))
                hasAlpha= true;
            else if(Character.isDigit(password.charAt(i))) {
                hasNumeric = true;
                numericCount++;
            }
        }
        return hasAlpha&&hasNumeric&&numericCount>=2&&password.length()>=8;
    }

    boolean isValidName(String name){
        return name.length()>=3;
    }

    boolean isValidAge(int age){
        return age>=10;
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}

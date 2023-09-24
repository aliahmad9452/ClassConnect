package com.example.easychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easychat.model.UserModel;
import com.example.easychat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class LoginUsernameActivity extends AppCompatActivity {

    EditText usernameInput,rollNumber;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;
    Spinner classSpinner, semesterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);
        rollNumber = findViewById(R.id.rollNumber);
        usernameInput = findViewById(R.id.usernameInput);
        letMeInBtn = findViewById(R.id.login_let_me_in_btn);
        progressBar =findViewById(R.id.login_progress_bar);
        classSpinner = findViewById(R.id.classSpinner);
        semesterItems = findViewById(R.id.semesterItems);


        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.classes_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().contains("MS")) {
                    ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(LoginUsernameActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.semester4_array));
                    adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    semesterItems.setAdapter(adapterSemester);
                } else {
                    ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(LoginUsernameActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.semester8_array));
                    adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    semesterItems.setAdapter(adapterSemester);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        letMeInBtn.setOnClickListener((v -> {
            setUsername();
        }));


    }

    void setUsername(){
        String rollnumber = rollNumber.getText().toString();
        String username = usernameInput.getText().toString();
        String selectedClass = classSpinner.getSelectedItem().toString();
        String selectedSemester = semesterItems.getSelectedItem().toString();

        if(username.isEmpty() || username.length()<3|| rollnumber.isEmpty() || rollnumber.length()<1){
            usernameInput.setError("Username length should be at least 3 chars");
            rollNumber.setError("Roll Number can not be empty");
            return;
        }
        setInProgress(true);
        if(userModel!=null){
            userModel.setUsername(username);
            userModel.setRollNumber(rollnumber);
        }else{
            userModel = new UserModel(phoneNumber, rollnumber,username, selectedClass, selectedSemester, Timestamp.now(), FirebaseUtil.currentUserId());
        }



        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                   Intent intent = new Intent(LoginUsernameActivity.this,MainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                   startActivity(intent);
                }
            }
        });

    }

    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                  userModel =    task.getResult().toObject(UserModel.class);
                 if(userModel!=null){
                     usernameInput.setText(userModel.getUsername());
                 }
                }
            }
        });

    }
    void getRollnumber(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    userModel =    task.getResult().toObject(UserModel.class);
                    if(userModel!=null){
                        rollNumber.setText(userModel.getUsername());
                    }
                }
            }
        });

    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}
package com.example.rideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    EditText editTextFirstName,editTextLastName;
    Button btnAddData;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser;
    String userID;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

         pd = new ProgressDialog(this);

        //Initialize FireBase and db connection
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference().child(userID).child("USER");

        //Locating EditTexts
        editTextFirstName =  findViewById(R.id.firstName);
        editTextLastName =  findViewById(R.id.lastName);

        //Event on submit button
        btnAddData = findViewById(R.id.save_user);
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }
    // Method to receive data entered
    public void addUser(){
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phoneNumber = getIntent().getStringExtra("PhoneNumber");


        // Checking Validation
        if (firstName.isEmpty()){
            editTextFirstName.setError("First Name required!");
        }
        if (lastName.isEmpty()){
            editTextLastName.setError("Last Name required!");
        }
        else {
            pd.setMessage("Please wait...");
            pd.show();
            SaveUser saveUser = new SaveUser(firstName,lastName,phoneNumber);
            databaseUser.setValue(saveUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(RegisterUser.this,"Saved Successfully!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterUser.this,Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(RegisterUser.this,"Error"+e .getMessage(),Toast.LENGTH_SHORT);
                }
            });



        }

    }
}

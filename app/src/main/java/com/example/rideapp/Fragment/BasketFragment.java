package com.example.rideapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rideapp.R;
import com.example.rideapp.RegisterUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class BasketFragment extends Fragment {

    private  View basketView;
    Button basketBtn;
    TextView tokenText,textView;
    FirebaseAuth firebaseAuth;
    DatabaseReference tokenReference;
    String userID,tokenView;

    ProgressDialog pd;

    public static String getRandomNmberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);

        return String.format("%06d",number);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        basketView = inflater.inflate(R.layout.fragment_basket, container, false);


        basketBtn = basketView.findViewById(R.id.basketBtn);
        tokenText = basketView.findViewById(R.id.tokenText);
        textView = basketView.findViewById(R.id.basketText);

        pd = new ProgressDialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();


        tokenReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Token");

        basketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBasketToken();
            }
        });

        getToken();
        return basketView;
    }

    private void createBasketToken() {
        String token = getRandomNmberString();
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        if (token.isEmpty()) {
            pd.dismiss();
            Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();

        } else {
            tokenReference.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Token Created", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getContext(),"Error"+e .getMessage(),Toast.LENGTH_SHORT);
                }
            });
        }

    }

    private void getToken() {
     DatabaseReference   tokenRetrieve = FirebaseDatabase.getInstance().getReference().child(userID);

        tokenRetrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    if(dataSnapshot.hasChild("Token"))
                    {
                       String token = dataSnapshot.child("Token").getValue(String.class);
                        tokenText.setText(token);
                    }
                    /*
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("Token") !=null){
                        tokenView = map.get("Token").toString();
                        tokenText.setText(tokenView);
                    }

                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

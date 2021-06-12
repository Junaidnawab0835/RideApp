package com.example.rideapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rideapp.MainActivity;
import com.example.rideapp.R;
import com.example.rideapp.RegisterUser;
import com.example.rideapp.Verify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ProfileFragment extends Fragment {
    TextView balance_txt;
    private DatabaseReference dbReference;
    FirebaseAuth firebaseAuth;
    String userID;
    Button top_up_btn;
    Button log_out_btn;
    TextView nameTxt;
    String balance_entered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        balance_txt = view.findViewById(R.id.balance_txt);
        top_up_btn = view.findViewById(R.id.top_up_btn);
        log_out_btn = view.findViewById(R.id.logout_btn);
        nameTxt = view.findViewById(R.id.nameText);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child(userID);
        get_balance();
        getName();

        top_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topUpDialog();
            }
        });


        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Verify.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void topUpDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Top Up Balance");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View pay_Layout = inflater.inflate(R.layout.top_up_layout, null);
        // int variable and view
        final EditText topup_edtxt = pay_Layout.findViewById(R.id.balance_topup);



        dialog.setView(pay_Layout);

        // set buttons

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                balance_entered = topup_edtxt.getText().toString();
                updateBalance(balance_entered);
                get_balance();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    void get_balance()
    {
        try {

            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Balance")) {
                        String balance = dataSnapshot.child("Balance").getValue(String.class);
                        balance_txt.setText(balance);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"Error occured"+e,Toast.LENGTH_SHORT).show();
        }

    }

    public void updateBalance(String amount) {
        try {

            FirebaseAuth firebaseAuth;
            firebaseAuth = FirebaseAuth.getInstance();
            String userID = firebaseAuth.getUid();
            DatabaseReference balance = FirebaseDatabase.getInstance().getReference().child(userID);
            String update_amount = String.valueOf(Integer.parseInt(amount) + Integer.parseInt(balance_txt.getText().toString()));
            balance.child("Balance").setValue(update_amount);
        /*
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Balance", amount);
        balance.updateChildren(hashMap);

         */
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"Error Occured"+e,Toast.LENGTH_SHORT).show();
        }
    }

    public void getName()
    {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("USER")) {
                    String name = dataSnapshot.child("USER").child("firstName").getValue(String.class);
                    nameTxt.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

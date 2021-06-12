package com.example.rideapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rideapp.CartAdapter;
import com.example.rideapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CartFragment extends Fragment {

    private View viewCartItems;
    private RecyclerView list_view;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String> list_of_items = new ArrayList<>();
    private ArrayList<Item_Model> item_list  = new ArrayList<>();
    private DatabaseReference listReference;
    private String userID;
    private DatabaseReference dbReference;
    private FirebaseAuth firebaseAuth;
    private Button payingToken,payingCash;
    private CartAdapter cartAdapter;
    Item_Model item_model;
    TextView total_price;
    Button clearCartBtn;
    int total_price_value = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       viewCartItems = inflater.inflate(R.layout.fragment_cart, container, false);

       total_price = viewCartItems.findViewById(R.id.totalCost);
        clearCartBtn = viewCartItems.findViewById(R.id.clearCartBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        Log.d("test123",userID);

       listReference = FirebaseDatabase.getInstance().getReference().child(userID).child("Cart");

       InitializeList();

       displayList();

       payingToken = viewCartItems.findViewById(R.id.payToken);
       payingToken.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               payTokenDialog();
           }
       });

       payingCash = viewCartItems.findViewById(R.id.payCash);
       payingCash.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               payCashDialog();
           }
       });

       clearCartBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               clearCart();
               Toast.makeText(getContext(),"Cart is Cleared",Toast.LENGTH_SHORT).show();
           }
       });
       total_price.setText(total_price_value+" ");


        return viewCartItems;
    }


    private void InitializeList() {

        list_view =  viewCartItems.findViewById(R.id.cart_item_list);
        cartAdapter = new CartAdapter(getContext(),item_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(llm);
        //arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_items);
        list_view.setAdapter(cartAdapter);
    }


    private void displayList() {

        listReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item_model = postSnapshot.getValue(Item_Model.class);
                    item_list.add(item_model);
                    total_price_value = total_price_value + method(item_model.price);
                    cartAdapter.notifyDataSetChanged();
                }
                Log.d("test123","total"+total_price_value);
                total_price.setText(Integer.toString(total_price_value));

                /*
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());

                }

                 */

              //  list_of_items.clear();
              //  list_of_items.addAll(set);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public int method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '$') {
            str = str.substring(0, str.length() - 1);
        }
        return Integer.parseInt(str);
    }
    private void payTokenDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Partial Payment");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View pay_Layout = inflater.inflate(R.layout.pay_bill,null);
        // int variable and view
        final EditText editText_tokenNumber = pay_Layout.findViewById(R.id.tokenNumber);

        // Get current data
        //editText_tokenNumber.setText(token);


        dialog.setView(pay_Layout);

        // set buttons

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payByToken(editText_tokenNumber.getText().toString());
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



    private void payCashDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Payment Confirmation");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View pay_Layout_cash = inflater.inflate(R.layout.pay_cash,null);
        // int variable and view
        //final EditText editText_tokenNumber = pay_Layout.findViewById(R.id.tokenNumber);

        // Get current data
        //editText_tokenNumber.setText(token);


        dialog.setView(pay_Layout_cash);

        // set buttons

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCart();
                Toast.makeText(getContext(),"payed Cash Successfully",Toast.LENGTH_SHORT).show();
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

    void payByToken(final String tokenNumber)
    {
        dbReference = FirebaseDatabase.getInstance().getReference();
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                      if(snapshot.child("Token").getValue(String.class).equals(tokenNumber))
                      {
                          FirebaseAuth firebaseAuth;
                          firebaseAuth = FirebaseAuth.getInstance();
                          String userID = firebaseAuth.getUid();
                          if(!snapshot.getKey().equals(userID)) {
                              int otherUserBlnc = Integer.parseInt(snapshot.child("Balance").getValue(String.class));
                              String otherUserKey = snapshot.getKey();
                              int total_bill = Integer.parseInt(total_price.getText().toString());
                              if (otherUserBlnc >= total_bill) {
                                  otherUserBlnc = otherUserBlnc - total_bill;
                                  updateBalance(otherUserBlnc, otherUserKey);
                                  clearCart();
                                  Toast.makeText(getContext(), "Bill is cut from other user wallet", Toast.LENGTH_SHORT).show();

                              } else {
                                  Toast.makeText(getContext(), "Other User Balance is less then total bill", Toast.LENGTH_SHORT).show();
                              }
                          }
                          else
                          {
                              Toast.makeText(getContext(),"Cannot Use your token to Pay",Toast.LENGTH_SHORT).show();
                          }
                      }
                      else
                      {
                          Toast.makeText(getContext(),"Token is not match!",Toast.LENGTH_SHORT).show();
                      }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void updateBalance(int amount,String userid) {
        DatabaseReference balance = FirebaseDatabase.getInstance().getReference().child(userid);
        balance.child("Balance").setValue(amount);
    }
    public void clearCart()
    {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(userID);
        db.child("Cart").removeValue();
        item_list.clear();
        total_price.setText("0");
        cartAdapter.notifyDataSetChanged();

    }

}

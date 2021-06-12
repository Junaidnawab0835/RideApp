package com.example.rideapp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideapp.Fragment.Item_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context ctx;
    LayoutInflater inflater;
    private ArrayList<Item_Model> list_of_items = new ArrayList<>();
    DatabaseReference reference;
    String userID, balanceTxt;
    FirebaseAuth firebaseAuth;
    int item_position;

    public CartAdapter(Context ctx, ArrayList<Item_Model> list_of_items){
        this.list_of_items = list_of_items;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);


    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_row,parent,false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, final int position) {
        /*
        holder.txtDesc.setText(titles.get(position));
        holder.txtPrice.setText(price.get(position));
        holder.imageView.setImageResource(images.get(position));

         */
        holder.txtPrice.setText(list_of_items.get(position).getPrice());
        holder.txtName.setText(list_of_items.get(position).getName());
        holder.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removeSingleItemDialog(position);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtPrice;
        LinearLayout item_layout;
        public ViewHolder(View itemView) {
            super(itemView);

           txtName = (TextView) itemView.findViewById(R.id.name_txt);
           txtPrice = (TextView) itemView.findViewById(R.id.price_txt);
           item_layout = itemView.findViewById(R.id.cart_item_layout);

                }




        }
    private void removeSingleItemDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle("Remove This Item");
        LayoutInflater inflater = LayoutInflater.from(ctx);
        item_position = position;
        // View pay_Layout_cash = inflater.inflate(R.layout.pay_cash,null);


        // dialog.setView(pay_Layout_cash);

        // set buttons

        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth = FirebaseAuth.getInstance();
                userID = firebaseAuth.getUid();
                reference = FirebaseDatabase.getInstance().getReference().child(userID).child("Cart");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            if(snapshot.hasChild("id")) {
                                if (snapshot.child("id").getValue(String.class).equals(list_of_items.get(item_position).getId())) {
                                    removeitem(snapshot.getKey());
                                    Toast.makeText(ctx, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(ctx,"No item in Cart",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ctx,"Item Not Removed Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void removeitem(String key) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(userID);
        db.child("Cart").child(key).removeValue();
        list_of_items.remove(item_position);
        notifyDataSetChanged();
    }

}


package com.example.rideapp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

//private static final String TAG = "RecyclerAdapter";
//private Context mContext;
//private ArrayList<Model> imagesList;

List<String>titles;
List<Integer>images;
List<String>price;
Context ctx;
LayoutInflater inflater;

DatabaseReference reference;
String userID, balanceTxt;
FirebaseAuth firebaseAuth;




//public RecyclerAdapter(Context context, ArrayList<Model> imagesList){
//        this.mContext = context;
//        this.imagesList = imagesList;
//        }

    public RecyclerAdapter(Context ctx, List<String>titles, List<Integer> images, List<String> price){
        this.titles = titles;
        this.images = images;
        this.price = price;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        View view = inflater.inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.txtDesc.setText(imagesList.get(position).getDescription());
        //Picasso.get().load(imagesList.get(position).getUrl()).into(holder.imageView);

        holder.txtDesc.setText(titles.get(position));
        holder.txtPrice.setText(price.get(position));
        holder.imageView.setImageResource(images.get(position));


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(userID).child("Cart").push();

    }

    @Override
    public int getItemCount() {
        //return imagesList.size();
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txtDesc;
        TextView txtPrice;
        Button cartBtn;

        public ViewHolder(View itemView){

            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            txtDesc = (TextView) itemView.findViewById(R.id.productName);
            txtPrice = (TextView) itemView.findViewById(R.id.productPrice);
            cartBtn = (Button) itemView.findViewById(R.id.addToCartBtn);

            cartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                    String name = txtDesc.getText().toString();
                    String price =  txtPrice.getText().toString();
                    int random = new Random().nextInt(100);
                    Item_Model item_model = new Item_Model(name,price+"$",String.valueOf(random));
                    reference.setValue(item_model);
                }
            });


        }


    }
}

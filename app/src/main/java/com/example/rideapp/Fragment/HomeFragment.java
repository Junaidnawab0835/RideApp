package com.example.rideapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rideapp.Home;
import com.example.rideapp.Model;
import com.example.rideapp.R;
import com.example.rideapp.RecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<String>titles;
    List<Integer>images;
    List<String>price;
    RecyclerAdapter adapter;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);



        titles = new ArrayList<>();
        price = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("Oil");
        titles.add("Milk");
        titles.add("Jam");
        titles.add("Food");
        titles.add("Ghee Bottle");
        titles.add("Coke");
        titles.add("White Bread");
        titles.add("Biscuits");
        titles.add("Grooves");
        titles.add("Bread");
        titles.add("Brown Bread");
        titles.add("Bun");
        titles.add("Stone Ground");
        titles.add("Sausage");
        titles.add("Lewis Bread");
        titles.add("Grain Bread");
        titles.add("Food Cane");
        titles.add("Eggs");
        titles.add("ZWAN");
        titles.add("Sea Food");
        titles.add("Meat");
        titles.add("Nishiki");
        titles.add("Food Pack");
        titles.add("Sweet");
        titles.add("Doritos");
        titles.add("item");
        titles.add("Tosttos");
        titles.add("strawberry");
        titles.add("Ketchup");
        titles.add("Food");

        price.add("20");
        price.add("100");
        price.add("50");
        price.add("200");
        price.add("60");
        price.add("2");
        price.add("10");
        price.add("44");
        price.add("30");
        price.add("80");
        price.add("65");
        price.add("67");
        price.add("20");
        price.add("100");
        price.add("50");
        price.add("200");
        price.add("60");
        price.add("2");
        price.add("10");
        price.add("44");
        price.add("30");
        price.add("80");
        price.add("65");
        price.add("67");
        price.add("80");
        price.add("65");
        price.add("67");
        price.add("20");
        price.add("100");
        price.add("50");

        images.add(R.drawable.a);
        images.add(R.drawable.b);
        images.add(R.drawable.c);
        images.add(R.drawable.d);
        images.add(R.drawable.e);
        images.add(R.drawable.f);
        images.add(R.drawable.i);
        images.add(R.drawable.j);
        images.add(R.drawable.k);
        images.add(R.drawable.l);
        images.add(R.drawable.m);
        images.add(R.drawable.n);
        images.add(R.drawable.o);
        images.add(R.drawable.p);
        images.add(R.drawable.q);
        images.add(R.drawable.r);
        images.add(R.drawable.s);
        images.add(R.drawable.t);
        images.add(R.drawable.u);
        images.add(R.drawable.v);
        images.add(R.drawable.x);
        images.add(R.drawable.y);
        images.add(R.drawable.z);
        images.add(R.drawable.ab);
        images.add(R.drawable.ac);
        images.add(R.drawable.ad);
        images.add(R.drawable.ae);
        images.add(R.drawable.af);
        images.add(R.drawable.ah);
        images.add(R.drawable.ai);

        adapter = new RecyclerAdapter(getContext(),titles,images,price);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx,2,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);



//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setHasFixedSize(true);
//
//        reference = FirebaseDatabase.getInstance().getReference().child("Uploads");
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        imagesList = new ArrayList<>();
//
//        init();
        // Inflate the layout for this fragment
        return view;
    }



//    private void init(){
//        //clearAll();
//
//        Query query = reference.child("Images");
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                clearAll();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Model model = new Model();
//                    model.setUrl(snapshot.child("url").getValue().toString());
//                    model.setDescription(snapshot.child("description").getValue().toString());
//                    imagesList.add(model);
//                }
//
//                recyclerAdapter = new RecyclerAdapter(mContext, imagesList);
//                mRecyclerView.setAdapter(recyclerAdapter);
//                recyclerAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    private void clearAll(){
//        if (imagesList != null){
//            imagesList.clear();
//
//            if (recyclerAdapter != null){
//                recyclerAdapter.notifyDataSetChanged();
//            }
//        }
//
//        imagesList = new ArrayList<>();
//    }

}

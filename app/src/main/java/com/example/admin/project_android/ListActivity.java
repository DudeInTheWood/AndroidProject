package com.example.admin.project_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ResViewAdapter.OnItemClickListener {
    public RecyclerView recyclerView;
    public ResViewAdapter resViewAdapter;
    public FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    public boolean first = true;

    //firebase
    public DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private List<Restaurant> restaurants;


    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(ListActivity.this, CheckInListActivity.class);
        intent.putExtra("res",restaurants.get(pos).getName());
        intent.putExtra("image", restaurants.get(pos).getImageUri());
        intent.putExtra("key", restaurants.get(pos).getmKey());
        intent.putExtra("desc",restaurants.get(pos).getDescription());
        intent.putExtra("love",restaurants.get(pos).getLove());
        startActivity(intent);
    }

    @Override
    public void OnwhatEverClick(int pos) {
        Toast.makeText(this, "LIKE "+restaurants.get(pos).getName()+" !", Toast.LENGTH_SHORT).show();
        databaseReference.child(restaurants.get(pos).getmKey()).child("love")
                .setValue(restaurants.get(pos).getLove()+1);
    }

    @Override
    public void OnDeleteClick(int pos) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurants = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("upload");

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        if (first){
            refresh();
            first = false;
        }


    }
    public void refresh() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurants.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Restaurant restaurant = postSnapshot.getValue(Restaurant.class);
                    restaurant.setmKey(postSnapshot.getKey());

                    restaurants.add(restaurant);
                }
                resViewAdapter = new ResViewAdapter(ListActivity.this, restaurants);
                recyclerView.setAdapter(resViewAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();

                resViewAdapter.setOnItemClickListener(ListActivity.this);

                progressBar.setVisibility(View.INVISIBLE);
                notification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void notification() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ku_hew)
                        .setContentTitle("KUhew")
                        .setContentText("Some Suggest a new restaurant...")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

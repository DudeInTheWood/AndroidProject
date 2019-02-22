package com.example.admin.project_android;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CheckInListActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView resName, desc;
    private Intent intent, intent2;
    private ProgressBar checkProgressBar;

    public RecyclerView recyclerView;
    public CheckInAdapter checkInAdapter;
    public FloatingActionButton floatingActionButton;
    public int love;
    List<CheckInData> checkInList;


    //firebase
    public DatabaseReference databaseReference, commentRef;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_list);
        intent = getIntent();
        intent2 = new Intent(CheckInListActivity.this, UpComment.class);
        resName = findViewById(R.id.restaurantText);
        imageView = findViewById(R.id.restaurantImage);
        desc = findViewById(R.id.descriptText);
        Picasso.get().load(intent.getStringExtra("image")).fit().centerCrop().into(imageView);
        resName.setText(intent.getStringExtra("res"));
        desc.setText(intent.getStringExtra("desc"));
        key = intent.getStringExtra("key");
        love = intent.getIntExtra("love",0);

        checkInList = new ArrayList<>();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);




        //recyclerView = findViewById(R.id.recycler_CheckView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("upload");
        commentRef = FirebaseDatabase.getInstance().getReference("comment");

        floatingActionButton = findViewById(R.id.checkFloatBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseReference.child(key).child("love")
//                        .setValue(love+1);
//                Toast.makeText(CheckInListActivity.this, "Love This !", Toast.LENGTH_SHORT).show();
                intent2.putExtra("key",key);
                startActivity(intent2);
            }
        });
        recyclerView = findViewById(R.id.comment_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkInList.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    CheckInData cdata = postSnapShot.getValue(CheckInData.class);
                    if (cdata.getKey().equals(key)){
                        checkInList.add(cdata);
                        Log.i("commented",""+cdata.getComment());
                    }
                    checkInAdapter = new CheckInAdapter(CheckInListActivity.this,checkInList);
                    recyclerView.setAdapter(checkInAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(CheckInListActivity.this, "comment loaded...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

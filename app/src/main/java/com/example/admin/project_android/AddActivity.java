package com.example.admin.project_android;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class  AddActivity extends AppCompatActivity {
    public Intent intent,intent2;
    public Bitmap bit;
    public Uri uri;
    public ImageView imageView;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    public Button okBtn;
    public EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //firebase database
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("upload");
        storageRef = FirebaseStorage.getInstance().getReference("upload");

        //textField
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);

        //UI
        imageView = findViewById(R.id.imageView);
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        okBtn = findViewById(R.id.button2);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
            } else if (type.startsWith("image/")) {
                try {
                    handleSendImage(intent); // Handle single image being sent
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void handleSendImage(Intent intent) throws IOException {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.i("yeah", "that's: "+imageUri);
            this.uri = imageUri;
            bit = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //imageView.setImageBitmap(bit);
            // Update UI to reflect image being shared
        }
    }


    public void clk(View view){
        if (view.getId() == R.id.button3){
            finish();
        }else {
            intent2 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent2, 0);
            Log.i("dd","WOW");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            uri = data.getData();
            bit = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Picasso.get().load(uri).centerCrop().fit().into(imageView);
            //imageView.setImageBitmap(bit);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void upload () {
        if (uri != null){
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis()+""+getFileExtension(uri));
            fileRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    },3000);
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String storeUri = uri.toString();
                            Log.i("getUri",storeUri);
                            Restaurant restaurant = new Restaurant(editText.getText().toString(),editText2.getText().toString(),storeUri);

                            databaseRef.push().setValue(restaurant);
                        }
                    });
                    Toast.makeText(AddActivity.this, "photo has bean uploaded....", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    //progressBar.setProgress((int) progress);
                }
            });
        }
    }
}

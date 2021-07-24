package com.example.madcampweek4.ui.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madcampweek4.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class NewPostActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    ImageView iv_newPostImage;
    EditText et_newPostDescription;
    Bitmap bitmap;
    byte[] bitmap_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        ActionBar ab = (NewPostActivity.this).getSupportActionBar();
        ab.setTitle("New Post");

        iv_newPostImage = (ImageView)findViewById(R.id.iv_newPostImage);

        database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Post"); //realtime database

        storage = FirebaseStorage.getInstance();  //for image
        storageReference = storage.getReference().child("Post/post_url");

        iv_newPostImage = findViewById(R.id.iv_newPostImage);
        et_newPostDescription = findViewById(R.id.et_newPostDescription);

        findViewById(R.id.btn_addPostImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(NewPostActivity.this)
                        .withPermissions(Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                showCamera(v);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                Toast.makeText(NewPostActivity.this, "촬영이 거부되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).check();
            }
        });

        findViewById(R.id.btn_uploadNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                Intent intent = new Intent(NewPostActivity.this,BoardActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            bitmap = (Bitmap)data.getParcelableExtra("data");
            iv_newPostImage.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bitmap_content = baos.toByteArray();
        }
    }

    public void insertData(){
        String postId = databaseReference.push().getKey().toString();

        StorageReference contentImageRef = storageReference.child(postId);
        UploadTask uploadTask = contentImageRef.putBytes(bitmap_content);

        Board boardItem = new Board("", "", "", "", et_newPostDescription.getText().toString(), bitmap, "", 0);

        databaseReference.child(postId).setValue(boardItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data upload Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "Data upload Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
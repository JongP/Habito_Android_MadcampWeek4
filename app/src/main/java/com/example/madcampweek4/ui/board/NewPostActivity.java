package com.example.madcampweek4.ui.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewPostActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    ImageView iv_newPostImage;
    EditText et_newPostDescription;
    Bitmap bitmap;
    Uri uri_newPostImage;
    String userName, postId;
    Board boardItem;

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
        storageReference = storage.getReference();

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
        Log.d("카메라 성공", "제발");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Log.d("결과 받기 성공", "제발");
            bitmap = data.getParcelableExtra("data");
            uri_newPostImage = getImageUri(this, bitmap);
            Log.d("uri로 받기 성공", "제발");
            iv_newPostImage.setImageURI(uri_newPostImage);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        Log.d("이미지 받기 성공", "제발");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public void insertData(){
        storageReference= storage.getReference().child("Post");
        postId = databaseReference.push().getKey().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : null;

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("아이디 이름 받기 성공", "제발 "+uid);
        DatabaseReference name = mDatabase.child("MadCampWeek4/UserAccount").child(user.getUid()).child("name");

        name.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(NewPostActivity.this, "아이디 가져오기 실패", Toast.LENGTH_SHORT).show();
                    userName="";
                }else{
                    StorageReference contentImageRef = storageReference.child("Post/post_uri/"+ postId);
                    UploadTask uploadTask = contentImageRef.putFile(uri_newPostImage);
                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

                    userName = String.valueOf(task.getResult().getValue());
                    Log.d("아이디 받는 함수 성공", "제발 " + userName);

                    boardItem = new Board(postId, userName, "", "", et_newPostDescription.getText().toString(), uri_newPostImage.toString(), timeStamp, 0);

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
                    Log.d("아이디 받는 함수 성공22", "제발 " + userName);
                }
            }
        });

    }

}
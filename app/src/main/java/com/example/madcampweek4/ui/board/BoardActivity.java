package com.example.madcampweek4.ui.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madcampweek4.Login;
import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.example.madcampweek4.UserAccount;
import com.example.madcampweek4.ui.group.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private BoardRecyclerViewAdapter boardRecyclerViewAdapter;
    private ArrayList<Board> boardData;
    private FirebaseDatabase database, mdatabase;
    private DatabaseReference databaseReference, mdatabaseReference, userDatabase;
    private String groupId, groupName;
    private Dialog dialog;

    private FirebaseStorage storage, mstorage;
    private StorageReference storageReference, mstorageReference;

    ImageView iv_newPostImage;
    EditText et_newPostDescription;
    Bitmap bitmap;
    Uri uri_newPostImage;
    String userName, postId;
    Board boardItem;

    private String TAG="BoardActTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        groupId=intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        Log.d(TAG, groupId);

        boardData= new ArrayList<>();

        recyclerView = findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Post/"+groupId); //db Table 연동 : 오빠

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Post/Post/post_uri");

        mdatabase= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        mdatabaseReference = mdatabase.getReference("MadCampWeek4/Post/"+ groupId); //realtime database

        mstorage = FirebaseStorage.getInstance();  //for image
        mstorageReference = mstorage.getReference();

/*
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //getting data from firebase
                boardData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Board board = dataSnapshot.getValue(Board.class);
                    String postId=board.getPost_id();
                    StorageReference postUriRef = storageReference.child(postId);

                    if(board.getPost_uri().equals("")) {
                        postUriRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "Uri of" + groupId + ": " + uri.toString());
                                board.setPost_uri(uri.toString());
                                boardData.add(board);
                                boardRecyclerViewAdapter.notifyDataSetChanged();


                                database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
                                databaseReference = database.getReference("MadCampWeek4/Post/"+board.getGroup_id()+"/"+board.getPost_id()); //db Table 연동 :
                                HashMap<String,Object> hashMap= new HashMap<>();
                                hashMap.put("post_uri",uri.toString());
                                databaseReference.updateChildren(hashMap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(TAG, "wtf of " + postId);
                                Log.d(TAG, e.toString());
                            }
                        });
                    }else{
                        boardData.add(board);
                        boardRecyclerViewAdapter.notifyDataSetChanged();
                    }

                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                //error while loading data
                Log.e("BoardActivity", String.valueOf(error.toException()));  //print error
            }
        });*/

        boardRecyclerViewAdapter = new BoardRecyclerViewAdapter(this, boardData);
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_new_post);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        iv_newPostImage = (ImageView) dialog.findViewById(R.id.iv_newPostImage);
        et_newPostDescription = dialog.findViewById(R.id.et_newPostDescription);
        ImageView iv_closeDialog = dialog.findViewById(R.id.iv_closeDialog);

        findViewById(R.id.btn_newPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPostDialog();
                dialog.show();
            }
        });
        iv_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBoardData();
    }

    private void fetchBoardData(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //getting data from firebase
                boardData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Board board = dataSnapshot.getValue(Board.class);
                    String postId=board.getPost_id();
                    StorageReference postUriRef = storageReference.child(postId);

                    if(board.getPost_uri().equals("")) {
                        postUriRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "Uri of" + groupId + ": " + uri.toString());
                                board.setPost_uri(uri.toString());
                                boardData.add(board);
                                boardRecyclerViewAdapter.notifyDataSetChanged();


                                database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
                                databaseReference = database.getReference("MadCampWeek4/Post/"+board.getGroup_id()+"/"+board.getPost_id()); //db Table 연동 :
                                HashMap<String,Object> hashMap= new HashMap<>();
                                hashMap.put("post_uri",uri.toString());
                                databaseReference.updateChildren(hashMap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(TAG, "wtf of " + postId);
                                Log.d(TAG, e.toString());
                            }
                        });
                    }else{
                        boardData.add(board);
                        boardRecyclerViewAdapter.notifyDataSetChanged();
                    }

                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                //error while loading data
                Log.e("BoardActivity", String.valueOf(error.toException()));  //print error
            }
        });
    }

    public void newPostDialog(){
        Button btn_addPostImage = dialog.findViewById(R.id.btn_addPostImage);
        btn_addPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(BoardActivity.this)
                        .withPermissions(Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                showCamera(v);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                Toast.makeText(BoardActivity.this, "촬영이 거부되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).check();
            }
        });

        dialog.findViewById(R.id.btn_uploadNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                Intent intent = new Intent(BoardActivity.this,BoardActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupName",groupName);

                startActivity(intent);
                finish();
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

        if (resultCode == RESULT_OK) {
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

    private void insertData(){
        storageReference= storage.getReference().child("Post");
        postId = databaseReference.push().getKey().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : null;

        firebaseAuth = FirebaseAuth.getInstance();
        userDatabase = database.getReference("MadCampWeek4/UserAccount/"+uid);

        userDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(BoardActivity.this, "아이디 가져오기 실패", Toast.LENGTH_SHORT).show();
                    userName="";
                }else{
                    StorageReference contentImageRef = storageReference.child("Post/post_uri/"+ postId);
                    UploadTask uploadTask = contentImageRef.putFile(uri_newPostImage);

                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                    UserAccount userAccount = task.getResult().getValue(UserAccount.class);
                    userName = userAccount.getName();

                    boardItem = new Board(postId, uid, userName,userAccount.getProfileURL(),groupId, et_newPostDescription.getText().toString(), "", timeStamp, 0);

                    databaseReference.child(postId).setValue(boardItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BoardActivity.this.getApplicationContext(), "Post upload Success", Toast.LENGTH_SHORT).show();
                            Login.setPostNum(Login.getPostNum()+1);
                            HashMap<String,Object> todayMap = new HashMap<>();
                            todayMap.put("postNum",Login.getPostNum());
                            userDatabase.child("posts/today").updateChildren(todayMap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(BoardActivity.this.getApplicationContext(), "Data upload Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
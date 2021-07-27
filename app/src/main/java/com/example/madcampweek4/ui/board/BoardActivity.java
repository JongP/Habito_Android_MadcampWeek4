package com.example.madcampweek4.ui.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.example.madcampweek4.ui.group.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BoardRecyclerViewAdapter boardRecyclerViewAdapter;
    private ArrayList<Board> boardData;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String groupId;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String TAG="BoardActTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        groupId=intent.getStringExtra("groupId");
        String groupName = intent.getStringExtra("groupName");
        Log.d(TAG, groupId);

        ActionBar ab = (BoardActivity.this).getSupportActionBar();
        ab.setTitle(groupName);


        boardData= new ArrayList<>();

        recyclerView = findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Post/"+groupId); //db Table 연동 : 오빠

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Post/Post/post_uri");
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

        findViewById(R.id.btn_newPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this,NewPostActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupName",groupName);
                startActivity(intent);
                finish();
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
}
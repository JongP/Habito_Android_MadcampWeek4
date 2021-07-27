package com.example.madcampweek4.ui.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BoardRecyclerViewAdapter boardRecyclerViewAdapter;
    private ArrayList<Board> boardData;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String groupId;

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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //getting data from firebase
                boardData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Board board = dataSnapshot.getValue(Board.class);
                    Log.d(TAG, "snapshot value: "+dataSnapshot.getValue().toString());
                    Log.d(TAG, "class value: " + board.getPost_content());
                    Log.d(TAG, "class value: " + board.getContent());

                    boardData.add(board);
                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                //error while loading data
                Log.e("BoardActivity", String.valueOf(error.toException()));  //print error
            }
        });

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
}
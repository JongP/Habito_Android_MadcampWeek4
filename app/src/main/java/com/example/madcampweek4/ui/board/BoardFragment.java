package com.example.madcampweek4.ui.board;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BoardFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private BoardRecyclerViewAdapter boardRecyclerViewAdapter;
    private ArrayList<Board> boardData;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_board, container, false);

        boardData= new ArrayList<>();
        boardData.add(new Board("", "user1","","group1", "post content1", "", "", 1));
        boardData.add(new Board("", "user2","","group2", "post content2", "", "", 1));
        boardData.add(new Board("", "user3","","group3", "post content3", "", "", 1));

        recyclerView = view.findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boardRecyclerViewAdapter = new BoardRecyclerViewAdapter(getContext(), boardData);
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Post"); //db Table 연동 : 오빠

        view.findViewById(R.id.btn_newPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFragment(1);
            }
        });

        return view;
    }
}
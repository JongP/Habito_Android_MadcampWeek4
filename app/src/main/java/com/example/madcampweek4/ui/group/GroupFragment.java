package com.example.madcampweek4.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.example.madcampweek4.ui.board.BoardActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GroupRecyclerViewAdapter groupRecyclerViewAdapter;
    private ArrayList<Group> groupData;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);

        groupData = new ArrayList<>();  //객체 담을 arraylist
        groupData.add(new Group("", "Group1", "Content1"));

        recyclerView = view.findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupRecyclerViewAdapter = new GroupRecyclerViewAdapter(getContext(), groupData);
        recyclerView.setAdapter(groupRecyclerViewAdapter);

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Group"); //db Table 연동 : 오빠



        groupRecyclerViewAdapter.setOnItemClickListener(new GroupRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
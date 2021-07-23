package com.example.madcampweek4.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.R;
import com.example.madcampweek4.adapters.SearchGroupAdapter;
import com.example.madcampweek4.item.SearchGroupItem;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private TextView tv_search;
    private RecyclerView rv_searchGroup;
    private SearchGroupAdapter searchGroupAdapter;
    private List<SearchGroupItem> searchGroupItemList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rv_searchGroup= view.findViewById(R.id.rv_groupSearch);
        rv_searchGroup.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        searchGroupItemList= new ArrayList<SearchGroupItem>();
        searchGroupAdapter = new SearchGroupAdapter(searchGroupItemList,getContext());

        rv_searchGroup.setAdapter(searchGroupAdapter);


        searchGroupItemList.add(new SearchGroupItem("testgroupName","testGroupContent"));
        searchGroupAdapter.notifyDataSetChanged();

        return view;
    }

}
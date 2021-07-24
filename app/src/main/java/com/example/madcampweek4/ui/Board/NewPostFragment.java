package com.example.madcampweek4.ui.Board;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;

import static android.app.Activity.RESULT_OK;

public class NewPostFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_new_post, container, false);

        view.findViewById(R.id.btn_addPostImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;



}
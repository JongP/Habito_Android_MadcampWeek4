package com.example.madcampweek4.ui.gacha;

import android.animation.Animator;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.madcampweek4.R;


public class GachaFragment extends Fragment {
    private View view;
    private AppCompatButton btn_fishing;
    private LottieAnimationView lottie_fishing,lottie_fish,lottie_conffeti;
    public GachaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_gacha, container, false);

        lottie_fishing = view.findViewById(R.id.lottie_fishing);
        lottie_fish = view.findViewById(R.id.lottie_fish);
        lottie_conffeti= view.findViewById(R.id.lottie_conffeti);
        btn_fishing = view.findViewById(R.id.btn_fishing);

        btn_fishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_fishing.setVisibility(View.GONE);
                lottie_fishing.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottie_fish.setAnimation(R.raw.fish_spin_jelly);
                        lottie_fish.playAnimation();
                        lottie_conffeti.playAnimation();
                    }
                },4600);

            }
        });


        return  view;
    }
}
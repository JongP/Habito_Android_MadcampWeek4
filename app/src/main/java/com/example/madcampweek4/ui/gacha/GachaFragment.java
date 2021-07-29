package com.example.madcampweek4.ui.gacha;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.madcampweek4.Fish;
import com.example.madcampweek4.Login;
import com.example.madcampweek4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.util.HashMap;
import java.util.Random;


public class GachaFragment extends Fragment {
    private View view;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseRef;
    private TextView tv_gachaPoint;
    private LottieAnimationView lottie_fishing,lottie_fish,lottie_conffeti,btn_fishing,btn_pop;
    private RotatingTextWrapper textWrapper;

    private String TAG = "GachaFragTAG";
    public GachaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_gacha, container, false);

        //firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("MadCampWeek4/UserAccount/"+userId);

        lottie_fishing = view.findViewById(R.id.lottie_fishing);
        lottie_fish = view.findViewById(R.id.lottie_fish);
        lottie_conffeti= view.findViewById(R.id.lottie_conffeti);
        btn_fishing = view.findViewById(R.id.btn_fishing);
        tv_gachaPoint = view.findViewById(R.id.tv_gachaPoint);
        btn_pop=view.findViewById(R.id.btn_pop);
        textWrapper=view.findViewById(R.id.textWrapper);

        tv_gachaPoint.setText(String.valueOf(Login.getPoints()));

        textWrapper.setSize(30);
        Rotatable rotatable = new Rotatable(Color.parseColor("#539ae0"),1700,
                "Jellyfish","Turtle","Shark","Whale","Blow","Tuna","Chub","Loach","Catfish","Salmon");
        rotatable.setSize(30);
        rotatable.setAnimationDuration(500);

        textWrapper.setContent("300 coins for ?",rotatable);



        btn_fishing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(Login.getPoints()<300){
                    Toast.makeText(getContext(),"Not Enough Points",Toast.LENGTH_SHORT).show();
                    return false;
                }
                Login.setPoints(Login.getPoints()-300);
                tv_gachaPoint.setText(String.valueOf(Login.getPoints()));


                HashMap<String,Object> map = new HashMap<>();
                map.put("points",Login.getPoints());
                databaseRef.updateChildren(map);

                btn_pop.setAnimation(R.raw.button_pop);
                btn_pop.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        btn_fishing.setVisibility(View.GONE);

                        textWrapper.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_pop.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                lottie_fishing.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickFish();
                        lottie_fish.playAnimation();
                        lottie_conffeti.playAnimation();
                    }
                },4600);
                return true;
            }
        });
        btn_fishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Long Click for Gacha",Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }

    private void pickFish(){
        Random random = new Random();
        int num = random.nextInt(Fish.getMaxFish());
        Fish.getOwn().set(num,true);
        Log.d(TAG, "num: "+String.valueOf(num)+" "+Fish.getOwn().toString());
        databaseRef.child("fish").setValue(Fish.getOwn());

        if(num==0) lottie_fish.setAnimation(R.raw.cutefish);
        else if(num==1) lottie_fish.setAnimation(R.raw.fish_shark);
        else if(num==2) lottie_fish.setAnimation(R.raw.fish_spin_jelly);
        else if(num==3) lottie_fish.setAnimation(R.raw.fish_turtle);
        else if(num==4) lottie_fish.setAnimation(R.raw.fish_whale);
        else if(num==5) lottie_fish.setAnimation(R.raw.fish_balloon);
        else if(num==6) lottie_fish.setAnimation(R.raw.fish_blue);

    }
}
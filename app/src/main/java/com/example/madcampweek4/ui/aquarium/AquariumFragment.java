package com.example.madcampweek4.ui.aquarium;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.madcampweek4.Display_Fish;
import com.airbnb.lottie.LottieAnimationView;
import com.example.madcampweek4.Fish;
import com.example.madcampweek4.R;

import java.util.ArrayList;
import java.util.Random;


public class AquariumFragment extends Fragment {
    private  View view;
    private ImageView iv_fish,iv_cutefish,iv_fishShark,iv_fishSpinJelly,iv_fishTurtle,iv_fishWhale
            ,iv_fishBalloon,iv_fishBlue;
    private String TAG = "AquaTAG";

    private Boolean touched = false;
    private int touchedX,touchedY;

    private LottieAnimationView lottie_powder;

    int width,height,min_height;

    public AquariumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aquarium, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        min_height = height/4;

        iv_fish=view.findViewById(R.id.iv_fish);
        iv_cutefish= view.findViewById(R.id.iv_cuteFish);
        iv_fishShark = view.findViewById(R.id.iv_fishShark);
        iv_fishSpinJelly=view.findViewById(R.id.iv_fishSpinJelly);
        iv_fishTurtle=view.findViewById(R.id.iv_fishTurtle);
        iv_fishWhale=view.findViewById(R.id.iv_fishWhale);
        iv_fishBalloon=view.findViewById(R.id.iv_fishBalloon);
        iv_fishBlue=view.findViewById(R.id.iv_fishBlue);
        lottie_powder=view.findViewById(R.id.lottie_powder);



        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;

                float x =event.getX();
                float y = event.getY();

                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "x : "+String.valueOf(x)+"  y: "+String.valueOf(y));
                        touchedX = (int) x;
                        touchedY = (int) y;
                        touched=true;
                        lottie_powder.setX(x-140);
                        lottie_powder.setY(y-160);

                        lottie_powder.setVisibility(View.VISIBLE);
                        ret =true;
                        break;
                    case MotionEvent.ACTION_UP:
                        touched=false;
                        lottie_powder.setVisibility(View.GONE);
                        ret=true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ret=true;
                        break;
                }


                return ret;
            }
        });


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    randomFishMove(iv_fish,0,0,1);
                    ownFishMove();

                }
            });


        return view;
    }



    private void randomFishMove(ImageView iv,int currentX,int currentY,int head){
        int[] location = new int[2] ;
        int deltaX;
        int durationTime;
        TranslateAnimation anim;
        iv.getLocationOnScreen(location);

        Random r = new Random();
        int translationX = r.nextInt(width);
        int translationY = min_height+r.nextInt(height-min_height);

        int toX,toY;

        if(!touched){
            toX=translationX-location[0];
            toY=translationY-location[1];
            deltaX = (translationX-location[0]-currentX)*head;
            durationTime = 3000+r.nextInt(4000);
        }else{
            toX=touchedX-location[0];
            toY=touchedY-location[1];
            deltaX = (touchedX-location[0]-currentX)*head;
            durationTime = 2000+r.nextInt(2000);
        }
        anim = new TranslateAnimation(currentX,toX,currentY,toY);

        if(deltaX<0)
            iv.setScaleX(-1f);
        else
            iv.setScaleX(1f);

        iv.startAnimation(anim);

        anim.setDuration(durationTime);
        anim.setFillAfter(true);


        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    randomFishMove(iv, toX, toY,head);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void ownFishMove(){
        ArrayList<Boolean> ownFish = Fish.getOwn();
        ArrayList<Boolean> displayFish= Display_Fish.getOwn();
        Log.d("아쿠아리움", displayFish.toString());

        if(ownFish.get(0)&&displayFish.get(0)) randomFishMove(iv_cutefish,0,0,-1);
        else iv_cutefish.setVisibility(View.GONE);
        if(ownFish.get(1)&&displayFish.get(1)) randomFishMove(iv_fishShark,0,0,-1);
        else iv_fishShark.setVisibility(View.GONE);
        if(ownFish.get(2)&&displayFish.get(2)) randomFishMove(iv_fishSpinJelly,0,0,1);
        else iv_fishSpinJelly.setVisibility(View.GONE);
        if(ownFish.get(3)&&displayFish.get(3)) randomFishMove(iv_fishTurtle,0,0,1);
        else iv_fishTurtle.setVisibility(View.GONE);
        if(ownFish.get(4)&&displayFish.get(4)) randomFishMove(iv_fishWhale,0,0,1);
        else iv_fishWhale.setVisibility(View.GONE);
        if(ownFish.get(5)&&displayFish.get(5)) randomFishMove(iv_fishBalloon,0,0,-1);
        else iv_fishBalloon.setVisibility(View.GONE);
        if(ownFish.get(6)&&displayFish.get(6))  randomFishMove(iv_fishBlue,0,0,1);
        else iv_fishBlue.setVisibility(View.GONE);

    }
}
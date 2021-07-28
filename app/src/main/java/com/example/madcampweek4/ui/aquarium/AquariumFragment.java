package com.example.madcampweek4.ui.aquarium;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.madcampweek4.R;

import java.util.Random;


public class AquariumFragment extends Fragment {
    private  View view;
    private ImageView iv_fish;
    private String TAG = "AquaTAG";
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
        //Animation fishAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fish_move);




        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    int[] location = new int[2] ;
                    iv_fish.getLocationOnScreen(location);
                    randomFishMove(iv_fish,0,0,0);
                    //testMove(iv_fish,location[0], location[1]);

                }
            });

        //iv_fish.startAnimation(fishAnimation);


        return view;
    }

    private void randomFishMove(ImageView iv,int currentX,int currentY,int degree){

        Log.d(TAG, "width:" +String.valueOf(width)+"  height: "+String.valueOf(height));
        int[] location = new int[2] ;
        iv.getLocationOnScreen(location);


        Random r = new Random();
        int translationX = r.nextInt(width);
        int translationY = min_height+r.nextInt(height-min_height);
        Log.d(TAG, "x: "+String.valueOf(location[0])+"  y: "+String.valueOf(location[1]));
        Log.d(TAG, "tx: "+String.valueOf(translationX)+"  ty: "+String.valueOf(translationY));
        Log.d(TAG, "mx: "+String.valueOf(translationX-location[0])+"  my: "+String.valueOf(translationY-location[1]));


        TranslateAnimation anim = new TranslateAnimation(currentX,translationX-location[0],currentY,translationY-location[1]);

        int deltaX = (translationX-location[0]-currentX);
        int deltaY = -(translationY-location[1]-currentY);
        double head=Math.toDegrees(Math.atan((double)deltaX/deltaY));
        Log.d(TAG, "dx: "+String.valueOf(deltaX)+"  dy: "+String.valueOf(deltaY));
        Log.d(TAG, " int head: "+String.valueOf((int)head));
        RotateAnimation rotateAnimation = new RotateAnimation(degree,90);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        anim.setDuration(5000);
        anim.setFillAfter(true);
        iv.startAnimation(rotateAnimation);
        iv.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                randomFishMove(iv, translationX-location[0], translationY-location[1],(int)head);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
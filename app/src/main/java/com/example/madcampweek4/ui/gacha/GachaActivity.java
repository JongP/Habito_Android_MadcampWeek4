package com.example.madcampweek4.ui.gacha;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.madcampweek4.R;
import java.util.ArrayList;
import java.util.Random;


public class GachaActivity extends AppCompatActivity {
    //Lottie 없다고 가정
    ArrayList<Integer> fishnum;
    Button btn_gacha;
    ImageView iv_gachaimg;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacha);

        btn_gacha=findViewById(R.id.btn_gacha);
        iv_gachaimg=findViewById(R.id.iv_gachaimg);
        btn_gacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randnum=new Random().nextInt(2);
                randnum+=1;
                String fishy="fish"+randnum;
                Context context = iv_gachaimg.getContext();
                int id = context.getResources().getIdentifier(fishy, "drawable", context.getPackageName());
                iv_gachaimg.setImageResource(id);

            }
        });


    }

}

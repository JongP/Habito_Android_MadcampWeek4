package com.example.madcampweek4.ui.slideshow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madcampweek4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class SlideshowFragment extends Fragment {

    public String fname=null;
    public String str=null;
    public MaterialCalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    String name, userID;

    //앞에서 받아오기~
    String[] result= {"2021,07,18","2021,07,20","2021,08,05","2021,08,18", "2021,08,19", "2021,08,20", "2021,08,28", "2021,08,29"};
    double[] result_ratio={0.0,0.2,0.3,0.5, 0.6,0.78,0.9, 1.0};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_slideshow, container, false);

        calendarView=view.findViewById(R.id.calendarView);
        diaryTextView=view.findViewById(R.id.diaryTextView);

        save_Btn=view.findViewById(R.id.save_Btn);
        del_Btn=view.findViewById(R.id.del_Btn);
        cha_Btn=view.findViewById(R.id.cha_Btn);

        textView2=view.findViewById(R.id.textView2);
        textView3=view.findViewById(R.id.textView3);

        contextEditText=view.findViewById(R.id.contextEditText);

        // firebase
        mFirebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        userID=firebaseUser.getUid();
        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    name=String.valueOf(task.getResult().getValue());
                    textView3.setText(name+"님의 달력 일기장");
                }
            }
        });


        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator());
        calendarView.addDecorator(new MySelectorDecorator(getActivity()));

        OneDayDecorator oneDayDecorator=new OneDayDecorator();
        calendarView.addDecorators(oneDayDecorator);


        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.INVISIBLE);
//                cha_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
//                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
//                contextEditText.setText("");
//                checkDay(year,month,dayOfMonth,userID);
//            }
//        });
//
//        save_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveDiary(fname);
//                str=contextEditText.getText().toString();
//                textView2.setText(str);
//                save_Btn.setVisibility(View.INVISIBLE);
//                cha_Btn.setVisibility(View.VISIBLE);
//                del_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.INVISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//
//            }
//        });
        return view;
    }

    class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            // string 문자열인 Time_Result 을 받아와서 ,를 기준으로 자르고 string을 int 로 변환

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for(int i = 0 ; i < Time_Result.length+1 ; i++){
                CalendarDay day = CalendarDay.from(calendar);
                if (i>=0 && i<Time_Result.length){
                    Log.d("고른 날짜들 ", Time_Result[i]);
                    String[] time = Time_Result[i].split(",");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);
                    calendar.set(year,month-1,dayy);
                    Log.d("고른 날짜22 ", day.toString());
                }
                if (i>0) {
                    dates.add(day);
                }
            }

            for(int i = 0 ; i < dates.size() ; i ++){
                Log.d("고른 날짜들33 ", dates.get(i).toString());
            }
            return dates;
        }
        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            calendarView.addDecorators(new EventDecorator0_4(Color.GREEN, calendarDays, result_ratio, getActivity()),
                    new EventDecorator1_4(Color.GREEN, calendarDays, result_ratio, getActivity()),
                    new EventDecorator2_4(Color.GREEN, calendarDays, result_ratio, getActivity()),
                    new EventDecorator3_4(Color.GREEN, calendarDays, result_ratio, getActivity()),
                    new EventDecorator4_4(Color.GREEN, calendarDays, result_ratio, getActivity()),
                    new EventDecorator5_4(Color.GREEN, calendarDays, result_ratio, getActivity()));
            Log.d("필터 하려구 숫자", result_ratio.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
        FileInputStream fis=null;//FileStream fis 변수

        try{
            fis=getContext().openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(fname);
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=getContext().openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=getContext().openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


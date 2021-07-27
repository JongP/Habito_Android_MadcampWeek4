package com.example.madcampweek4.ui.calendar;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.madcampweek4.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator0_4 implements DayViewDecorator {

    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates=new HashSet<>();
    private double[] result_ratio;
    //{0.0,0.2,0.3,0.5, 0.6,0.78,1.0};

    public EventDecorator0_4(int color, Collection<CalendarDay> dates, double[] result_ratio, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.more0_4);
        this.color = color;
        //this.dates = new HashSet<>(dates);
        this.result_ratio=result_ratio;
        for (int i=0;i<dates.size();i++){
            if (result_ratio[i]==0){
                Log.d("필터 성공 ", dates.toArray()[i].toString()+" "+result_ratio[i]+" "+i);
                this.dates.add((CalendarDay) dates.toArray()[i]);
            }
        }
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new DotSpan(5, color)); // 날자밑에 점
    }
}
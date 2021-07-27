package com.example.madcampweek4.ui.slideshow;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.madcampweek4.R;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class OneDayDecorator implements DayViewDecorator {
    private CalendarDay date;
    private int color;

    public OneDayDecorator(int color){
        this.date=CalendarDay.today();
        this.color=color;
    }
    public boolean shouldDecorate(CalendarDay day){
        return day.equals(date);
    }
    public void decorate(DayViewFacade view){
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(color));
    }
    public void setDate(Date date){
        this.date=CalendarDay.from(date);
    }
}

package com.example.madcampweek4.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.madcampweek4.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class MySelectorDecorator implements DayViewDecorator {
    private final Drawable drawable;
    public MySelectorDecorator(Activity context){
        drawable=context.getResources().getDrawable(R.drawable.calendar_my_selector);
    }
    public boolean shouldDecorate(CalendarDay day){
        return true;
    }
    @SuppressLint("ResourceAsColor")
    public void decorate(DayViewFacade view){
        view.addSpan(new ForegroundColorSpan(R.color.black));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.2f));
        view.setSelectionDrawable(drawable);
    }
}

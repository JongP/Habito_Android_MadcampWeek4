package com.example.madcampweek4.ui.slideshow;

import android.app.Activity;
import android.graphics.drawable.Drawable;

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
    public void decorate(DayViewFacade view){
        view.setSelectionDrawable(drawable);
    }
}

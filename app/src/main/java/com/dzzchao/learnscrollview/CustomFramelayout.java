package com.dzzchao.learnscrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomFramelayout extends FrameLayout {

    public CustomFramelayout(@NonNull Context context) {
        super(context);
    }

    public CustomFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("CustomFramelayout", "dispatchTouchEvent()" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("CustomFramelayout", "onInterceptTouchEvent()");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("CustomFramelayout", "onTouchEvent()" + event.getAction());
        Log.i("CustomFramelayout", Boolean.toString(super.onTouchEvent(event)));
        return super.onTouchEvent(event);
    }
}

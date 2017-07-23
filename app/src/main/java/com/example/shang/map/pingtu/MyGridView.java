package com.example.shang.map.pingtu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by shang on 2017/7/21.
 */

// 自定义GridView，实现GridView不能上下滑动
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }
}

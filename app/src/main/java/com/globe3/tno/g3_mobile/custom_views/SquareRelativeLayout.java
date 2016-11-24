package com.globe3.tno.g3_mobile.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquareRelativeLayout extends LinearLayout {
    public SquareRelativeLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }
}

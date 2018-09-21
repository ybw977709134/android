package com.onemeter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 描述：自定义textView
 * 时间：2016/4/6 15:49
 * 备注：
 */
public class AlwaysMarqueeTextView  extends TextView {

    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

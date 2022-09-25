package com.food.app.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomSwipeImageView extends androidx.appcompat.widget.AppCompatImageView {
    DialogFragment relativeFragment;

    public CustomSwipeImageView(@NonNull Context context) {
        super(context);
    }
    public CustomSwipeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomSwipeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                performClick();
                return true;
            case MotionEvent.ACTION_UP:
                return false;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if ( relativeFragment != null ) {
            relativeFragment.dismiss();
        }
        return true;
    }

    public void setRelativeFragment(DialogFragment relativeFragment) {
        this.relativeFragment = relativeFragment;
    }

}
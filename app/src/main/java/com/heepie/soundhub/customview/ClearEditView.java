package com.heepie.soundhub.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.heepie.soundhub.R;


/**
 * Created by eunmin on 2017-11-27.
 */

public class ClearEditView extends AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener{

    private Drawable clearDrawable;
    private OnTouchListener onTouchListener;
    private OnFocusChangeListener onFocusChangeListener;

    public ClearEditView(Context context) {
        super(context);
        init();
    }

    public ClearEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable temp = ContextCompat.getDrawable(getContext(), R.drawable.ximage);
        clearDrawable = DrawableCompat.wrap(temp);
        DrawableCompat.setTintList(clearDrawable, getHintTextColors());
        clearDrawable.setBounds(0, 0, 30, 30);
        setClearIconVisible(false);

        addTextChangedListener(this);
        super.setOnFocusChangeListener(this);
        super.setOnTouchListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);
        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }



    //  ======================================================================================================TouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        if(clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - 30) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        } else {
            return false;
        }
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    @Override
    public void setOnFocusChangeListener( OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }



    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }


    //  ========================================================================================================= TextWatcher

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
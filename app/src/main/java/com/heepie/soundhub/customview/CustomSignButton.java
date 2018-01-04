package com.heepie.soundhub.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heepie.soundhub.R;

/**
 * Created by Heepie on 2017. 12. 4..
 */

public class CustomSignButton extends LinearLayout {
    LinearLayout background;
    ImageView imgView;
    TextView text;

    public CustomSignButton(Context context) {
        super(context);
        initView();
    }

    public CustomSignButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomSignButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_sign_btn, this, false);

        addView(view);

        background = findViewById(R.id.background);
        imgView = findViewById(R.id.imgView);
        text = findViewById(R.id.text);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SignBtn);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        // 3번째 인자는 해당 View의 스타일, 4번째 인자는 Style에 대한 Default 값
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SignBtn, defStyle, 0);
        setTypeArray(typedArray);
    }


    // Attribute를 해당 View에 설정하는 메소드
    private void setTypeArray(TypedArray typedArray) {
        // 1. typeArray에 전달 받은 값을 꺼낸다. (2번째 인자는 디폴드 값)
        int bg_resId = typedArray.getResourceId(R.styleable.SignBtn_bg, R.color.com_facebook_blue);

        // 2. 꺼낸 값 설정
        background.setBackgroundResource(bg_resId);

        int imgView_resId = typedArray.getResourceId(R.styleable.SignBtn_imgSrc, R.drawable.email);
        imgView.setImageResource(imgView_resId);

        int textColor = typedArray.getColor(R.styleable.SignBtn_textColor, 0);
        text.setTextColor(textColor);

        String text_value = typedArray.getString(R.styleable.SignBtn_text);
        text.setText(text_value);

        // typedArray를 재사용하기 위한 설정
        typedArray.recycle();
    }

    void setBg(int bg_resID) { background.setBackgroundResource(bg_resID); }

    void setImgView(int symbol_resID) { imgView.setImageResource(symbol_resID); }

    void setTextColor(int color) { text.setTextColor(color); }

    void setText(String text_string) { text.setText(text_string); }

    void setText(int text_resID) { text.setText(text_resID); }

}
package com.example.zhoumohan.ancient.demo;

import android.os.Bundle;

import com.example.zhoumohan.ancient.base.BaseActivity;
import com.example.zhoumohan.ancient.common.widget.BookPageView;

public class ReadPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new BookPageView(this));
    }
}

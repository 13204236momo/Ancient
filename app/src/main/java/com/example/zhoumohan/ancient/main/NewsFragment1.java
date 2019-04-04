package com.example.zhoumohan.ancient.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zhoumohan.ancient.R;
import com.example.zhoumohan.ancient.base.BaseFragment;
import com.example.zhoumohan.ancient.common.widget.SplashView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment1 extends BaseFragment {

    @BindView(R.id.btn_success)
    Button btnSuccess;
    @BindView(R.id.splash)
    SplashView splashView;
    @Override
    protected int getLayoutResID() {
        return R.layout.item_vp1;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashView.setLoadingEnd();
            }
        });
    }

}

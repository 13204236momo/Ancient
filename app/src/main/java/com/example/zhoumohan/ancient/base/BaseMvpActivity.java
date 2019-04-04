package com.example.zhoumohan.ancient.base;

import android.os.Bundle;

import com.example.zhoumohan.ancient.base.mvp.BaseModel;
import com.example.zhoumohan.ancient.base.mvp.BasePresenter;
import com.example.zhoumohan.ancient.base.mvp.BaseView;
import com.example.zhoumohan.ancient.base.mvp.TUtil;


public abstract class BaseMvpActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity {
    public T mPresenter;
    public E mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        init();
    }

    protected void init() {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) mPresenter.setVM(this, mModel);
    }

}


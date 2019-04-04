package com.example.zhoumohan.ancient.community;

import com.example.zhoumohan.ancient.base.mvp.BaseModel;
import com.example.zhoumohan.ancient.base.mvp.BasePresenter;
import com.example.zhoumohan.ancient.base.mvp.BaseView;

public interface CommunityContract {
    interface View extends BaseView {
        void upDate();

    }

    interface Model extends BaseModel {

        void upDate();

    }

    abstract class Presenter extends BasePresenter<Model,View> {

    }
}

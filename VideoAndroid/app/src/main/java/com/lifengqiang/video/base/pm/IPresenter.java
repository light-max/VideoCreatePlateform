package com.lifengqiang.video.base.pm;

import com.lifengqiang.video.base.call.Base;

public interface IPresenter<View extends IView<?>> extends Base {
    View getIView();
}

package com.zptioning.lib_api;

import android.content.Context;

/**
 * Created by hjy on 2018/10/23.
 */

public interface IAppLike {

    int MAX_PRIORITY = 10;
    int NORM_PRIORITY = 5;
    int MIN_PRIORITY = 1;

    int getPriority();

    void onCreate(Context context);

    void onTerminate();

}
package com.zptioning.gradledemo1;

import android.content.Context;
import android.util.Log;

import com.zptioning.lib_annotation.AppLifeCycle;
import com.zptioning.lib_api.IAppLike;


/**
 * Created by hjy on 2018/10/23.
 */
@AppLifeCycle
public class ModuleAAppLike implements IAppLike {

    @Override
    public int getPriority() {
        return NORM_PRIORITY;
    }

    @Override
    public void onCreate(Context context) {
        Log.d("AppLike", "onCreate(): this is in ModuleAAppLike.");
    }

    @Override
    public void onTerminate() {
        Log.d("AppLike", "onTerminate(): this is in ModuleAAppLike.");
    }
}

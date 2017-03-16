package com.sanat.birthdayreminder;

import android.app.Application;
import android.content.Context;
import android.view.View;

/**
 * Created by healthcaremagic on 3/5/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mSelf;

    public MyApplication() {
        mSelf = this;
    }

    public static MyApplication getInstance() {
        return mSelf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void image(View view) {
        String test = "";
    }

    @Override
    protected void attachBaseContext(Context context) {

    }
}

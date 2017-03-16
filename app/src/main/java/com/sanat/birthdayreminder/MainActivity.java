package com.sanat.birthdayreminder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.sanat.birthdayreminder.firendList.FriendListActivity;
import com.sanat.birthdayreminder.sycnFacebook.FacebookUtils;

public class MainActivity extends AppCompatActivity {
    public static CallbackManager mCallbackManager;
    MyDataBase dataBase;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        dataBase = new MyDataBase(this);
        if(dataBase.getFrndListCount() == 0){
            progressBar.setVisibility(View.VISIBLE);
            new FacebookUtils(this,progressBar);
        }
        else{
            startActivity(new Intent(getApplicationContext(), FriendListActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (mCallbackManager != null) {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
        catch (Exception e){
            e.fillInStackTrace();
        }
    }
}

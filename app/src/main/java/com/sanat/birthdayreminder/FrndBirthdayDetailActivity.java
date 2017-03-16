package com.sanat.birthdayreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanat.birthdayreminder.firendList.FriendModel;
import com.sanat.birthdayreminder.utils.CommonUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by healthcaremagic on 3/5/2017.
 */

public class FrndBirthdayDetailActivity extends AppCompatActivity {
    Toolbar mToolbar;
    public static FriendModel model;
    ImageView profile;
    TextView actionTV,nameTV,birthdayTV;
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.birthday_detail);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            actionTV = (TextView)findViewById(R.id.actionTV);
            nameTV = (TextView)findViewById(R.id.nameTV);
            birthdayTV = (TextView)findViewById(R.id.birthdayTV);
            profile = (ImageView)findViewById(R.id.profile);
            Picasso.with(this).load("https://graph.facebook.com/"+model.getId()+"/picture?width=1000&height=1000").into(profile);
            nameTV.setText(model.getName());
            birthdayTV.setText(model.getdisplayDateOfBirth());
            if((CommonUtils.getSplitedStringByComma(model.getActualFacebookDate()).get(0) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(0)) && (CommonUtils.getSplitedStringByComma(model.getActualFacebookDate()).get(1) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(1))) {
                actionTV.setText("Send Wishes");
            }
            else{
                actionTV.setText("Send Advance Email");
            }
            actionTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if((CommonUtils.getSplitedStringByComma(model.getActualFacebookDate()).get(0) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(0)) && (CommonUtils.getSplitedStringByComma(model.getActualFacebookDate()).get(1) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(1))) {
                            //share
                        }
                        else{
                            EmailActivity.model = model;
                            startActivity(new Intent(getApplicationContext(),EmailActivity.class));
                        }
                    }
                    catch (Exception e){
                        e.fillInStackTrace();
                    }
                }
            });
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(model.getName());

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

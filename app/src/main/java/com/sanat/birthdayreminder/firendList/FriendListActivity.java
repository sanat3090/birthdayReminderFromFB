package com.sanat.birthdayreminder.firendList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanat.birthdayreminder.MainActivity;
import com.sanat.birthdayreminder.MyDataBase;
import com.sanat.birthdayreminder.R;
import com.sanat.birthdayreminder.utils.CommonUtils;
import com.sanat.birthdayreminder.utils.Constants;

/**
 * Created by healthcaremagic on 3/3/2017.
 */

public class FriendListActivity extends AppCompatActivity {
    Toolbar mToolbar;
    ListView listView;
    FriendListAdapter adapter;
    MyDataBase dataBase;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.freind_list);
            dataBase = new MyDataBase(this);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setTitle("BirthDay Bash");
            listView = (ListView) findViewById(R.id.listView);
            FloatingActionButton rightActionTV = (FloatingActionButton)findViewById(R.id.floatingActionButtonList);
            rightActionTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        commonAlert(FriendListActivity.this,"Do you want to Sync your facebook friend list?");
                    }
                    catch (Exception e){
                        e.fillInStackTrace();
                    }
                }
            });
            adapter = new FriendListAdapter(this, this, R.layout.freind_birthday__row, FriendModel.ITEMS);
            listView.setAdapter(adapter);
            if (adapter != null && FriendListAdapter.filteredList != null && FriendListAdapter.filteredList.size() > 0) {
                adapter.notifyDataSetChanged();
            }
            else if(dataBase.getFrndListCount() == 0){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else{
                FriendListAdapter.filteredList.clear();
                FriendModel.ITEMS.clear();
                FriendListAdapter.filteredList = dataBase.getFrndList();
                FriendModel.ITEMS.addAll(FriendListAdapter.filteredList);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    public void commonAlert(final Activity context, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(message);
        dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBase.deleteAllFrndList();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        dialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

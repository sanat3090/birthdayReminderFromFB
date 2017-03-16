package com.sanat.birthdayreminder.sycnFacebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sanat.birthdayreminder.MainActivity;
import com.sanat.birthdayreminder.MyDataBase;
import com.sanat.birthdayreminder.firendList.FriendListActivity;
import com.sanat.birthdayreminder.firendList.FriendModel;
import com.sanat.birthdayreminder.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by healthcaremagic on 3/3/2017.
 */

public class FacebookUtils {
    String afterCursor;
    Activity activity;
    MyDataBase dataBase;
    ProgressBar progressBar;
    public FacebookUtils(final Activity activity, ProgressBar progressBar) {
        try {
            dataBase = new MyDataBase(activity);
            this.activity = activity;
            this.progressBar = progressBar;
            afterCursor = "";
            FacebookSdk.sdkInitialize(activity);
            MainActivity.mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(MainActivity.mCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            try {
                                Log.d("Success", "Login");
                                getFriends(activity);
                            } catch (Exception e) {
                                e.fillInStackTrace();
                            }
                        }

                        @Override
                        public void onCancel() {
                            try {
                                Log.d("Success", "Login");
                                if (AccessToken.getCurrentAccessToken() != null){
                                    getFriends(activity);
                                }
                            } catch (Exception e) {
                                e.fillInStackTrace();
                            }
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            try {
                                if (exception instanceof FacebookAuthorizationException) {
                                    if (AccessToken.getCurrentAccessToken() != null) {
                                        LoginManager.getInstance().logOut();
                                        CommonUtils.message(activity, "User logged in as different Facebook user, Please do the same exercise again.");
                                    }
                                }
                            }
                            catch (Exception e){
                                e.fillInStackTrace();
                            }
                        }
                    });
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("user_friends,user_birthday"));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void getFriends(Activity activity) {
        try {
            Bundle params = new Bundle();
            params.putString("fields", "name,birthday,picture");
            if(CommonUtils.isNotNull(afterCursor)){
                params.putString("after", afterCursor);
                afterCursor = "";
            }
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + AccessToken.getCurrentAccessToken().getUserId() + "/friends",
                    params,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                if (response.getError() == null) {
                                    JSONObject joMain = response.getJSONObject();
                                    renderFrnds(joMain);
                                }
                            } catch (Exception e) {
                                e.fillInStackTrace();
                            }
                        }
                    }
            ).executeAsync();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void renderFrnds(JSONObject joMain) {
        try{
            JSONArray data = joMain.getJSONArray("data");
            for(int i = 0;i<data.length();i++){
                JSONObject rowObj = data.getJSONObject(i);
                if(rowObj.has("birthday")){
                    String name = rowObj.has("name")?rowObj.getString("name"):"";
                    String birthday = rowObj.has("birthday")?rowObj.getString("birthday"):"";
                    String id = rowObj.has("id")?rowObj.getString("id"):"";
                    String picture = "";
                    if(rowObj.has("picture")){
                        JSONObject pictureObj = rowObj.getJSONObject("picture");
                        if(pictureObj.has("data")){
                            JSONObject pictureDataObj = pictureObj.getJSONObject("data");
                            if(pictureDataObj.has("url")){
                                picture = pictureDataObj.has("url")?pictureDataObj.getString("url"):"";
                            }
                        }
                    }
                    long a = dataBase.insertInFrndTable(name,id,birthday,picture,CommonUtils.getDateStringFromMillis(CommonUtils.getDateInLong(birthday)),CommonUtils.getDateInLong(birthday)+"");
                    String test = "";
                }
            }
            if(joMain.has("paging")){
                JSONObject paggingObj = joMain.getJSONObject("paging");
                if(paggingObj.has("cursors")){
                    JSONObject cursorsObj = paggingObj.getJSONObject("cursors");
                    afterCursor = cursorsObj.getString("after");
                }
            }
            if(CommonUtils.isNotNull(afterCursor)){
                getFriends(activity);
            }
            else if(dataBase.getFrndListCount()>0){
                progressBar.setVisibility(View.GONE);
                activity.startActivity(new Intent(activity, FriendListActivity.class));
            }
        }
        catch (Exception e){
            e.fillInStackTrace();
        }
    }
}

package com.sanat.birthdayreminder.firendList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanat.birthdayreminder.FrndBirthdayDetailActivity;
import com.sanat.birthdayreminder.R;
import com.sanat.birthdayreminder.utils.CircleTransform;
import com.sanat.birthdayreminder.utils.CommonShare;
import com.sanat.birthdayreminder.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by healthcaremagic on 3/3/2017.
 */

public class FriendListAdapter extends ArrayAdapter<FriendModel> {

    //ViewHolder object
    ViewHolderItem viewHolder;
    FriendModel data;
    public static ArrayList<FriendModel> filteredList = null;
    Activity activity;
    Context context;
    //DisplayImageOptions options;
    public FriendListAdapter(Activity activity,Context context, int textViewResourceId, ArrayList<FriendModel> items) {
        super(context, textViewResourceId, items);
        filteredList = new ArrayList<FriendModel>(items);
        data = new FriendModel();
        this.activity=activity;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void clear() {
        super.clear();
        filteredList.clear();
    }

    @Override
    public FriendModel getItem(int position) {
        return filteredList.get(position);}

    View previousRow;

    @Override
    public void addAll(Collection<? extends FriendModel> collection) {
        this.filteredList = new ArrayList<FriendModel>(collection);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {
            data = filteredList.get(position);
            viewHolder = new ViewHolderItem();
            if (row == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.freind_birthday__row, null);
                viewHolder.nameTV = (TextView) row.findViewById(R.id.nameTV);
                viewHolder.dateTV = (TextView) row.findViewById(R.id.dateTV);
                viewHolder.actionTV = (TextView) row.findViewById(R.id.actionTV);
                viewHolder.dayCountTV = (TextView) row.findViewById(R.id.dayCountTV);
                viewHolder.ribbonTV = (TextView) row.findViewById(R.id.ribbonTV);
                viewHolder.profileTV = (ImageView) row.findViewById(R.id.imageView);
                row.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolderItem) row.getTag();
            }
        /*
           step 1:converting Resource Image to the Bitmap
           step 2:resizing the Bitmap image to 56*56
           step 3:Rounding the image by calling the RoundImage UserDefined class
           step 4:setting Rounded image to the imageView
         */
            if (data != null) {
                viewHolder.nameTV.setText(data.getName());
                viewHolder.dateTV.setText(data.getdisplayDateOfBirth());
                viewHolder.dayCountTV.setVisibility(View.VISIBLE);
                viewHolder.actionTV.setVisibility(View.GONE);
                if((CommonUtils.getSplitedStringByComma(data.getActualFacebookDate()).get(0) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(0)) && (CommonUtils.getSplitedStringByComma(data.getActualFacebookDate()).get(1) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(1))) {
                    viewHolder.dayCountTV.setBackgroundResource(R.drawable.com_facebook_button_icon);
                    viewHolder.ribbonTV.setText("Today");
                    viewHolder.actionTV.setText("Send Wishes");
                }
                else{
                    viewHolder.ribbonTV.setText(data.getdisplayDateOfBirth());
                    viewHolder.dayCountTV.setVisibility(View.GONE);
                    viewHolder.actionTV.setText("Send advance Email");
                }
                Picasso.with(activity).load("https://graph.facebook.com/"+data.getId()+"/picture?width=200&height=200").transform(new CircleTransform()).resize(100, 100).error(R.drawable.user).placeholder(R.drawable.user).into(viewHolder.profileTV);
                viewHolder.actionTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            if((CommonUtils.getSplitedStringByComma(data.getActualFacebookDate()).get(0) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(0)) && (CommonUtils.getSplitedStringByComma(data.getActualFacebookDate()).get(1) == CommonUtils.getSplitedStringByComma(CommonUtils.getTodayDate()).get(1))) {
                                CommonShare.getInstance().launchShareText(activity, activity);
                            }
                            else{
                                activity.startActivity(new Intent(activity,FrndBirthdayDetailActivity.class));
                            }
                        }
                        catch (Exception e){
                            e.fillInStackTrace();
                        }
                    }
                });

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            FrndBirthdayDetailActivity.model = filteredList.get(position);
                            activity.startActivity(new Intent(activity,FrndBirthdayDetailActivity.class));
                        }
                        catch (Exception e){
                            e.fillInStackTrace();
                        }
                    }
                });
            }
            if (row != null)
                previousRow = row;
            if (row == null && previousRow != null) {
                row = previousRow;
            }
        }
        catch (Exception e){
            e.fillInStackTrace();
        }
        return row;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolderItem {
        TextView nameTV,dateTV,actionTV,dayCountTV,ribbonTV;
        ImageView profileTV;
    }
}

package com.sanat.birthdayreminder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.sanat.birthdayreminder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by healthcaremagic on 3/5/2017.
 */

public class CommonShare {
    String TAG = "CommonShare";
    private volatile static CommonShare instance;

    protected CommonShare() {
        String test = "";
    }

    public static CommonShare getInstance() {
        try {
            if (instance == null) {
                synchronized (CommonShare.class) {
                    if (instance == null) {
                        instance = new CommonShare();
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return instance;
    }

    public void launchShareText(final Activity activity, final Context context) {
        try {
            Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
            sendIntent.setType("*/*");
            /*String defaultApplication = Settings.Secure.getString(context.getContentResolver(),"sms_default_application");
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);*/
            List<ResolveInfo> activities = activity.getPackageManager().queryIntentActivities(sendIntent, 0);
            final Dialog dialog = new Dialog(activity, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_share_layout);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final List data = new ArrayList();
            for (ResolveInfo info : activities) {
                if (info.activityInfo.packageName.contains("facebook") || info.activityInfo.packageName.contains("mms") || info.activityInfo.packageName.contains("whatsapp") /*|| info.activityInfo.packageName.contains("twitter")*/ || info.activityInfo.packageName.contains("android.email")
                        || info.activityInfo.packageName.contains("com.google.android.gm") || info.activityInfo.packageName.contains("sonyericsson.conversations") || info.activityInfo.packageName.contains("android.mail") || info.activityInfo.packageName.contains("com.google.android.apps.messaging")
                       /*Copy clipboard*/ /*|| info.activityInfo.packageName.contains("apps.docs")*/) {

                    if (/*for sms*/info.activityInfo.name.contains("ConversationListActivity") || info.activityInfo.name.contains("ComposeMessageActivity") || info.activityInfo.name.equalsIgnoreCase("com.google.android.apps.messaging.ui.conversationlist.ShareIntentActivity") || info.activityInfo.name.contains("ComposeMessageRouterActivity") ||
                      /*for gmail,email*/info.activityInfo.name.contains("MessageCompose") || info.activityInfo.name.contains("ComposeActivityGmail")
                          /*for whatsapp*/ || info.activityInfo.name.contains("ContactPicker") ||
                            /*for facebook,messenger*/info.activityInfo.name.contains("ImplicitShareIntentHandlerDefaultAlias") || info.activityInfo.name.contains("ShareIntentHandler")
                            /*Copy clipboard*/ /*|| info.activityInfo.name.contains("SendTextToClipboardActivity")*/) {
                        data.add(info);
                    }
                }
            }
            //to remove duplicate item in list
            /*HashSet hs = new HashSet();
            hs.addAll(data);
            data.clear();
            data.addAll(hs);*/
            if (data != null && data.size() > 0) {
                final CustomShareAdapter adapter = new CustomShareAdapter(activity, data.toArray());
                TextView shareTitleTV = (TextView) dialog.findViewById(R.id.shareTitleTextView);
                shareTitleTV.setText("Choose App");

                GridView gridViewCustomDialog = (GridView) dialog.findViewById(R.id.shareGridView);
                gridViewCustomDialog.setAdapter(adapter);
                dialog.show();

                gridViewCustomDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ResolveInfo info = (ResolveInfo) adapter.getItem(position);
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            if (info.activityInfo.packageName.contains("mms") || info.activityInfo.packageName.contains("sonyericsson.conversations") || info.activityInfo.packageName.contains("com.google.android.apps.messaging")) {
                                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                                intent.putExtra(Intent.EXTRA_TEXT, "Happy Birthday");
                            } else if (info.activityInfo.packageName.contains("facebook")) {
                                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday");
                                intent.putExtra(Intent.EXTRA_TEXT, "Happy Birthday");
                            } else if (info.activityInfo.packageName.contains("whatsapp")) {
                                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                                intent.putExtra(Intent.EXTRA_TEXT, "Happy Birthday");
                            } else if (info.activityInfo.packageName.contains("twitter")) {
                                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                                intent.putExtra(Intent.EXTRA_TEXT, "#Becomeaqueen");
                            } else if (info.activityInfo.packageName.contains("android.email") || info.activityInfo.packageName.contains("android.mail") || info.activityInfo.packageName.contains("com.google.android.gm")) {
                                intent.setType("message/rfc822");
                                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday");
                                intent.putExtra(Intent.EXTRA_TEXT, "Happy Birthday");
                            }
                            context.startActivity(intent);
                            dialog.dismiss();
                            //Log.d("CitySelected",mCityText);
                        } catch (Exception e) {
                            e.fillInStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public class CustomShareAdapter extends BaseAdapter {
        Object[] items;
        private LayoutInflater mInflater;
        Context context;

        public CustomShareAdapter(Context context, Object[] items) {
            this.mInflater = LayoutInflater.from(context);
            this.items = items;
            this.context = context;
        }

        public int getCount() {
            return items.length;
        }

        public Object getItem(int position) {
            return items[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.custom_share_layout_row_item, null);
                    holder = new ViewHolder();
                    holder.name = (TextView) convertView.findViewById(R.id.shareRowItemTextView);
                    holder.logo = (TextView) convertView.findViewById(R.id.shareRowItemImageView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (((ResolveInfo) items[position]).activityInfo.packageName.contains("mms") || ((ResolveInfo) items[position]).activityInfo.packageName.contains("sonyericsson.conversations") || ((ResolveInfo) items[position]).activityInfo.packageName.contains("com.google.android.apps.messaging")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_sms_new, R.color.color_whatsapp));
                    holder.name.setText(context.getResources().getString(R.string.sms));
                } else if (((ResolveInfo) items[position]).activityInfo.packageName.contains("facebook") && ((ResolveInfo) items[position]).activityInfo.name.contains("ImplicitShareIntentHandlerDefaultAlias")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_fb_new, R.color.color_facebook));
                    holder.name.setText(context.getResources().getString(R.string.facebook));
                }else if (((ResolveInfo) items[position]).activityInfo.packageName.contains("facebook") && ((ResolveInfo) items[position]).activityInfo.name.contains("ShareIntentHandler")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_fb_messenger, R.color.color_facebook_messenger));
                    holder.name.setText(context.getResources().getString(R.string.facebook_messenger));
                } else if (((ResolveInfo) items[position]).activityInfo.packageName.contains("whatsapp")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_whatsapp_1, R.color.color_whatsapp));
                    holder.name.setText(context.getResources().getString(R.string.whatsapp));
                } else if (((ResolveInfo) items[position]).activityInfo.packageName.contains("android.email") || ((ResolveInfo) items[position]).activityInfo.packageName.contains("android.mail")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_mail_alt, R.color.color_email));
                    holder.name.setText(context.getResources().getString(R.string.email_text));
                } else if (((ResolveInfo) items[position]).activityInfo.packageName.contains("com.google.android.gm")) {
                    holder.logo.setTypeface(CommonUtils.getLQFontTypeFace(context,holder.logo, R.string.icon_gmail_new, R.color.color_gmail));
                    holder.name.setText(context.getResources().getString(R.string.gmail));
                } else {
                    holder.logo.setCompoundDrawablesWithIntrinsicBounds(null, ((ResolveInfo) items[position]).activityInfo.applicationInfo
                            .loadIcon(context.getPackageManager()), null, null);
                    holder.name.setText(((ResolveInfo) items[position]).activityInfo.applicationInfo
                            .loadLabel(context.getPackageManager()).toString());
                }

            } catch (Exception e) {
                e.fillInStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView logo;
        }
    }
}

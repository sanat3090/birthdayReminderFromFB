package com.sanat.birthdayreminder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sanat.birthdayreminder.MyApplication;
import com.sanat.birthdayreminder.R;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by healthcaremagic on 3/3/2017.
 */

public class CommonUtils {
    public static SharedPreferences prefs;
    private static String PREFS_FILE_NAME = "FILTER_TIME";
    public static void message(Context context, String message) {
        try {
            messageShort(context, message);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void messageShort(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    public static long getDateInLong(String dateStr) {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            if (CommonUtils.isNotNull(dateStr))
                date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
        return date.getTime();
    }

    public static List<String> getSplitedStringByComma(String str) {
        List<String> elephantList = Arrays.asList(str.split("/"));
        return elephantList;
    }

    public static String getDateStringFromMillis(long timeInMillis) {
        Date date = new Date(timeInMillis);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = df2.format(date);
        List<String> dateList = getSplitedStringByComma(formatedDate);
        String month = getMonthString(Integer.valueOf(dateList.get(1)));
        String dayDate = dateList.get(0);
        String year = dateList.get(2);
        return month+", "+dayDate;
    }

    public static Bitmap uriToBitmap(Context context, Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);


            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return image;
    }

    public static String getMonthString(int month) {
        String monthString;
        switch (month) {
            case 1:
                monthString = "Jan";
                break;
            case 2:
                monthString = "Feb";
                break;
            case 3:
                monthString = "Mar";
                break;
            case 4:
                monthString = "Apr";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun";
                break;
            case 7:
                monthString = "Jul";
                break;
            case 8:
                monthString = "Aug";
                break;
            case 9:
                monthString = "Sep";
                break;
            case 10:
                monthString = "Oct";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dec";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    public static long getDateDiff(String curr, String pre) {
        long diffDays = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date1 = myFormat.parse(curr);
            Date date2 = myFormat.parse(pre);
            long diff = date2.getTime() - date1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            //TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
        return diffDays;
    }
    public static String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String newdate = sdf.format(date);
        return newdate;
    }

    public static void setStringPrefrences(Context context, String prefName, String Value) {
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, Value);
        editor.commit();
    }

    public static String getStringPrefrences(Context context, String prefName) {
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(prefName))
            return prefs.getString(prefName, null);
        else
            return "";
    }

    public static Typeface getLQFontTypeFace(Context context,TextView textView, int iconId, int color) {
        Typeface typeFace = null;
        try {
            typeFace = Typeface.createFromAsset(context.getAssets(), "lqfont.ttf");
            textView.setText(iconId);
            if (color > 0)
                textView.setTextColor(context.getResources().getColor(color));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return typeFace;
    }

    public static String getRealPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }


}

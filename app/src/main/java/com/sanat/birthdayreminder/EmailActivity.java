package com.sanat.birthdayreminder;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanat.birthdayreminder.firendList.FriendModel;
import com.sanat.birthdayreminder.utils.CommonUtils;

import java.io.File;

/**
 * Created by healthcaremagic on 3/5/2017.
 */

public class EmailActivity extends AppCompatActivity {
    Toolbar mToolbar;
    public static FriendModel model;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int READ_PERMISSION = 3;
    private static final int WRITE_PERMISSION = 4;
    String selectedPath;
    EditText editText;
    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.send_email_activity);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            editText = (EditText) findViewById(R.id.editText);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(model.getName());
            TextView nameTV = (TextView) findViewById(R.id.nameTV);
            TextView birthdayTV = (TextView) findViewById(R.id.birthdayTV);
            imageView = (ImageView)findViewById(R.id.productIV);
            nameTV.setText(model.getName());
            birthdayTV.setText(model.getdisplayDateOfBirth());
            findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        askReadAndWritePermission();
                    } catch (Exception e) {
                        e.fillInStackTrace();
                    }
                }
            });
            findViewById(R.id.sendEmailTV).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!CommonUtils.isNotNull(editText.getText().toString())) {
                            CommonUtils.message(getApplicationContext(), "Please say something...");
                        } else {
                            sendMail(editText.getText().toString());
                        }
                    } catch (Exception e) {
                        e.fillInStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void askReadAndWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            selectImage1();
        }
    }

    public void selectImage1() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(
                R.layout.custom_gallery_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.uploadUsingCameraTV).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                try {
                    intent.putExtra("return-data", true);

                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.fillInStackTrace();
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.chooseFromGalleryTV).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_FILE);
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    private void sendMail(String chatMail) {
        try {
            if (CommonUtils.isNotNull(chatMail)) {
                Intent emailIntent = null;
                emailIntent = new Intent(Intent.ACTION_SEND);
                if (CommonUtils.isNotNull(selectedPath)) {
                    File file;
                    file = new File(selectedPath);
                    Uri path = Uri.fromFile(file);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                }
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri capturedUri = null;
        if (requestCode == PICK_FROM_CAMERA && mImageCaptureUri != null && resultCode != 0) {
            if (mImageCaptureUri != null) {
                String path1 = mImageCaptureUri.getPath();
                if (path1 != null) {
                    File file1 = new File(path1);
                    capturedUri = Uri.fromFile(file1);
                    selectedPath = CommonUtils.getRealPathFromURI(getApplicationContext(), capturedUri);
                    imageView.setImageURI(capturedUri);
                }
            }
        } else if (requestCode == PICK_FROM_FILE && data != null) {
            capturedUri = data.getData();
            selectedPath = CommonUtils.getRealPathFromURI(getApplicationContext(), capturedUri);
            imageView.setImageURI(capturedUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                askReadAndWritePermission();
            } else {
                Toast.makeText(getApplicationContext(), "permission is required.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                askReadAndWritePermission();
            } else {
                Toast.makeText(getApplicationContext(), "permission is required.", Toast.LENGTH_SHORT).show();
            }
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

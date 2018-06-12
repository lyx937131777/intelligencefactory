package com.intelligencefactory.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;
import com.intelligencefactory.android.util.PermissionsUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditUserInfoActivity extends AppCompatActivity
{
    private EditText et_nickname;
    private EditText et_phone_number;
    private TextView tv_userID;
    private de.hdodenhof.circleimageview.CircleImageView photo;
    private Button bt_confrim;
    private Button bt_cancel;
    private Dialog dialog;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private String imagePath = null;
    private File new_photo = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String userID;
    private String nickname;
    private String profile_photo;
    private String phone_number;
    private boolean img_wait = false;
    private boolean info_wait = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = pref.getString("userID", null);
        User user = DataSupport.where("userID = ?", userID).findFirst(User.class);
        nickname = user.getNickname();
        phone_number = user.getPhone_number();
        profile_photo = user.getProfile_photo();

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        tv_userID = (TextView) findViewById(R.id.tv_userid);
        photo = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.icon_image);


        et_nickname.setText(nickname);
        tv_userID.setText(userID);
        et_phone_number.setText(phone_number);
        if (!profile_photo.equals("null"))
        {
            Glide.with(EditUserInfoActivity.this).load(HttpUtil.LocalAddress + "/" +
                    profile_photo).signature
                    (new StringSignature(pref.getString("latest", ""))).into(photo);
        } else
        {
            Glide.with(EditUserInfoActivity.this).load(R.drawable.nav_icon).into(photo);
        }

        init_dialog();


        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo", "click img");
                dialog.show();
            }
        });

        bt_confrim = (Button) findViewById(R.id.bt_confirm_edit_info);
        bt_confrim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nickname = et_nickname.getText().toString();
                phone_number = et_phone_number.getText().toString();
                Log.e("editUserInfo", "===============\n 昵称：" + nickname + "\n 手机：" +
                        phone_number + "\n===============");
                // post json_data to server
                // if imagePath != null post imagepath ,else post profile_photo
                if (new_photo != null)
                {
                    img_wait = true;
                    String address = HttpUtil.LocalAddress + "/UpdateUserImg";
                    HttpUtil.modifyImgRequest(address, userID, new_photo, new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            final String responseData = response.body().string();
                            Log.e("Edit!", responseData);
                            if (responseData.equals("true"))
                            {
                                User user = new User();
                                user.setProfile_photo("/images/" + userID + ".jpg");
                                user.updateAll("userID = ?", userID);
                                editor = pref.edit();
                                editor.putString("latest", String.valueOf(System
                                        .currentTimeMillis()));
                                editor.apply();
                                img_wait = false;
                            }
                        }
                    });
                }
                String address = HttpUtil.LocalAddress + "/UpdateUserInfo";
                HttpUtil.modifyInfoRequest(address, userID, phone_number, nickname, new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        final String responseData = response.body().string();
                        if (responseData.equals("true"))
                        {
                            User user = new User();
                            user.setPhone_number(phone_number);
                            user.setNickname(nickname);
                            user.updateAll("userID = ?", userID);
                            info_wait = false;
                        }
                    }
                });

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar p = (ProgressBar)findViewById(R.id.pro);
                        p.setVisibility(View.VISIBLE);
                        while (img_wait || info_wait)
                        {
                            try
                            {
                                TimeUnit.SECONDS.sleep(1);
                                Log.e("EditUserInfoActivity","Waiting!!!");
                            } catch (InterruptedException e)
                            {

                            }
                        }
                        p.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

        bt_cancel = (Button) findViewById(R.id.bt_cancel_edit_info);
        bt_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void init_dialog()
    {
        dialog = new Dialog(this, R.style.AppTheme);
        View view = View.inflate(this, R.layout.dialog_bottom, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        Button bt_dialog_camera = (Button) view.findViewById(R.id.bt_dialog_camera);
        Button bt_dialog_album = (Button) view.findViewById(R.id.bt_dialog_album);
        Button bt_dialog_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click camera");

                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "profile_image.jpg");
                imagePath = outputImage.getAbsolutePath();
                try
                {
                    if (outputImage.exists())
                    {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT < 24)
                {
                    imageUri = Uri.fromFile(outputImage);
                } else
                {
                    imageUri = FileProvider.getUriForFile(EditUserInfoActivity.this, "com" +
                            ".intelligencefactory.android.fileprovider", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                dialog.cancel();
            }
        });

        bt_dialog_album.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click album");

                if (ContextCompat.checkSelfPermission(EditUserInfoActivity.this, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(EditUserInfoActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else
                {
                    openAlbum();
                }
                dialog.cancel();
            }

        });
        bt_dialog_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click cancel");
                dialog.cancel();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    // Camera and Album
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                } else
                {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        Log.e("camera", getContentResolver().openInputStream(imageUri).toString());
                        Log.e("camera", "imageUri:" + imageUri.toString());
                        //Log.e("camera","imagePath:"+getImagePath(imageUri, null).toString());
                        photo.setImageBitmap(bitmap);
                        new_photo = new File(imagePath);
                    } catch (Exception e)
                    {
                        Log.e("test", "Here Wrong!!!!!!!");
                        e.printStackTrace();
                        Log.e("test", "Here Wrong!!!!!!!");
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19)
                    {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else
                    {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                    new_photo = new File(imagePath);
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri))
        {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse
                        ("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        Log.e("album", "imagePath:" + imagePath);
        if (imagePath != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            photo.setImageBitmap(bitmap);
        } else
        {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}

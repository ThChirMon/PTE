package com.pte.Activity;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pte.R;
import com.pte.Util.DeleteCharString;
import com.pte.Util.LocationUtils;

import java.io.File;
import java.io.IOException;

public class PhotoActivity extends BaseActivity {
    private static final String TAG = "PhotoActivity";
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;
    private ImageView img;
    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private File file;// 拍照保存的图片文件
    private String filepath; // 上传文件地址
    private boolean hasPermission = false;
    private Dialog mShareDialog;
    private Button button;
    private TextView txt_take_photo,txt_gallery,txt_exit;
    private String location;
    private TextView txt_location;


    private Button photo,gallery;
    @Override
    protected int initLayout() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initView() {
        img = findViewById(R.id.vw_img);
        button = findViewById(R.id.button);
        txt_take_photo = findViewById(R.id.txt_take_photo);
        txt_location = findViewById(R.id.txt_location);
        txt_gallery = findViewById(R.id.txt_gallery);
        txt_exit = findViewById(R.id.txt_exit);
        Btnlistener btnlistener = new Btnlistener();
        button.setOnClickListener(btnlistener);
        txt_exit.setOnClickListener(btnlistener);
        new Handler(mContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DeleteCharString delete = new DeleteCharString();
                location = delete.deleteCharString(LocationUtils.getInstance().getLocations(mContext).toString(),'[');
                location = delete.deleteCharString(location,']');
                location = delete.deleteCharString(location,',');
                location = delete.deleteCharString(location,' ');
                txt_location.setText(location);
                // 1s后会执行的操作
            }
        });
    }

    @Override
    protected void initData() {
        checkPermissions();
    }

    private class Btnlistener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    DeleteCharString delete = new DeleteCharString();
                    location = delete.deleteCharString(LocationUtils.getInstance().getLocations(mContext).toString(),'[');
                    location = delete.deleteCharString(location,']');
                    location = delete.deleteCharString(location,',');
                    location = delete.deleteCharString(location,' ');
                    txt_location.setText(location);
                    showDialog();
                    break;
                case R.id.txt_exit:
                    //点击注销按键后调用LoginActivity提供的resetSprfMain()方法执行editorMain.putBoolean("main",false);，即将"main"对应的值修改为false
                    resetSprfMain();
                    Intent intent=new Intent(PhotoActivity.this,LoginActivity.class);
                    startActivity(intent);
                    PhotoActivity.this.finish();
            }
        }
    }

    public void resetSprfMain(){

        SharedPreferences sprfMain = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editorMain = sprfMain.edit();

        editorMain.putBoolean("main",false);

        editorMain.commit();

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS}, REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    private void takePhone() throws IOException {
        // 要保存的图片文件
        // 获取文件
        file = createFileIfNeed("flower.png");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(this, file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private File createFileIfNeed(String fileName) throws IOException {
        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/sfpic";
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    private Uri getUriForFile(Context context, File file) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (currentapiVersion < 24) {
            // 从文件中创建uri
            uri = Uri.fromFile(file);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    System.out.println("成功");
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imgUri.toString());
                    try {
                        cropPhoto(imgUri, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                // 裁剪后设置图片
                case REQUEST_CROP:
                    // setImageURI会缓存uri，对于同一个地址的uri不会再设置，所以这里先进行清除缓存再设置
                    img.setImageURI(null);
                    img.setImageURI(mCutUri);
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    try {
                        cropPhoto(data.getData(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) throws IOException {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        if(fromCapture){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("crop", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(file);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            File mCutFile = createFileIfNeed("flower.png");
            mCutUri = Uri.fromFile(mCutFile);
        }
        filepath = mCutUri.getPath();
        //UploadUtils.uploadFile(filepath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);
        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }

    //按钮 单击事件
    public void btnShowDialog(View view) {
        showDialog();// 单击按钮后 调用显示视图的 showDialog 方法，
    }
    /**
     * 显示弹出框
     */
    private void showDialog() {
        if (mShareDialog == null) {
            initShareDialog();
        }
        mShareDialog.show();
    }
    /**
     * 初始化分享弹出框
     */
    private void initShareDialog() {
        mShareDialog = new Dialog(this, R.style.dialog_bottom_full);
        mShareDialog.setCanceledOnTouchOutside(true); //手指触碰到外界取消
        mShareDialog.setCancelable(true);             //可取消 为true
        Window window = mShareDialog.getWindow();      // 得到dialog的窗体
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);

        View view = View.inflate(this, R.layout.lay_share, null); //获取布局视图
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShareDialog != null && mShareDialog.isShowing()) {
                    mShareDialog.dismiss();
                }

            }
        });
        view.findViewById(R.id.txt_take_photo).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (hasPermission) {
                    try {
                        takePhone();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        view.findViewById(R.id.txt_gallery).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (hasPermission) {
                    openGallery();
                }
            }
        });
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }

}
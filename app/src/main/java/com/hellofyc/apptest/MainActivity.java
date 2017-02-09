package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.content.CameraHelper;
import com.hellofyc.base.content.WifiHelper;
import com.hellofyc.base.security.SecurityHelper;
import com.hellofyc.base.utils.FLog;
import com.hellofyc.base.widget.ClearableEditText;
import com.hellofyc.base.widget.SwipeRefreshRecyclerView;

import java.security.KeyStore;
import java.util.List;

import static com.hellofyc.base.content.WifiHelper.getInstance;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;
    private ClearableEditText mInputTime;
    private SwipeRefreshRecyclerView mList;
//    private BaseRecyclerViewAdapter<String, ItemViewHolder> mAdapter;
    private ImageView mImageView;

    private CameraHelper mCameraHelper;
    private KeyStore mKeyStore;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setViewsOnClickListener(R.id.text);
        init();


        if (getInstance(this).isWifiApEnabled()) {
            FLog.i("Wifi Ap is enabled");
            getInstance(this).setWifiApDisabled();
        } else {
            FLog.i("Wifi Ap is disabled");
        }

        getInstance(this).setWifiEnabled(true, null);

        List<ScanResult> scanResults = WifiHelper.getInstance(this).getScanResults();
        for (ScanResult result : scanResults) {
            FLog.i("result:" + result.SSID);
        }


//        WifiInfo wifiInfo = WifiHelper.getInstance(this).getConnectionInfo();
//        if (wifiInfo != null && !"0x".equals(wifiInfo.getSSID())) {
//            FLog.i("wifi-info:" + wifiInfo.getSSID());
//        } else {
            WifiHelper.getInstance(this).connect("360-Canbus", "fangyucun", new WifiHelper.SimpleOnWifiConnectListener() {

                @Override
                public void onWifiConnecting(int status) {
                    super.onWifiConnecting(status);
                    FLog.i("onWifiConnecting:", status);
                }

                @Override
                public void onWifiConnectSuccess(WifiInfo wifiInfo) {
                    super.onWifiConnectSuccess(wifiInfo);
                    FLog.i("onWifiConnectSuccess:", wifiInfo.getSSID());
                }

                @Override
                public void onWifiConnectFailure(int errorCode) {
                    super.onWifiConnectFailure(errorCode);
                    FLog.i("onWifiConnectFailure:", errorCode);
                }
            });
//        }

//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                for (int i=0; i<10; i++) {
//                    SecurityHelper.getInstance().encrypt(MainActivity.this, "token", "我是房余存" + i,
//                            new SecurityHelper.OnEncryptCallback() {
//
//                                @Override
//                                public void onEncrypt(byte[] data, String base64String) {
//                                    FLog.i("token:" + base64String);
//                                    getProviders("token", base64String);
//                                }
//                            });
//                }
//            }
//        });
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void getProviders(String alias, String value) {
        SecurityHelper.getInstance().decrypt(this, alias, value, new SecurityHelper.OnDecryptCallback(){

            @Override
            public void onDecrypt(String decryptString) {
                FLog.i("source:" + decryptString);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void addNewKey() {
    }


    private void init() {
        setViewsOnClickListener(R.id.btn);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.text:
//                Intent intent = new Intent(this, Test1Activity.class);
//                startActivity(intent);

//                if (!checkPermission(Manifest.permission.READ_CONTACTS)) {
//                    requestPermission(Manifest.permission.READ_CONTACTS);
//                }


                break;
            case R.id.btn:

//                if (checkPermissions(
//                        new String[]{PermissionHelper.PERMISSION_CAMERA, PermissionHelper.PERMISSION_STORAGE},
//                        PermissionHelper.REQUEST_CODE_CAMERA )) {
//                        mCameraHelper
//                                .setNeedCrop(false)
//                                .openAlbum(new CameraHelper.OnCaptureListener() {
//
//                                    @Override
//                                    public void onCapture(Uri imageUri) {
//                                        mImageView.setImageURI(imageUri);
//                                    }
//
//                                });
//                }

//                Bitmap bitmap = QRCodeHelper.newInstance()
//                        .setMargin(2)
//                        .setLogo(((BitmapDrawable)getDrawableCompat(R.mipmap.ic_launcher)).getBitmap())
//                        .setContent("http://www.so.com")
//                        .generate();
//                mImageView.setImageBitmap(bitmap);

//                startActivity(new Intent(this, DatabindingActivity.class));

//                startActivity(new Intent(this, QRCodeActivity.class));

//                AppCompatDialog dialog = new AppCompatDialog(this, android.support.design.R.style.Theme_Design_Light_BottomSheetDialog);
//                dialog.setTitle("Hello!");
//                TextView tv = new TextView(this);
//                tv.setBackgroundColor(Color.RED);
//                tv.setText("Hello World!");
//                dialog.setContentView(tv);
//                dialog.show();
//
//                dialog.getWindow().setLayout(900, 1800);

//                enterPictureInPicture();

//                ToastUtils.show(this, TimeUtils.getShowTimeFromNow((System.currentTimeMillis() - Long.parseLong(mInputTime.getText().toString()) * 1000)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void doPermissionGranted() {
    }
}

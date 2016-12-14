package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellofyc.base.Keccak;
import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.content.CameraHelper;
import com.hellofyc.base.security.SHAUtils;
import com.hellofyc.base.view.OnValidClickListener;
import com.hellofyc.base.widget.ClearableEditText;
import com.hellofyc.base.widget.SwipeRefreshRecyclerView;

import java.util.Formatter;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;
    private ClearableEditText mInputTime;
    private SwipeRefreshRecyclerView mList;
//    private BaseRecyclerViewAdapter<String, ItemViewHolder> mAdapter;
    private ImageView mImageView;

    private CameraHelper mCameraHelper;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setViewsOnClickListener(R.id.text);
        init();

        findViewById(R.id.btn).setOnClickListener(new OnValidClickListener(2000) {

            @Override
            public void onValidClick(View v) {
                byte[] b = getByteArray("The quick brown fox jumps over the lazy dog");
                String s = getHexStringByByteArray(b);
                Keccak keccak = new Keccak(1600);

                System.out.println("sha-256 = " + SHAUtils.encodeWithSHA_256(s));
                System.out.println("sha3-224 = " + SHAUtils.encodeWithSHA3_224(b));
                System.out.println("sha3-256 = " + SHAUtils.encodeWithSHA3_256(b));
                System.out.println("sha3-384 = " + SHAUtils.encodeWithSHA3_384(b));
                System.out.println("sha3-512 = " + SHAUtils.encodeWithSHA3_512(b));








//                String encrypt =
//                String result = RSAUtils.decrypt(, RSAUtils.getPublicKey(MainActivity.this, "private.oem"));
//                FLog.i("result:" + result);
            }
        });
    }

    public static byte[] getByteArray(String s) {
        return (s != null) ? s.getBytes(): null;
    }

    public static String getHexStringByByteArray(byte[] array) {
        if (array == null)
            return null;

        StringBuilder stringBuilder = new StringBuilder(array.length * 2);
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter(stringBuilder);
        for (byte tempByte : array)
            formatter.format("%02x", tempByte);

        return stringBuilder.toString();
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

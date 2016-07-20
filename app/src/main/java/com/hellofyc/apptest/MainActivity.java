package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.content.CameraHelper;
import com.hellofyc.base.content.PermissionHelper;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.widget.ClearableEditText;
import com.hellofyc.base.widget.SwipeRefreshRecyclerView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;
    private ClearableEditText mInputTime;
    private SwipeRefreshRecyclerView mList;
//    private BaseRecyclerViewAdapter<String, ItemViewHolder> mAdapter;
    private ImageView mImageView;

    private CameraHelper mCameraHelper;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FLog.i("===onCreate===");
        setStatusBarColorTransparent();
        getWindow().setStatusBarColor(Color.BLUE);

        setContentView(R.layout.activity_main);
        setViewsOnClickListener(R.id.text);
//        mImageView = (ImageView) findViewById(R.id.image);
//        mList = (SwipeRefreshRecyclerView) findViewById(R.id.list);
//        mList.setLayoutManager(new LinearLayoutManager(this));
//        mList.setSupportRefresh(false);
//        List<String> list = new ArrayList<>();
//        for (int i=0; i<200; i++) {
//            list.add("Hello" + i);
//        }
//        mAdapter = new BaseRecyclerViewAdapter<String, ItemViewHolder>(this, list) {
//
//            @Override
//            public ViewHolder onCreateItemViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//                return new ViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
//            }
//
//            @Override
//            public void onBindItemViewHolder(ViewHolder itemHolder, int position, int viewType) {
//                itemHolder.mText.setText(getItem(position));
//            }
//        };
//        mList.setAdapter(mAdapter);
        init();
//        mCameraHelper = CameraHelper.newInstance(this);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(android.R.id.text1);
        }
    }

    private void init() {
        setViewsOnClickListener(R.id.btn);

//        mAdapter.updateItems(new ArrayList<String>());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                mList.setEmptyViewStatus(EmptyView.STATUS_NO_DATA);
//                ToastUtils.show(MainActivity.this, mList.getEmptyView().getHeight() + "");
            }

        }, TimeUnit.SECONDS.toMillis(3));
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
                Intent intent = new Intent(this, Test1Activity.class);
                startActivity(intent);
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

                startActivity(new Intent(this, DatabindingActivity.class));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionHelper.REQUEST_CODE_CALENDAR:
                if (grantResults[0] == PermissionHelper.PERMISSION_GRANTED) {
                    FLog.i("授权了!");
                    doPermissionGranted();
                } else {
                    FLog.i("未授权");
                }
                break;
        }
    }

    private void doPermissionGranted() {
    }
}

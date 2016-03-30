package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.helper.PermissionHelper;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.TimeUtils;
import com.hellofyc.base.util.ToastUtils;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;
    private EditText mInputTime;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FLog.i("onCreate");
        init();
    }

    private void init() {
        mInputTime = (EditText) findViewById(R.id.input_time);

//        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_sheet);
//        View view = coordinatorLayout.findViewById(R.id.imageView);
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        FLog.i("newState:" + "STATE_COLLAPSED");
//                        break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        FLog.i("newState:" + "STATE_DRAGGING");
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        FLog.i("newState:" + "STATE_EXPANDED");
//                        break;
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        FLog.i("newState:" + "STATE_HIDDEN");
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        FLog.i("newState:" + "STATE_SETTLING");
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
////                FLog.i("slideOffset:" + slideOffset);
//            }
//        });

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
            case R.id.btn:

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

                ToastUtils.showDefault(this, TimeUtils.getShowTimeFromNow((System.currentTimeMillis() - Long.parseLong(mInputTime.getText().toString()) * 1000)));

                break;
        }
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

    @Override
    public void onMultiWindowChanged(boolean inMultiWindow) {
        super.onMultiWindowChanged(inMultiWindow);
    }

    @Override
    public void onPictureInPictureChanged(boolean inPictureInPicture) {
        super.onPictureInPictureChanged(inPictureInPicture);
    }

    private void doPermissionGranted() {
    }
}

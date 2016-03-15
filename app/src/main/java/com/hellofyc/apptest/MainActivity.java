package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.content.IntentHelper;
import com.hellofyc.base.helper.PermissionHelper;
import com.hellofyc.base.util.FLog;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FLog.i("onCreate");
        init();
    }

    private void init() {

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

    }

    @Override
    protected void onPause() {
        super.onPause();
        FLog.i("onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FLog.i("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FLog.i("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FLog.i("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FLog.i("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FLog.i("onDestroy");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn:
//                BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
//                dialog.setContentView(R.layout.layout_bottom_sheet);
//                dialog.show();

//                if (checkPermission(PermissionHelper.REQUEST_CODE_CALENDAR, PermissionHelper.PERMISSION_CALENDAR)) {
//                    doPermissionGranted();
//                }

                Intent intent = IntentHelper.getOpenAppDetailActivityIntent(this, getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, com.hellofyc.base.R.anim.base_right_enter, com.hellofyc.base.R.anim.base_slow_fade_exit);
                    startActivity(intent, options.toBundle());
                }

//                enterPictureInPicture();

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
        FLog.i("onMultiWindowChanged:" + inMultiWindow);
    }

    @Override
    public void onPictureInPictureChanged(boolean inPictureInPicture) {
        super.onPictureInPictureChanged(inPictureInPicture);
        FLog.i("onPictureInPictureChanged:" + inPictureInPicture);
    }

    private void doPermissionGranted() {
        FLog.i("授权之后的事!");
    }
}

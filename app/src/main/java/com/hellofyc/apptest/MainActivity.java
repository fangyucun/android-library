package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
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

        init();
    }

    private void init() {

        setViewsOnClickListener(R.id.btn);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn:
//                BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
//                dialog.setContentView(R.layout.layout_bottom_sheet);
//                dialog.show();

                if (checkPermission(PermissionHelper.REQUEST_CODE_CALENDAR, PermissionHelper.PERMISSION_CALENDAR)) {
                    doPermissionGranted();
                }

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

    private void doPermissionGranted() {
        FLog.i("授权之后的事!");
    }
}

package com.hellofyc.apptest;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.net.http.HttpRequest;
import com.hellofyc.base.net.http.HttpUtils;

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

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ArrayMap<String, Object> params = new ArrayMap<>();
                        params.put("page", 2);
                        params.put("timestamp", 1456908238);
                        HttpUtils.create().setReqeustParams(HttpRequest.create().add("page", 2).add("time:", 22)).setDebugEnable().setUrl("http://106.38.193.197/Api/getAllInfo").request();
                    }
                }).start();
                break;
        }
    }
}

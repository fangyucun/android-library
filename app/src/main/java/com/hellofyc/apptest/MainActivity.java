package com.hellofyc.apptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.view);
        mBtn = (Button) findViewById(R.id.btn);

//        Drawable drawable = getDrawableCompat(R.drawable.ic_account_circle_24dp);
//
//        mBtn.setOnClickListener(v -> {
//
//            List<String> list = new ArrayList<>();
//            list.add("item1");
//            list.add("item2");
//            list.add("item3");
//
//            Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::print);
//
////            Stream.of(list).filter(item -> !item.equals("item1")).forEach(item -> FLog.i(item));
//            List<String> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long");
//            System.out.print(Stream.of(splitUpNames).map(name -> name.split(" ")).collect(Collectors.toList()).listIterator(0));
//            FLog.i(Stream.of(splitUpNames).map(name -> name.split(" ")).collect(Collectors.toList()).listIterator(0))
//        });

//        PrefsHelper.create(this).putValue("a1", "2").apply();

    }
}

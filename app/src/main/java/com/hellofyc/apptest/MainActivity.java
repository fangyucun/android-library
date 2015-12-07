package com.hellofyc.apptest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.hellofyc.applib.app.activity.BaseActivity;
import com.hellofyc.applib.util.Flog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.view);
        mBtn = (Button) findViewById(R.id.btn);

        Drawable drawable = getDrawableCompat(R.drawable.ic_account_circle_24dp);

        mBtn.setOnClickListener(v -> {

            List<String> list = new ArrayList<>();
            list.add("item1");
            list.add("item2");
            list.add("item3");

            Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::print);

//            Stream.of(list).filter(item -> !item.equals("item1")).forEach(item -> Flog.i(item));
            List<String> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long");
            System.out.print(Stream.of(splitUpNames).map(name -> name.split(" ")).collect(Collectors.toList()).listIterator(0));
            Flog.i(Stream.of(splitUpNames).map(name -> name.split(" ")).collect(Collectors.toList()).listIterator(0));
        });
    }
}

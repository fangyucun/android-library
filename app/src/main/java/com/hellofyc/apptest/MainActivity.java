package com.hellofyc.apptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.base.app.activity.BaseActivity;
import com.hellofyc.base.util.FLog;
import com.hellofyc.base.util.PrefsHelper;

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
        PrefsHelper.create(this).putValueAndApply("a1", 2);
        PrefsHelper.create(this).putValueAndApply("a2", "3");
        PrefsHelper.create(this).keyEncrypt().putValueAndApply("a3", "2");
        PrefsHelper.create(this).valueEncrypt().putValue("a4", "2").apply();
        PrefsHelper.create(this).valueEncrypt().putValue("a5", 3).apply();
        PrefsHelper.create(this).keyEncrypt().valueEncrypt().putValue("a6", 3).apply();
        PrefsHelper.create(this).keyEncrypt().valueEncrypt().setEncryptKey("12345678").putValue("a6", 3).apply();
        PrefsHelper.create(this).setFileName("_File_Name" +
                "").keyEncrypt().valueEncrypt().setEncryptKey("12345678").putValue("a6", 3).apply();

        FLog.i("a3", PrefsHelper.create(this).keyEncrypt().getString("a3", "2"));
        FLog.i("a4", PrefsHelper.create(this).valueEncrypt().getString("a4", "3"));
        FLog.i("a6", PrefsHelper.create(this).setEncryptKey("12345678").keyEncrypt().valueEncrypt().getInt("a6", 2));

    }
}

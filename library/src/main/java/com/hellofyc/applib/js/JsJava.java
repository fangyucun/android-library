package com.hellofyc.applib.js;

import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.lang.reflect.Method;

/**
 * Created on 2015/12/1.
 *
 * @author Yucun Fang
 */
public class JsJava {
    private static final boolean DEBUG = false;

    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";
    private static final String[] mFilterMethods = { "getClass", "hashCode", "notify", "notifyAll", "equals",
            "toString", "wait", };

    private WebView mWebView;

    private JsJava(WebView webView) {
        mWebView = webView;
    }

    public static JsJava with(@NonNull WebView webView) {
        return new JsJava(webView);
    }

    public void callJs(String methodName, String... params) {
        callJs(methodName, null, params);
    }

    public void callJs(String methodName, final OnJsCallback callback, String... params) {
        StringBuilder text = new StringBuilder("(");
        if (params == null || params.length == 0) {
            text.append(")");
        } else {
            int length = params.length;
            for (String param : params) {
                if (length == 1) {
                    text.append(param.concat(")"));
                } else {
                    text.append(param.concat(","));
                }
                length--;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(methodName.concat(text.toString()), new JsValueCallback(callback));
        } else {
            mWebView.loadUrl("javascript:" + methodName + text);
        }
    }

    private static class JsValueCallback implements ValueCallback<String> {

        public OnJsCallback mCallback;

        public JsValueCallback(OnJsCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onReceiveValue(String value) {
            mCallback.onJsCallback(value);
        }
    }

    private void createJsMethod(@NonNull String methodName, @NonNull Object obj, @NonNull StringBuilder script) {
        Class<? extends Object> objClass = obj.getClass();

        script.append("if(typeof(window.").append(methodName).append(")!='undefined'){");
        if (DEBUG) {
            script.append("    console.log('window.".concat(methodName).concat("_js_interface_name is exist!!');"));
        }

        script.append("}else {");
        script.append("    window.").append(methodName).append("={");

        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            // 过滤掉Object类的方法，包括getClass()方法，因为在Js中就是通过getClass()方法来得到Runtime实例
            if (filterMethods(name)) {
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
            script.append(KEY_INTERFACE_NAME).append(":'").append(methodName).append("',");
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

    public interface OnJsCallback {
        void onJsCallback(String result);
    }

    private boolean filterMethods(String methodName) {
        for (String method : mFilterMethods) {
            if (method.equals(methodName)) {
                return true;
            }
        }
        return false;
    }


}

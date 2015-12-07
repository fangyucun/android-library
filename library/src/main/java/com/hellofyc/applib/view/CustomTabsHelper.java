package com.hellofyc.applib.view;

/**
 * Created on 2015/9/16.
 *
 * @author Yucun Fang
 */
public class CustomTabsHelper {
//    private static final boolean DEBUG = true;
//
//    private static final String PACKAGE_NAME_CHROME = "com.android.chrome";
//    private static CustomTabsHelper sInstance;
//    private CustomTabsClient mCustomTabsClient;
//    private Activity mActivity;
//    private Uri mUri;
//
//    private CustomTabsHelper() {
//    }
//
//    private boolean initCustomTabsServiceConnection(@NonNull Context context) {
//        CustomTabsServiceConnection customTabsServiceConnection = new CustomTabsServiceConnection() {
//
//            @Override
//            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
//                if (DEBUG) Flog.i("===onCustomTabsServiceConnected===");
//                onCustomTabsServiceConnectSuccess(componentName, customTabsClient);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                if (DEBUG) Flog.i("===onServiceDisconnected===");
//            }
//        };
//        return CustomTabsClient.bindCustomTabsService(context, PACKAGE_NAME_CHROME, customTabsServiceConnection);
//    }
//
//    private void onCustomTabsServiceConnectSuccess(ComponentName componentName, CustomTabsClient customTabsClient) {
//        mCustomTabsClient = customTabsClient;
//        customTabsClient.warmup(0L);
//        doLoadUri();
//    }
//
//    public static CustomTabsHelper getInstance() {
//        if (sInstance == null) {
//            return new CustomTabsHelper();
//        }
//        return sInstance;
//    }
//
//    public boolean loadUrl(@NonNull final Activity activity, @NonNull final String urlString) {
//        return loadUri(activity, Uri.parse(urlString));
//    }
//
//    public boolean loadUri(@NonNull final Activity activity, @NonNull final Uri uri) {
//        mActivity = activity;
//        mUri = uri;
//        return initCustomTabsServiceConnection(activity);
//    }
//
//    private boolean doLoadUri() {
//        CustomTabsSession session = mCustomTabsClient.newSession(null);
//        boolean mayLaunchUrl = session.mayLaunchUrl(mUri, null, null);
//        if (mayLaunchUrl) {
//            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
//            builder.setToolbarColor(ContextCompat.getColor(mActivity, R.color.primary));
//            builder.setCloseButtonIcon(((BitmapDrawable) ContextCompat.getDrawable(mActivity, R.drawable.ic_btn_back)).getBitmap());
//            builder.setStartAnimations(mActivity, R.anim.right_enter, R.anim.slow_fade_exit);
//            builder.setExitAnimations(mActivity, 0, R.anim.right_exit);
//            builder.build().launchUrl(mActivity, mUri);
//        }
//        return mayLaunchUrl;
//    }
}

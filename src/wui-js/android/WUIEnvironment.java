/*
 * @file WUIEnvironment.java
 * @class WUIEnvironment
 * @version 0.1
 * @author Sergio E. Belmar V. (wuijs.project@gmail.com)
 * @copyright Sergio E. Belmar V. (wuijs.project@gmail.com)
 */

package app.android.mypackage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.net.http.SslError;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.io.File;
import android.app.DownloadManager;

public class WUIEnvironment {
	
    public WebView webView;
    private final Context context;
    private AppCompatActivity activity;
    private boolean developMode = false;
    private String deepLinkURL = null;
    private boolean pageLoaded = false;
    private final String logTag = "WUIEnvironment";
    
    public WUIEnvironment(Context context, boolean developMode) throws JSONException {
        this.context = context;
        this.developMode = developMode;
        webViewInit();
    }

    public WUIEnvironment(Context context) throws JSONException {
        this.context = context;
        webViewInit();
    }

    private void webViewInit() throws JSONException {
        webView = new WebView(context);
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            activity.setContentView(webView);
            setupWebViewSettings(developMode);
            setupWebViewClient(developMode);
            setupBackPressHandler();
            setupDownloadHandler();
        }
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebViewSettings(boolean developMode) throws JSONException {
        JSONObject appInfo = getAppInfo();
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString()+" WUIEnvironment ("+appInfo.get("name")+"/"+appInfo.get("version")+")");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
    }

    @SuppressLint("JavascriptInterface")
    private void setupWebViewClient(boolean developMode) {
        webView.addJavascriptInterface(new WebViewJavascriptInterface(), "Android");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(logTag + " (JS)", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("file:///android_asset/")) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
            
            @SuppressLint("WebViewClientOnReceivedSslError")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                int errorCode = error.getPrimaryError();
                Log.e(logTag, "SSL error: " + error.toString());
                switch (errorCode) {
                    case SslError.SSL_DATE_INVALID: Log.e(logTag, "SSL error: Certificate date is invalid (code: " + errorCode + ")"); break;
                    case SslError.SSL_EXPIRED: Log.e(logTag, "SSL error: Certificate has expired (code: " + errorCode + ")"); break;
                    case SslError.SSL_IDMISMATCH: Log.e(logTag, "SSL error: Certificate ID mismatch (code: " + errorCode + ")"); break;
                    case SslError.SSL_UNTRUSTED: Log.e(logTag, "SSL error: Certificate is not trusted (code: " + errorCode + ")"); break;
                    case SslError.SSL_NOTYETVALID: Log.e(logTag, "SSL error: Certificate is not yet valid (code: " + errorCode + ")"); break;
                    default: Log.e(logTag, "SSL error: Unknown SSL error (code: " + errorCode + ")"); break;
                }
                if (developMode || errorCode == SslError.SSL_UNTRUSTED) {
                    handler.proceed();
                } else {
                    handler.cancel();
                }
            }
            
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(logTag, "WebView Error (" + request.getUrl() + "): " + error.getDescription() + " (Code: " + error.getErrorCode() + ")");
                if (request.isForMainFrame()) {
                    Log.e(logTag, "Main frame failed to load!");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pageLoaded = true;
                Log.i(logTag, "Page loaded: " + url);
                if (deepLinkURL != null) {
                    try {
                        JSONObject arguments = new JSONObject();
                        arguments.put("event", "onReceiveDeepLink");
                        arguments.put("url", deepLinkURL);
                        pushJavascript(arguments);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void setupBackPressHandler() {
        activity.getOnBackPressedDispatcher().addCallback(activity, new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    this.setEnabled(false);
                    activity.getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void setupDownloadHandler() {
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String filename = "";
                File downloadFile = null;
                boolean downloaded = false;
                Log.d(logTag, "Start download '"+url+"'");
                if (url.startsWith("file:///android_asset/")) {
                    try {
                        String assetPath = url.replace("file:///android_asset/", "");
                        File sourceFile = new File(activity.getCacheDir(), assetPath);
                        if (!sourceFile.exists()) {
                            sourceFile.getParentFile().mkdirs();
                            try (java.io.InputStream in = context.getAssets().open(assetPath);
                                 java.io.FileOutputStream out = new java.io.FileOutputStream(sourceFile)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, len);
                                }
                            }
                        }
                        if (sourceFile.exists()) {
                            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            if (!downloadsDir.exists()) {
                                downloadsDir.mkdirs();
                            }
                            filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                            downloadFile = new File(downloadsDir, filename);
                            int version = 1;
                            while (downloadFile.exists()) {
                                String nameWithoutExt = filename.replaceFirst("[.][^.]+$", "");
                                String extension = filename.substring(filename.lastIndexOf('.'));
                                downloadFile = new File(downloadsDir, nameWithoutExt + " (" + version + ")" + extension);
                                version++;
                            }
                            try (java.io.FileInputStream in = new java.io.FileInputStream(sourceFile);
                                 java.io.FileOutputStream out = new java.io.FileOutputStream(downloadFile)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, len);
                                }
                            }
                            Log.i(logTag, "Asset file downloaded to: " + downloadFile.getAbsolutePath());
                            downloaded = true;
                        }
                    } catch (Exception e) {
                        Log.e(logTag, "Error downloading asset file: " + e.getMessage());
                    }
                } else if (url.startsWith("data:")) {
                    // downloadFile = ...;
                    // downloaded = true;
                } else {
                    filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    downloadFile = new File(downloadsDir, filename);
                    DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setTitle(filename);
                    request.setMimeType(mimetype);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                    downloadManager.enqueue(request);
                    downloaded = true;
                }
                if (downloaded) {
                    MediaScannerConnection.scanFile(context, new String[]{downloadFile.getAbsolutePath()}, null, null);
                    Intent openIntent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.fromFile(downloadFile);
                    openIntent.setDataAndType(uri, mimetype != null && !mimetype.isEmpty() ? mimetype : "application/octet-stream");
                    openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(openIntent);
                    } catch (Exception e) {
                        Log.e(logTag, "No app found to open the file: " + e.getMessage());
                    }
                    try {
                        JSONObject arguments = new JSONObject();
                        arguments.put("event", "onDownloadFile");
                        arguments.put("filename", filename);
                        arguments.put("mimetype", mimetype);
                        arguments.put("uri", uri);
                        pushJavascript(arguments);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public boolean isAppInForeground() {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;
        String packageName = activity.getPackageName();
        for (RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.processName.equals(packageName)) {
                return processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    @SuppressLint("HardwareIds")
    private String getDeviceID() {
        String deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceId != null && !deviceId.equals("9774d56d682e549c")) {
            return deviceId;
        }
        return "";
    }

    private String getDeviceUUID() {
        String deviceId = this.getDeviceID();
        if (!deviceId.isEmpty()) {
            UUID deviceUUID = UUID.nameUUIDFromBytes(deviceId.getBytes());
            return deviceUUID.toString();
        }
        return "";
    }

    private String getDeviceName() {
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.w(logTag, "Bluetooth permission not granted, using Build.USER as device name");
                return Build.USER;
            }
            BluetoothAdapter device = BluetoothAdapter.getDefaultAdapter();
            if (device != null) {
                return device.getName();
            }
        } catch (SecurityException e) {
            Log.w(logTag, "SecurityException accessing Bluetooth: " + e.getMessage());
            return Build.USER;
        } catch (Exception e) {
            Log.w(logTag, "Exception getting device name: " + e.getMessage());
            return Build.USER;
        }
        return Build.USER;
    }

    public JSONObject getDeviceInfo() {
        JSONObject deviceInfo = new JSONObject();
        try {
            deviceInfo.put("id", this.getDeviceID());
            deviceInfo.put("uuid", this.getDeviceUUID());
            deviceInfo.put("name", this.getDeviceName());
            deviceInfo.put("platform", "Android");
            deviceInfo.put("version", Build.VERSION.RELEASE);
            deviceInfo.put("maker", Build.MANUFACTURER);
            deviceInfo.put("model", Build.MODEL);
        } catch (JSONException e) {
            try {
                deviceInfo.put("error", "Failed to get device information: " + e.getMessage());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        return deviceInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
    public JSONObject getDisplayInfo() {
        JSONObject displayInfo = new JSONObject();
        try {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            float density = context.getResources().getDisplayMetrics().density;
            int densityDpi = context.getResources().getDisplayMetrics().densityDpi;

            // Current physical dimensions
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int height = context.getResources().getDisplayMetrics().heightPixels;

            // Orientation
            String orientation = context.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE ? "landscape" : "portrait";

            // Refresh rate (Hz)
            float refreshRate = activity.getWindowManager().getDefaultDisplay().getRefreshRate();

            // Modern Insets detection (Safe Area)
            int statusbarHeight = 0;
            int navigationbarHeight = 0;
            boolean hasNotch = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.view.WindowInsets insets = decorView.getRootWindowInsets();
                if (insets != null) {
                    statusbarHeight = insets.getSystemWindowInsetTop();
                    navigationbarHeight = insets.getSystemWindowInsetBottom();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        android.view.DisplayCutout cutout = insets.getDisplayCutout();
                        hasNotch = cutout != null;
                    }
                }
            }

            // Fallback if insets are 0 (before view is attached or on older versions)
            if (statusbarHeight == 0) {
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) statusbarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
            if (navigationbarHeight == 0) {
                int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) navigationbarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }

            // Navigation Mode detection (Gestures vs Buttons)
            // 0: 3-button, 1: 2-button, 2: Gestures
            String navigationMode = "unknown";
            try {
                int mode = Settings.Secure.getInt(context.getContentResolver(), "navigation_mode");
                navigationMode = mode == 2 ? "gestures" : mode == 1 ? "2-button" : "3-button";
            } catch (Settings.SettingNotFoundException e) {
                // On some Honor/Huawei devices detect by bar height
                if (navigationbarHeight > 0 && navigationbarHeight < (20 * density)) navigationMode = "gestures_hint";
            }

            displayInfo.put("width", (int) (width / density));
            displayInfo.put("height", (int) (height / density));
            displayInfo.put("density", density);
            displayInfo.put("densityDpi", densityDpi);
            displayInfo.put("orientation", orientation);
            displayInfo.put("refreshRate", Math.round(refreshRate));
            displayInfo.put("aspectRatio", (float) Math.max(width, height) / Math.min(width, height));
            displayInfo.put("navigationMode", navigationMode);
            displayInfo.put("statusbarHeight", (int) (statusbarHeight / density));
            displayInfo.put("navigationbarHeight", (int) (navigationbarHeight / density));
            displayInfo.put("notch", hasNotch);

            // Style and compatibility flags
            boolean statusbarTransparent = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
            boolean statusbarLightMode = (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
            boolean navigationbarTransparent = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != 0;
            boolean navigationbarLightMode = (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) != 0;
            boolean systembarDrawsBackgrounds = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) != 0;

            displayInfo.put("statusbarTransparent", statusbarTransparent);
            displayInfo.put("statusbarLightMode", statusbarLightMode);
            displayInfo.put("navigationbarTransparent", navigationbarTransparent);
            displayInfo.put("navigationbarLightMode", navigationbarLightMode);
            displayInfo.put("systembarDrawsBackgrounds", systembarDrawsBackgrounds);

        } catch (JSONException e) {
            try {
                displayInfo.put("error", "Failed to get display information: " + e.getMessage());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        return displayInfo;
    }

    public JSONObject getAppInfo() {
        JSONObject appInfo = new JSONObject();
        try {
            PackageManager packageManager = activity.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            appInfo.put("name", packageManager.getApplicationLabel(Objects.requireNonNull(packageInfo.applicationInfo)).toString());
            appInfo.put("version", packageInfo.versionName);
            appInfo.put("package", packageInfo.packageName);
            appInfo.put("build", packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException | JSONException e) {
            try {
                appInfo.put("error", "Could not get application information: " + e.getMessage());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        return appInfo;
    }

    public JSONObject getPermissionsStatus() {
        JSONObject permissions = new JSONObject();
        JSONObject manifestPermissions = new JSONObject();
        try {
            manifestPermissions.put("phone", Manifest.permission.READ_PHONE_STATE);
            manifestPermissions.put("location", Manifest.permission.ACCESS_FINE_LOCATION);
            manifestPermissions.put("location.2", Manifest.permission.ACCESS_COARSE_LOCATION);
            manifestPermissions.put("storage", Manifest.permission.READ_EXTERNAL_STORAGE);
            manifestPermissions.put("storage.2", Manifest.permission.WRITE_EXTERNAL_STORAGE);
            manifestPermissions.put("contacts", Manifest.permission.READ_CONTACTS);
            manifestPermissions.put("camera", Manifest.permission.CAMERA);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                manifestPermissions.put("notifications", Manifest.permission.POST_NOTIFICATIONS);
            }
            manifestPermissions.keys().forEachRemaining(key -> {
                if (!key.contains(".")) {
                    try {
                        String permission = manifestPermissions.getString(key);
                        String permission2 = manifestPermissions.has(key + ".2") ? manifestPermissions.getString(key + ".2") : null;
                        boolean granted = ActivityCompat.checkSelfPermission(activity, permission) == PermissionChecker.PERMISSION_GRANTED || (permission2 != null && ActivityCompat.checkSelfPermission(activity, permission2) == PermissionChecker.PERMISSION_GRANTED);
                        if (granted) {
                            permissions.put(key, "granted");
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || (permission2 != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission2))) {
                            permissions.put(key, "denied");
                        } else {
                            permissions.put(key, "default");
                        }
                    } catch (Exception e) {
                        try { permissions.put(key, "undefined"); } catch (Exception ignore) {}
                    }
                }
            });
        } catch (Exception e) {
            try { permissions.put("error", "Failed to get permissions: " + e.getMessage()); } catch (Exception ignore) {}
        }
        return permissions;
    }

    public JSONObject getCurrentPosition() {
        JSONObject position = new JSONObject();
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1001);
                position.put("error", "Location permission not granted. Requested permission.");
                return position;
            }
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                position.put("latitude", location.getLatitude());
                position.put("longitude", location.getLongitude());
                position.put("accuracy", location.getAccuracy());
                position.put("provider", location.getProvider());
            } else {
                position.put("error", "Could not get current position");
            }
        } catch (Exception e) {
            try {
                position.put("error", "Failed to get current position: " + e.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return position;
    }

    public boolean getConnectionStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }
        return false;
    }

    public void setStatusbarStyle(String color, boolean darkIcons) {
		activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = activity.getWindow();
                int colorCode;
                if (color.startsWith("#")) {
                    try {
                        colorCode = android.graphics.Color.parseColor(color);
                    } catch (IllegalArgumentException e) {
                        colorCode = ContextCompat.getColor(context, R.color.statusbarLightColor);
                    }
                } else {
                    switch (color) {
                        case "statusbarLightColor": colorCode = ContextCompat.getColor(context, R.color.statusbarLightColor); break;
                        case "statusbarLightOverlayColor": colorCode = ContextCompat.getColor(context, R.color.statusbarLightOverlayColor); break;
                        case "statusbarDarkColor": colorCode = ContextCompat.getColor(context, R.color.statusbarDarkColor); break;
                        case "statusbarDarkOverlayColor": colorCode = ContextCompat.getColor(context, R.color.statusbarDarkOverlayColor); break;
                        default: colorCode = ContextCompat.getColor(context, R.color.statusbarLightColor); break;
                    }
                }
                Log.i(logTag, "Statusbar set color: " + color);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorCode);
                View decor = window.getDecorView();
                int flags = decor.getSystemUiVisibility();
                if (darkIcons) {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decor.setSystemUiVisibility(flags);
            }
        });
    }

    public void setNavigationbarStyle(String color, boolean darkIcons) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = activity.getWindow();
                int colorCode;
                if (color.startsWith("#")) {
                    try {
                        colorCode = android.graphics.Color.parseColor(color);
                    } catch (IllegalArgumentException e) {
                        colorCode = ContextCompat.getColor(context, R.color.white);
                    }
                } else {
                    switch (color) {
                        case "navigationbarLightColor": colorCode = ContextCompat.getColor(context, R.color.navigationbarLightColor); break;
                        case "navigationbarLightOverlayColor": colorCode = ContextCompat.getColor(context, R.color.navigationbarLightOverlayColor); break;
                        case "navigationbarDarkColor": colorCode = ContextCompat.getColor(context, R.color.navigationbarDarkColor); break;
                        case "navigationbarDarkOverlayColor": colorCode = ContextCompat.getColor(context, R.color.navigationbarDarkOverlayColor); break;
                        default: colorCode = ContextCompat.getColor(context, R.color.white); break;
                    }
                }
                Log.i(logTag, "Navigationbar set color: " + color);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(colorCode);
                View decor = window.getDecorView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int flags = decor.getSystemUiVisibility();
                    if (darkIcons) {
                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    } else {
                        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    }
                    decor.setSystemUiVisibility(flags);
                }
            }
        });
    }

    public boolean saveFile(String name, String content) {
        try (FileOutputStream fileOutput = activity.openFileOutput(name, Context.MODE_PRIVATE)) {
            fileOutput.write(content.getBytes("UTF-8"));
            fileOutput.flush();
            Log.i(logTag, "File saved: " + name);
            return true;
        } catch (IOException e) {
            Log.e(logTag, "Failed to save file: " + name + " - " + e.getMessage());
            return false;
        }
    }

    public String readFile(String name) {
        try (FileInputStream fileInput = activity.openFileInput(name)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInput.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String result = baos.toString("UTF-8");
            Log.i(logTag, "File readed: " + name);
            return result;
        } catch (IOException e) {
            Log.e(logTag, "Failed to read file: " + name + " - " + e.getMessage());
            return null;
        }
    }

    public boolean removeFile(String name) {
        try {
            boolean result = activity.deleteFile(name);
            Log.i(logTag, "File removed: " + name);
            return result;
        } catch (Exception e) {
            Log.e(logTag, "Failed to remove file: " + name + " - " + e.getMessage());
            return false;
        }
    }

    public void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void openURL(final String url) {
        Log.i(logTag, "openURL requested: " + url);
        if (url.startsWith("file:///android_asset/")) {
            String assetPath = url.replace("file:///android_asset/", "");
            try {
                context.getAssets().open(assetPath).close();
                Log.i(logTag, "Asset confirmed exists: " + assetPath);
            } catch (IOException e) {
                Log.e(logTag, "Asset NOT found via AssetManager: " + assetPath);
            }
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (url.startsWith("file://")) {
                    webView.loadUrl(url);
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void saveDeepLink(Intent intent) throws JSONException {
        if (intent != null && intent.getData() != null) {
            deepLinkURL = intent.getData().toString();
            sendDeepLink();
        }
    }

    public void sendDeepLink() {
        if (pageLoaded) {
            try {
                JSONObject arguments = new JSONObject();
                arguments.put("event", "onReceiveDeepLink");
                arguments.put("url", deepLinkURL);
                pushJavascript(arguments);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String readDeepLink() {
        return deepLinkURL;
    }

    public void clearDeepLink() {
        deepLinkURL = null;
    }

    private void pushJavascript(JSONObject arguments) throws JSONException {
        if (webView != null) {
            String js = "WUIEnvironment.response("+arguments.toString()+")";
            webView.evaluateJavascript(js, null);
        }
    }

    class WebViewJavascriptInterface {
        @JavascriptInterface
        public String request(String argumentsString) throws JSONException {
            JSONObject arguments = new JSONObject(argumentsString);
            String func = arguments.get("func").toString();
            if (func.matches("^(getDeviceInfo|getDisplayInfo|getAppInfo|getPermissionsStatus|getCurrentPosition|readFile|readDeepLink)$")) {
                return
                    func.equals("getDeviceInfo") ? getDeviceInfo().toString() :
                    func.equals("getDisplayInfo") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? getDisplayInfo().toString() :
                    func.equals("getAppInfo") ? getAppInfo().toString() :
                    func.equals("getPermissionsStatus") ? getPermissionsStatus().toString() :
                    func.equals("getCurrentPosition") ? getCurrentPosition().toString() :
                    func.equals("readFile") ? readFile(arguments.get("name").toString()) :
                    func.equals("readDeepLink") ? readDeepLink() :
                    "";
            } else if (func.matches("^(isAppInForeground|getConnectionStatus|saveFile|removeFile)$")) {
                return
                    func.equals("isAppInForeground") && isAppInForeground() ? "true" :
                    func.equals("getConnectionStatus") && getConnectionStatus() ? "true" :
                    func.equals("saveFile") && saveFile(arguments.get("name").toString(), arguments.get("content").toString()) ? "true" :
                    func.equals("removeFile") && removeFile(arguments.get("name").toString()) ? "true" :
                    "false";
            } else if (func.matches("^(setStatusbarStyle|setNavigationbarStyle|openAppSettings|openURL|clearDeepLink)$")) {
                switch (func) {
                    case "setStatusbarStyle": setStatusbarStyle(arguments.get("color").toString(), (Boolean) arguments.get("darkIcons")); break;
                    case "setNavigationbarStyle": setNavigationbarStyle(arguments.get("color").toString(), (Boolean) arguments.get("darkIcons")); break;
                    case "openAppSettings": openAppSettings(); break;
                    case "openURL": openURL(arguments.get("url").toString()); break;
                    case "clearDeepLink": clearDeepLink(); break;
                }
                return "null";
            }
            return "";
        }
    }
}